package ui.shader.builder.types

class GlVec4Type extends GlType {
  override def toGlsl: String = {
    "vec4"
  }
}

object GlVec4Type {
  def apply(): GlVec4Type = {
    new GlVec4Type
  }
}

