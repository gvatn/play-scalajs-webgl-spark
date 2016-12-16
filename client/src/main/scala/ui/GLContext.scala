package ui

import org.scalajs.dom
import org.scalajs.dom.raw.WebGLRenderingContext

class GLContext(val elementId: String) {
  var gl: WebGLRenderingContext = _

  def init(): Unit = {
    val el = dom.document.getElementById(elementId).asInstanceOf[dom.html.Canvas]
    gl = el.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext]
  }
}
