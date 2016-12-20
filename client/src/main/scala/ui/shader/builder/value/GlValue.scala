package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlType}

abstract class GlValue[+T <: GlType] {
  def toGlsl: String

}

object GlValue {
  implicit def floatToGlVal(float: Float): GlValue[GlFloatType] = {
    GlFloatVal(float)
  }
}