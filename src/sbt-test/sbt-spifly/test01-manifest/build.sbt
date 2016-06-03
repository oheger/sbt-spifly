lazy val test00 = (project in file (".")).enablePlugins(SbtOsgi, SbtSpiFly)

scalaVersion := "2.11.7"

version := "0.1"

name := "test01"

osgiSettings

spiFlySettings

OsgiKeys.additionalHeaders :=
  Map("SPI-Consumer" -> "javax.sound.sampled.AudioSystem#getAudioInputStream",
    "Test-Header" -> "present")

TaskKey[Unit]("verify-manifest") <<= SpiFlyKeys.spiFly map { file =>
  import java.io.IOException
  import java.util.zip.ZipFile
  import scala.io.Source
  val zipFile = new ZipFile(file)
  val manifestIn = zipFile.getInputStream(zipFile.getEntry("META-INF/MANIFEST.MF"))
  try {
    val lines = Source.fromInputStream(manifestIn).getLines().toList
    if(!lines.exists(_.startsWith("X-SpiFly-Processed-")))
      sys.error("SpiFly-Processed header not found!")
    if(!lines.exists(_.startsWith("Test-Header")))
      sys.error("Custom header not found!")
  } catch {
    case e: IOException => sys.error("Expected to be able to read the manifest, but got exception!" +  e)
  } finally manifestIn.close()
}
