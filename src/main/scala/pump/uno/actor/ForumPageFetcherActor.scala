package pump.uno.actor

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import pump.uno.model.Fetch
import pump.uno.service.ForumPageFetcherComponent
import pump.uno.service.impl.SprayForumPageFetcherComponent

trait ForumPageFetcherActor extends Actor with ActorLogging {
  this: ForumPageFetcherComponent =>

  override def receive = {
    case Fetch(url, auth) =>
      forumPageFetcher.fetch(url, auth) pipeTo sender
  }
}

class ForumPageFetcherActorImpl extends ForumPageFetcherActor with SprayForumPageFetcherComponent
