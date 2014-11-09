package pump.uno.actor

import akka.actor.{Actor, ActorRef, Props}
import pump.uno.model.{Fetch, TopicPage}
import pump.util.{ActorLogging, Loggable}
import spray.http.HttpCookie

trait TopicPageActor extends Actor with Loggable {

  def createTopicPageFetcherActor: ActorRef

  override def receive = {
    case message: Fetch =>
      createTopicPageFetcherActor ! message
      context.become(waitingForPage(message.auth))
  }

  def waitingForPage(auth: HttpCookie): Receive = {
    case page: TopicPage =>
      // TODO implement
      context.stop(self)
  }
}

class TopicPageActorImpl extends TopicPageActor with ActorLogging {
  override def createTopicPageFetcherActor = context.actorOf(Props[TopicPageFetcherActorImpl])
}
