package pump.uno.service

import spray.http.HttpCookie

import scala.concurrent.Future

trait LoginServiceComponent {

  def loginService: LoginService

  trait LoginService {

    def login(): Future[HttpCookie]
  }

}
