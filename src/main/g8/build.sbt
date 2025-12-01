import Dependencies.*

ThisBuild / organization := "com.github.lensgolda"

val scala2Version = "2.13.13"
val scala3Version = "3.3.7"

lazy val root = project
    .in(file("."))
    .settings(
      name := "zio-basic-kafka-consumer",
      version := "0.1.0",
      resolvers ++= Seq(
        "Sonatype OSS releases" at "https://oss.sonatype.org/content/repositories/releases"
      ),
      libraryDependencies ++= zioDeps
          ++ loggingDeps
          ++ scalafixDependencies
          ++ Seq("org.scalameta" %% "munit" % "0.7.29" % Test),

      // To make the default compiler and REPL use Dotty
      scalaVersion := scala3Version,

      // To cross compile with Scala 3 and Scala 2
      crossScalaVersions := Seq(scala3Version, scala2Version)
    )
