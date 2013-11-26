package pump.uno

import akka.actor.{PoisonPill, ActorLogging, Actor}
import akka.routing.Broadcast
import scala.collection.convert.WrapAsScala._
import com.ning.http.client.Cookie
import dispatch._, Defaults._
import java.nio.charset.Charset

object Index {

  case object Load

}

class Index extends Actor with ActorLogging {

  val categoryRouter = context.actorSelection("/user/app/categoryRouter")
  val settings = Settings(context.system)

  def receive = {
    case Index.Load => {
      val auth = login()
      for (categories <- loadCategories(auth)) {
        log.info(s"submitting categories...${categories.length}")
        categories.foreach {
          categoryLink =>
            categoryRouter ! Category.Load(categoryLink.text,
              categoryLink.attr("href"), auth)
        }
        log.info("submitting categories...Done")

        categoryRouter ! Broadcast(PoisonPill)
      }
    }
  }

  def loadCategories(auth: Cookie) = {
    val svc = url(settings.index).addCookie(auth)
    Http(svc OK as.jsoup.QueryWithEncoding(settings.encoding, "div.category > h3 > a"))
  }

  def login(): Cookie = {
    val params = Map(
      "login_username" -> settings.username,
      "login_password" -> settings.password,
      "login" -> "%C2%F5%EE%E4"
    )
    val svc = url(settings.login) << params
    val response = Http(svc)

    response().getCookies.head
  }
}
