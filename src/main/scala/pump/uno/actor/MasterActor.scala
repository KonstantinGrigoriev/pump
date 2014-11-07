package pump.uno.actor

import akka.actor._
import pump.uno.Settings
import pump.uno.model.Fetch

class MasterActor extends Actor with ActorLogging with StopOnChildTermination {
  lazy val settings: Settings = Settings(context.system)

  lazy val loginActor = context.actorOf(Props[LoginActorImpl], "login")
  lazy val indexActor = context.actorOf(Props[ForumPageActor], "index")

  loginActor ! LoginActor.Login

  def receive = {
    case LoginActor.Success(auth) =>
      log.info(s"Login success ($auth) -> loading index")
      indexActor ! Fetch(settings.index, auth)
      waitForTermination(indexActor)
  }

}
