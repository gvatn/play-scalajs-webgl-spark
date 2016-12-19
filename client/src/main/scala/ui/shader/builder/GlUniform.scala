package ui.shader.builder

import ui.shader.builder.types.GlType

class GlUniform[+T <: GlType](name: String, glType: T) {

  def toGlsl: String = s"uniform ${glType.toGlsl} $name;\n"
}

object GlUniform {
  def apply[T <: GlType](name: String, glType: T): GlUniform[T] = {
    new GlUniform[T](name, glType)
  }
}

