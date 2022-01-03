/*
 * Copyright 2016-2022 The developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        case "2.12" => "1.2.1"
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
    },
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false
  )
