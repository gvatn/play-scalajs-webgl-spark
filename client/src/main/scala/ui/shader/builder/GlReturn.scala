package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlReturn[T <: GlType](val expr: GlValue[T]) extends GlCommand {
  override def toGlsl: String = {
    "return " + expr.toGlsl + ";"
  }
}

object GlReturn {
  def apply[T <: GlType](expr: GlValue[T]): GlReturn[T] = {
    new GlReturn[T](expr)
  }
}
