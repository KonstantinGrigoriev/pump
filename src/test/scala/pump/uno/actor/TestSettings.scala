package pump.uno.actor

import java.nio.charset.Charset

import pump.uno.Settings

trait TestSettings {
  val settings: Settings = new Settings {
    override val root = ""

    override val username = "username"

    override val encoding = Charset.forName("UTF-8")

    override val password = "password"

    override val index = "index"

    override val login = "login"
  }

}
