package ui.math

import org.scalajs.dom

class Mat4[T](var x1: T,
              var x2: T,
              var x3: T,
              var x4: T,
              var x5: T,
              var x6: T,
              var x7: T,
              var x8: T,
              var x9: T,
              var x10: T,
              var x11: T,
              var x12: T,
              var x13: T,
              var x14: T,
              var x15: T,
              var x16: T) {
  def *(vec3: Vec3[T])(implicit tc: Mat4TypeClass[T]): Vec3[T] = {
    Vec3(
      tc.+(tc.*(x1, vec3.x), tc.+(tc.*(x2, vec3.y), tc.+(tc.*(x3, vec3.z), tc.*(x4, tc.one)))),
      tc.+(tc.*(x5, vec3.x), tc.+(tc.*(x6, vec3.y), tc.+(tc.*(x7, vec3.z), tc.*(x8, tc.one)))),
      tc.+(tc.*(x9, vec3.x), tc.+(tc.*(x10, vec3.y), tc.+(tc.*(x11, vec3.z), tc.*(x12, tc.one))))
    )
  }
}

object Mat4 {
  def apply[T](x1: T,
               x2: T,
               x3: T,
               x4: T,
               x5: T,
               x6: T,
               x7: T,
               x8: T,
               x9: T,
               x10: T,
               x11: T,
               x12: T,
               x13: T,
               x14: T,
               x15: T,
               x16: T): Mat4[T] = {
    new Mat4[T](x1, x2, x3, x4,
      x5, x6, x7, x8, x9,
      x10, x11, x12, x13, x14, x15, x16)
  }

  def identity: Mat4[Double] = {
    new Mat4[Double](
      1.0, 0.0, 0.0, 0.0,
      0.0, 1.0, 0.0, 0.0,
      0.0, 0.0, 1.0, 0.0,
      0.0, 0.0, 0.0, 1.0
    )
  }

  def rotation(rotation: Vec3[Double]): Mat4[Double] = {
    // x, y, x combined
    import Mat3._
    val combined = Mat3[Double](
      1.0, 0.0, 0.0,
      0.0, math.cos(rotation.x), math.sin(rotation.x),
      0.0, -math.sin(rotation.x), math.cos(rotation.x)
    ) * Mat3[Double](
      math.cos(rotation.y), 0.0, -math.sin(rotation.y),
      0.0, 1.0, 0.0,
      math.sin(rotation.y), 0.0, math.cos(rotation.y)
    ) * Mat3[Double](
      math.cos(rotation.z), math.sin(rotation.z), 0.0,
      -math.sin(rotation.z), math.cos(rotation.z), 0.0,
      0.0, 0.0, 1.0
    )
    Mat4[Double](
      combined.x1, combined.x2, combined.x3, 0.0,
      combined.x4, combined.x5, combined.x6, 0.0,
      combined.x7, combined.x8, combined.x9, 0.0,
      0.0, 0.0, 0.0, 1.0
    )
  }

  def apply[T](mat3: Mat3[T])(implicit tc: Mat4TypeClass[T]): Mat4[T] = {
    new Mat4[T](
      mat3.x1, mat3.x2, mat3.x3, tc.zero,
      mat3.x4, mat3.x5, mat3.x6, tc.zero,
      mat3.x7, mat3.x8, mat3.x9, tc.zero,
      tc.zero, tc.zero, tc.zero, tc.one
    )
  }

  implicit object ImplicitMat4Double extends Mat4TypeClass[Double] {
    override def +(self: Double, that: Double): Double = self + that
    override def -(self: Double, that: Double): Double = self - that
    override def *(self: Double, that: Double): Double = self * that
    override def /(self: Double, that: Double): Double = self / that
    override def sqrt(self: Double): Double = math.sqrt(self)
    override def zero: Double = 0d
    override def one: Double = 1d
  }

  implicit object ImplicitMat4Float extends Mat4TypeClass[Float] {
    override def +(self: Float, that: Float): Float = self + that
    override def -(self: Float, that: Float): Float = self - that
    override def *(self: Float, that: Float): Float = self * that
    override def /(self: Float, that: Double): Double = self / that
    override def sqrt(self: Float): Double = math.sqrt(self)
    override def zero: Float = 0f
    override def one: Float = 1f
  }

  implicit object ImplicitMat4Int extends Mat4TypeClass[Int] {
    override def +(self: Int, that: Int): Int = self + that
    override def -(self: Int, that: Int): Int = self - that
    override def *(self: Int, that: Int): Int = self * that
    override def /(self: Int, that: Double): Double = self / that
    override def sqrt(self: Int): Double = math.sqrt(self)
    override def zero: Int = 0
    override def one: Int = 1
  }
}

trait Mat4TypeClass[T] {
  def +(self: T, that: T): T
  def -(self: T, that: T): T
  def *(self: T, that: T): T
  def /(self: T, that: Double): Double
  def sqrt(self: T): Double
  def zero: T
  def one: T
}