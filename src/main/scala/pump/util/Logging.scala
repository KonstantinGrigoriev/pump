package pump.util

trait Loggable {
  type LoggerLike = {
    def info(message: String)
    def debug(message: String)
    def error(cause: Throwable, message: String)
  }

  def log: LoggerLike
}

trait NoLogging extends Loggable {
  override val log = new {
    def info(message: String) {}

    def debug(message: String) {}

    def error(cause: Throwable, message: String) {}
  }

}
