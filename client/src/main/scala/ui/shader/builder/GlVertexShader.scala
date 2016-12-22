package ui.shader.builder

import ui.shader.Shader
import ui.shader.builder.types.{GlType, GlVoidType}

import scala.collection.mutable.ListBuffer

class GlVertexShader(val attributes: ListBuffer[GlAttribute[GlType]] = ListBuffer[GlAttribute[GlType]](),
                     val uniforms: ListBuffer[GlUniform[GlType]] = ListBuffer[GlUniform[GlType]](),
                     val varyings: ListBuffer[GlVarying[GlType]] = ListBuffer[GlVarying[GlType]](),
                     val mainBlock: GlBlock,
                     val functions: ListBuffer[GlFunction[GlType]] = ListBuffer[GlFunction[GlType]]()) {

  def createShader(): Shader = {
    new Shader(toGlsl)
  }

  def toGlsl: String = {
    val mainFunction = GlFunction("main", GlVoidType(), mainBlock)
    s"precision mediump float;\n" +
      s"${attributes.foldLeft("")(_ + _.toGlsl)}" +
      s"${uniforms.foldLeft("")(_ + _.toGlsl)}\n" +
      s"${varyings.foldLeft("")(_ + _.toGlsl)}\n" +
      s"${functions.foldLeft("")(_ + _.toGlsl + "\n\n")}\n" +
      s"${mainFunction.toGlsl}"
  }
}
