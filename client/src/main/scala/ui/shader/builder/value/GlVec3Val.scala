package ui.shader.builder.value

import ui.shader.builder.types.GlVec3Type

class GlVec3Val(val x: GlFloatVal,
                val y: GlFloatVal,
                val z: GlFloatVal) extends GlValue[GlVec3Type] {

  override def toGlsl: String = {
    "vec3(" + x.toGlsl + ", " + y.toGlsl + ", " + z.toGlsl + ")"
  }
}
