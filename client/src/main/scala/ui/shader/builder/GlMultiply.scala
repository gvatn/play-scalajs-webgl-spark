package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlMultiply[T <: GlType, U <: GlType](val operand1: GlValue[T],
                                           val operand2: GlValue[U]) extends GlValue[T] {
  override def toGlsl: String = {
    s"${operand1.toGlsl} * ${operand2.toGlsl}"
  }
}

object GlMultiply {
  def apply[T <: GlType, U <: GlType](operand1: GlValue[T], operand2: GlValue[U]): GlMultiply[T,U] = {
    new GlMultiply[T,U](operand1, operand2)
  }
}
