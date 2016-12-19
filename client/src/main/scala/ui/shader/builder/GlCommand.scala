package ui.shader.builder

trait GlCommand {
  def toGlsl: String
}
