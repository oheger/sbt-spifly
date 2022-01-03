import com.github.oheger.sbt.spifly.SbtSpiFly
import com.github.oheger.sbt.spifly.SbtSpiFly.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.sbt.osgi.{OsgiKeys, SbtOsgi}

lazy val test02 = (project in file ("."))
  .enablePlugins(SbtSpiFly)
  .settings(osgiSettings: _*)
  .settings(spiFlySettings: _*)
  .settings(
    version := "0.1",
    SpiFlyKeys.classifier := Some("test"),
    OsgiKeys.additionalHeaders :=
      Map("SPI-Consumer" -> "javax.sound.sampled.AudioSystem#getAudioInputStream"),
    TaskKey[Unit]("checkArtifact") := {
      val file = SpiFlyKeys.spiFly.value
      if(!file.name.contains("_test"))
        sys.error("Artifact file does not contain the custom classifier!")
    }
  )
