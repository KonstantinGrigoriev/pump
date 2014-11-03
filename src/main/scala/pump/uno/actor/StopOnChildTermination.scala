package pump.uno.actor

import akka.actor.{Actor, ActorRef, Terminated}
import pump.util.Loggable

trait StopOnChildTermination extends Loggable {
  this: Actor =>

  def waitForTermination(actor: ActorRef) {
    context.watch(actor)
    context.become(waitingForTermination(actor))
  }

  def waitingForTermination(actor: ActorRef): Receive = {
    case Terminated(x) if x == actor =>
      log.info(s"Actor ${x.path} terminated -> terminating ${self.path}")
      context.stop(self)
  }
}
