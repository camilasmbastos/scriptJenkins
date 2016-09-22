import groovy.util.XmlParser

def env = System.getenv()

def workspace = env['WORKSPACE']

def xmlFile = "${workspace}/target/test-reports/cobertura/coverage.xml"

println xmlFile

def coverage =new XmlParser().parse(new File(xmlFile))

coverage.packages.package.classes.class.lines.each {

  def remLine = it.children().find{ it.@number == "0"}
  
  if (remLine) {    
    it.remove(remLine )
  }

}

new XmlNodePrinter(new PrintWriter(new FileWriter(xmlFile))).print(coverage)
