package ui.math

object Vec4 {
  def apply(x: Float, y: Float, z: Float, a: Float): Vec4 = new Vec4(x, y, z, a)
}

class Vec4(var x: Float, var y: Float, var z: Float, var a: Float)

