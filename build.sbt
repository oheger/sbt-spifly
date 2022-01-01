import sbt.Keys.publishMavenStyle

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-spifly",
    version := "0.3.0-SNAPSHOT",
    organization := "com.github.oheger.sbt",
    homepage := Some(url("https://github.com/oheger/sbt-spifly")),
    licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/oheger/sbt-spifly.git"),
        "scm:git:git@github.com:oheger/sbt-spifly.git"
      )
    ),
    developers := List(
      Developer(
        id    = "oheger",
        name  = "Oliver Heger",
        email = "oheger@apache.org",
        url = url("https://github.com/oheger")
      )
    ),
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.1.6"
      }
    },
    libraryDependencies ++= Dependencies.sbtSpiFly,
    addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.9.6"),
    publishMavenStyle := true,
    pomIncludeRepository := { _ => false },
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }
  )
