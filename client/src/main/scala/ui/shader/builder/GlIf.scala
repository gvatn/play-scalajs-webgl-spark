package ui.shader.builder

import ui.shader.builder.types.GlBoolType
import ui.shader.builder.value.GlValue

class GlIf(val cond: GlValue[GlBoolType],
           val thenBlock: GlBlock,
           val elseBlock: Option[GlBlock] = None) extends GlCommand{
  override def toGlsl: String = {
    s"if (${cond.toGlsl}) " +
      s"${thenBlock.toGlsl}\n" +
      s"${elseBlock.map((block) => s"else ${block.toGlsl}").mkString("")}"
  }
}

object GlIf {
  def apply(cond: GlValue[GlBoolType],
            thenBlock: GlBlock,
            elseBlock: Option[GlBlock] = None): GlIf = {
    new GlIf(cond, thenBlock, elseBlock)
  }
}
