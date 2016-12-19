package ui.shader.builder.types

abstract class GlType {
  def toGlsl: String
}

object GlType {
  def glslTypeString(test: String): Unit = {}
}
