# Amazon Youtube Player
A simple Youtube Player for Amazon Alexa Devices.

This project uses the NewPipe extractor and ASK (Alexa Skills Kit SDK) to play Youtube audio on any Alexa powered device.

<h2>Installation: </h2>
To build the JAR package required for AWS Lambda, run the following command in the AmazonYoutubePlayer directory of the project and retrieve the JAR file from the target folder.

<code>mvn org.apache.maven.plugins:maven-assembly-plugin:2.6:assembly -DdescriptorId=jar-with-dependencies package</code>


<h2>Usage:</h2>
To invoke the Alexa Skill use the following approximate invocation forms:
<code>use youtube player to play {video}</code>
<code>use youtube player to play {video} and skip {skip}</code>
