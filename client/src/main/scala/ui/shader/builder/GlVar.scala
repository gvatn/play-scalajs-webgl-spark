package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlVar[+T <: GlType](val name: String, val glType: T) extends GlValue[T] {
  def toGlsl: String = name
}

object GlVar {
  def apply[T <: GlType](name: String, glType: T): GlVar[T] = {
    new GlVar[T](name, glType)
  }
}

