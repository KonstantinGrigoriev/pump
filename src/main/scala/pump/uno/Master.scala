package pump.uno

import akka.actor._
import akka.routing.{FromConfig, Broadcast}
import akka.actor.Terminated
import dispatch.Http

class Master extends Actor with ActorLogging {

  val categoryRouter = context.actorOf(Props[Category].withRouter(FromConfig()), name = "categoryRouter")

  val subCategoryRouter = context.actorOf(Props[SubCategory].withRouter(FromConfig()), name = "subCategoryRouter")

  context.watch(categoryRouter)
  context.watch(subCategoryRouter)

  val index = context.actorOf(Props[Index])

  index ! Index.Load

  def receive = {
    case Terminated(x) if x == categoryRouter => {
      log.info("categories processing finished -> sending poison pill to subcategories")
      subCategoryRouter ! Broadcast(PoisonPill)
    }
    case Terminated(x) if x == subCategoryRouter => finish()
    case _ => log.error("Got a message I don't understand.")
  }

  def finish() {
    Http.shutdown()
    context.stop(self)
  }
}
