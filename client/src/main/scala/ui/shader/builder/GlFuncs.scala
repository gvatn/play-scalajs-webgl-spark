package ui.shader.builder

import ui.shader.builder.types.{GlFloatType, GlType, GlVec2Type, GlVec4Type}
import ui.shader.builder.value.GlValue

object GlFuncs {
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

  def cos(x: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "cos", GlFloatType(),
      x
    )
  }

  def mix[T <: GlType](x: GlValue[GlVec4Type], y: GlValue[GlVec4Type], mix: GlValue[T]): GlValue[GlVec4Type] = {
    GlCall(
      "mix", GlVec4Type(),
      x, y, mix
    )
  }

  def length(x: GlValue[GlVec2Type]): GlValue[GlFloatType] = {
    GlCall(
      "length", GlFloatType(),
      x
    )
  }

  def abs(x: GlValue[GlVec2Type]): GlValue[GlVec2Type] = {
    GlCall(
      "abs", GlVec2Type(),
      x
    )
  }

  def mod(x: GlValue[GlVec2Type],
          y: GlValue[GlVec2Type]): GlValue[GlVec2Type] = {
    GlCall(
      "mod", GlVec2Type(),
      x, y
    )
  }
}
