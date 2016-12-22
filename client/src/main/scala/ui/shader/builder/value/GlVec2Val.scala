package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlVec2Type, GlVec3Type}

abstract class GlVec2Val extends GlValue[GlVec2Type] {
  def x: GlVec2ValX = new GlVec2ValX(this)
  def y: GlVec2ValY = new GlVec2ValY(this)
}

object GlVec2Val {
  def apply(x: GlValue[GlFloatType], y: GlValue[GlFloatType]): GlVec2Val = {
    new GlVec2ValFF(x, y)
  }

  def apply(varName: String): GlVec2Val = {
    new GlVec2ValVar(varName)
  }

  def apply(vec3: GlVec3Val): GlVec2Val = {
    new GlVec2RefXY(vec3)
  }
}

class GlVec2ValX(val vec2: GlValue[GlVec2Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec2.toGlsl}.x"
  }
}

class GlVec2ValY(val vec2: GlValue[GlVec2Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec2.toGlsl}.y"
  }
}

class GlVec2ValFF(val xVal: GlValue[GlFloatType],
                  val yVal: GlValue[GlFloatType]) extends GlVec2Val {
  override def toGlsl: String = {
    "vec2(" + xVal.toGlsl + ", " + yVal.toGlsl + ")"
  }
}

class GlVec2ValVar(val varName: String) extends GlVec2Val {
  override def toGlsl: String = {
    varName
  }
}

class GlVec2RefXY(val ref: GlValue[GlVec3Type]) extends GlVec2Val{
  override def toGlsl: String = {
    s"vec3(${ref.toGlsl}).xy"
  }
}