package ui.scene

import ui.arrayBuffer.{ArrayBuffer, ElementBuffer}
import ui.texture.Texture
import org.scalajs.dom
import org.scalajs.dom.raw.WebGLUniformLocation
import ui.math.{Mat3, Vec2}
import ui.program.Program


class SceneItem(val program: Program,
                val buffers: Seq[ArrayBuffer] = Seq[ArrayBuffer](),
                val indexBuffer: ElementBuffer = null,
                val translate: Vec2 = new Vec2(0f, 0f),
                val scale: Vec2 = new Vec2(1f, 1f),
                val rotate: Vec2 = new Vec2(0f, 0f)) {

  import scala.collection.mutable
  val textures: mutable.Map[String,Texture] = new mutable.HashMap[String,Texture]()
  val textureUniforms: mutable.Map[String,WebGLUniformLocation] = new mutable.HashMap[String,WebGLUniformLocation]()

  var modelMatrixUniform: WebGLUniformLocation = _


  def modelMatrix(): Mat3 = {
    // Translation, rotation and scale
    Mat3(Seq(
      1f, 0f, 0f,
      0f, 1f, 0f,
      translate.x, translate.y, 1f
    )) * Mat3(Seq(
      math.cos(rotate.y).toFloat, -math.sin(rotate.y).toFloat, 0f,
      math.sin(rotate.y).toFloat, math.cos(rotate.y).toFloat, 0f,
      0f, 0f, 1f
    )) * Mat3(Seq(
      scale.x, 0f, 0f,
      0f, scale.y, 0f,
      0f, 0f, 1f
    ))
  }

  def init(): Unit = {
    program.init()
    val gl = program.gLContext.gl
    import dom.raw.WebGLRenderingContext._
    gl.useProgram(program.ref)
    buffers.foreach(_.init(gl))
    if (indexBuffer != null) {
      indexBuffer.init(gl)
    }
    modelMatrixUniform = gl.getUniformLocation(program.ref, "model")
    for ((uniformKey, texture) <- textures) {
      texture.init(program.gLContext)
      textureUniforms += uniformKey -> gl.getUniformLocation(program.ref, uniformKey)
    }
  }

  def draw(): Unit = {
    import dom.raw.WebGLRenderingContext._
    val gl = program.gLContext.gl
    gl.useProgram(program.ref)
    // Element array buffer
    if (indexBuffer != null) {
      indexBuffer.bind(gl)
    }
    gl.uniformMatrix3fv(modelMatrixUniform, false, modelMatrix())
    var i = 0
    // Texture as uniform
    for ((uniformKey, texture) <- textures) {
      gl.activeTexture(TEXTURE0 + i)
      gl.bindTexture(TEXTURE_2D, texture.glReference)
      gl.uniform1i(textureUniforms(uniformKey), i)
      i += 1
    }
    program.draw(this, 6)
  }
}
