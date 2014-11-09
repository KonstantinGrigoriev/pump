package pump.uno.actor

import akka.actor.Actor
import akka.pattern.pipe
import pump.uno.model.Fetch
import pump.uno.service.TopicPageFetcherComponent
import pump.uno.service.impl.SprayTopicPageFetcherComponent
import pump.util.{ActorLogging, Loggable}

trait TopicPageFetcherActor extends Actor with Loggable {
  this: TopicPageFetcherComponent =>

  import context.dispatcher

  override def receive = {
    case Fetch(url, auth) =>
      pipe(topicPageFetcher.fetch(url, auth)) pipeTo sender
  }
}

class TopicPageFetcherActorImpl extends TopicPageFetcherActor with SprayTopicPageFetcherComponent with ActorLogging
