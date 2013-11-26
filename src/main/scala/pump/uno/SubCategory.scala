package pump.uno

import akka.actor.{ActorLogging, Actor}

object SubCategory {
  case class Load(name:String, category: Category, url:String)
}

class SubCategory extends Actor with ActorLogging {

  def receive = {
    case SubCategory.Load(name, category, url) => {
      log.info(s"processing subcategory $name...")
      Thread.sleep(3000)
      log.info(s"processing subcategory $name...Done")
    }
  }
}
