package pump.uno

import spray.http.HttpCookie

package object model {

  case class Forum(id: Long, name: String, url: String)

  case class Topic(id: Long, name: String, url: String)

  case class Page(forums: Seq[Forum], topics: Seq[Topic], totalPages: Long)

  case class Category(id: Long, name: String, url: String)

  case class Fetch(url: String, auth: HttpCookie)

  case class FetchCategories(auth: HttpCookie, categories: Seq[Category])

}
