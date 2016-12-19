package ui.program

import org.scalajs.dom
import org.scalajs.dom.raw.{WebGLProgram, WebGLShader}
import ui.scene.SceneItem
import ui.shader.Shader
import ui.GLContext

import scala.collection.mutable


class Program(
               val gLContext: GLContext,
               val vertShader: Shader,
               val fragShader: Shader,
               val attributes: Seq[Attribute] = Seq[Attribute](),
               val uniforms: Seq[Uniform] = Seq[Uniform]()) {

  var program: Option[WebGLProgram] = None

  val attributePositions: mutable.Map[Attribute,Int] = mutable.Map[Attribute,Int]()

  def init(): Unit = {
    import dom.raw.WebGLRenderingContext._

    val gl = gLContext.gl
    val vShader = compileShader(vertShader, VERTEX_SHADER)
    val fShader = compileShader(fragShader, FRAGMENT_SHADER)

    val programRef = gl.createProgram()
    program = Some(programRef)
    gl.attachShader(programRef, vShader)
    gl.attachShader(programRef, fShader)
    gl.linkProgram(programRef)
    gl.useProgram(programRef)
    // Get attribute positions and enable
    attributes.foreach((attribute: Attribute) => {
      val attributePos = gl.getAttribLocation(programRef, attribute.name)
      attributePositions += attribute -> attributePos
      gl.enableVertexAttribArray(attributePos)
    })
  }

  def ref: WebGLProgram = {
    val Some(programRef) = program
    programRef
  }

  def draw(sceneItem: SceneItem, numItems: Int): Unit = {
    import dom.raw.WebGLRenderingContext._
    val gl = gLContext.gl
    // Activate shader attributes
    var attribPosition = 0
    attributes.foreach((attribute: Attribute) => {
      // Using attribute position to find buffer
      sceneItem.buffers(attribPosition).bind(gl)
      gl.vertexAttribPointer(
        attributePositions(attribute),
        attribute.size,
        attribute.dataType match {
          case DataType.GlFloat => FLOAT
          case DataType.GlInt => INT
          case _ => FLOAT
        },
        normalized = false,
        0,
        0
      )
      attribPosition += 1
    })
    var totalSize = 0
    attributes.foreach((attribute) => totalSize += attribute.size)
    //val totalSize: Int = attributes.fold(0)((size, attribute) => size + attribute.size)
    if (sceneItem.indexBuffer != null) {
      val numEls = sceneItem.indexBuffer.data.length
      gl.drawElements(TRIANGLES, numEls, UNSIGNED_SHORT, 0)
    } else {
      gl.drawArrays(TRIANGLES, 0, numItems)
    }

  }

  def compileShader(shader: Shader, shaderType: Int): WebGLShader = {
    import dom.raw.WebGLRenderingContext._
    val gl = gLContext.gl
    val vShader = gl.createShader(shaderType)
    gl.shaderSource(vShader, shader.source)
    gl.compileShader(vShader)
    var compileStatus = gl.getShaderParameter(vShader, COMPILE_STATUS).asInstanceOf[Boolean]
    if (!compileStatus) {
      var compilationLog = gl.getShaderInfoLog(vShader)
      dom.console.error("Shader compilation failed")
      dom.console.warn(compilationLog)
    }
    vShader
  }

}
