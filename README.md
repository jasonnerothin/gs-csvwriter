gs-adminlogger
=========

An application that writes information to an arbitrary grid based on contents of a csv file

Prerequisites:

* maven & gradle

Setup:

* Download and install GigaSpaces using [this 5 minute recipe](http://docs.gigaspaces.com/tutorials/your-first-data-grid-application.html). You do not need to deploy a service grid, just download and unzip.
* Install GigaSpaces' maven libraries using [these instructions](http://docs.gigaspaces.com/xap97/maven-plugin.html#prior-to-installation).
* Open build.gradle in a text editor.
* Set the [localRepo variable](https://github.com/jasonnerothin/gs-scalatest/blob/master/build.gradle#L4) to the location (directory path) to your _local_ maven repository (default is %PROFILE%/.m2/respository)
* Set the [jsHomedir variable](https://github.com/jasonnerothin/gs-scalatest/blob/master/build.gradle#L5) to the location (directory path) of your GigaSpaces install (unzipped directory folder).

To test:

	$ gradle test -i

To run:

    $ gradle run
