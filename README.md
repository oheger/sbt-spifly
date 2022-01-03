sbt-spifly
==========

Plugin for [sbt](http://www.scala-sbt.org) to invoke the static weaving tool of
[Apache Aries SPI Fly](http://aries.apache.org/modules/spi-fly.html).

SPI Fly makes the Java service loader mechanism work in an OSGi environment. To
achieve this, it supports both a dynamic and a static approach. The former uses
byte code manipulation at runtime when a client bundle is installed in an
OSGi container; it requires no special manipulations on client OSGi bundles.

The static approach in contrast is applied at compile time as an additional
processing step; it requires the execution of a static weaving tool on a
client bundle. This plugin simplifies this step by providing a corresponding
task that can be executed directly in the sbt build.

As SPI Fly is about OSGi, you will typically use this plugin together with the
[sbt-osgi plugin](https://github.com/sbt/sbt-osgi). Then sbt-osgi creates a
bundle jar with a correct manifest, and sbt-spifly passes this jar to the
Aries SPI Fly weaving tool, generating a second artifact with the `spifly`
classifier.

Versions
--------

The initial version of the sbt-spifly plugin, 0.1.0, targeted sbt versions 0.x,
starting from 0.13.8. Later plugin versions require sbt 1.x. So if you still 
use a 0.x sbt version, you are limited to version 0.1.0.

Using sbt-spifly
----------------

sbt-spifly is an sbt *Autoplugin*. In order to use the plugin in a build
project it has to be declared in `<PROJECT_ROOT>/project/plugins.sbt` first:

```
// Other stuff

addSbtPlugin("com.github.oheger.sbt" % "sbt-spifly" % "0.3.0")
```

Then it can be enabled for the current project. Here is an example how this
looks like for a `<PROJECT_ROOT>/build.sbt` file:

```scala
import com.github.oheger.sbt.spifly.SbtSpiFly
import com.github.oheger.sbt.spifly.SbtSpiFly.autoImport._
import com.typesafe.sbt.osgi.SbtOsgi.autoImport._
import com.typesafe.sbt.osgi.{OsgiKeys, SbtOsgi}

lazy val myBundleProject = (project in file ("."))  // project reference
  .enablePlugins(SbtOsgi, SbtSpiFly)  // enables both osgi and spifly plugins
  .settings(osgiSettings: _*)         // import settings for OSGi plugin
  .settings(spiFlySettings: _*)       // import settings for spifly plugin
  .settings(
    // configure bundle manifest, e.g. set a SPI Fly header
    OsgiKeys.additionalHeaders :=
      Map("SPI-Consumer" -> "javax.sound.sampled.AudioSystem#getAudioInputStream"),
    ...
  )
```

By importing the settings of the osgi and spifly plugins, it is ensured that
the default publishing behavior is changed and the `bundle` and `spiFly` tasks
are executed. This means it is sufficient to invoke

`sbt publishLocal`

to generate an OSGi bundle processed with the SPI Fly weaving tool. Actually,
the build produces two artifacts:
* the normal OSGi bundle jar
* a second jar with the classifier `spifly` which has been processed by SPI Fly

The `spifly` artifact can then be deployed in an OSGi framework.

Further Options
---------------

As the static SPI Fly weaving tool does not define any configuration options,
there is not much need for many settings of the plugin either. It just invokes
the weaving tool (using the full classpath of the project, so that all 
referenced classes can be resolved), and that's all.

It is, however, possible to configure the classifier, under which the processed
artifact is published. This can be done via the `classifier` settings key. If
it is not specified, *spifly* is used as default. The setting is actually an
`Option`. It can be set to `None`, then no classifier is used at all, and the
processed artifact overrides the original one.

Below is an example how to set an alternative classifier, *osgi*:

```scala
lazy val myBundleProject = (project in file (".")) 
  .enablePlugins(SbtOsgi, SbtSpiFly) 
  .settings(osgiSettings: _*)        
  .settings(spiFlySettings: _*)      
  .settings(
    SpiFlyKeys.classifier := Some("osgi")
  )
```

The following example disables the classifier:

```scala
lazy val myBundleProject = (project in file (".")) 
  .enablePlugins(SbtOsgi, SbtSpiFly) 
  .settings(osgiSettings: _*)        
  .settings(spiFlySettings: _*)      
  .settings(
    SpiFlyKeys.classifier := None
  )
```

The plugin introduces the `spiFly` task which can also be invoked directly. But
this task requires that the bundle jar has already been created.

License
-------

This code is open source software licensed under the
[Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

Release notes
-------------

### Version 0.3.0
- Added the `classifier` key that allows customizing or disabling the 
  classifier, under which the processed artifact gets published.

### Version 0.2.0
- Support for newer versions of sbt.

### Version 0.1.0
- Initial release
