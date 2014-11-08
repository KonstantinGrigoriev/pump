package pump.uno

import spray.http.HttpCookie

package object model {

  case class Forum(id: Long, name: String, url: String)

  case class Topic(id: Long, name: String, url: String)

  case class ForumPage(forums: Seq[Forum], topics: Seq[Topic], totalPages: Long)

  case class TopicPage()

  case class Fetch(url: String, auth: HttpCookie)

}
