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

import sbt._
import sbt.internal.util.ManagedLogger

/**
  * The task implementation.
  */
object SpiFly {
  /** The default classifier for processed artifacts. */
  final val DefaultClassifier = "spifly"

  /** The path separator of the current platform. */
  private val PathSeparator = System.getProperty("path.separator")

  /** The main class to be invoked from Aries. */
  private val SpiFlyMainClass = classOf[org.apache.aries.spifly.statictool.Main]

  /**
    * The actual task for invoking Apache Aries SPI Fly. This task forks a
    * process to invoke the main class of the Aries SPI Fly static weaving
    * tool passing in the project's classpath and the classpath of the Aries
    * jar. The resulting processed jar is renamed according to the selected
    * classifier. (If no classifier is provided, the original artifact is
    * overridden with the processed jar.)
    *
    * If the option to skip SpiFly was set, the file for the passed in artifact
    * is returned directly. (Then the classifier is ignored.)
    *
    * @param fullClasspath the full classpath of the current project
    * @param artifactPath  the path to the current jar artifact
    * @param classifier    an option with the classifier to be used
    * @param skipSpiFly    flag to skip the SpiFly invocation
    * @param log           the logger
    * @return the path to the processed file
    */
  def spiFlyTask(fullClasspath: Seq[Attributed[File]],
                 artifactPath: File,
                 classifier: Option[String],
                 skipSpiFly: Boolean,
                 log: ManagedLogger): File = {
    if (skipSpiFly) {
      if (classifier.isDefined) {
        log.warn("Ignoring classifier because skipSpiFly flag is set.")
      }
      artifactPath
    } else {

      val classPathElements = spiFlyJar() :: fullClasspath.map(_.data.getAbsolutePath).toList
      val classPath = classPathElements.mkString(PathSeparator)
      val javaOptions = Vector("-classpath", classPath)
      val arguments = List(SpiFlyMainClass.getName, artifactPath.getAbsolutePath)
      val process = Fork.java.fork(ForkOptions().withRunJVMOptions(javaOptions), arguments)
      val exitValue = process.exitValue()
      if (exitValue != 0) {
        sys.error(s"Invocation of SPI Fly failed with exit code $exitValue.")
      }

      classifier match {
        case Some(value) if value == DefaultClassifier =>
          pathWithClassifier(artifactPath, DefaultClassifier)
        case Some(value) =>
          writeResultFile(artifactPath, pathWithClassifier(artifactPath, value), log)
        case None =>
          writeResultFile(artifactPath, artifactPath, log)
      }
    }
  }

  /**
    * Returns the path to the jar that contains the SpiFly tool with all its
    * dependencies.
    *
    * @return the path to the static SpiFly tool jar
    */
  private def spiFlyJar(): String = {
    val location = SpiFlyMainClass.getProtectionDomain.getCodeSource.getLocation
    val path = location.getPath
    val index = path indexOf '!'
    if (index > 0) path.substring(0, index) else path
  }

  /**
    * Copies the result of the SpiFly processing tool to the given result path.
    *
    * @param artifactPath the path of the original artifact
    * @param resultPath   the result path
    * @param log          the logger
    * @return the ''File'' pointing to the result path
    */
  private def writeResultFile(artifactPath: File, resultPath: File, log: ManagedLogger): File = {
    val processedPath = pathWithClassifier(artifactPath, DefaultClassifier)
    log.info(s"Copying result of SpiFly processing to '$resultPath'.")
    IO.copyFile(processedPath, resultPath)
    resultPath
  }

  /**
    * Generates a path based on the original artifact path with a specific
    * classifier.
    *
    * @param artifactPath the path of the original artifact
    * @param classifier   the classifier
    * @return the resulting path
    */
  private def pathWithClassifier(artifactPath: File, classifier: String): File =
    file(artifactPath.getAbsolutePath.replace(".jar", s"_$classifier.jar"))
}
