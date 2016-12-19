package ui.shader.builder

import ui.shader.builder.types.GlType

class GlVarying[+T <: GlType](name: String, glType: T) {
  def toGlsl: String = s"varying ${glType.toGlsl} $name;\n"
}

object GlVarying {
  def apply[T <: GlType](name: String, glType: T): GlVarying[T] = {
    new GlVarying[T](name, glType)
  }
}

