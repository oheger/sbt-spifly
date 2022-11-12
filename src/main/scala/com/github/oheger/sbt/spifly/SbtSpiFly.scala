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

package com.github.oheger.sbt.spifly

import com.typesafe.sbt.osgi.SbtOsgi
import sbt.Keys._
import sbt._

/**
  * The main class of this plugin.
  */
object SbtSpiFly extends AutoPlugin {
  override val requires: Plugins = SbtOsgi

  override lazy val projectSettings: Seq[Def.Setting[_]] = defaultSpiFlySettings

  object autoImport {
    val SpiFlyKeys = com.github.oheger.sbt.spifly.SpiFlyKeys

    def spiFlySettings: Seq[Setting[_]] = Seq(
      addArtifact(Artifact("foo", "spifly"), SpiFlyKeys.invokeSpiFly).settings: _*
    ) ++ Seq(
      artifact in SpiFlyKeys.spiFly := Artifact(moduleName.value,
        SpiFlyKeys.classifier.value getOrElse SpiFly.DefaultClassifier)
    )
  }

  def defaultSpiFlySettings: Seq[Setting[_]] = {
    import SpiFlyKeys._
    Seq(
      classifier := Some(SpiFly.DefaultClassifier),
      skipSpiFly := false,
      spiFly := SpiFly.spiFlyTask((fullClasspath in Compile).value,
        (artifactPath in(Compile, packageBin)).value, SpiFlyKeys.classifier.value, streams.value.log),
      invokeSpiFly := Def.sequential(SbtOsgi.autoImport.OsgiKeys.bundle, spiFly).value
    )
  }
}
