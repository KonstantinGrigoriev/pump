package dispatch.as.jsoup

import com.ning.http.client.Response
import org.jsoup.{Jsoup, nodes}
import org.jsoup.select.Elements
import java.nio.charset.Charset

object DocumentWithEncoding {

  def decode(encoding: Charset)(response: Array[Byte]) = new String(response, encoding)

  def apply(encoding: Charset): Response => nodes.Document =
    dispatch.as.Bytes andThen decode(encoding) andThen Jsoup.parse
}

object QueryWithEncoding {
  def apply(encoding: Charset, query: String): Response => Elements =
    DocumentWithEncoding(encoding)(_).select(query)
}
