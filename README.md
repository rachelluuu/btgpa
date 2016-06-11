Bergen Tech GPA Calculator
======================================

Download latest version of [Oracle Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

Clone the github repository:

    $ git clone https://github.com/rachelluuu/btgpa.git
    $ cd btgpa
    
This is a maven project, to build it:

    $ mvn clean package

To run the example `org.eclipse.jetty.demo.Main`:

    $ java $JAVA_OPTS -cp target/gpa-2-SNAPSHOT/WEB-INF/lib/*:target/gpa-2-SNAPSHOT/WEB-INF/classes -Dport=$PORT com.bt.gpa.Main

Open your web browser to:

    http://localhost:8080/  

To stop Jetty:

  use <kbd>CTRL</kbd>+<kbd>C</kbd>


Team Members
----------------
	Rachel Lu
	Jennifer He
	Gabe Wehrle
