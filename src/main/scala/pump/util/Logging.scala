package pump.util

import akka.actor.Actor
import akka.event.LoggingAdapter

trait Loggable {

  trait LoggerLike {
    def info(message: String)

    def debug(message: String)

    def error(cause: Throwable, message: String)
  }

  def log: LoggerLike
}

trait NoLogging extends Loggable {
  override val log = new LoggerLike {
    override def info(message: String) {}

    override def debug(message: String) {}

    override def error(cause: Throwable, message: String) {}
  }

}

trait ActorLogging extends Loggable {
  this: Actor =>

  private lazy val _log: LoggingAdapter = akka.event.Logging(context.system, this)

  override val log = new LoggerLike {
    override def info(message: String) = _log.info(message)

    override def debug(message: String) = _log.debug(message)

    override def error(cause: Throwable, message: String) = _log.error(cause, message)
  }

}
