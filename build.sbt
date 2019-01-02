import sbt.Keys.publishMavenStyle

lazy val root = (project in file(".")).
  settings(
    name := "sbt-spifly",
    version := "0.2.0-SNAPSHOT",
    organization := "com.github.oheger.sbt",
    homepage := Some(url("https://github.com/oheger/sbt-spifly")),
    scalaVersion := "2.12.8",
    sbtPlugin := true,
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
    sbtVersion in Global := "1.2.8",
    libraryDependencies ++= Dependencies.sbtSpiFly,
    addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.9.4"),
    crossSbtVersions := Seq("0.13.17", "1.1.6"),
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
