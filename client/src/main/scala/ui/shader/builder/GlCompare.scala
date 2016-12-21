package ui.shader.builder

import ui.shader.builder.types.{GlBoolType, GlType}
import ui.shader.builder.value.GlValue

class GlCompare[T <: GlType](val operand1: GlValue[T],
                             val operand2: GlValue[T],
                             val comparison: String = "==") extends GlValue[GlBoolType] {

  override def toGlsl: String = {
    s"${operand1.toGlsl} $comparison ${operand2.toGlsl}"
  }
}

object GlCompare {
  def apply[T <: GlType](operand1: GlValue[T],
                         operand2: GlValue[T],
                         comparison: String = "=="): GlCompare[T] = {
    new GlCompare[T](operand1, operand2, comparison)
  }
}
