import hudson.model.*
import hudson.triggers.*
import org.jenkinsci.plugins.gwt.*
import groovy.transform.Field

@Field
TriggerDescriptor SCM_TRIGGER_DESCRIPTOR = Hudson.instance.getDescriptorOrDie(GenericTrigger.class)
println(SCM_TRIGGER_DESCRIPTOR)

def findGenericTrigger(item) {

    if (item instanceof com.cloudbees.hudson.plugins.folder.Folder) {
    	
      item.getAllJobs().each {
      	findGenericTrigger(it)
      }
      	
    } else {
      	     	
      	def trigger = item?.getTriggers()?.get(SCM_TRIGGER_DESCRIPTOR)
		
        if(!(trigger != null && trigger instanceof GenericTrigger)) {
          println("-----------------------------") 
       	  println("Working on project <$item>") 
          println "Não usa GenericTrigger"
        }
    }
}

 for(item in Hudson.instance.items)
  {
	findGenericTrigger(item)
  }
