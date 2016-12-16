package ui.arrayBuffer
import org.scalajs.dom.raw.{WebGLBuffer, WebGLRenderingContext}

import scala.scalajs.js.typedarray.Uint16Array
import org.scalajs.dom

class Int16Buffer(val data: Uint16Array) extends ArrayBuffer{

  var bufferRef: WebGLBuffer = _

  override def init(gl: WebGLRenderingContext): Unit = {
    import dom.raw.WebGLRenderingContext._
    bufferRef = gl.createBuffer()
    gl.bindBuffer(ELEMENT_ARRAY_BUFFER, bufferRef)
    gl.bufferData(ELEMENT_ARRAY_BUFFER, data, STATIC_DRAW)
  }

  override def bind(gl: WebGLRenderingContext): Unit = {
    import dom.raw.WebGLRenderingContext._
    gl.bindBuffer(ELEMENT_ARRAY_BUFFER, bufferRef)
  }
}
