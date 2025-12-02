import zio._
import zio.kafka.consumer._
import zio.kafka.serde.*
import zio.config.typesafe.*
import zio.logging.backend.SLF4J
import _root_.config.Configuration.AppConfig

object BasicConsumer extends ZIOAppDefault {

    override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
        Runtime.setConfigProvider(ConfigProvider.fromResourcePath())
            >>> Runtime.removeDefaultLoggers
            >>> SLF4J.slf4j

    val consumerSettingsZIO: ZIO[AppConfig, Throwable, ConsumerSettings] =
        ZIO.serviceWith[AppConfig](config =>
            val saslSettings = Map(
              "sasl.mechanism" -> config.kafka.sasl.mechanism,
              // "sasl.jaas.config" -> s"""org.apache.kafka.common.security.plain.PlainLoginModule required username="\${config.kafka.sasl.username}" password="\${config.kafka.sasl.password}";""",
              "sasl.jaas.config" -> s"""org.apache.kafka.common.security.scram.ScramLoginModule required username="\${config.kafka.sasl.username}" password="\${config.kafka.sasl.password}";""",
              "security.protocol" -> config.kafka.sasl.securityProtocol,
              "enable.auto.commit" -> "false"
              // "log_level" -> "DEBUG"
            )
            ConsumerSettings(config.kafka.bootstrapServers)
                .withGroupId(config.kafka.groupId)
                .withProperty("auto.offset.reset", "earliest")
                .withClientId(config.kafka.clientId)
                .withProperties(saslSettings)
        )

    val subscriptionService: ZIO[AppConfig, Nothing, Subscription] =
        ZIO.serviceWith[AppConfig](config =>
            Subscription.topics(config.kafka.topic)
        )

    val runConsumer: ZIO[AppConfig & Consumer, Throwable, Unit] = for {
        _ <- ZIO.logInfo(s">>> Starting App >>>")
        kafkaConfig <- ZIO.serviceWith[AppConfig](_.kafka)
        _ <- ZIO.logInfo(s">>> KafkaConfig: \$kafkaConfig")
        subscription <- subscriptionService
        consumer <- ZIO.service[Consumer]
        _ <- consumer
            .plainStream(subscription, Serde.string, Serde.string)
            .take(3)
            .runForeach(record =>
                ZIO.logInfo(s"Received: \${record.key} -> \${record.value}")
                // *> record.offset.commit
            )
            .catchAll(err =>
                ZIO.logError(s"Stream drain error \$err")
                    *> ZIO.fail(err).orDie
            )
    } yield ()

    val run: ZIO[Any, Nothing, Unit] =
        runConsumer
            .onInterrupt(
              ZIO.logError(">>> Interrupted")
                  *> ZIO.die(new Exception("Interrupted"))
            )
            .retry(Schedule.fixed(5.seconds) && Schedule.recurs(2))
            .provide(
              AppConfig.layer,
              consumerSettings,
              Consumer.live
            )
            .catchAll(err => ZIO.fail(err).orDie)
}
