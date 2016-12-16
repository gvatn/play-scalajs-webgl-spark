package ui.math

import scala.scalajs.js.typedarray.Float32Array


object Mat3 {
  def apply(vals: Seq[Float]): Mat3 = {
    if (vals.length != 9) {
      throw new Exception("Need nine elements")
    }
    new Mat3(vals)
  }

  implicit def toFloat32Array(mat3: Mat3): Float32Array = {
    val arr = new Float32Array(9)
    arr(0) = mat3.vals(0)
    arr(1) = mat3.vals(1)
    arr(2) = mat3.vals(2)
    arr(3) = mat3.vals(3)
    arr(4) = mat3.vals(4)
    arr(5) = mat3.vals(5)
    arr(6) = mat3.vals(6)
    arr(7) = mat3.vals(7)
    arr(8) = mat3.vals(8)
    arr
  }
}

class Mat3(val vals: Seq[Float]) {
  def *(vec2: Vec2): Vec2 = {
    Vec2(
      vec2.x * vals(0) + vec2.y * vals(1) + 1 * vals(2),
      vec2.x * vals(3) + vec2.y * vals(4) + 1 * vals(5)
    )
  }

  // dot product
  def *(mat3: Mat3): Mat3 = {
    Mat3(Seq(
      vals(0) * mat3.vals(0) + vals(1) * mat3.vals(3) + vals(2) * mat3.vals(6),
      vals(0) * mat3.vals(1) + vals(1) * mat3.vals(4) + vals(2) * mat3.vals(7),
      vals(0) * mat3.vals(2) + vals(1) * mat3.vals(5) + vals(2) * mat3.vals(8),

      vals(3) * mat3.vals(0) + vals(4) * mat3.vals(3) + vals(5) * mat3.vals(6),
      vals(3) * mat3.vals(1) + vals(4) * mat3.vals(4) + vals(5) * mat3.vals(7),
      vals(3) * mat3.vals(2) + vals(4) * mat3.vals(5) + vals(5) * mat3.vals(8),

      vals(6) * mat3.vals(0) + vals(7) * mat3.vals(3) + vals(8) * mat3.vals(6),
      vals(6) * mat3.vals(1) + vals(7) * mat3.vals(4) + vals(8) * mat3.vals(7),
      vals(6) * mat3.vals(2) + vals(7) * mat3.vals(5) + vals(8) * mat3.vals(8)
    ))
  }


  override def toString: String = {
    vals.toString()
  }
}
