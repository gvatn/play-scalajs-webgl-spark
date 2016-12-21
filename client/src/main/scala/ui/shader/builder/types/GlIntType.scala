package ui.shader.builder.types

class GlIntType extends GlType{
  override def toGlsl: String = "int"
}

object GlIntType {
  def apply(): GlIntType = {
    new GlIntType
  }
}

