package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.{GlValue, GlVec4Val}

class GlAssign[T <: GlType](val variable: GlVar[T], val expr: GlValue[T]) extends GlCommand {
  override def toGlsl: String = {
    variable.toGlsl + " = " + expr.toGlsl + ";"
  }
}

object GlAssign {
  def apply[T <: GlType](variable: GlVar[T], expr: GlValue[T]): GlAssign[T] = {
    new GlAssign[T](variable, expr)
  }
}
