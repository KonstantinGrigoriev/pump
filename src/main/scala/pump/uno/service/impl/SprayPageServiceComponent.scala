package pump.uno.service.impl

import pump.uno.model.Page
import pump.uno.service.PageServiceComponent
import pump.util.Loggable
import pump.util.Unmarshallers._
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest}

import scala.concurrent.Future

trait SprayPageServiceComponent extends PageServiceComponent with SprayServiceComponent with Loggable {

  override val pageService = new SprayPageService

  class SprayPageService extends PageService {

    def fetchAll(auth: HttpCookie): Future[Page] = {
      val pipeline: HttpRequest => Future[Page] =
        addHeader(Cookie(auth)) ~> sendReceive ~> unmarshal[Page]
      pipeline(Get(settings.index))
    }
  }

}
