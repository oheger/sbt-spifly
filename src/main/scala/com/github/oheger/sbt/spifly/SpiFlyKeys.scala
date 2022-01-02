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

import sbt.{File, SettingKey, TaskKey}

/**
  * Defines the keys supported by this plugin.
  */
object SpiFlyKeys {
  /**
    * Key for the ''spiFly'' task with invokes the static weaving tool to
    * process the current project's main artifact.
    */
  val spiFly = TaskKey[File]("spiFly",
    "Applies the Apache Aries SpiFly static weaving tool to the current project's artifact.")

  /**
    * Key for a helper task used during a publish operation. This task makes
    * sure that the ''spiFly'' task is invoked after the OSGi bundle for the
    * project has been created.
    */
  val invokeSpiFly = TaskKey[File]("invokeSpiFly",
    "Invokes Apache Aries SpiFly for the current project's artifact during build.")

  /**
    * Key for the ''classifier'' property that allows changing the classifier
    * of the generated jar artifact.
    */
  val classifier = SettingKey[Option[String]]("classifier",
    "Allows defining a classifier for the processed artifact from the default classifier 'spifly'. " +
      "A value of None means that no classifier is used, and the original artifact is overridden with the " +
      "version processed by the SpiFly tool.")
}
