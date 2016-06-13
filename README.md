Bergen Tech GPA Calculator
======================================

Download and install latest version of [Oracle Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

Install [Homebrew](http://brew.sh) package manager by launching Terminal application and entering:

    $ /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

Install [Apache Maven](https://maven.apache.org/): 

    $ brew install maven

Clone the `Github` repository:

    $ git clone https://github.com/rachelluuu/btgpa.git
    
Compile and package the Java sources:

    $ cd btgpa
    $ mvn package

Test by running the [Jetty](http://www.eclipse.org/jetty/) application server:

    $ mvn exec:exec

Open your web browser to:

    http://localhost:8080/  

To stop `Jetty` just hit <kbd>CTRL</kbd>+<kbd>C</kbd>

Deploy to [Heroku](http://heroku.com/):

    git push heroku

Team Members
----------------
*	Rachel Lu
*	Jennifer He
*	Gabe Wehrle
