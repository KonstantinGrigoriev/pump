package pump.uno.actor

import akka.actor.Actor
import akka.pattern.pipe
import pump.uno.model.Fetch
import pump.uno.service.ForumPageFetcherComponent
import pump.uno.service.impl.SprayForumPageFetcherComponent
import pump.util.{ActorLogging, Loggable}

trait ForumPageFetcherActor extends Actor with Loggable {
  this: ForumPageFetcherComponent =>

  import context.dispatcher

  override def receive = {
    case Fetch(url, auth) =>
      pipe(forumPageFetcher.fetch(url, auth)) pipeTo sender
  }
}

class ForumPageFetcherActorImpl extends ForumPageFetcherActor with SprayForumPageFetcherComponent with ActorLogging
