package ui.shader.builder.types

class GlMat3Type extends GlType{
  override def toGlsl: String = {
    "mat3"
  }
}

object GlMat3Type  {
  def apply(): GlMat3Type = {
    new GlMat3Type
  }
}
