package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlVec2Type, GlVec3Type}

abstract class GlVec3Val extends GlValue[GlVec3Type] {
  def xy: GlValue[GlVec2Type] = {
    GlVec2Val(this)
  }
}

object GlVec3Val {
  def apply(x: GlValue[GlFloatType],
            y: GlValue[GlFloatType],
            z: GlValue[GlFloatType]): GlVec3Val = {
    new GlVec3ValF(x, y, z)
  }

  def apply(vec2: GlValue[GlVec2Type],
            z: GlValue[GlFloatType]): GlVec3Val = {
    new GlVec3ValV2F(vec2, z)
  }

  def apply(vec3: GlValue[GlVec3Type]): GlVec3Val = {
    new GlVec3ValV3F(vec3)
  }
}

class GlVec3ValF(val x: GlValue[GlFloatType],
                val y: GlValue[GlFloatType],
                val z: GlValue[GlFloatType]) extends GlVec3Val {

  override def toGlsl: String = {
    "vec3(" + x.toGlsl + ", " + y.toGlsl + ", " + z.toGlsl + ")"
  }

}

class GlVec3ValV2F(val vec2: GlValue[GlVec2Type],
                   val z: GlValue[GlFloatType]) extends GlVec3Val {

  override def toGlsl: String = {
    "vec3(" + vec2.toGlsl + ", " + z.toGlsl + ")"
  }
}

class GlVec3ValV3F(val vec3: GlValue[GlVec3Type]) extends GlVec3Val {

  override def toGlsl: String = {
    "vec3(" + vec3.toGlsl + ")"
  }
}
