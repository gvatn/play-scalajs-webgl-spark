package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlVec2Type, GlVec3Type}

abstract class GlVec2Val extends GlValue[GlVec2Type] {
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

class GlVec2ValFF(val x: GlValue[GlFloatType], val y: GlValue[GlFloatType]) extends GlVec2Val {
  override def toGlsl: String = {
    "vec2(" + x.toGlsl + ", " + y.toGlsl + ")"
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