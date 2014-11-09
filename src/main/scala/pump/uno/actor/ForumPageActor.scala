package pump.uno.actor

import akka.actor.{Actor, ActorRef, Props, Terminated}
import pump.uno.Settings
import pump.uno.model.{Fetch, Forum, ForumPage, Topic}
import pump.util.{ActorLogging, Loggable}
import spray.http.HttpCookie

trait ForumPageActor extends Actor with Loggable {
  def settings: Settings

  def createForumPageFetcherActor: ActorRef

  def createForumPageActor: ActorRef

  def createTopicPageActor: ActorRef

  override def receive: Receive = {
    case message: Fetch =>
      createForumPageFetcherActor ! message
      context.become(waitingForPage(message.auth))
  }

  def waitingForPage(auth: HttpCookie): Receive = {
    case page: ForumPage =>
      val children = page.forums.length + page.topics.length
      if (children == 0) {
        log.debug(s"  no children -> stopping right now")
        context.stop(self)
      } else {
        log.debug(s"  waiting for $children children to finish")
        context.become(waitingForChildren(children))
      }
      processForums(auth, page.forums)
      processTopics(auth, page.topics)
  }

  private def processForums(auth: HttpCookie, forums: Seq[Forum]) {
    log.debug(s" processing ${forums.length} subforums")
    forums.foreach { forum =>
      val forumPageActor = createForumPageActor
      context.watch(forumPageActor)
      forumPageActor ! Fetch(settings.root + forum.url, auth)
    }
  }

  private def processTopics(auth: HttpCookie, topics: Seq[Topic]) {
    log.debug(s" processing ${topics.length} topics")
    topics.foreach { topic =>
      val topicPageActor = createTopicPageActor
      context.watch(topicPageActor)
      topicPageActor ! Fetch(settings.root + topic.url, auth)
    }
  }

  def waitingForChildren(children: Int): Receive = {
    case Terminated(_) =>
      if (children == 1) context.stop(self)
      else context.become(waitingForChildren(children - 1))
  }
}

class ForumPageActorImpl extends ForumPageActor with ActorLogging {
  override lazy val settings = Settings(context.system)

  override def createForumPageFetcherActor = context.actorOf(Props[ForumPageFetcherActorImpl], "fpf")

  override def createForumPageActor = context.actorOf(Props[ForumPageActorImpl])

  override def createTopicPageActor = context.actorOf(Props[TopicPageActorImpl])
}
