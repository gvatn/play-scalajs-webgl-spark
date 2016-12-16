package ui.arrayBuffer

import org.scalajs.dom.raw.{WebGLBuffer, WebGLRenderingContext}
import org.scalajs.dom

import scala.scalajs.js.typedarray.Float32Array

class Float32Buffer(val data: Float32Array) extends ArrayBuffer {

  var bufferRef: WebGLBuffer = _

  def init(gl: WebGLRenderingContext): Unit = {
    import dom.raw.WebGLRenderingContext._
    bufferRef = gl.createBuffer()
    gl.bindBuffer(ARRAY_BUFFER, bufferRef)
    gl.bufferData(ARRAY_BUFFER, data, STATIC_DRAW)
  }

  def bind(gl: WebGLRenderingContext): Unit = {
    import dom.raw.WebGLRenderingContext._
    gl.bindBuffer(ARRAY_BUFFER, bufferRef)

  }
}
