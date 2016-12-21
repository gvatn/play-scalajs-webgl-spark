package ui.shader.builder

import ui.shader.builder.types._
import ui.shader.builder.value.GlValue

object GlFuncs {
  def smoothstep(edge0: GlValue[GlFloatType],
                 edge1: GlValue[GlFloatType],
                 x: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "smoothstep", GlFloatType(),
      edge0,
      edge1,
      x
    )
  }

  def min(x: GlValue[GlFloatType], y: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall("min", GlFloatType(), x, y)
  }

  def max(x: GlValue[GlFloatType], y: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall("max", GlFloatType(), x, y)
  }

  def sin[T <: GlType](x: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("sin", typeClass.typeObj, x)
  }

  def cos[T <: GlType](x: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("cos", typeClass.typeObj, x)
  }

  def tan[T <: GlType](x: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("tan", typeClass.typeObj, x)
  }

  def radians[T <: GlType](x: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("radians", typeClass.typeObj, x)
  }

  def normalize[T <: GlType](x: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("normalize", typeClass.typeObj, x)
  }

  def reflect[T <: GlType](x: GlValue[T], y: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("reflect", typeClass.typeObj, x, y)
  }

  def dot[T <: GlType](x: GlValue[T], y: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[GlFloatType] = {
    GlCall("dot", GlFloatType(), x, y)
  }

  def pow[T <: GlType](x: GlValue[T], y: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("pow", typeClass.typeObj, x, y)
  }

  def mix[T <: GlType](x: GlValue[GlVec4Type], y: GlValue[GlVec4Type], mix: GlValue[T]): GlValue[GlVec4Type] = {
    GlCall("mix", GlVec4Type(), x, y, mix)
  }

  def length[T <: GlType](x: GlValue[T]): GlValue[GlFloatType] = {
    GlCall("length", GlFloatType(), x)
  }

  def abs(x: GlValue[GlVec2Type]): GlValue[GlVec2Type] = {
    GlCall("abs", GlVec2Type(), x)
  }

  def mod(x: GlValue[GlVec2Type],
          y: GlValue[GlVec2Type]): GlValue[GlVec2Type] = {
    GlCall("mod", GlVec2Type(), x, y)
  }
}
