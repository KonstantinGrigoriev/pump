package pump.uno

import akka.actor.ActorSystem
import akka.actor.Extension
import akka.actor.ExtensionId
import akka.actor.ExtensionIdProvider
import akka.actor.ExtendedActorSystem
import com.typesafe.config.{ConfigFactory, Config}
import java.nio.charset.Charset

class SettingsImpl(config: Config) extends Extension {
  private val uno = config.getConfig("pump.uno")
  val root: String = uno.getString("page.root")
  val index: String = uno.getString("page.index")
  val login: String = uno.getString("page.login")
  val username: String = uno.getString("username")
  val password: String = uno.getString("password")
  val encoding: Charset = Charset.forName(uno.getString("encoding"))
}

object Settings extends ExtensionId[SettingsImpl] with ExtensionIdProvider {

  override def lookup() = Settings

  override def createExtension(system: ExtendedActorSystem) =
    new SettingsImpl(system.settings.config)

  override def get(system: ActorSystem): SettingsImpl = super.get(system)
}
