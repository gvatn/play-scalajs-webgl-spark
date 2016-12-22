package ui.math

class Vec3[T](var x: T, var y: T, var z: T) {
  def +=(that: Vec3[T])(implicit typeClass: Vec3TypeClass[T]): Vec3[T] = {
    x = typeClass.+(x, that.x)
    y = typeClass.+(y, that.y)
    z = typeClass.+(z, that.z)
    this
  }

  def -=(that: Vec3[T])(implicit typeClass: Vec3TypeClass[T]): Vec3[T] = {
    x = typeClass.-(x, that.x)
    y = typeClass.-(y, that.y)
    z = typeClass.-(z, that.z)
    this
  }

  def *(that: T)(implicit typeClass: Vec3TypeClass[T]): Vec3[T] = {
    Vec3(typeClass.*(x, that), typeClass.*(y, that), typeClass.*(z, that))
  }

  def dot(that: Vec3[T])(implicit typeClass: Vec3TypeClass[T]): T = {
    typeClass.+(
      typeClass.*(x, that.x),
      typeClass.+(
        typeClass.*(y, that.y),
        typeClass.*(z, that.z)
      )
    )
  }

  // Turns into double
  def normalize()(implicit typeClass: Vec3TypeClass[T]): Vec3[Double] = {
    val eucLength = length
    Vec3(typeClass./(x, eucLength), typeClass./(y, eucLength), typeClass./(z, eucLength))
  }

  def cross(that: Vec3[T])(implicit typeClass: Vec3TypeClass[T]): Vec3[T] = {
    Vec3(
      typeClass.-(typeClass.*(y, that.z), typeClass.*(z, that.y)),
      typeClass.-(typeClass.*(z, that.x), typeClass.*(x, that.z)),
      typeClass.-(typeClass.*(x, that.y), typeClass.*(y, that.x))
    )
  }

  def length()(implicit typeClass: Vec3TypeClass[T]): Double = {
    typeClass.sqrt(dot(this))
  }

  override def toString: String = {
    s"(${x.toString}, ${y.toString}, ${z.toString})"
  }
}

object Vec3 {
  def apply[T](x: T, y: T, z: T): Vec3[T] = new Vec3(x, y, z)
  def zeros: Vec3[Double] = new Vec3(0.0, 0.0, 0.0)
  def ones: Vec3[Double] = new Vec3(1.0, 1.0, 1.0)

  implicit object ImplicitVec3Double extends Vec3TypeClass[Double] {
    override def +(self: Double, that: Double): Double = self + that
    override def -(self: Double, that: Double): Double = self - that
    override def *(self: Double, that: Double): Double = self * that
    override def /(self: Double, that: Double): Double = self / that
    override def sqrt(self: Double): Double = math.sqrt(self)
  }

  implicit object ImplicitVec3Float extends Vec3TypeClass[Float] {
    override def +(self: Float, that: Float): Float = self + that
    override def -(self: Float, that: Float): Float = self - that
    override def *(self: Float, that: Float): Float = self * that
    override def /(self: Float, that: Double): Double = self / that
    override def sqrt(self: Float): Double = math.sqrt(self)
  }

  implicit object ImplicitVec3Int extends Vec3TypeClass[Int] {
    override def +(self: Int, that: Int): Int = self + that
    override def -(self: Int, that: Int): Int = self - that
    override def *(self: Int, that: Int): Int = self * that
    override def /(self: Int, that: Double): Double = self / that
    override def sqrt(self: Int): Double = math.sqrt(self)
  }
}

trait Vec3TypeClass[T] {
  def +(self: T, that: T): T
  def -(self: T, that: T): T
  def *(self: T, that: T): T
  def /(self: T, that: Double): Double
  def sqrt(self: T): Double
}
