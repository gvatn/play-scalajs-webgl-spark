package sparkConn

import javax.inject._

import akka.actor.ActorRef
import org.apache.spark.streaming.Seconds
import play.api.inject.ApplicationLifecycle
import sparkStream.TwitterReceiver

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

object TwitterStreamer {

  val webSocketActors: ArrayBuffer[ActorRef] = ArrayBuffer[ActorRef]()

  def broadCastMessage(message: String) = {
    webSocketActors.foreach { socketActor =>
      socketActor ! message
    }
  }

  def registerSocket(socketActor: ActorRef) = {
    webSocketActors += socketActor
  }

  def unregisterSocket(socketActor: ActorRef) = {
    webSocketActors -= socketActor
  }
}

@Singleton
class TwitterStreamer @Inject() (lifecycle: ApplicationLifecycle) {

  val ssc = SparkCommon.ssc

  val twitterProps = Map(
    "consumerKey" -> "OPkX8Iv0wMzEs6TvZEFTEXHHv",
    "consumerSecret" -> "Y0MkBVlWdUm7Ono1QjQy91R0XcWsjzXyJfdOEDluonBTlJp3QB",
    "accessToken" -> "192328239-m2FtMS0bPsPWs7o6VHulMQXJkv52JIQEN2j7qSJF",
    "accessTokenSecret" -> "ykZ0rPV8WsjPLlpnZiqYT4CO3gRyd7z3RIlRmT46taf8T"
  )
  for ((key, value) <- twitterProps) {
    System.setProperty("twitter4j.oauth." + key, value)
  }

  val receiver = ssc.receiverStream(new TwitterReceiver)

  val texts = receiver.map(status => status.getText)

  val words = texts.flatMap(text => text.split(" ")).map(_.toLowerCase())
  val unCommonWords = words.filter { word =>
    !SparkCommon.noiseWords.contains(word) && word.forall(char => SparkCommon.allowedChars.contains(char)) && !SparkCommon.mostUsedWords.contains(word)
  }
  val wordKeyValues = unCommonWords.map(word => (word, 1))
  val wordCounts = wordKeyValues.reduceByKeyAndWindow(
    (x,y) => x + y,
    (x,y) => x - y,
    Seconds(200),
    Seconds(5)
  )
  val sortedWords = wordCounts.transform(rdd => rdd.sortBy(x => x._2, false))
  sortedWords.foreachRDD { (rdd,time) =>
    val collected = rdd.collect()
    val summary = collected.take(4).map(wordCount => wordCount._1 + ": " + wordCount._2).mkString(", ")
    TwitterStreamer.broadCastMessage(summary)
  }
  // Trending hash tags
  val hashTagCounts = words.filter(_.startsWith("#"))
    .map((_, 1))
    .reduceByKeyAndWindow(_ + _, _ - _, Seconds(200), Seconds(5))
    .transform(rdd => rdd.sortBy(x => x._2, false))
  hashTagCounts.foreachRDD { (rdd, time) =>
    val collected = rdd.collect()
    val summary = collected.take(8).map(tagCount => tagCount._1 + ": " + tagCount._2).mkString(", ")
    TwitterStreamer.broadCastMessage(summary)
  }


  texts.foreachRDD { (rdd, time) =>
    rdd.collect().foreach { text =>
      TwitterStreamer.broadCastMessage(text)
    }
  }
  ssc.checkpoint("c:\\checkpointSpark")
  ssc.start()

  lifecycle.addStopHook { () =>
    receiver.stop()
    ssc.stop()
    Future.successful(ssc.awaitTerminationOrTimeout(10))
  }
}
