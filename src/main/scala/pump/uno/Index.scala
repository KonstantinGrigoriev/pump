package pump.uno

import java.io.{ByteArrayInputStream, InputStreamReader}

import akka.actor.{Actor, ActorLogging, PoisonPill}
import akka.routing.Broadcast
import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import spray.client.pipelining._
import spray.http.HttpHeaders.{Cookie, `Set-Cookie`}
import spray.http.MediaTypes._
import spray.http._
import spray.httpx.unmarshalling.Unmarshaller

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.xml.{NodeSeq, XML}

object Index {

  case object Load

}

class Index extends Actor with ActorLogging {

  val categoryRouter = context.actorSelection("/user/app/categoryRouter")
  val settings = Settings(context.system)

  import context.dispatcher

  implicit val NodeSeqUnmarshaller =
    Unmarshaller[NodeSeq](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
      case HttpEntity.NonEmpty(contentType, data) ⇒
        val parserFactory = new SAXFactoryImpl
        XML.withSAXParser(parserFactory.newSAXParser()).load(new InputStreamReader(new ByteArrayInputStream(data.toByteArray), contentType.charset.nioCharset))
      case HttpEntity.Empty ⇒ NodeSeq.Empty
    }

  def receive = {
    case Index.Load =>

      login().flatMap {
        cookie => {
          loadCategories(cookie)
        }
      }.onComplete {
        case Success(result) =>
          val categories = (result \\ "div" filter (el => (el \ "@class" toString()) == "category")) \\ "h3" \\ "a"
          log.info(s"submitting categories...${categories.length}")
          categories.foreach {
            categoryLink =>
              categoryRouter ! Category.Load(categoryLink.text, categoryLink \\ "@href" toString(), null)
          }
          log.info("submitting categories...Done")

          categoryRouter ! Broadcast(PoisonPill)
        case Failure(exception) =>
          log.error(exception, "error")
      }
  }

  def loadCategories(auth: HttpCookie) = {
    val pipeline: HttpRequest => Future[NodeSeq] =
      addHeader(Cookie(auth)) ~> sendReceive ~> unmarshal[NodeSeq]
    pipeline(Get(settings.index))
  }

  def login(): Future[HttpCookie] = {
    log.info("login...")
    val params = FormData(Seq(
      "login_username" -> settings.username,
      "login_password" -> settings.password,
      "login" -> "%C2%F5%EE%E4"
    ))

    val extractCookie: HttpResponse => HttpCookie =
      it => it.headers.collect { case `Set-Cookie`(hc) => hc}.head

    val pipeline: HttpRequest => Future[HttpCookie] =
      sendReceive ~> extractCookie

    pipeline(Post(settings.login, params))
  }
}
