import akka.{Main => AkkaMain}
import pump.uno.actor.MasterActorImpl

object Main extends App {
  AkkaMain.main(Array(classOf[MasterActorImpl].getName))
}
