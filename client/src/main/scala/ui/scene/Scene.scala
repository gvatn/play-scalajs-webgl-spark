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
  var displayWidth: Float = 0f
  var displayHeight: Float = 0f

  def init(): Unit = {
    items.foreach((item: SceneItem) => {
      item.init()
    })
  }

  def draw(): Unit = {
    val gl = gLContext.gl
    import dom.raw.WebGLRenderingContext._
    if (gLContext.element.clientWidth.toFloat != displayWidth
    || gLContext.element.clientHeight.toFloat != displayHeight) {
      displayWidth = gLContext.element.clientWidth.toFloat
      displayHeight = gLContext.element.clientHeight.toFloat
      gLContext.element.width = gLContext.element.clientWidth
      gLContext.element.height = gLContext.element.clientHeight
      gl.viewport(0f, 0f, displayWidth, displayHeight)
    }
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
