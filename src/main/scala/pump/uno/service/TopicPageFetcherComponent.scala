package pump.uno.service

import pump.uno.model.TopicPage
import spray.http.HttpCookie

import scala.concurrent.Future

trait TopicPageFetcherComponent {
  def topicPageFetcher: TopicPageFetcher

  trait TopicPageFetcher {
    def fetch(url: String, auth: HttpCookie): Future[TopicPage]
  }

}
