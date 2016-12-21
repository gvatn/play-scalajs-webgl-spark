package ui.shader.builder.value

import ui.shader.builder.types.GlIntType

class GlIntVal(int: Int) extends GlValue[GlIntType] {
  override def toGlsl: String = {
    int.toString
  }
}

object GlIntVal {
  def apply(int: Int): GlIntVal = {
    new GlIntVal(int)
  }
}

