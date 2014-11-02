package pump.uno.service.impl

import pump.uno.service.LoginServiceComponent
import pump.util.Loggable
import spray.client.pipelining._
import spray.http.HttpHeaders.`Set-Cookie`
import spray.http.{FormData, HttpCookie, HttpRequest, HttpResponse}

import scala.concurrent.Future

trait SprayLoginServiceComponent extends LoginServiceComponent with SprayServiceComponent with Loggable {

  override val loginService = new SprayLoginService

  class SprayLoginService extends LoginService {

    def login(): Future[HttpCookie] = {
      log.debug("login...")
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

}
