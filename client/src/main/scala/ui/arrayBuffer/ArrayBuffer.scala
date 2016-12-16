package ui.arrayBuffer

import org.scalajs.dom.raw.WebGLRenderingContext

abstract class ArrayBuffer {
  def init(gl: WebGLRenderingContext): Unit
  def bind(gl: WebGLRenderingContext): Unit
}
