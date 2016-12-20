package ui.program

import org.scalajs.dom
import org.scalajs.dom.raw.{WebGLProgram, WebGLShader, WebGLUniformLocation}
import ui.scene.SceneItem
import ui.shader.Shader
import ui.GLContext

import scala.collection.mutable

import scala.scalajs.js

class Program(
               val gLContext: GLContext,
               val vertShader: Shader,
               val fragShader: Shader,
               val attributes: Seq[Attribute] = Seq[Attribute](),
               val uniforms: Seq[Uniform] = Seq[Uniform]()) {

  var program: Option[WebGLProgram] = None

  val attributePositions: mutable.Map[Attribute,Int] = mutable.Map[Attribute,Int]()

  val uniformPositions: mutable.Map[Uniform,WebGLUniformLocation] = mutable.Map[Uniform,WebGLUniformLocation]()

  val uniformValuesF: mutable.Map[Uniform,Double] = mutable.Map[Uniform,Double]()

  var startTime: Double = 0d

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
    uniforms.foreach((uniform: Uniform) => {
      val uniformPos = gl.getUniformLocation(programRef, uniform.name)
      uniformPositions += uniform -> uniformPos
      uniform.dataType match {
        case DataType.GlFloat =>
          uniformValuesF(uniform) = 0.0f
      }
    })
  }

  def ref: WebGLProgram = program match {
    case Some(ref) => ref
    case None => throw new Exception("No webgl reference")
  }

  def draw(sceneItem: SceneItem, numItems: Int): Unit = {
    if (startTime == 0) {
      startTime = js.Date.now()
    }
    val elapsed = (js.Date.now() - startTime) / 1000d
    uniformValuesF(Uniform("iGlobalTime", DataType.GlFloat)) = elapsed
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
    uniforms.foreach((uniform: Uniform) => {
      uniform.dataType match {
        case DataType.GlFloat =>
          gl.uniform1f(uniformPositions(uniform), uniformValuesF(uniform))
      }
    })
    // Time uniform
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
