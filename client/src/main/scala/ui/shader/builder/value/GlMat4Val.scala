package ui.shader.builder.value

import ui.shader.builder.GlMultiply
import ui.shader.builder.types.{GlFloatType, GlMat4Type, GlType, GlVec4Type}

abstract class GlMat4Val extends GlValue[GlMat4Type]{
  def matrixMult[U <: GlType](that: GlValue[U]): GlValue[U] = {
    new GlMatrixVectorProduct[GlMat4Type,U](this, that)
  }
}

class GlMatrixVectorProduct[T <: GlType, U <: GlType](val operand1: GlValue[T],
                                           val operand2: GlValue[U]) extends GlValue[U] {
  override def toGlsl: String = {
    s"${operand1.toGlsl} * ${operand2.toGlsl}"
  }
}

object GlMat4Val {
  def apply(varName: String): GlMat4Val = {
    new GlMat4ValVar(varName)
  }

  def apply(x1: GlValue[GlFloatType],
            x2: GlValue[GlFloatType],
            x3: GlValue[GlFloatType],
            x4: GlValue[GlFloatType],
            x5: GlValue[GlFloatType],
            x6: GlValue[GlFloatType],
            x7: GlValue[GlFloatType],
            x8: GlValue[GlFloatType],
            x9: GlValue[GlFloatType],
            x10: GlValue[GlFloatType],
            x11: GlValue[GlFloatType],
            x12: GlValue[GlFloatType],
            x13: GlValue[GlFloatType],
            x14: GlValue[GlFloatType],
            x15: GlValue[GlFloatType],
            x16: GlValue[GlFloatType]): GlMat4Val = {
    new GlMat4ValF(x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16)
  }

  def apply(x1: GlValue[GlVec4Type],
            x2: GlValue[GlVec4Type],
            x3: GlValue[GlVec4Type],
            x4: GlValue[GlVec4Type]): GlMat4Val = {
    new GlMat4ValV4(x1, x2, x3, x4)
  }
}

class GlMat4ValVar(var name: String) extends GlMat4Val{
  override def toGlsl: String = {
    name
  }
}

class GlMat4ValF(val x1: GlValue[GlFloatType],
                 val x2: GlValue[GlFloatType],
                 val x3: GlValue[GlFloatType],
                 val x4: GlValue[GlFloatType],
                 val x5: GlValue[GlFloatType],
                 val x6: GlValue[GlFloatType],
                 val x7: GlValue[GlFloatType],
                 val x8: GlValue[GlFloatType],
                 val x9: GlValue[GlFloatType],
                 val x10: GlValue[GlFloatType],
                 val x11: GlValue[GlFloatType],
                 val x12: GlValue[GlFloatType],
                 val x13: GlValue[GlFloatType],
                 val x14: GlValue[GlFloatType],
                 val x15: GlValue[GlFloatType],
                 val x16: GlValue[GlFloatType]
                ) extends GlMat4Val {
  override def toGlsl: String = {
    s"mat4(${x1.toGlsl},${x2.toGlsl},${x3.toGlsl},${x4.toGlsl}," +
      s"${x5.toGlsl},${x6.toGlsl},${x7.toGlsl},${x8.toGlsl}," +
      s"${x9.toGlsl},${x10.toGlsl},${x11.toGlsl},${x12.toGlsl}," +
      s"${x13.toGlsl},${x14.toGlsl},${x15.toGlsl},${x16.toGlsl})"
  }
}

class GlMat4ValV4(val x1: GlValue[GlVec4Type],
                  val x2: GlValue[GlVec4Type],
                  val x3: GlValue[GlVec4Type],
                  val x4: GlValue[GlVec4Type]) extends GlMat4Val {
  override def toGlsl: String = {
    s"mat4(${x1.toGlsl}, ${x2.toGlsl}, ${x3.toGlsl}, ${x4.toGlsl})"
  }
}
