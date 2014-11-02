package pump.uno

import java.nio.charset.Charset

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.config.Config

trait Settings {
  def root: String

  def index: String

  def login: String

  def username: String

  def password: String

  def encoding: Charset
}

class SettingsImpl(config: Config) extends Extension with Settings {
  private val uno = config.getConfig("pump.uno")
  override val root: String = uno.getString("page.root")
  override val index: String = uno.getString("page.index")
  override val login: String = uno.getString("page.login")
  override val username: String = uno.getString("username")
  override val password: String = uno.getString("password")
  override val encoding: Charset = Charset.forName(uno.getString("encoding"))
}

object Settings extends ExtensionId[SettingsImpl] with ExtensionIdProvider {

  override def lookup() = Settings

  override def createExtension(system: ExtendedActorSystem) =
    new SettingsImpl(system.settings.config)

  override def get(system: ActorSystem): SettingsImpl = super.get(system)
}
