package ui.sdf

import ui.GLContext
import ui.arrayBuffer.{ElementBuffer, Float32Buffer}
import ui.geometry.Geometry
import ui.scene.SceneItem
import ui.shader.builder._
import ui.shader.builder.types._

import scala.collection.mutable.ListBuffer
import org.scalajs.dom
import ui.program.{Attribute, DataType}
import ui.shader.builder.value._

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
          GlCall(
            "mix", GlVec4Type(),
            Colors.black,
            Colors.white,
            sdfShape()
          )
        )
      )
    )
  }

  def sdfShape(): GlValue[GlFloatType] = {

    smoothBorder(
      subtract(
        circle(animateFloat(0.25f, 0.2f)),
        box(0.4f, 0.4f)
      )
    )

  }

  def animateFloat(constant: GlValue[GlFloatType] = new GlFloatVal(0f),
                   coef: GlValue[GlFloatType] = new GlFloatVal(1f)): GlValue[GlFloatType] = {
    GlAdd(
      GlMultiply(
        GlFunctions.sin(GlVar("iGlobalTime", GlFloatType())),
        coef
      ),
      constant
    )
  }

  def smoothBorder(glValue: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFunctions.smoothstep(0f, 0.005f, glValue)
  }

  def union(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFunctions.min(sdf1, sdf2)
  }

  def subtract(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "max", GlFloatType(),
      GlMultiply(GlFloatVal(-1f), sdf1),
      sdf2
    )
  }

  def intersect(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlCall(
      "max", GlFloatType(),
      sdf1,
      sdf2
    )
  }

  def circle(radius: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlBraces(
      GlSubtract(
        radius,
        GlCall("length", GlVec2Type(), GlVar("vPosition", GlVec2Type()))
      )
    )
  }

  def box(width: GlValue[GlFloatType], height: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlBraces(
      GlCall("length", GlFloatType(),
        GlCall("max", GlVec2Type(),
          GlSubtract(
            GlVec2Val(width, height),
            GlCall("abs", GlVec2Type(), GlVar("vPosition", GlVec2Type()))
          ),
          GlVec2Val(0f, 0f)
        )
      )
    )
  }

}
