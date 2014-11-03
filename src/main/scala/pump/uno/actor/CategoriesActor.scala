package pump.uno.actor

import akka.actor.{Actor, ActorLogging}
import pump.uno.model.FetchCategories

trait CategoriesActor extends Actor with ActorLogging {
  override def receive = {
    case FetchCategories(auth, categories) =>
      log.info("Fetching categories " + categories.length)
      context.stop(self)
  }
}

class CategoriesActorImpl extends CategoriesActor
