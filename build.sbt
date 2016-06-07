organization := "com.github.oheger.sbt"

name := "sbt-spifly"

version := "0.1.0"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))


homepage := Some(url("https://github.com/oheger/sbt-spifly"))

libraryDependencies ++= Dependencies.sbtSpiFly

sbtPlugin := true

addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.8.0")

scriptedSettings
scriptedLaunchOpts <+= version apply { v => "-Dproject.version="+v }

useGpg := true

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra :=
  <scm>
    <url>https://github.com/oheger/sbt-spifly.git</url>
    <connection>scm:git:git@github.com:oheger/sbt-spifly.git</connection>
  </scm>
  <developers>
    <developer>
      <id>oheger</id>
      <name>Oliver Heger</name>
    </developer>
  </developers>
