package ui.math

import scala.scalajs.js.typedarray.Float32Array
import Mat3._

class Mat3[T](var x1: T,
              var x2: T,
              var x3: T,
              var x4: T,
              var x5: T,
              var x6: T,
              var x7: T,
              var x8: T,
              var x9: T) {

  def *(vec2: Vec2)(implicit tc: Mat3TypeClass[T]): Vec2 = {
    Vec2(
      tc.*(x1, vec2.x) + tc.*(x2, vec2.y) + tc.*(x3, 1.0f),
      tc.*(x4, vec2.x) + tc.*(x5, vec2.y) + tc.*(x6, 1.0f)
    )
  }

  // dot product
  def *(mat3: Mat3[T])(implicit tc: Mat3TypeClass[T]): Mat3[T] = {
    Mat3(
      tc.+(tc.*(x1, mat3.x1), tc.+(tc.*(x2, mat3.x4), tc.*(x3, mat3.x7))),
      tc.+(tc.*(x1, mat3.x2), tc.+(tc.*(x2, mat3.x5), tc.*(x3, mat3.x8))),
      tc.+(tc.*(x1, mat3.x3), tc.+(tc.*(x2, mat3.x6), tc.*(x3, mat3.x9))),

      tc.+(tc.*(x4, mat3.x1), tc.+(tc.*(x5, mat3.x4), tc.*(x6, mat3.x7))),
      tc.+(tc.*(x4, mat3.x2), tc.+(tc.*(x5, mat3.x5), tc.*(x6, mat3.x8))),
      tc.+(tc.*(x4, mat3.x3), tc.+(tc.*(x5, mat3.x6), tc.*(x6, mat3.x9))),

      tc.+(tc.*(x7, mat3.x1), tc.+(tc.*(x8, mat3.x4), tc.*(x9, mat3.x7))),
      tc.+(tc.*(x7, mat3.x2), tc.+(tc.*(x8, mat3.x5), tc.*(x9, mat3.x8))),
      tc.+(tc.*(x7, mat3.x3), tc.+(tc.*(x8, mat3.x6), tc.*(x9, mat3.x9)))
    )
    /*
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
    ))*/
  }

  def toString()(implicit tc: Mat3TypeClass[T]): String = {
    s"(${x1.toString},${x2.toString},${x3.toString}," +
      s" ${x4.toString},${x5.toString},${x6.toString}," +
      s" ${x7.toString},${x8.toString},${x9.toString})"
  }
}

object Mat3 {
  def apply[T](x1: T,
               x2: T,
               x3: T,
               x4: T,
               x5: T,
               x6: T,
               x7: T,
               x8: T,
               x9: T): Mat3[T] = {
    new Mat3[T](x1, x2, x3,
      x4, x5, x6,
      x7, x8, x9)
  }

  def identity: Mat3[Double] = {
    new Mat3[Double](
      1.0, 0.0, 0.0,
      0.0, 1.0, 0.0,
      0.0, 0.0, 1.0
    )
  }

  implicit def toFloat32Array[T](mat3: Mat3[T])(implicit tc: Mat3TypeClass[T]): Float32Array = {
    val arr = new Float32Array(9)
    arr(0) = tc.toFloat(mat3.x1)
    arr(1) = tc.toFloat(mat3.x2)
    arr(2) = tc.toFloat(mat3.x3)
    arr(3) = tc.toFloat(mat3.x4)
    arr(4) = tc.toFloat(mat3.x5)
    arr(5) = tc.toFloat(mat3.x6)
    arr(6) = tc.toFloat(mat3.x7)
    arr(7) = tc.toFloat(mat3.x8)
    arr(8) = tc.toFloat(mat3.x9)
    arr
  }

  implicit object ImplicitMat3Double extends Mat3TypeClass[Double] {
    override def +(self: Double, that: Double): Double = self + that
    override def -(self: Double, that: Double): Double = self - that
    override def *(self: Double, that: Double): Double = self * that
    override def *(self: Double, that: Float): Float = (self * that).toFloat
    override def /(self: Double, that: Double): Double = self / that
    override def sqrt(self: Double): Double = math.sqrt(self)
    override def toDouble(self: Double): Double = self
    override def toFloat(self: Double): Float = self.toFloat
    override def toString(self: Double): String = self.toString
  }

  implicit object ImplicitMat3Float extends Mat3TypeClass[Float] {
    override def +(self: Float, that: Float): Float = self + that
    override def -(self: Float, that: Float): Float = self - that
    override def *(self: Float, that: Float): Float = self * that
    override def /(self: Float, that: Double): Double = self / that
    override def sqrt(self: Float): Double = math.sqrt(self)
    override def toDouble(self: Float): Double = self.toDouble
    override def toFloat(self: Float): Float = self
    override def toString(self: Float): String = self.toString
  }

  implicit object ImplicitMat3Int extends Mat3TypeClass[Int] {
    override def +(self: Int, that: Int): Int = self + that
    override def -(self: Int, that: Int): Int = self - that
    override def *(self: Int, that: Int): Int = self * that
    override def *(self: Int, that: Float): Float = (self * that).toFloat
    override def /(self: Int, that: Double): Double = self / that
    override def sqrt(self: Int): Double = math.sqrt(self)
    override def toDouble(self: Int): Double = self.toDouble
    override def toFloat(self: Int): Float = self.toFloat
    override def toString(self: Int): String = self.toString
  }
}

trait Mat3TypeClass[T] {
  def +(self: T, that: T): T
  def -(self: T, that: T): T
  def *(self: T, that: T): T
  def *(self: T, that: Float): Float
  def /(self: T, that: Double): Double
  def sqrt(self: T): Double
  def toFloat(self: T): Float
  def toDouble(self: T): Double
  def toString(self: T): String
}

