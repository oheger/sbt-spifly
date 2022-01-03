import com.github.oheger.sbt.spifly.SbtSpiFly
import com.github.oheger.sbt.spifly.SbtSpiFly.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.sbt.osgi.{OsgiKeys, SbtOsgi}

lazy val test03 = (project in file ("."))
  .enablePlugins(SbtSpiFly)
  .settings(osgiSettings: _*)
  .settings(spiFlySettings: _*)
  .settings(
    version := "0.1",
    SpiFlyKeys.classifier := None,
    OsgiKeys.additionalHeaders :=
      Map("SPI-Consumer" -> "javax.sound.sampled.AudioSystem#getAudioInputStream"),
    TaskKey[Unit]("checkArtifact") := {
      import java.io.IOException
      import java.util.zip.ZipFile
      import scala.io.Source
      val file = SpiFlyKeys.spiFly.value
      if(file.name != "test03_2.12-0.1.jar")
        sys.error(s"Wrong name of artifact: '${file.name}' was not 'test03_2.12-0.1.jar'.")

      val zipFile = new ZipFile(file)
      val manifestIn = zipFile.getInputStream(zipFile.getEntry("META-INF/MANIFEST.MF"))
      try {
        val lines = Source.fromInputStream(manifestIn).getLines().toList
        if(!lines.exists(_.startsWith("X-SpiFly-Processed-")))
          sys.error("SpiFly-Processed header not found!")
      } catch {
        case e: IOException => sys.error("Expected to be able to read the manifest, but got exception!" +  e)
      } finally manifestIn.close()
    }
  )
