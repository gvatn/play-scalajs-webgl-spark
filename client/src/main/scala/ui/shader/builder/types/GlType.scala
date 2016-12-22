package ui.shader.builder.types

abstract class GlType {
  def toGlsl: String
}

trait GlTypeClass[T] {
  def typeObj: T
}

object GlType {
  def glslTypeString(test: String): Unit = {}

  implicit object ImplicitGlTypeBool extends GlTypeClass[GlBoolType] {
    override def typeObj = GlBoolType()
  }
  implicit object ImplicitGlTypeFloat extends GlTypeClass[GlFloatType] {
    override def typeObj = GlFloatType()
  }
  implicit object ImplicitGlTypeInt extends GlTypeClass[GlIntType] {
    override def typeObj = GlIntType()
  }
  implicit object ImplicitGlTypeMat2 extends GlTypeClass[GlMat2Type] {
    override def typeObj = GlMat2Type()
  }
  implicit object ImplicitGlTypeMat3 extends GlTypeClass[GlMat3Type] {
    override def typeObj = GlMat3Type()
  }
  implicit object ImplicitGlTypeVec2 extends GlTypeClass[GlVec2Type] {
    override def typeObj = GlVec2Type()
  }
  implicit object ImplicitGlTypeVec3 extends GlTypeClass[GlVec3Type] {
    override def typeObj = GlVec3Type()
  }
  implicit object ImplicitGlTypeVec4 extends GlTypeClass[GlVec4Type] {
    override def typeObj = GlVec4Type()
  }
  implicit object ImplicitGlTypeVoid extends GlTypeClass[GlVoidType] {
    override def typeObj = GlVoidType()
  }
}
