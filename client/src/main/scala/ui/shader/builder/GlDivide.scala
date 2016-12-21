package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlDivide[T <: GlType, U <: GlType](val operand1: GlValue[T],
                                           val operand2: GlValue[U]) extends GlValue[T] {
  override def toGlsl: String = {
    s"${operand1.toGlsl} / ${operand2.toGlsl}"
  }
}

object GlDivide {
  def apply[T <: GlType, U <: GlType](operand1: GlValue[T], operand2: GlValue[U]): GlDivide[T,U] = {
    new GlDivide[T,U](operand1, operand2)
  }
}

