package ui.shader.builder.value

import ui.shader.builder.types.GlType

abstract class GlValue[+T <: GlType] {
  def toGlsl: String
}
