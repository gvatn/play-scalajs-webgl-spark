package ui

import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.{HTMLCanvasElement, WebGLRenderingContext}

class GLContext(val elementId: String) {
  var gl: WebGLRenderingContext = _

  var element: Canvas = _

  def init(): Unit = {
    import dom.raw.WebGLRenderingContext._
    element = dom.document.getElementById(elementId).asInstanceOf[Canvas]
    gl = element.getContext("webgl").asInstanceOf[dom.raw.WebGLRenderingContext]
    gl.enable(DEPTH_TEST)
  }
}
