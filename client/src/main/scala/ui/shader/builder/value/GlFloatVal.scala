package ui.shader.builder.value

import ui.shader.builder.types.GlFloatType

class GlFloatVal(float: Float) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    val formatted = f"$float%8.20f"
    val dotPos = formatted.indexOf('.')
    val zeros = formatted.reverse.takeWhile(_ == '0').length
    formatted.substring(0, math.max(dotPos + 2, formatted.length - zeros))
  }
}

object GlFloatVal {
  def apply(float: Float): GlFloatVal = {
    new GlFloatVal(float)
  }

  implicit def floatToVal(float: Float): GlFloatVal = {
    new GlFloatVal(float)
  }
}
