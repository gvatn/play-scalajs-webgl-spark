package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

class GlFunction[+T <: GlType](name: String,
                               returnType: T,
                               block: GlBlock) extends GlValue[T]{

  def toGlsl: String = {
    s"${returnType.toGlsl} $name() ${block.toGlsl}\n"
  }
}

object GlFunction {
  def apply[T <: GlType](name: String, returnType: T, block: GlBlock): GlFunction[T] = {
    new GlFunction[T](name, returnType, block)
  }

  // One arg
  def apply[T <: GlType, U <: GlType](identifier: String,
                                      returnType: T,
                                      arg1: GlArgument[U],
                                         block: GlBlock): GlFunction[T] = {
    new GlFunction1[T,U](identifier, returnType, arg1, block)
  }

  // Two args
  def apply[T <: GlType, U <: GlType, V <: GlType](identifier: String,
                                                   returnType: T,
                                                   arg1: GlArgument[U], arg2: GlArgument[V],
                                         block: GlBlock): GlFunction[T] = {
    new GlFunction2[T,U,V](identifier, returnType, arg1, arg2, block)
  }

  // Three args
  def apply[T <: GlType, U <: GlType, V <: GlType, W <: GlType](identifier: String,
                                                                returnType: T,
                                                                arg1: GlArgument[U], arg2: GlArgument[V], arg3: GlArgument[W],
                                         block: GlBlock): GlFunction[T] = {
    new GlFunction3[T,U,V,W](identifier, returnType, arg1, arg2, arg3, block)
  }
  // Six args
  def apply[T <: GlType, U <: GlType, V <: GlType, W <: GlType, X <: GlType, Y <: GlType, Z <: GlType](identifier: String,
                                                                returnType: T,
                                                                arg1: GlArgument[U], arg2: GlArgument[V], arg3: GlArgument[W],
                                                                arg4: GlArgument[X], arg5: GlArgument[Y], arg6: GlArgument[Z],
                                                                block: GlBlock): GlFunction[T] = {
    new GlFunction6[T,U,V,W,X,Y,Z](identifier, returnType, arg1, arg2, arg3, arg4, arg5, arg6, block)
  }
  // Seven args
  def apply[T <: GlType, U <: GlType, V <: GlType, W <: GlType, X <: GlType, Y <: GlType, Z <: GlType, A <: GlType](identifier: String,
                                                                                                       returnType: T,
                                                                                                       arg1: GlArgument[U], arg2: GlArgument[V], arg3: GlArgument[W],
                                                                                                       arg4: GlArgument[X], arg5: GlArgument[Y], arg6: GlArgument[Z],
                                                                                                                    arg7: GlArgument[A],
                                                                                                       block: GlBlock): GlFunction[T] = {
    new GlFunction7[T,U,V,W,X,Y,Z,A](identifier, returnType, arg1, arg2, arg3, arg4, arg5, arg6, arg7, block)
  }
}

class GlFunction1[+T <: GlType, U <: GlType](identifier: String,
                                         returnType: T,
                                         val arg1: GlArgument[U],
                                         block: GlBlock) extends GlFunction[T](identifier, returnType, block) {
  override def toGlsl: String = {
    s"${returnType.toGlsl} $identifier(${arg1.toGlsl}) ${block.toGlsl}"
  }
}

class GlFunction2[+T <: GlType, U <: GlType, V <: GlType](identifier: String,
                                                      returnType: T,
                                                      val arg1: GlArgument[U],
                                                      val arg2: GlArgument[V],
                                         block: GlBlock) extends GlFunction[T](identifier, returnType, block) {
  override def toGlsl: String = {
    s"${returnType.toGlsl} $identifier(${arg1.toGlsl}, ${arg2.toGlsl}) ${block.toGlsl}"
  }
}

class GlFunction3[+T <: GlType, U <: GlType, V <: GlType, W <: GlType](identifier: String,
                                                                   returnType: T,
                                                                   val arg1: GlArgument[U], val arg2: GlArgument[V], val arg3: GlArgument[W],
                                         block: GlBlock) extends GlFunction[T](identifier, returnType, block) {
  override def toGlsl: String = {
    s"${returnType.toGlsl} $identifier(${arg1.toGlsl}, ${arg2.toGlsl}, ${arg3.toGlsl}) ${block.toGlsl}"
  }
}

class GlFunction6[+T <: GlType,U <: GlType,V <: GlType, W <: GlType, X <: GlType, Y <: GlType, Z <: GlType](identifier: String,
                                                                       returnType: T,
                                                                       val arg1: GlArgument[U], val arg2: GlArgument[V], val arg3: GlArgument[W],
                                                                       val arg4: GlArgument[X], val arg5: GlArgument[Y], val arg6: GlArgument[Z],
                                                                       block: GlBlock) extends GlFunction[T](identifier, returnType, block) {
  override def toGlsl: String = {
    s"${returnType.toGlsl} $identifier(${arg1.toGlsl}, ${arg2.toGlsl}, ${arg3.toGlsl}, ${arg4.toGlsl}, ${arg5.toGlsl}, ${arg6.toGlsl}) ${block.toGlsl}"
  }
}

class GlFunction7[+T <: GlType,U <: GlType,V <: GlType, W <: GlType, X <: GlType, Y <: GlType, Z <: GlType, A <: GlType](identifier: String,
                                                                                                            returnType: T,
                                                                                                            val arg1: GlArgument[U], val arg2: GlArgument[V], val arg3: GlArgument[W],
                                                                                                            val arg4: GlArgument[X], val arg5: GlArgument[Y], val arg6: GlArgument[Z],
                                                                                                                         val arg7: GlArgument[A],
                                                                                                            block: GlBlock) extends GlFunction[T](identifier, returnType, block) {
  override def toGlsl: String = {
    s"${returnType.toGlsl} $identifier(${arg1.toGlsl}, ${arg2.toGlsl}, ${arg3.toGlsl}, ${arg4.toGlsl}, ${arg5.toGlsl}, ${arg6.toGlsl}, ${arg7.toGlsl}) ${block.toGlsl}"
  }
}
