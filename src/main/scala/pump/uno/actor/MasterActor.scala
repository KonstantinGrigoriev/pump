package pump.uno.actor

import akka.actor.{Terminated, _}
import pump.uno.Index

class MasterActor extends Actor with ActorLogging {

  lazy val login = context.actorOf(Props[LoginActorImpl], "login")
  lazy val index = context.actorOf(Props[Index], "index")

  login ! LoginActor.Login

  def receive = {
    case LoginActor.Success(auth) =>
      index ! Index.Fetch(auth)
      context.watch(index)
      context.become(waitingForFinish)
  }

  def waitingForFinish: Receive = {
    case Terminated(x) if x == index =>
      log.info(s"Actor ${x.path} terminated -> terminating Master")
      finish()
  }

  def finish() {
    context.stop(self)
  }
}
