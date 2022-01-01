import sbt._

object Library {

  // Versions
  val spiflyVersion = "1.3.4"

  // Libraries
  val spiflyTool = "org.apache.aries.spifly" % "org.apache.aries.spifly.static.tool" % spiflyVersion classifier "jar-with-dependencies"
}

object Dependencies {

  import Library._

  val sbtSpiFly = List(
    spiflyTool
  )
}

