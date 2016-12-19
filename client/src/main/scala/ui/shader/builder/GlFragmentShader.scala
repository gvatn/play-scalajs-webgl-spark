package ui.shader.builder

import ui.shader.Shader
import ui.shader.builder.types.{GlType, GlVoidType}

import scala.collection.mutable.ListBuffer

class GlFragmentShader(val attributes: ListBuffer[GlAttribute[GlType]] = ListBuffer[GlAttribute[GlType]](),
                       val uniforms: ListBuffer[GlUniform[GlType]] = ListBuffer[GlUniform[GlType]](),
                       val varyings: ListBuffer[GlVarying[GlType]] = ListBuffer[GlVarying[GlType]](),
                       val mainBlock: GlBlock) {
  def createShader(): Shader = {
    new Shader(toGlsl)
  }

  def toGlsl: String = {
    val mainFunction = GlFunction("main", GlVoidType(), mainBlock)
    s"precision mediump float;\n${attributes.foldLeft("")(_ + _.toGlsl)}${uniforms.foldLeft("")(_ + _.toGlsl)}\n${varyings.foldLeft("")(_ + _.toGlsl)}\n${mainFunction.toGlsl}"
  }
}
