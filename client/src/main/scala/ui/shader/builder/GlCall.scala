package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlCall[+T <: GlType](val identifier: String,
                           val returnType: T) extends GlValue[T] with GlCommand {

  override def toGlsl: String = {
    s"$identifier()"
  }
}

object GlCall {
  def apply[T <: GlType](identifier: String, returnType: T): GlCall[T] = {
    new GlCall[T](identifier, returnType)
  }

  // One arg
  def apply[T <: GlType, U <: GlType](identifier: String,
                                      returnType: T,
                                      arg1: GlValue[U]): GlCall[T] = {
    new GlCall1[T,U](identifier, returnType, arg1)
  }

  // Two args
  def apply[T <: GlType, U <: GlType, V <: GlType](identifier: String,
                                      returnType: T,
                                      arg1: GlValue[U], arg2: GlValue[V]): GlCall[T] = {
    new GlCall2[T,U,V](identifier, returnType, arg1, arg2)
  }

  // Three args
  def apply[T <: GlType, U <: GlType, V <: GlType, W <: GlType](identifier: String,
                                                   returnType: T,
                                                   arg1: GlValue[U], arg2: GlValue[V], arg3: GlValue[W]): GlCall[T] = {
    new GlCall3[T,U,V,W](identifier, returnType, arg1, arg2, arg3)
  }
}

class GlCall1[+T <: GlType, U <: GlType](identifier: String,
                                         returnType: T,
                                         val arg1: GlValue[U]) extends GlCall[T](identifier, returnType) {
  override def toGlsl: String = {
    s"$identifier(${arg1.toGlsl})"
  }
}

class GlCall2[+T <: GlType, U <: GlType, V <: GlType](identifier: String,
                                         returnType: T,
                                         val arg1: GlValue[U],
                                         val arg2: GlValue[V]) extends GlCall[T](identifier, returnType) {
  override def toGlsl: String = {
    s"$identifier(${arg1.toGlsl}, ${arg2.toGlsl})"
  }
}

class GlCall3[+T <: GlType, U <: GlType, V <: GlType, W <: GlType](identifier: String,
                                         returnType: T,
                                         val arg1: GlValue[U], val arg2: GlValue[V], val arg3: GlValue[W]) extends GlCall[T](identifier, returnType) {
  override def toGlsl: String = {
    s"$identifier(${arg1.toGlsl}, ${arg2.toGlsl}, ${arg3.toGlsl})"
  }
}
