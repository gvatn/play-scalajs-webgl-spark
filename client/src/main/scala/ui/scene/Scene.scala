package ui.scene

import ui.GLContext
import org.scalajs.dom
import ui.math.{Vec2, Vec3, Vec4}

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.raw.WebGLUniformLocation
import ui.program.{DataType, Uniform}

import scala.collection.mutable.ListBuffer

class Scene(val glContext: GLContext,
            val clearColor: Vec4 = Vec4(1f, 1f, 1f, 1f)) {

  val items: ListBuffer[SceneItem] = ListBuffer[SceneItem]()
  var displayWidth: Float = 0f
  var displayHeight: Float = 0f
  val sceneModules: ListBuffer[SceneModule] = ListBuffer[SceneModule]()

  def init(): Unit = {
    items.foreach((item: SceneItem) => {
      item.init()
    })
    sceneModules.foreach((module: SceneModule) => {
      module.initSceneModule(this)
    })
  }

  def draw(): Unit = {
    val gl = glContext.gl
    import dom.raw.WebGLRenderingContext._
    if (glContext.element.clientWidth.toFloat != displayWidth
    || glContext.element.clientHeight.toFloat != displayHeight) {
      displayWidth = glContext.element.clientWidth.toFloat
      displayHeight = glContext.element.clientHeight.toFloat
      glContext.element.width = glContext.element.clientWidth
      glContext.element.height = glContext.element.clientHeight
      gl.viewport(0f, 0f, displayWidth, displayHeight)
    }
    gl.clearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.a)
    gl.clear(COLOR_BUFFER_BIT)
    sceneModules.foreach((module: SceneModule) => {
      module.sceneDraw(this)
    })
    items.foreach((item: SceneItem) => {
      sceneModules.foreach((module: SceneModule) => {
        module.sceneItemDraw(this, item)
      })
      val uViewport = Uniform("uViewport", DataType.GlVec2)
      item.program.uniformPositions.get(uViewport).foreach((uPos: WebGLUniformLocation) => {
        item.program.uniformValuesV2(uViewport) = Vec2(displayWidth, displayHeight)
      })
      item.draw()
    })
    dom.window.requestAnimationFrame((deltaTime: Double) => {
      draw()
    })
  }
}
