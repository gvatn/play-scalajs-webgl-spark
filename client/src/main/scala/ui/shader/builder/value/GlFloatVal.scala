package ui.shader.builder.value

import org.scalajs.dom
import ui.shader.builder.types.GlFloatType

abstract class GlFloatVal extends GlValue[GlFloatType] {

}

object GlFloatVal {

  def apply(double: Double): GlValue[GlFloatType] = {
    new GlFloatValD(double)
  }

  def apply(float: Float): GlValue[GlFloatType] = {
    new GlFloatValF(float)
  }

  def apply(varName: String): GlValue[GlFloatType] = {
    new GlFloatValVar(varName)
  }

  implicit def floatToVal(float: Float): GlValue[GlFloatType] = {
    new GlFloatValF(float)
  }
}

class GlFloatValD(double: Double) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    val formatted = f"$double%8.12f"
    val dotPos = formatted.indexOf('.')
    val zeros = formatted.reverse.takeWhile(_ == '0').length
    formatted.substring(0, math.max(dotPos + 2, formatted.length - zeros))
  }
}

class GlFloatValF(float: Float) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    val formatted = f"$float%8.12f"
    val dotPos = formatted.indexOf('.')
    val zeros = formatted.reverse.takeWhile(_ == '0').length
    formatted.substring(0, math.max(dotPos + 2, formatted.length - zeros))
  }
}

class GlFloatValVar(name: String) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    name
  }
}