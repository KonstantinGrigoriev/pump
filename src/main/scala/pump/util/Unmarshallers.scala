package pump.util

import java.io.{ByteArrayInputStream, InputStreamReader}

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import pump.uno.model.{Forum, ForumPage, Topic}
import spray.http.HttpEntity
import spray.http.MediaTypes._
import spray.httpx.unmarshalling.Unmarshaller

import scala.xml.{Node, NodeSeq, XML}

object Unmarshallers {
  implicit val NodeSeqUnmarshaller =
    Unmarshaller[NodeSeq](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
      case HttpEntity.NonEmpty(contentType, data) ⇒
        val parserFactory = new SAXFactoryImpl
        XML.withSAXParser(parserFactory.newSAXParser()).load(new InputStreamReader(new ByteArrayInputStream(data.toByteArray), contentType.charset.nioCharset))
      case HttpEntity.Empty ⇒ NodeSeq.Empty
    }

  implicit class RichNode(val xml: Node) extends AnyVal {
    def hasClass(cls: String) = {
      (xml \ "@class").text.split("\\s+").contains(cls)
    }
  }

  implicit val pageUnmarshaller =
    Unmarshaller.delegate[NodeSeq, ForumPage](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
      result =>
        val forums = for {
          div <- result \\ "h4" if div hasClass "forumlink"
          anchor <- div \ "a"
          href = anchor \\ "@href" toString()
          id = href.split('=').last.toInt
        } yield Forum(id, anchor.text, href)

        val topics = for {
          div <- result \\ "div" if div hasClass "torTopic"
          anchors = div \ "a" if anchors.length > 1
          anchor = anchors(1)
          href = anchor \\ "@href" toString()
          id = href.split('=').last.toInt
        } yield Topic(id, anchor.text, href)

        val pgElements = result \\ "a" filter (_.hasClass("pg"))
        val totalPages = if (pgElements.isEmpty) {
          1
        } else {
          val lastPgElement = pgElements(pgElements.length - 2)
          lastPgElement.text.toLong
        }
        ForumPage(forums, topics, totalPages)
    }
}
