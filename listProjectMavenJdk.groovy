import hudson.model.*
import hudson.maven.*


for(item in Hudson.instance.allItems) {
    
  if(item instanceof MavenModuleSet) {    
    if (item.getJDK().toString().contains('JDK1.6')) {
      println("JOB : " +item.name);       
      println("JDK : " +item.getJDK().toString());
      println "================================================================"  
    }
    
  }   
  
}
