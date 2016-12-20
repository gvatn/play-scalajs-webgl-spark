package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlMat3Type}

abstract class GlMat3Val extends GlValue[GlMat3Type]{

}

object GlMat3Val {
  def apply(x1: GlValue[GlFloatType],
                 x2: GlValue[GlFloatType],
                 x3: GlValue[GlFloatType],
                 x4: GlValue[GlFloatType],
                 x5: GlValue[GlFloatType],
                 x6: GlValue[GlFloatType],
                 x7: GlValue[GlFloatType],
                 x8: GlValue[GlFloatType],
                 x9: GlValue[GlFloatType]): GlMat3Val = {
    new GlMat3ValF(x1, x2, x3, x4, x5, x6, x7, x8, x9)
  }
}

class GlMat3ValF(val x1: GlValue[GlFloatType],
                 val x2: GlValue[GlFloatType],
                 val x3: GlValue[GlFloatType],
                 val x4: GlValue[GlFloatType],
                 val x5: GlValue[GlFloatType],
                 val x6: GlValue[GlFloatType],
                 val x7: GlValue[GlFloatType],
                 val x8: GlValue[GlFloatType],
                 val x9: GlValue[GlFloatType]
                ) extends GlMat3Val {
  override def toGlsl: String = {
    s"mat3(${x1.toGlsl},${x2.toGlsl},${x3.toGlsl},${x4.toGlsl},${x5.toGlsl},${x6.toGlsl},${x7.toGlsl},${x8.toGlsl},${x9.toGlsl})"
  }
}
