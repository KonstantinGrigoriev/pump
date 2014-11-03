package pump.util

import java.io.{ByteArrayInputStream, InputStreamReader}

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import pump.uno.model.{Forum, Page}
import spray.http.HttpEntity
import spray.http.MediaTypes._
import spray.httpx.unmarshalling.Unmarshaller

import scala.xml.{NodeSeq, XML}

object Unmarshallers {
  implicit val NodeSeqUnmarshaller =
    Unmarshaller[NodeSeq](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
      case HttpEntity.NonEmpty(contentType, data) ⇒
        val parserFactory = new SAXFactoryImpl
        XML.withSAXParser(parserFactory.newSAXParser()).load(new InputStreamReader(new ByteArrayInputStream(data.toByteArray), contentType.charset.nioCharset))
      case HttpEntity.Empty ⇒ NodeSeq.Empty
    }

  implicit val CategoryUnmarshaller =
    Unmarshaller.delegate[NodeSeq, Page](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
      result =>
        val forums = ((result \\ "h4" filter (el => (el \ "@class" toString()) == "forumlink")) \\ "a").map {
          node =>
            val href = node \\ "@href" toString()
            val id = href.split('=').last.toInt
            Forum(id, node.text, href)
        }
        val topics = List()
        // TODO fix topics
        //        val topics = (((result \\ "div" filter (el => (el \ "@class" toString()) == "torTopic")) \\ "h3") \\ "a").map {
        //          node =>
        //            val href = node \\ "@href" toString()
        //            val id = href.split('=').last.toInt
        //            Topic(id, node.text, href)
        //        }
        Page(forums, topics)
    }
}
