package ui.shader.builder.types

class GlBoolType extends GlType{
  override def toGlsl: String = "bool"
}

object GlBoolType {
  def apply(): GlBoolType = {
    new GlBoolType
  }
}

