package pump.uno.service.impl

import pump.uno.model.Page
import pump.uno.service.ForumPageFetcherComponent
import pump.util.Loggable
import pump.util.Unmarshallers._
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest}

import scala.concurrent.Future

trait SprayForumPageFetcherComponent extends ForumPageFetcherComponent with SprayServiceComponent with Loggable {

  override val forumPageFetcher = new SprayForumPageFetcher

  class SprayForumPageFetcher extends ForumPageFetcher {

    def fetch(url: String, auth: HttpCookie): Future[Page] = {
      val pipeline: HttpRequest => Future[Page] =
        addHeader(Cookie(auth)) ~> sendReceive ~> unmarshal[Page]
      pipeline(Get(url))
    }
  }

}