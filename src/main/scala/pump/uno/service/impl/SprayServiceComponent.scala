package pump.uno.service.impl

import akka.actor.ActorContext
import pump.uno.Settings

trait SprayServiceComponent {

  implicit def context: ActorContext

  //  implicit val system: ActorSystem = context.system
  implicit lazy val dispatcher = context.dispatcher

  lazy val settings: Settings = Settings(context.system)
}
