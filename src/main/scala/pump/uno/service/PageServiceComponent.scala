package pump.uno.service

import pump.uno.model.Page
import spray.http.HttpCookie

import scala.concurrent.Future

trait PageServiceComponent {
  def pageService: PageService

  trait PageService {
    def fetchAll(auth: HttpCookie): Future[Page]
  }

}
