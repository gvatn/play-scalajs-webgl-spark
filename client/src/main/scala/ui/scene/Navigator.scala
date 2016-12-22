package ui.scene

import org.scalajs.dom
import org.scalajs.dom.raw.WebGLUniformLocation
import ui.math.{Mat4, Vec3}
import ui.program.{DataType, Uniform}

import scala.collection.mutable

class Navigator(val pos: Vec3[Double],
                var direction: Vec3[Double]) extends SceneModule {

  val keyCodes: Map[String,Int] = Map(
    "w" -> 87,
    "s" -> 83,
    "a" -> 65,
    "d" -> 68,
    "q" -> 81,
    "e" -> 69
  )

  val upKey: Int = keyCodes("w")
  val downKey: Int = keyCodes("a")
  val leftKey: Int = keyCodes("a")
  val rightKey: Int = keyCodes("d")
  val strifeLeftKey: Int = keyCodes("q")
  val strifeRightKey: Int = keyCodes("e")

  val movementKeys: Seq[Int] = Seq(
    upKey,
    downKey,
    leftKey,
    rightKey,
    strifeLeftKey,
    strifeRightKey
  )

  val activeKeys: mutable.Set[Int] = mutable.Set.empty[Int]

  var velocity: Vec3[Double] = Vec3.zeros

  var rotation: Vec3[Double] = Vec3.zeros

  override def initSceneModule(scene: Scene): Unit = {
    val el = scene.glContext.element
    el.onkeydown = {(e: dom.KeyboardEvent) => {
      activeKeys.add(e.keyCode)
      if (movementKeys.contains(e.keyCode)) e.preventDefault()
    }}
    el.onkeyup = {(e: dom.KeyboardEvent) => {
      activeKeys.remove(e.keyCode)
    }}
  }

  override def sceneDraw(scene: Scene): Unit = {
    import Vec3._
    // Rotation
    if (activeKeys.contains(leftKey)) {
      rotation.y -= 0.0001
    } else if (activeKeys.contains(rightKey)) {
      rotation.y += 0.0001
    } else {
      rotation.y = rotation.y * (1.0 - 0.025)
    }
    import Mat4.ImplicitMat4Double
    direction = (Mat4.rotation(rotation) * direction).normalize()

    // Velocity
    if (activeKeys.contains(upKey)) {
      velocity += direction * 0.003
    } else {
      velocity = velocity * (1.0 - 0.04)
    }
    pos += velocity
  }

  override def sceneItemDraw(scene: Scene, sceneItem: SceneItem): Unit = {
    import dom.raw.WebGLRenderingContext._
    val gl = scene.glContext.gl
    val uCameraPos = Uniform("uCameraPos", DataType.GlVec3)
    val uCameraDir = Uniform("uCameraDir", DataType.GlVec3)
    sceneItem.program.uniformPositions.get(uCameraPos).foreach((uPos: WebGLUniformLocation) => {
      sceneItem.program.uniformValuesV3(uCameraPos) = pos
    })
    sceneItem.program.uniformPositions.get(uCameraDir).foreach((uPos: WebGLUniformLocation) => {
      sceneItem.program.uniformValuesV3(uCameraDir) = direction
    })
  }
}
