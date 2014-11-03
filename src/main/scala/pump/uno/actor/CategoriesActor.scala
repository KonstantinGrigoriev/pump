package pump.uno.actor

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import pump.uno.model.FetchCategories

trait CategoriesActor extends Actor with ActorLogging {
  override def receive = {
    case FetchCategories(auth, categories) =>
      log.info(s"Fetching ${categories.length} categories ")
      categories.foreach { category =>
        val categoryActor = context.actorOf(Props[CategoryActorImpl], category.id.toString)
        context.watch(categoryActor)
        categoryActor ! category
      }
      context.become(waitingForChildren(categories.length))
  }

  def waitingForChildren(children: Int): Receive = {
    case Terminated(x) =>
      if (children == 1) context.stop(self)
      else context.become(waitingForChildren(children - 1))
  }
}

class CategoriesActorImpl extends CategoriesActor
