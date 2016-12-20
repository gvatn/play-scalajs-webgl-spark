package ui.shader.builder

import ui.shader.builder.types.{GlVec2Type, GlVec4Type}
import ui.shader.builder.value.{GlFloatVal, GlValue, GlVec4Val}

object Colors {
  def white: GlVec4Val = GlVec4Val(1f, 1f, 1f, 1f)
  def black: GlVec4Val = GlVec4Val(0f, 0f, 0f, 1f)
  def grey: GlVec4Val = GlVec4Val(0.5f, 0.5f, 0.5f, 1f)
  def bluePurple: GlValue[GlVec4Type] = GlVec4Val(
    GlVar("vPosition", GlVec2Type()) * GlFloatVal(0.5f) + 0.5f,
    1.0f,
    1.0f
  )
  def greenBlue: GlValue[GlVec4Type] = GlVec4Val.fv2f(
    1.0f,
    GlVar("vPosition", GlVec2Type()) * GlFloatVal(0.5f) + 0.5f,
    1.0f
  )
}
