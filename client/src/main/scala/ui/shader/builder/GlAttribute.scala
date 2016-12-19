package ui.shader.builder

import ui.shader.builder.types.GlType

class GlAttribute[+T <: GlType](name: String, glType: T) {
  def toGlsl: String = s"attribute ${glType.toGlsl} $name;\n"
}

object GlAttribute {
  def apply[T <: GlType](name: String, glType: T): GlAttribute[T] = {
    new GlAttribute[T](name, glType)
  }
}
