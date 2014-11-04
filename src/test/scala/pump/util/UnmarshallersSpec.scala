package pump.util

import org.scalatest.{FlatSpec, Matchers}
import pump.uno.model.{Forum, Topic}
import spray.http._

import scala.io.Source

class UnmarshallersSpec extends FlatSpec with Matchers {

  val sut = Unmarshallers.pageUnmarshaller

  val `text/html` = MediaTypes.`text/html`.withCharset(HttpCharsets.`UTF-8`)

  "Unmarshallers" should "parse page with forums" in {
    // given
    val data = HttpEntity(`text/html`, load("/forums.html"))

    // when
    val result = sut(data)

    // then
    result should be('right)
    val page = result.right.get
    page.forums should have size 3
    page.forums(0) should equal(Forum(1, "forum1", "url?id=1"))
    page.forums(1) should equal(Forum(2, "forum2", "url?id=2"))
    page.forums(2) should equal(Forum(3, "forum3", "url?id=3"))
  }

  it should "parse page with topics" in {
    // given
    val data = HttpEntity(`text/html`, load("/topics.html"))

    // when
    val result = sut(data)

    // then
    result should be('right)
    val page = result.right.get
    page.topics should have size 2
    page.topics(0) should equal(Topic(1, "topic1", "url?id=1"))
    page.topics(1) should equal(Topic(2, "topic2", "url?id=2"))
  }

  it should "parse page with forums and topics" in {
    // given
    val data = HttpEntity(`text/html`, load("/forums-and-topics.html"))

    // when
    val result = sut(data)

    // then
    result should be('right)
    val page = result.right.get
    page.forums should have size 3
    page.topics should have size 2
  }

  it should "parse page without pagination" in {
    // given
    val data = HttpEntity(`text/html`, load("/forums-and-topics.html"))

    // when
    val result = sut(data)

    // then
    result should be('right)
    val page = result.right.get
    page.totalPages should equal(1)
  }

  it should "parse page with pagination" in {
    // given
    val data = HttpEntity(`text/html`, load("/with-pagination.html"))

    // when
    val result = sut(data)

    // then
    result should be('right)
    val page = result.right.get
    page.totalPages should equal(344)
  }

  def load(file: String) =
    Source.fromInputStream(getClass.getResourceAsStream(file)).mkString
}
