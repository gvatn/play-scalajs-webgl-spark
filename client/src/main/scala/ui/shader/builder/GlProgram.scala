package ui.shader.builder

import ui.GLContext
import ui.program.{Attribute, DataType, Program, Uniform}

class GlProgram(val vertexShader: GlVertexShader,
                val fragmentShader: GlFragmentShader) {

  def createProgram(glContext: GLContext): Program = {
    new Program(
      glContext,
      vertexShader.createShader(),
      fragmentShader.createShader(),
      Seq(Attribute("position", DataType.GlFloat, 2)),
      Seq(
        Uniform("iGlobalTime", DataType.GlFloat),
        Uniform("uCameraPos", DataType.GlVec3),
        Uniform("uCameraDir", DataType.GlVec3),
        Uniform("uViewport", DataType.GlVec2)
      )
    )
  }
}
