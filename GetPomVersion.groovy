import hudson.FilePath
import hudson.remoting.VirtualChannel

def pomFile = build.getParent().getWorkspace().child('pom.xml').readToString();
def project = new XmlSlurper().parseText(pomFile);       
def param = new hudson.model.StringParameterValue("POM_VERSION", project.version.toString());
def paramAction = new hudson.model.ParametersAction(param);
build.addAction(paramAction);
