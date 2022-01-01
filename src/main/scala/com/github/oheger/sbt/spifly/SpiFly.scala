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

/**
  * The task implementation.
  */
object SpiFly {
  /** The path separator of the current platform. */
  private val PathSeparator = System.getProperty("path.separator")

  /** The main class to be invoked from Aries. */
  private val SpiFlyMainClass = classOf[org.apache.aries.spifly.statictool.Main]

  /**
    * The actual task for invoking Apache Aries SPI Fly. This task forks a
    * process to invoke the main class of the Aries SPI Fly static weaving
    * tool passing in the project's classpath and the classpath of the Aries
    * jar.
    *
    * @param fullClasspath the full classpath of the current project
    * @param artifactPath  the path to the current jar artifact
    * @return the path to the processed file
    */
  def spiFlyTask(fullClasspath: Seq[Attributed[File]],
                 artifactPath: File): File = {
    val classPathElements = spiFlyJar() :: fullClasspath.map(_.data.getAbsolutePath).toList
    val classPath = classPathElements.mkString(PathSeparator)
    val javaOptions = Vector("-classpath", classPath)
    val arguments = List(SpiFlyMainClass.getName, artifactPath.getAbsolutePath)
    val process = Fork.java.fork(ForkOptions().withRunJVMOptions(javaOptions), arguments)
    val exitValue = process.exitValue()
    if (exitValue != 0) {
      sys.error(s"Invocation of SPI Fly failed with exit code $exitValue.")
    }

    file(artifactPath.getAbsolutePath.replace(".jar", "_spifly.jar"))
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
}
