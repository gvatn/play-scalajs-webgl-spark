package ui.shader.builder

import ui.GLContext
import ui.program.{Attribute, DataType, Program}

class GlProgram(val vertexShader: GlVertexShader,
                val fragmentShader: GlFragmentShader) {

  def createProgram(glContext: GLContext): Program = {
    new Program(
      glContext,
      vertexShader.createShader(),
      fragmentShader.createShader(),
      Seq(Attribute("position", DataType.GlFloat, 2))
    )
  }
}
