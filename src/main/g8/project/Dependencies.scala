import sbt.*

object Dependencies {

    lazy val zioVersion = "2.1.23"
    lazy val zioKafkaVersion = "3.2.0"
    lazy val zioConfigVersion = "4.0.4"
    lazy val logbackClassicVersion = "1.5.19"
    lazy val zioLoggingVersion = "2.5.1"

    val zioDeps = Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-kafka" % zioKafkaVersion,
      "dev.zio" %% "zio-config" % zioConfigVersion,
      "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
      "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
      "dev.zio" %% "zio-logging" % zioLoggingVersion,
      "dev.zio" %% "zio-logging-slf4j2" % zioLoggingVersion,
      "dev.zio" %% "zio-kafka-testkit" % zioKafkaVersion % Test
    )

    val loggingDeps = Seq(
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "ch.qos.logback.contrib" % "logback-json-classic" % "0.1.5",
      "ch.qos.logback.contrib" % "logback-jackson" % "0.1.5",
      "org.slf4j" % "slf4j-api" % "2.0.17",

      // For JSON layout
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.19.2"
    )

    val scalafixDependencies = Seq(
      "com.github.liancheng" % "organize-imports_2.13" % "0.6.0" excludeAll (
        ExclusionRule(organization = "org.scala-lang.modules")
      )
    )

}
