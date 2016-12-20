package app

import ui.math.Vec2
import ui.sdf.SdfScene
import ui.sdf.SdfScene._

object Scenes {

  def main: SdfScene = anim1


  def anim1: SdfScene = SdfScene(() => {
    subtract(
      circle(
        animateFloat(0.25f, 0.2f),
        pointTranslate(Vec2(-0.2f, 0.0f))
      ),
      box(
        0.4f, 0.4f,
        pointRotate(animateFloat(0.25f, 0.2f))
      )
    )
  })
}
