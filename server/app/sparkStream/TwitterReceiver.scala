package sparkStream

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import sparkConn.SparkCommon
import twitter4j._

class TwitterReceiver extends Receiver[Status](StorageLevel.MEMORY_ONLY) {
  def onStart(): Unit = {
    new Thread("Twitter receiver") {
      override def run(): Unit = {
        receive()
      }
    }.start()
  }

  def onStop(): Unit = {

  }

  private def createScalaQuery(): FilterQuery = {
    val query = new FilterQuery()
    query.language("en")
    query.track("scala")
    query
  }


  private def createNoQuery(): FilterQuery = {
    val query = new FilterQuery()
    query.language("no")
    query.track(SparkCommon.mostUsedWords.mkString(","))
    query
  }

  private def createTbgQuery(): FilterQuery = {
    val query = new FilterQuery()
    val locations: Array[Array[Double]] = Array(
      Array(59.044594, 10.308609),
      Array(59.299354, 10.518723)
    )
    query.locations(locations:_*)
    query
  }

  private def createVestfoldQuery(): FilterQuery = {
    val query = new FilterQuery()
    val locations: Array[Array[Double]] = Array(
      Array(58.935042, 9.797058),
      Array(59.636430, 10.560608)
    )
    query.locations(locations:_*)
    query
  }

  private def receive(): Unit = {
    val twitterStream = new TwitterStreamFactory().getInstance()
    // This will create a new thread
    twitterStream.addListener(new StatusListener {
      override def onStatus(status: Status) = {
        store(status)
      }
      override def onStallWarning(warning: StallWarning) = {}
      override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) = {}
      override def onScrubGeo(userId: Long, upToStatusId: Long) = {}
      override def onTrackLimitationNotice(numberOfLimitedStatuses: Int) = {}
      override def onException(ex: Exception) = {
        stop("Error", ex)
      }
    })
    twitterStream.filter(createNoQuery())
    // val query: FilterQuery = new FilterQuery
    // query.track(Seq(1,2))
    // twitterStream.filter(query)
    //twitterStream.sample()
  }
}
