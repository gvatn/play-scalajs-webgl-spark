package ui.math

object Vec2 {
  def apply(x: Float, y: Float): Vec2 = {
    new Vec2(x, y)
  }
}

class Vec2(var x: Float, var y: Float) {

}
