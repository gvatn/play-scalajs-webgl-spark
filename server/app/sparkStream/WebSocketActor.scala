package sparkStream

import akka.actor._
import sparkConn.{SparkCommon, TwitterStreamer}
import sparkStream.WebSocketActor.TwitterMessage

object WebSocketActor {
  def props(out: ActorRef) = Props(new WebSocketActor(out))

  case class TwitterMessage(message: String)
}

class WebSocketActor(out: ActorRef) extends Actor {

  TwitterStreamer.registerSocket(self)

  override def receive = {
    case msg: String => out ! msg
    case TwitterMessage(message) => out ! message
  }

  override def postStop(): Unit = {
    TwitterStreamer.unregisterSocket(self)
  }

}
