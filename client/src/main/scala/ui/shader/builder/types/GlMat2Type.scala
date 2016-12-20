package ui.shader.builder.types

class GlMat2Type extends GlType{
  override def toGlsl: String = {
    "mat2"
  }
}

object GlMat2Type  {
  def apply(): GlMat2Type = {
    new GlMat2Type
  }
}
