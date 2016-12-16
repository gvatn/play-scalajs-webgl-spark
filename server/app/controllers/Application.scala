package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.google.inject.Inject
import play.api.mvc._
import shared.SharedMessages
import play.api.libs.streams.ActorFlow
import sparkConn.{SparkCommon, TwitterStreamer}
import sparkStream._

class Application @Inject() (implicit system: ActorSystem,
                             materializer: Materializer/*,
                             twitterStreamer: TwitterStreamer*/) extends Controller {

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }

  def sparkSocket = WebSocket.accept[String,String] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out))
  }

  def setupLogging() = {
   import org.apache.log4j.{Level,Logger}
    val rootLogger = Logger.getRootLogger
    rootLogger.setLevel(Level.ERROR)
  }

  def fontDir(file: String) = Action.async {
    implicit request => {
      controllers.Assets.at("/../client/src/main/resources/", file, false).apply(request)
    }
  }

  def sparkBookData = Action {
    val sc = SparkCommon.sc
    //System.setProperty("twitter4j.oauth")
    val inputList = sc.parallelize(List(1,2,3,4))
    val text = "Test123" + inputList.collect().foldLeft(" ")((a,b) => a + b)

    val input = sc.textFile("book.txt")
    var words = input.flatMap(line => line.split(' '))
    val lowerCaseWords = words.map(_.toLowerCase())
    val wordCounts = lowerCaseWords.countByValue()

    val sample = wordCounts.take(20)

    for ((word, count) <- sample) {
      println(word + " " + count)
    }

    //val ssc = SparkCommon.ssc

    Ok(text)
  }

}
