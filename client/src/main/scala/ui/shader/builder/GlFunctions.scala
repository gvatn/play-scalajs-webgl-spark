package ui.shader.builder

import ui.shader.builder.types.GlFloatType
import ui.shader.builder.value.GlValue

object GlFunctions {
  def smoothstep(edge0: GlValue[GlFloatType],
                 edge1: GlValue[GlFloatType],
                 x: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "smoothstep", GlFloatType(),
      edge0,
      edge1,
      x
    )
  }

  def min(x: GlValue[GlFloatType], y: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "min", GlFloatType(),
      x, y
    )
  }

  def max(x: GlValue[GlFloatType], y: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "max", GlFloatType(),
      x, y
    )
  }

  def sin(x: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "sin", GlFloatType(),
      x
    )
  }
}
