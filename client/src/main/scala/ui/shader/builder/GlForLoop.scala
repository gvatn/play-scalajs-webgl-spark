package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlForLoop[T <: GlType](val variable: GlVar[T],
                             val upperBound: GlValue[T],
                             val block: GlBlock) extends GlCommand {

  override def toGlsl: String = {
    s"for (${variable.glType.toGlsl} ${variable.toGlsl} = 0; ${variable.toGlsl} < ${upperBound.toGlsl}; ${variable.toGlsl}++) " +
      s"${block.toGlsl}" +
      s"\n"
  }
}

object GlForLoop {
  def apply[T <: GlType](variable: GlVar[T],
                         upperBound: GlValue[T],
                         block: GlBlock): GlForLoop[T] = {
    new GlForLoop[T](variable, upperBound, block)
  }
}
