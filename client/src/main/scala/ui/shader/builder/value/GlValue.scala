package ui.shader.builder.value

import ui.math.Vec2
import ui.shader.builder.{GlAdd, GlDivide, GlMultiply, GlSubtract}
import ui.shader.builder.types.{GlFloatType, GlIntType, GlType, GlVec2Type}

abstract class GlValue[+T <: GlType] {
  def toGlsl: String

  def +[U <: GlType](that: GlValue[U]): GlValue[T] = {
    GlAdd(this, that)
  }

  def -[U <: GlType](that: GlValue[U]): GlValue[T] = {
    GlSubtract(this, that)
  }

  def *[U <: GlType](that: GlValue[U]): GlValue[T] = {
    GlMultiply(this, that)
  }

  def /[U <: GlType](that: GlValue[U]): GlValue[T] = {
    GlDivide(this, that)
  }
}

// Implicits available when used as parameter
object GlValue {
  implicit def floatToGlVal(float: Float): GlValue[GlFloatType] = {
    GlFloatVal(float)
  }

  implicit def vec2ToGlVal(vec2: Vec2): GlValue[GlVec2Type] = {
    GlVec2Val(GlFloatVal(vec2.x), GlFloatVal(vec2.y))
  }

  implicit def intToGlVal(int: Int): GlValue[GlIntType] = {
    GlIntVal(int)
  }

  implicit def longToGlVal(int: Long): GlValue[GlIntType] = {
    GlIntVal(int.toInt)
  }
}