package pump.uno.service

import pump.uno.model.ForumPage
import spray.http.HttpCookie

import scala.concurrent.Future

trait ForumPageFetcherComponent {
  def forumPageFetcher: ForumPageFetcher

  trait ForumPageFetcher {
    def fetch(url: String, auth: HttpCookie): Future[ForumPage]
  }

}
