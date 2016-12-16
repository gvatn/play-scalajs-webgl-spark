package ui.scene

import ui.GLContext
import org.scalajs.dom

import scala.collection.mutable.ListBuffer

class Scene(val gLContext: GLContext) {

  val items: ListBuffer[SceneItem] = ListBuffer[SceneItem]()

  def init(): Unit = {

    items.foreach((item: SceneItem) => {
      item.init()
    })
  }

  def draw(): Unit = {
    val gl = gLContext.gl
    import dom.raw.WebGLRenderingContext._
    gl.viewport(0f, 0f, 600f, 400f)
    gl.clearColor(1.0, 1.0, 1.0, 1.0)
    gl.clear(COLOR_BUFFER_BIT)
    items.foreach((item: SceneItem) => {
      item.draw()
    })
  }
}
