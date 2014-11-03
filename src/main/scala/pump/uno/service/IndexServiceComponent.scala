package pump.uno.service

import pump.uno.model.Category
import spray.http.HttpCookie

import scala.concurrent.Future

trait IndexServiceComponent {
  def indexService: IndexService

  trait IndexService {
    def fetch(auth: HttpCookie): Future[Seq[Category]]
  }

}
