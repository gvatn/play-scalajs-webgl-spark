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

  def max[T <: GlType](x: GlValue[T], y: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("max", typeClass.typeObj, x, y)
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

  // Cross product, only vec3
  def cross(x: GlValue[GlVec3Type], y: GlValue[GlVec3Type]): GlValue[GlVec3Type] = {
    GlCall("cross", GlVec3Type(), x, y)
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

  def abs[T <: GlType](x: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("abs", typeClass.typeObj, x)
  }

  def mod[T <: GlType](x: GlValue[T],
          y: GlValue[T])(implicit typeClass: GlTypeClass[T]): GlValue[T] = {
    GlCall("mod", typeClass.typeObj, x, y)
  }
}
