package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlBraces[T <: GlType](val glValue: GlValue[T]) extends GlValue[T]{

  def toGlsl: String = {
    s"(${glValue.toGlsl})"
  }
}

object GlBraces {
  def apply[T <: GlType](glValue: GlValue[T]): GlBraces[T] = {
    new GlBraces(glValue)
  }
}
