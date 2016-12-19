package ui.math

object Vec3 {
  def apply(x: Float, y: Float, z: Float): Vec3 = new Vec3(x, y, z)
}

class Vec3(var x: Float, var y: Float, z: Float)

