import hudson.security.*
import jenkins.security.*

switch (Jenkins.instance.authorizationStrategy){
  
  case ProjectMatrixAuthorizationStrategy:    
    def jobs = Jenkins.instance.items
    jobs.each {      
      	println it.name.center(80,'-')
      	        
        def authorizationMatrixProperty = it.getProperty(AuthorizationMatrixProperty.class)
        
        def sids = authorizationMatrixProperty?.getAllSIDs()
        
        for (sid in sids){
            authorizationMatrixProperty?.add(hudson.plugins.release.ReleaseWrapper.RELEASE_PERMISSION, sid) 
                      
            println ''+sid+' has Build permission and RELEASE_PERMISSION permission will be add'
                        
        }
       it.save()        
    }
    break
  default:
    println "No permission need to be mofdified gloabally"
    break
}

return
