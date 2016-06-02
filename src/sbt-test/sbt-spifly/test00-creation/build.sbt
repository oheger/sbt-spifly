lazy val test00 = (project in file (".")).enablePlugins(SbtOsgi, SbtSpiFly)

scalaVersion := "2.11.7"

version := "0.1"

name := "test00"

osgiSettings

spiFlySettings

OsgiKeys.additionalHeaders :=
  Map("SPI-Consumer" -> "javax.sound.sampled.AudioSystem#getAudioInputStream")


