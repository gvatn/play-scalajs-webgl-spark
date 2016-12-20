package ui.shader.builder

import ui.shader.builder.types.GlVec4Type
import ui.shader.builder.value.{GlValue, GlVec4Val}

object Colors {
  def white: GlVec4Val = GlVec4Val(1f, 1f, 1f, 1f)
  def black: GlVec4Val = GlVec4Val(0f, 0f, 0f, 1f)
  def grey: GlVec4Val = GlVec4Val(0.5f, 0.5f, 0.5f, 1f)
}
