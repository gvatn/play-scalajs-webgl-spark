package ui.shader.builder.types

class GlVoidType extends GlType{
  override def toGlsl: String = "void"
}

object GlVoidType {
  def apply(): GlVoidType = {
    new GlVoidType
  }
}

