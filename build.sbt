organization := "com.github.oheger"

name := "sbt-spifly"

version := "0.1.0-SNAPSHOT"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

libraryDependencies ++= Dependencies.sbtSpiFly

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.8.0")
