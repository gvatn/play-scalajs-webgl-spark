package ui.sdf

import ui.GLContext
import ui.arrayBuffer.{ElementBuffer, Float32Buffer}
import ui.geometry.Geometry
import ui.scene.SceneItem
import ui.shader.builder._
import ui.shader.builder.types._

import scala.collection.mutable.ListBuffer
import org.scalajs.dom
import ui.math.Vec2
import ui.program.{Attribute, DataType}
import ui.shader.builder.value._
import SdfScene._

class SdfScene {
  val sdfItems: ListBuffer[SdfItem] = ListBuffer[SdfItem]()

  def createSceneItem(glContext: GLContext): SceneItem = {
    val program = new GlProgram(
      createVertexShader(),
      createFragmentShader()
    )
    new SceneItem(
      program.createProgram(glContext),
      Seq(
        new Float32Buffer(Seq(
          -1f, -1f,
          -1f, 1f,
          1f, 1f,
          1f, -1f
        ))
      ),
      new ElementBuffer(Geometry.quadIndices(1))
    )
  }

  def createVertexShader(): GlVertexShader = {
    new GlVertexShader(
      ListBuffer[GlAttribute[GlType]](
        GlAttribute("position", GlVec2Type())
      ),
      ListBuffer[GlUniform[GlType]](),
      ListBuffer[GlVarying[GlType]](
        GlVarying("vPosition", GlVec2Type())
      ),
      GlBlock(
        GlAssign(GlGlobals.Position, GlVec4Val(GlVar("position", GlVec2Type()), 0f, 1f)),
        GlAssign(GlVar("vPosition", GlVec2Type()), GlVar("position", GlVec2Type()))
      )
    )
  }

  def createFragmentShader(): GlFragmentShader = {
    new GlFragmentShader(
      ListBuffer[GlAttribute[GlType]](),
      ListBuffer[GlUniform[GlType]](
        GlUniform("iGlobalTime", GlFloatType())
      ),
      ListBuffer[GlVarying[GlType]](
        GlVarying("vPosition", GlVec2Type())
      ),
      GlBlock(
        GlAssign(
          GlGlobals.Color,
          GlFuncs.mix(Colors.black, Colors.white, smoothBorder(sdfShape()))
        )
      )
    )
  }

  def sdfShape(): GlValue[GlFloatType] = {
    subtract(
      circle(animateFloat(0.25f, 0.2f), pointTranslate(Vec2(-0.2f, 0.0f))),
      box(0.4f, 0.4f, pointRotate(animateFloat(0.25f, 0.2f)))
    )
  }
}

object SdfScene {
  def apply(shapeFunction: () => GlValue[GlFloatType]): SdfScene = {
    new SdfScene {
      override def sdfShape(): GlValue[GlFloatType] = shapeFunction()
    }
  }

  def circle(radius: GlValue[GlFloatType],
             point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlFloatType] = {

    GlBraces(
      GlFuncs.length(point) - radius
    )
  }

  def box(width: GlValue[GlFloatType], height: GlValue[GlFloatType],
          point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlFloatType] = {

    GlBraces(
      GlCall("length", GlFloatType(),
        GlCall("max", GlVec2Type(),
          GlFuncs.abs(point) - GlVec2Val(width, height),
          GlVec2Val(0f, 0f)
        )
      )
    )
  }

  def pointTranslate(translation: GlValue[GlVec2Type],
                     point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlBraces(
      point - translation
    )
  }

  def pointRotate(rotation: GlValue[GlFloatType],
                  point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlBraces(
      point * GlMat2Val(
        GlFuncs.cos(rotation), GlFuncs.sin(rotation) * GlFloatVal(-1f),
        GlFuncs.sin(rotation), GlFuncs.cos(rotation)
      )
    )
  }

  def animateFloat(constant: GlValue[GlFloatType] = new GlFloatVal(0f),
                   coef: GlValue[GlFloatType] = new GlFloatVal(1f)): GlValue[GlFloatType] = {
    constant + GlBraces(coef * GlFuncs.sin(GlVar("iGlobalTime", GlFloatType())))
  }

  def smoothBorder(glValue: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.smoothstep(0f, 0.005f, glValue)
  }

  def union(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.min(sdf1, sdf2)
  }

  def subtract(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.max(GlMultiply(GlFloatVal(-1f), sdf1), sdf2)
  }

  def intersect(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.max(sdf1, sdf2)
  }

}