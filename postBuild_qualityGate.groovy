import groovy.json.JsonSlurper

// shortcut to make writing to the log easier
def log = manager.listener.logger.&println
// this gets the filepath to the workspace
// note that FilePath can also reference remote files
def workspace = manager.build.getWorkspace()
def reportPath = workspace.withSuffix("/.sonar/report-task.txt")
log("Reading sonar taskId from file: '${reportPath}'")

// if anything goes wrong the build still needs to fail, therefore failure in the catch block
try
{
    if (!reportPath.exists())
    {
        log("[QualityGate] File '${reportPath}' does not exist. Without URL to check the task status the quality gate can not be checked.")
        manager.buildFailure()
    }
    else
    {
        // retrieve the taskUrl to check the task status
        def report = reportPath.read()
        def properties = new Properties()
        properties.load(report)
        def taskUrl = new URL(properties.ceTaskUrl)

        // used for parsing the response from the server
        def jsonParser = new JsonSlurper()

        def status = "PENDING"
        String analysisId = null
        while (status == "PENDING" || status == "IN_PROGRESS")
        {
            // loop until the task on the server is finished
            log("[QualityGate] Requesting task status from URL: " + properties.ceTaskUrl)
            def response = jsonParser.parse(taskUrl.newReader())
            status = response.task.status
            analysisId = response.task.analysisId
            sleep(1000)
        }

        if (status == "FAILED" || status == "CANCELED")
        {
            log("[QualityGate] Task failed or was canceled.")
            manager.buildFailure()
        }
        else if (status == "SUCCESS")
        {
            // once the task is finished on the server we can check the result
            def analysisUrl = new URL("${properties.serverUrl}/api/qualitygates/project_status?analysisId=${analysisId}")
            log("[QualityGate] Task finished, checking the result at ${analysisUrl}")
            def result = jsonParser.parse(analysisUrl.newReader())
            if (result.projectStatus.status == "OK")
            {
                log("[QualityGate] Analysis passed the quality gate.")
                manager.buildSuccess()
            }
            else if (result.projectStatus.status == "ERROR")
            {
                log("[QualityGate] Analysis did not pass the quality gate.")
                manager.buildFailure()
            }
            else
            {
                log("[QualityGate] Unknown quality gate status: '${result.projectStatus.status}'")
                manager.buildFailure()
            }
        }
        else
        {
            log("[QualityGate] Unknown task status '${status}. Aborting.")
            manager.buildFailure()
        }
    }
}
catch(Exception e)
{
    log("[QualityGate] Unexpected Exception: " + e.getMessage())
    manager.buildFailure()
}
