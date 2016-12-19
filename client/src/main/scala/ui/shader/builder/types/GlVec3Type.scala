package ui.shader.builder.types

class GlVec3Type extends GlType {
  override def toGlsl: String = {
    "vec3"
  }
}

object GlVec3Type {
  def apply(): GlVec3Type = {
    new GlVec3Type
  }
}

