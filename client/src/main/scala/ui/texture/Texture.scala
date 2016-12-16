package ui.texture

import ui.GLContext
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.raw.{HTMLImageElement, WebGLTexture, XMLHttpRequest}

import scala.scalajs.js
import scala.scalajs.js.typedarray.Uint8Array

object Texture {
  def seqToPixels(seq: Seq[Int]): Seq[Int] = {
    seq.flatMap(Seq(_, 0, 0))
  }

  def requestImage(url: String): Unit = {
    val imgElement: HTMLImageElement = document.createElement("img").asInstanceOf[HTMLImageElement]
    imgElement.src = url
    imgElement.onload = (event: Event) => {
      dom.console.log(event)
    }
  }
}

class Texture {

  private var customData: Seq[Int] = _
  private var customWidth: Int = 0
  private var customHeight: Int = 0

  private var imageEl: HTMLImageElement = _

  var glReference: WebGLTexture = _

  def this(data: Seq[Int], width: Int, height: Int) = {
    this()
    customData = data
    customWidth = width
    customHeight = height
  }

  def this(imageElement: HTMLImageElement) = {
    this()
    imageEl = imageElement
  }

  def setData(data: Seq[Int], width: Int, height: Int): Unit = {
    customData = data
    customWidth = width
    customHeight = height
  }

  def init(context: GLContext): Unit = {
    import org.scalajs.dom.raw.WebGLRenderingContext._
    val gl = context.gl
    glReference = gl.createTexture()
    gl.activeTexture(TEXTURE0)
    gl.bindTexture(TEXTURE_2D, glReference)
    if (customData != null) {
      val jsData = new Uint8Array(js.Array(customData:_*))
      gl.texImage2D(TEXTURE_2D, 0, RGB, customWidth, customHeight, 0, RGB, UNSIGNED_BYTE, jsData)
    } else if (imageEl != null) {
      gl.texImage2D(TEXTURE_2D, 0, RGBA, RGBA, UNSIGNED_BYTE, imageEl)
    }
    gl.texParameteri(TEXTURE_2D, TEXTURE_MAG_FILTER, LINEAR)
    gl.texParameteri(TEXTURE_2D, TEXTURE_MIN_FILTER, LINEAR_MIPMAP_LINEAR)
    // Wrap
    gl.texParameteri(TEXTURE_2D, TEXTURE_WRAP_S, CLAMP_TO_EDGE)
    gl.texParameteri(TEXTURE_2D, TEXTURE_WRAP_T, CLAMP_TO_EDGE)
    gl.generateMipmap(TEXTURE_2D)
  }

}
