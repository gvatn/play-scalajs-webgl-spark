package app

import ui.math.Vec2
import ui.sdf.SdfScene
import ui.sdf.SdfScene._
import ui.shader.builder.Colors
import ui.shader.builder.types.{GlFloatType, GlVec4Type}
import ui.shader.builder.value.{GlFloatVal, GlValue, GlVec2Val}

object Scenes {

  def main: GlValue[GlVec4Type] = layeredScene



  def layeredScene: GlValue[GlVec4Type] = layered(
    anim3, Colors.greenBlue,
    layered(
      anim4, Colors.bluePurple,
      Colors.white,
      0.2f
    )
  )


  // Combined
  def anim5: GlValue[GlFloatType] = intersect(anim2, anim4)

  // Repeated point
  def anim4: GlValue[GlFloatType] =
    box(
      animateFloat(0.1f, 0.02f), animateFloat(0.1f, 0.02f, 3.14f),
      repeatPoint(GlVec2Val(animateFloat(0.25f, 0.05f), animateFloat(0.28f, 0.05f, 2.7f)))
    )


  // Rotate around origin
  def anim3: GlValue[GlFloatType] =
    box(
      0.1f, 0.1f,
      pointRotateAroundOrigin(
        animateFloat(0.1f, 1.8f),
        0.3f, 0.3f,
        pointTranslate(Vec2(-0.2f, -0.1f))
      )
    )

  // Union two circles animating differently
  def anim2: GlValue[GlFloatType] =
    union(
      circle(animateFloat(0.2f, 0.1f)),
      circle(animateFloat(0.2f, 0.1f, 3.14f), pointTranslate(Vec2(0.4f, 0f)))
    )

  // Animated cicle subtracted from box
  def anim1: GlValue[GlFloatType] =
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
}
