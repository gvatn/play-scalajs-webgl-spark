package ui.shader.builder.value

import ui.shader.builder.types.GlBoolType

class GlBoolVal(bool: Boolean) extends GlValue[GlBoolType] {
  override def toGlsl: String = {
    if (bool) "true" else "false"
  }
}

object GlBoolVal {
  def apply(bool: Boolean): GlBoolVal = {
    new GlBoolVal(bool)
  }
}

