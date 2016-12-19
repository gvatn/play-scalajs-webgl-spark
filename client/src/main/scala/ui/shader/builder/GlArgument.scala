package ui.shader.builder

import ui.shader.builder.types.GlType

class GlArgument[+T <: GlType](name: String, glType: T) {
  def toGlsl: String = s"${glType.toGlsl} $name"
}

object GlArgument {
  def apply[T <: GlType](name: String, glType: T): GlArgument[T] = {
    new GlArgument[T](name, glType)
  }
}

