package config

import zio.*
import zio.config.*
import zio.config.typesafe.*
import zio.config.magnolia.*
import zio.config.derivation.name
import zio.kafka.consumer.*

final case class SaslConfig(
    username: String,
    password: String,
    securityProtocol: String,
    mechanism: String
)

final case class KafkaConfig(
    bootstrapServers: List[String],
    groupId: String,
    clientId: String,
    topic: String,
    sasl: SaslConfig
)

case class AppConfig(kafka: KafkaConfig)

object AppConfig {
    val layer: ZLayer[Any, Config.Error, AppConfig] =
        ZLayer(
          ZIO.config[AppConfig](
            deriveConfig[AppConfig].mapKey(toKebabCase)
          )
        )
}
