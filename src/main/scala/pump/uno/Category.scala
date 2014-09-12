package pump.uno

import akka.actor.{ActorLogging, Actor}
import spray.http.HttpHeaders.Cookie

object Category {

  case class Load(name: String,
                  url: String,
                  auth: Cookie)
}

class Category extends Actor with ActorLogging {
  val settings = Settings(context.system)
  val subCategoryRouter = context.actorSelection("/user/app/subCategoryRouter")

  def receive = {
    case Category.Load(name, url, cookie) => {
      log.info(s"processing category '$name'")
      log.info(" submitting subcategories...")
//            for (n <- 1 to 3) subCategoryRouter ! Message("%s.%d".format(msg, n))
      log.info(" submitting subcategories...Done")
    }
  }
//
//  def loadSubCategories(auth: Cookie, categoryUrl:String) = {
//    val svc = (url(settings.root) / categoryUrl).addCookie(auth)
//    Http(svc OK as.jsoup.QueryWithEncoding(settings.encoding, "div.category > h3 > a"))
//  }
}
