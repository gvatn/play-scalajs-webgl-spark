package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.{GlValue, GlVec4Val}

class GlAssign[T <: GlType](val variable: GlVar[T],
                            val expr: GlValue[T]) extends GlCommand {
  override def toGlsl: String = {
    variable.toGlsl + " = " + expr.toGlsl + ";"
  }
}

object GlAssign {
  def apply[T <: GlType](variable: GlVar[T],
                         expr: GlValue[T]): GlAssign[T] = {
    new GlAssign[T](variable, expr)
  }

  def init[T <: GlType](variable: GlVar[T],
                        expr: GlValue[T]): GlAssign[T] = {
    new GlAssignInit[T](variable, expr)
  }

  def incr[T <: GlType](variable: GlVar[T],
                         expr: GlValue[T]): GlAssign[T] = {
    new GlAssignIncr[T](variable, expr)
  }

  def decr[T <: GlType](variable: GlVar[T],
                        expr: GlValue[T]): GlAssign[T] = {
    new GlAssignDecr[T](variable, expr)
  }
}

class GlAssignInit[T <: GlType](variable: GlVar[T],
                                expr: GlValue[T]) extends GlAssign[T](variable, expr) {
  override def toGlsl: String = {
    s"${variable.glType.toGlsl} ${variable.toGlsl} = ${expr.toGlsl};"
  }
}

class GlAssignIncr[T <: GlType](variable: GlVar[T],
                                expr: GlValue[T]) extends GlAssign[T](variable, expr) {
  override def toGlsl: String = {
    variable.toGlsl + " += " + expr.toGlsl + ";"
  }
}

class GlAssignDecr[T <: GlType](variable: GlVar[T],
                                expr: GlValue[T]) extends GlAssign[T](variable, expr) {
  override def toGlsl: String = {
    variable.toGlsl + " -= " + expr.toGlsl + ";"
  }
}
