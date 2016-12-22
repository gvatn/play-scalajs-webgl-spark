package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlVec2Type, GlVec3Type, GlVec4Type}

abstract class GlVec4Val extends GlValue[GlVec4Type] {
  override def toGlsl: String

  def xyz: GlValue[GlVec3Type] = {
    new GlVec4ValXYZ(this)
  }

  def x: GlValue[GlFloatType] = {
    new GlVec4ValX(this)
  }

  def y: GlValue[GlFloatType] = {
    new GlVec4ValY(this)
  }
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

  def apply(vec4: GlValue[GlVec4Type]): GlVec4Val = {
    new GlVec4ValV4F(vec4)
  }

  def apply(varName: String): GlVec4Val = {
    new GlVec4ValVar(varName)
  }

  def apply(x: GlValue[GlFloatType], y: GlValue[GlFloatType], z: GlValue[GlFloatType], w: GlValue[GlFloatType]): GlVec4Val = {
    new GlVec4ValFFFF(x, y, z, w)
  }
}

class GlVec4ValX(val vec4: GlValue[GlVec4Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec4.toGlsl}.x"
  }
}

class GlVec4ValY(val vec4: GlValue[GlVec4Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec4.toGlsl}.y"
  }
}

class GlVec4ValZ(val vec4: GlValue[GlVec4Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec4.toGlsl}.z"
  }
}

class GlVec4ValW(val vec4: GlValue[GlVec4Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec4.toGlsl}.w"
  }
}

class GlVec4ValXYZ(val vec4: GlValue[GlVec4Type]) extends GlValue[GlVec3Type] {
  override def toGlsl: String = {
    s"${vec4.toGlsl}.xyz"
  }
}

private[value] class GlVec4ValFFFF(val xVal: GlValue[GlFloatType],
                val yVal: GlValue[GlFloatType],
                val zVal: GlValue[GlFloatType],
                val wVal: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${xVal.toGlsl}, ${yVal.toGlsl}, ${zVal.toGlsl}, ${wVal.toGlsl})"
  }
}

private[value] class GlVec4ValV2FF(val vec2: GlValue[GlVec2Type],
                                   val zVal: GlValue[GlFloatType],
                                   val wVal: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${vec2.toGlsl}, ${zVal.toGlsl}, ${wVal.toGlsl})"
  }
}

private[value] class GlVec4ValFV2F(val xVal: GlValue[GlFloatType],
                                   val vec2: GlValue[GlVec2Type],
                                   val wVal: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${xVal.toGlsl}, ${vec2.toGlsl}, ${wVal.toGlsl})"
  }

}

private[value] class GlVec4ValV3F(val vec3: GlValue[GlVec3Type],
                                   val wVal: GlValue[GlFloatType]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${vec3.toGlsl}, ${wVal.toGlsl})"
  }

}

private[value] class GlVec4ValV4F(val vec4: GlValue[GlVec4Type]) extends GlVec4Val {

  override def toGlsl: String = {
    s"vec4(${vec4.toGlsl})"
  }
}

class GlVec4ValVar(val varName: String) extends GlVec4Val {

  override def toGlsl: String = {
    varName
  }
}
