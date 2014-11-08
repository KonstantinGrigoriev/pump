package pump.uno.actor

import akka.actor.{Actor, ActorRef, Props, Terminated}
import pump.uno.Settings
import pump.uno.model.{Fetch, ForumPage}
import pump.util.{ActorLogging, Loggable}
import spray.http.HttpCookie

trait ForumPageActor extends Actor with Loggable {
  def settings: Settings

  def createForumPageFetcherActor: ActorRef

  def createForumPageActor: ActorRef

  override def receive: Receive = {
    case message: Fetch =>
      createForumPageFetcherActor ! message
      context.become(waitingForPage(message.auth))
  }

  def waitingForPage(auth: HttpCookie): Receive = {
    case page: ForumPage =>
      log.debug(s" processing ${page.forums.length} subforums")
      page.forums.foreach { forum =>
        val forumPageActor = createForumPageActor
        context.watch(forumPageActor)
        forumPageActor ! Fetch(settings.root + forum.url, auth)
      }
      log.debug(s" processing ${page.topics.length} topics")
      val children = page.forums.length
      if (children == 0) {
        log.debug(s"  no children -> stopping right now")
        context.stop(self)
      } else {
        log.debug(s"  waiting for $children children to finish")
        context.become(waitingForChildren(children))
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
}
