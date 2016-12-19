package ui.shader.builder.types

class GlFloatType extends GlType{
  override def toGlsl: String = "float"
}

object GlFloatType {
  def apply(): GlFloatType = {
    new GlFloatType
  }
}
