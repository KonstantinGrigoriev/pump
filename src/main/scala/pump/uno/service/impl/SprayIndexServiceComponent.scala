package pump.uno.service.impl

import pump.uno.model.Category
import pump.uno.service.IndexServiceComponent
import pump.util.Loggable
import pump.util.Unmarshallers._
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest}

import scala.concurrent.Future

trait SprayIndexServiceComponent extends IndexServiceComponent with SprayServiceComponent with Loggable {

  override val indexService = new SprayIndexService

  class SprayIndexService extends IndexService {

    def fetch(auth: HttpCookie): Future[Seq[Category]] = {
      val pipeline: HttpRequest => Future[Seq[Category]] =
        addHeader(Cookie(auth)) ~> sendReceive ~> unmarshal[Seq[Category]]
      pipeline(Get(settings.index))
    }
  }

}
