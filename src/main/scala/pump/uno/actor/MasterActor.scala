package pump.uno.actor

import akka.actor._
import pump.uno.model.Fetch

class MasterActor extends Actor with ActorLogging with StopOnChildTermination {

  lazy val loginActor = context.actorOf(Props[LoginActorImpl], "login")
  lazy val indexActor = context.actorOf(Props[IndexActorImpl], "index")

  loginActor ! LoginActor.Login

  def receive = {
    case LoginActor.Success(auth) =>
      log.info(s"Login success ($auth) -> loading index")
      indexActor ! Fetch(auth)
      waitForTermination(indexActor)
  }

}
