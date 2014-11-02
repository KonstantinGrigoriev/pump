package pump.uno.actor

import akka.actor.{Actor, ActorLogging}

class IndexActor extends Actor with ActorLogging {
  override def receive = {
    case _ =>
      log.info("index...")
      context.stop(self)
  }
}
