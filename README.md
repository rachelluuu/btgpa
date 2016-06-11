Bergen Tech GPA Calculator
======================================

Download and install latest version of [Oracle Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

Install [Homebrew](http://brew.sh) package manager by following the directions there.

Install [Apache Maven](https://maven.apache.org/) by opening launching Terminal application and enter:

    $ brew install maven

Clone the github repository:

    $ git clone https://github.com/rachelluuu/btgpa.git
    $ cd btgpa
    
Comppile the Java sources:

    $ mvn clean package

Run [jetty](http://www.eclipse.org/jetty/) application server:

    $ java -cp target/*/WEB-INF/lib/*:target/*/WEB-INF/classes com.bt.gpa.Main

Open your web browser to:

    http://localhost:8080/  

To stop Jetty:

  use <kbd>CTRL</kbd>+<kbd>C</kbd>


Team Members
----------------
	Rachel Lu
	Jennifer He
	Gabe Wehrle
