package ui.scene

import ui.GLContext
import org.scalajs.dom
import ui.math.{Vec3, Vec4}
import scala.scalajs.js
import org.scalajs.dom

import scala.collection.mutable.ListBuffer

class Scene(val gLContext: GLContext,
            val clearColor: Vec4 = Vec4(1f, 1f, 1f, 1f)) {

  val items: ListBuffer[SceneItem] = ListBuffer[SceneItem]()

  def init(): Unit = {
    items.foreach((item: SceneItem) => {
      item.init()
    })
  }

  def draw(): Unit = {
    val gl = gLContext.gl
    import dom.raw.WebGLRenderingContext._
    val width = gLContext.element.clientWidth.toFloat
    val height = gLContext.element.clientHeight.toFloat
    gl.viewport(0f, 0f, width, height)
    gl.clearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.a)
    gl.clear(COLOR_BUFFER_BIT)
    items.foreach((item: SceneItem) => {
      item.draw()
    })
    dom.window.requestAnimationFrame((deltaTime: Double) => {
      draw()
    })
  }
}
