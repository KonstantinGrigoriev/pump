package pump.uno.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import pump.uno.model.{Fetch, FetchCategories}
import pump.uno.service.IndexServiceComponent
import pump.uno.service.impl.SprayIndexServiceComponent

trait IndexActor extends Actor with ActorLogging with StopOnChildTermination {
  this: IndexServiceComponent =>

  import context.dispatcher

  lazy val categoriesActor = context.actorOf(Props[CategoriesActorImpl])

  override def receive = {
    case Fetch(auth) =>
      log.info("index...")
      indexService.fetch(auth).map(FetchCategories(auth, _)) pipeTo categoriesActor
      waitForTermination(categoriesActor)
  }
}

class IndexActorImpl extends IndexActor with SprayIndexServiceComponent
