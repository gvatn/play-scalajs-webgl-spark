package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlMat2Type}

abstract class GlMat2Val extends GlValue[GlMat2Type]{

}

object GlMat2Val {
  def apply(x1: GlValue[GlFloatType],
                 x2: GlValue[GlFloatType],
                 x3: GlValue[GlFloatType],
                 x4: GlValue[GlFloatType]): GlMat2Val = {
    new GlMat2ValF(x1, x2, x3, x4)
  }
}

class GlMat2ValF(val x1: GlValue[GlFloatType],
                 val x2: GlValue[GlFloatType],
                 val x3: GlValue[GlFloatType],
                 val x4: GlValue[GlFloatType]
                ) extends GlMat2Val {
  override def toGlsl: String = {
    s"mat2(${x1.toGlsl},${x2.toGlsl},${x3.toGlsl},${x4.toGlsl})"
  }
}
