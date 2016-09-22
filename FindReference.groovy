import static groovy.io.FileType.FILES

def directory = new File("/ciarea/jenkins/jobs/")


    
directory.eachDir() { dir ->  
   
  def job = new File(dir.getPath())
  
  job.eachFileRecurse(FILES) {

	if(it.name.equals('config.xml')) {
      
      def config = new File(it.toString())  
      
      if (config.getText().contains("teste")) {
        
        println "=================================================================================="
        println dir   
        //println config.getText()    
      }
    }   
  }
  
}
