package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlAdd[T <: GlType, U <: GlType](val operand1: GlValue[T],
                                           val operand2: GlValue[U]) extends GlValue[T] {
  override def toGlsl: String = {
    s"${operand2.toGlsl} + ${operand1.toGlsl}"
  }
}

object GlAdd {
  def apply[T <: GlType, U <: GlType](operand1: GlValue[T], operand2: GlValue[U]): GlAdd[T,U] = {
    new GlAdd[T,U](operand1, operand2)
  }
}

