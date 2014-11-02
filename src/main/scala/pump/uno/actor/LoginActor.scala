package pump.uno.actor

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import pump.uno.service.LoginServiceComponent
import pump.uno.service.impl.SprayLoginServiceComponent
import spray.http.HttpCookie

object LoginActor {

  case object Login

  case class Success(cookie: HttpCookie)

}

trait LoginActor extends Actor with ActorLogging {
  this: LoginServiceComponent =>

  import context.dispatcher
  import pump.uno.actor.LoginActor._

  override def receive = {
    case Login =>
      loginService.login().map(Success) pipeTo sender
  }


}

class LoginActorImpl extends LoginActor with SprayLoginServiceComponent
