package pump.uno.service.impl

import pump.uno.model.TopicPage
import pump.uno.service.TopicPageFetcherComponent
import pump.util.Loggable
import pump.util.Unmarshallers._
import spray.client.pipelining._
import spray.http.HttpHeaders.Cookie
import spray.http.{HttpCookie, HttpRequest}

import scala.concurrent.Future

trait SprayTopicPageFetcherComponent extends TopicPageFetcherComponent with SprayServiceComponent with Loggable {

  override val topicPageFetcher = new SprayTopicPageFetcher

  class SprayTopicPageFetcher extends TopicPageFetcher {

    def fetch(url: String, auth: HttpCookie): Future[TopicPage] = {
      log.debug(s"Fetching TOPIC from '$url'")
      val pipeline: HttpRequest => Future[TopicPage] =
        addHeader(Cookie(auth)) ~> sendReceive ~> unmarshal[TopicPage]
      pipeline(Get(url))
    }
  }

}
