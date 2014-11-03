package pump.util

import java.io.{ByteArrayInputStream, InputStreamReader}

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import pump.uno.model.{Forum, Page, Topic}
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
    Unmarshaller.delegate[NodeSeq, Page](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
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

        Page(forums, topics)
    }
}
