package pump.uno

import spray.http.HttpCookie

package object model {

  case class Category(id: Long, name: String, url: String)

  case class Fetch(auth: HttpCookie)

  case class FetchCategories(auth: HttpCookie, categories: Seq[Category])

}
