package pump.uno.actor

import akka.actor.{Actor, Props}
import pump.uno.Settings
import pump.uno.model.Fetch
import pump.util.{ActorLogging, Loggable}

trait MasterActor extends Actor with StopOnChildTermination with Loggable {
  lazy val settings: Settings = Settings(context.system)

  lazy val loginActor = context.actorOf(Props[LoginActorImpl], "login")
  lazy val indexActor = context.actorOf(Props[ForumPageActorImpl], "index")

  loginActor ! LoginActor.Login

  def receive = {
    case LoginActor.Success(auth) =>
      log.info(s"Login success ($auth) -> loading index from '${settings.index}'")
      indexActor ! Fetch(settings.index, auth)
      waitForTermination(indexActor)
  }
}

class MasterActorImpl extends MasterActor with ActorLogging
