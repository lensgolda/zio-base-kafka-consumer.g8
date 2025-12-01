// This build is for this Giter8 template.
// To test the template run `g8` or `g8Test` from the sbt session.
// See https://www.foundweekends.org/giter8/testing.html#Using+the+Giter8Plugin for more details.

ThisBuild / version := "0.1.0"

val scala2Version = "2.13.13"
val scala3Version = "3.3.7"

lazy val root = (project in file("."))
  .enablePlugins(ScriptedPlugin)
  .settings(
    name := "zio-kafka-consumer-template",
    Test / Keys.test := {
      val _ = (Test / g8Test).toTask("").value
    },
    scriptedLaunchOpts ++= List(
      "-Xms1024m", 
      "-Xmx1024m", 
      "-XX:ReservedCodeCacheSize=128m", 
      "-Xss2m", 
      "-Dfile.encoding=UTF-8"
    ),

    resolvers += Resolver.url(
      "typesafe",
      url("https://repo.typesafe.com/typesafe/ivy-releases/"),
    )(Resolver.ivyStylePatterns),

    // To cross compile with Scala 3 and Scala 2
    crossScalaVersions := Seq(scala3Version, scala2Version)
  )
