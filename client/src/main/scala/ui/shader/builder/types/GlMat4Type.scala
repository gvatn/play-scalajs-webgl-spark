package ui.shader.builder.types

class GlMat4Type extends GlType{
  override def toGlsl: String = {
    "mat4"
  }
}

object GlMat4Type  {
  def apply(): GlMat4Type = {
    new GlMat4Type
  }
}

