package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlVec2Type, GlVec3Type, GlVec4Type}

abstract class GlVec4Val extends GlValue[GlVec4Type] {
  override def toGlsl: String
}

object GlVec4Val {

  def apply(vec2: GlValue[GlVec2Type], z: GlValue[GlFloatType], w: GlValue[GlFloatType]): GlVec4Val = {
    new GlVec4ValV2FF(vec2, z, w)
  }

  def fv2f(x: GlValue[GlFloatType], vec2: GlValue[GlVec2Type], w: GlValue[GlFloatType]): GlVec4Val = {
    new GlVec4ValFV2F(x, vec2, w)
  }

  def v3f(vec3: GlValue[GlVec3Type], w: GlValue[GlFloatType]): GlVec4Val = {
    new GlVec4ValV3F(vec3, w)
  }

  def apply(x: GlValue[GlFloatType], y: GlValue[GlFloatType], z: GlValue[GlFloatType], w: GlValue[GlFloatType]): GlVec4Val = {
    new GlVec4ValFFFF(x, y, z, w)
  }
}

private[value] class GlVec4ValFFFF(val x: GlValue[GlFloatType],
                val y: GlValue[GlFloatType],
                val z: GlValue[GlFloatType],
                val w: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${x.toGlsl}, ${y.toGlsl}, ${z.toGlsl}, ${w.toGlsl})"
  }
}

private[value] class GlVec4ValV2FF(val vec2: GlValue[GlVec2Type],
                                   val z: GlValue[GlFloatType],
                                   val w: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${vec2.toGlsl}, ${z.toGlsl}, ${w.toGlsl})"
  }
}

private[value] class GlVec4ValFV2F(val x: GlValue[GlFloatType],
                                   val vec2: GlValue[GlVec2Type],
                                   val w: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${x.toGlsl}, ${vec2.toGlsl}, ${w.toGlsl})"
  }
}

private[value] class GlVec4ValV3F(val vec3: GlValue[GlVec3Type],
                                   val w: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${vec3.toGlsl}, ${w.toGlsl})"
  }
}
