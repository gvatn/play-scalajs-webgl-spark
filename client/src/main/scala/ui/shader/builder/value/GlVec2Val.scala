package ui.shader.builder.value

import ui.shader.builder.types.GlVec2Type

abstract class GlVec2Val extends GlValue[GlVec2Type] {
}

object GlVec2Val {
  def apply(x: GlFloatVal, y: GlFloatVal): GlVec2Val = {
    new GlVec2ValFF(x, y)
  }

  def apply(varName: String): GlVec2Val = {
    new GlVec2ValVar(varName)
  }
}

class GlVec2ValFF(val x: GlFloatVal, val y: GlFloatVal) extends GlVec2Val {
  override def toGlsl: String = {
    "vec2(" + x.toGlsl + ", " + y.toGlsl + ")"
  }
}

class GlVec2ValVar(val varName: String) extends GlVec2Val {
  override def toGlsl: String = {
    varName
  }
}
