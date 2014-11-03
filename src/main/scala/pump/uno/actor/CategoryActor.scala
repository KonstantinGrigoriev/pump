package pump.uno.actor


import akka.actor.{Actor, ActorLogging}
import pump.uno.model.Category

trait CategoryActor extends Actor with ActorLogging {
  override def receive = {
    case category: Category =>
      log.debug(s"Loading category $category")
      context.stop(self)
  }
}

class CategoryActorImpl extends CategoryActor
