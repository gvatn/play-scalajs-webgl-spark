package ui.shader.builder.types

class GlVec2Type extends GlType {
  override def toGlsl: String = {
    "vec2"
  }
}

object GlVec2Type {
  def apply(): GlVec2Type = {
    new GlVec2Type
  }
}
