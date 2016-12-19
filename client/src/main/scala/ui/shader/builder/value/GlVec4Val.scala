package ui.shader.builder.value

import ui.shader.builder.types.{GlVec2Type, GlVec4Type}

abstract class GlVec4Val extends GlValue[GlVec4Type] {
  override def toGlsl: String
}

object GlVec4Val {

  def apply(vec2: GlValue[GlVec2Type], z: GlFloatVal, w: GlFloatVal): GlVec4Val = {
    new GlVec4ValV2FF(vec2, z, w)
  }

  def apply(x: GlFloatVal, y: GlFloatVal, z: GlFloatVal, w: GlFloatVal): GlVec4Val = {
    new GlVec4ValFFFF(x, y, z, w)
  }
}

private[value] class GlVec4ValFFFF(val x: GlFloatVal,
                val y: GlFloatVal,
                val z: GlFloatVal,
                val w: GlFloatVal) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${x.toGlsl}, ${y.toGlsl}, ${z.toGlsl}, ${w.toGlsl})"
  }
}

private[value] class GlVec4ValV2FF(val vec2: GlValue[GlVec2Type],
                                   val z: GlFloatVal,
                                   val w: GlFloatVal) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${vec2.toGlsl}, ${z.toGlsl}, ${w.toGlsl})"
  }
}