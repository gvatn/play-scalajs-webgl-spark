package ui.loader

import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.raw.{HTMLImageElement, XMLHttpRequest}
import ui.binary.BinaryBuffer

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array}

object AjaxLoader {

  def loadFiles(files: Seq[RemoteAsset], callback: mutable.HashMap[RemoteAsset,AjaxResult] => Unit): Unit = {
    val totalFiles = files.length
    var completedFiles = 0
    val results = mutable.HashMap[RemoteAsset,AjaxResult]()
    for (i <- 0 until totalFiles) {
      dom.console.log(files(i).toString)
      val remoteAsset = files(i)
      remoteAsset match {
        case BinaryAsset(fileName) =>
          dom.console.log("Binary")
          val xhr = new XMLHttpRequest
          xhr.responseType = "arraybuffer"
          xhr.open("GET", "/assets/" + fileName)
          xhr.send()
          dom.console.log(xhr)
          xhr.onload = { (e: Event) =>
            if (xhr.status == 200) {
              val arrayBuffer = xhr.response.asInstanceOf[ArrayBuffer]
              results(remoteAsset) = new AjaxBinaryResult(new BinaryBuffer(new Uint8Array(arrayBuffer)))
              completedFiles += 1
              if (completedFiles == totalFiles) {
                callback(results)
              }
            }
          }
        case ImageAsset(fileName) =>
          val imgElement: HTMLImageElement = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
          imgElement.src = "/assets/" + fileName
          imgElement.onload = { (e: Event) =>
            org.scalajs.dom.console.log(imgElement, e)
            results(remoteAsset) = new AjaxImageResult(imgElement)
            completedFiles += 1
            if (completedFiles == totalFiles) {
              callback(results)
            }
          }
      }
    }
  }
}

abstract class RemoteAsset
case class BinaryAsset(file: String) extends RemoteAsset
case class ImageAsset(file: String) extends RemoteAsset

abstract class AjaxResult
class AjaxImageResult(val image: HTMLImageElement) extends AjaxResult
class AjaxBinaryResult(val bin: BinaryBuffer) extends AjaxResult
