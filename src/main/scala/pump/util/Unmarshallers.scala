package pump.util

import java.io.{ByteArrayInputStream, InputStreamReader}

import org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
import pump.uno.model.Category
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
    Unmarshaller.delegate[NodeSeq, Seq[Category]](`text/xml`, `application/xml`, `text/html`, `application/xhtml+xml`) {
      result =>
        (((result \\ "div" filter (el => (el \ "@class" toString()) == "category")) \\ "h3") \\ "a").map {
          node =>
            Category(node.text, node \\ "@href" toString())
        }
    }
}
