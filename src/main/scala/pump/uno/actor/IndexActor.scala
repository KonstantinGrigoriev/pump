package pump.uno.actor

import akka.actor.{Actor, ActorLogging, Props}
import pump.uno.model.Fetch
import pump.uno.service.PageServiceComponent
import pump.uno.service.impl.SprayPageServiceComponent

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait IndexActor extends Actor with ActorLogging with StopOnChildTermination {
  this: PageServiceComponent =>

  lazy val categoriesActor = context.actorOf(Props[CategoriesActorImpl], "categories")

  override def receive = {
    case Fetch(auth) =>
      log.info("index...")
      val page = Await.result(pageService.fetchAll(auth), Duration.Inf) //.map(FetchCategories(auth, _)) pipeTo categoriesActor
      log.debug(" page " + page)
      context.stop(self)
    //      waitForTermination(categoriesActor)
  }
}

class IndexActorImpl extends IndexActor with SprayPageServiceComponent
