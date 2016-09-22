import hudson.plugins.*

for(item in Hudson.instance.items)
{
    
  
  item?.getProperties().each {   
//  if (item?.name.equals("ARTWEB-develop") && it?.value instanceof hudson.plugins.buildblocker.BuildBlockerProperty) {
    if (it?.value instanceof hudson.plugins.buildblocker.BuildBlockerProperty) {
      
      if (it?.value?.isUseBuildBlocker()) {
     	println ("======================================")
     
      	println("Job: <$item.name>")
        
        def newblocker = new hudson.plugins.buildblocker.BuildBlockerProperty(true, "GLOBAL", "ALL", it?.value?.getBlockingJobs())
        
     
     	println it?.value?.getBlockingJobs()        
        println it?.value?.blockLevel
        println it?.value?.getScanQueueFor()
        
        item.removeProperty(it?.value)
        item.addProperty(newblocker)
        item.save()
      }
      
      
    } 
    
  }
    
}
