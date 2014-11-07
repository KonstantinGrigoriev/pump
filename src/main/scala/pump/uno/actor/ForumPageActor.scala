package pump.uno.actor

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import pump.uno.Settings
import pump.uno.model.{Fetch, Page}
import spray.http.HttpCookie

class ForumPageActor extends Actor with ActorLogging {
  lazy val settings: Settings = Settings(context.system)

  override def receive: Receive = {
    case request: Fetch =>
      context.actorOf(Props[ForumPageFetcherActorImpl], "fpf") ! request
      context.become(waitingForPage(request.auth))
  }

  def waitingForPage(auth: HttpCookie): Receive = {
    case page: Page =>
      page.forums.foreach { forum =>
        context.actorOf(Props[ForumPageActor]) ! Fetch(settings.root + forum.url, auth)
      }
      context.become(waitingForChildren(page.forums.length))
  }

  def waitingForChildren(children: Int): Receive = {
    case Terminated(x) =>
      if (children == 1) context.stop(self)
      else context.become(waitingForChildren(children - 1))
  }
}
