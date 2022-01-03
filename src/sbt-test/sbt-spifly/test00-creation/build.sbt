import com.github.oheger.sbt.spifly.SbtSpiFly
import com.github.oheger.sbt.spifly.SbtSpiFly.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.sbt.osgi.{OsgiKeys, SbtOsgi}

lazy val test00 = (project in file ("."))
  .enablePlugins(SbtSpiFly)
  .settings(osgiSettings: _*)
  .settings(spiFlySettings: _*)
  .settings(
    version := "0.1",
    OsgiKeys.additionalHeaders :=
      Map("SPI-Consumer" -> "javax.sound.sampled.AudioSystem#getAudioInputStream")
  )
