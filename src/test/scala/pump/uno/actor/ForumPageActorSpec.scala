package pump.uno.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import pump.uno.model.{Fetch, Forum, ForumPage}
import pump.util.ActorLogging
import spray.http.HttpCookie

import scala.collection.mutable.ListBuffer

class ForumPageActorSpec extends TestKit(ActorSystem("testSystem")) with FlatSpecLike with Matchers with BeforeAndAfterAll {

  case object given {
    val auth = HttpCookie("", "")
    val message = Fetch("some-url", auth)
  }

  trait ctx {
    val forumPageFetcherProbe = TestProbe()
    val forumPageProbes = ListBuffer[TestProbe]()

    val sut = TestActorRef[ForumPageActor](Props(new ForumPageActor with TestSettings with ActorLogging {
      override def createForumPageActor = {
        val forumPageProbe = TestProbe()
        forumPageProbes += forumPageProbe
        forumPageProbe.ref
      }

      override def createForumPageFetcherActor = forumPageFetcherProbe.ref
    }))
  }

  "ForumPageActor" should "send message to fetcher and start waiting for response" in new ctx {
    // when
    sut ! given.message

    // then
    forumPageFetcherProbe.expectMsg(given.message)
    sut ! given.message
    forumPageFetcherProbe.expectNoMsg()
  }

  it should "receive response from fetcher and send message for each forum link" in new ctx {
    // given
    val forumPage = ForumPage(List(Forum(1, "", "forum1-url"), Forum(1, "", "forum2-url")), List(), 1)
    sut.underlyingActor.context.become(sut.underlyingActor.waitingForPage(given.auth))

    // when
    forumPageFetcherProbe.send(sut, forumPage)

    // then
    forumPageProbes should have size 2
    forumPageProbes(0).expectMsg(Fetch("forum1-url", given.auth))
    forumPageProbes(1).expectMsg(Fetch("forum2-url", given.auth))
  }

  it should "stop if all children are terminated" in new ctx {
    // given
    val forumPage = ForumPage(List(Forum(1, "", "forum1-url"), Forum(1, "", "forum2-url")), List(), 1)
    sut.underlyingActor.context.become(sut.underlyingActor.waitingForPage(given.auth))
    forumPageFetcherProbe.send(sut, forumPage)
    forumPageProbes(0).expectMsg(Fetch("forum1-url", given.auth))
    forumPageProbes(1).expectMsg(Fetch("forum2-url", given.auth))
    watch(sut)

    // when
    forumPageProbes.foreach { probe => system.stop(probe.ref)}

    // then
    expectTerminated(sut)
  }

  it should "stop if no children to process" in new ctx {
    // given
    val forumPage = ForumPage(List(), List(), 1)
    sut.underlyingActor.context.become(sut.underlyingActor.waitingForPage(given.auth))
    watch(sut)

    // when
    forumPageFetcherProbe.send(sut, forumPage)

    // then
    expectTerminated(sut)
  }

  override protected def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }
}
