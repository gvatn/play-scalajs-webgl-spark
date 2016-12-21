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
      create3dFragmentShader()
    )
    dom.console.log(create3dFragmentShader().toGlsl)
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

  def create3dFragmentShader(): GlFragmentShader = {
    val epsilon = GlFloatVal(0.0001f)
    val minDist = GlFloatVal(0f)
    val maxDist = GlFloatVal(100f)
    val marchingSteps = GlIntVal(255)
    new GlFragmentShader(
      ListBuffer[GlAttribute[GlType]](),
      ListBuffer[GlUniform[GlType]](
        GlUniform("iGlobalTime", GlFloatType())
      ),
      ListBuffer[GlVarying[GlType]](
        GlVarying("vPosition", GlVec2Type())
      ),
      GlBlock(
        GlAssign.init(GlVar("dir", GlVec3Type()), GlCall("rayDirection", GlVec3Type())),
        GlAssign.init(GlVar("eye", GlVec3Type()), GlVec3Val(0f, 0f, 5f)),
        GlAssign.init(GlVar("dist", GlFloatType()), GlCall("shortestDistance", GlFloatType(),
          GlVar("eye", GlVec3Type()),
          GlVar("dir", GlVec3Type()))
        ),
        GlIf(
          GlCompare(GlVar("dist", GlFloatType()), maxDist - epsilon),
          GlBlock(
            GlAssign(GlGlobals.Color, Colors.black)
          ),
          Some(GlBlock(
            GlAssign.init(GlVar("p", GlVec3Type()), GlVec3Val("eye") + GlFloatVal("dist") * GlVec3Val("dir")),
            GlAssign.init(GlVar("K_a", GlVec3Type()), GlVec3Val(0.2f, 0.2f, 0.2f)),
            GlAssign.init(GlVar("K_d", GlVec3Type()), GlVec3Val(0.7f, 0.2f, 0.2f)),
            GlAssign.init(GlVar("K_s", GlVec3Type()), GlVec3Val(1f, 1f, 1f)),
            GlAssign.init(GlVar("shininess", GlFloatType()), GlFloatVal(10f)),
            GlAssign.init(GlVar("color", GlVec3Type()), GlCall(
              "phongIllumination", GlVec3Type(),
              GlVec3Val("K_a"),
              GlVec3Val("K_d"),
              GlVec3Val("K_s"),
              GlFloatVal("shininess"),
              GlVec3Val("p"),
              GlVec3Val("eye")
            )),
            GlAssign(
              GlGlobals.Color,
              GlVec4Val.v3f(GlVec3Val("color"), 1f)
            )
          ))
        )
      ),
      ListBuffer[GlFunction[GlType]](
        GlFunction(
          "sdfDist", GlFloatType(),
          GlArgument("point", GlVec3Type()),
          GlBlock(
            GlReturn(GlFuncs.length(GlVar("point", GlVec3Type())) - 1f)
          )
        ),
        GlFunction(
          "rayDirection", GlVec3Type(),
          GlBlock(
            GlReturn(GlFuncs.normalize(GlVec3Val(
              GlVar("vPosition", GlVec2Type()),
              GlFloatVal(2f) / GlFuncs.tan(GlFuncs.radians(45f) / 2f) * -1f
            )))
          )
        ),
        GlFunction(
          "estimateNormal", GlVec3Type(),
          GlArgument("point", GlVec3Type()),
          GlBlock(
            GlReturn(GlFuncs.normalize(GlVec3Val(
              /* x */
              GlCall(
                "sdfDist", GlFloatType(),
                GlVec3Val(
                  GlVec3Val("point").x + epsilon,
                  GlVec3Val("point").y,
                  GlVec3Val("point").z
                )
              ) - GlCall(
                "sdfDist", GlFloatType(),
                GlVec3Val(
                  GlVec3Val("point").x - epsilon,
                  GlVec3Val("point").y,
                  GlVec3Val("point").z
                )
              ),
              /* y */
              GlCall(
                "sdfDist", GlFloatType(),
                GlVec3Val(
                  GlVec3Val("point").x,
                  GlVec3Val("point").y + epsilon,
                  GlVec3Val("point").z
                )
              ) - GlCall(
                "sdfDist", GlFloatType(),
                GlVec3Val(
                  GlVec3Val("point").x,
                  GlVec3Val("point").y - epsilon,
                  GlVec3Val("point").z
                )
              ),
              /* z */
              GlCall(
                "sdfDist", GlFloatType(),
                GlVec3Val(
                  GlVec3Val("point").x,
                  GlVec3Val("point").y,
                  GlVec3Val("point").z + epsilon
                )
              ) - GlCall(
                "sdfDist", GlFloatType(),
                GlVec3Val(
                  GlVec3Val("point").x,
                  GlVec3Val("point").y,
                  GlVec3Val("point").z - epsilon
                )
              )
            )
            ))
          )
        ),
        GlFunction(
          "phongContribForLight", GlVec3Type(),
          GlArgument("k_d", GlVec3Type()),
          GlArgument("k_s", GlVec3Type()),
          GlArgument("alpha", GlFloatType()),
          GlArgument("point", GlVec3Type()),
          GlArgument("eye", GlVec3Type()),
          GlArgument("lightPos", GlVec3Type()),
          GlArgument("lightIntensity", GlVec3Type()),
          GlBlock(
            GlAssign.init(GlVar("N", GlVec3Type()), GlCall("estimateNormal", GlVec3Type(), GlVec3Val("point"))),
            GlAssign.init(GlVar("L", GlVec3Type()), GlFuncs.normalize(GlVec3Val("lightPos") - GlVec3Val("point"))),
            GlAssign.init(GlVar("V", GlVec3Type()), GlFuncs.normalize(GlVec3Val("eye") - GlVec3Val("point"))),
            GlAssign.init(GlVar("R", GlVec3Type()), GlFuncs.reflect(GlVec3Val("L") * -1f, GlVec3Val("N"))),
            GlAssign.init(GlVar("dotLN", GlFloatType()), GlFuncs.dot(GlVec3Val("L"), GlVec3Val("N"))),
            GlAssign.init(GlVar("dotRV", GlFloatType()), GlFuncs.dot(GlVec3Val("R"), GlVec3Val("V"))),
            GlIf(GlFloatVal("dotLN") < 0f, GlBlock(
              /* Light not visible from this point on the surface */
              GlReturn(GlVec3Val("lightIntensity") * GlBraces(GlVec3Val("k_d") * GlFloatVal("dotLN")))
            )),
            GlReturn(GlVec3Val("lightIntensity") * GlBraces(
              GlVec3Val("k_d") * GlFloatVal("dotLN")) + GlVec3Val("k_s") * GlFuncs.pow(GlFloatVal("dotRV"), GlFloatVal("alpha"))
            )
          )
        ),
        GlFunction(
          "phongIllumination", GlVec3Type(),
          GlArgument("k_a", GlVec3Type()),
          GlArgument("k_d", GlVec3Type()),
          GlArgument("k_s", GlVec3Type()),
          GlArgument("alpha", GlFloatType()),
          GlArgument("point", GlVec3Type()),
          GlArgument("eye", GlVec3Type()),
          GlBlock(
            GlAssign.init(GlVar("ambientLight", GlVec3Type()), GlVec3Val(0.5f, 0.5f, 0.5f)),
            GlAssign.init(GlVar("color", GlVec3Type()), GlVec3Val("ambientLight") * GlVec3Val("k_a")),
            GlAssign.init(GlVar("lightPos", GlVec3Type()), GlVec3Val(
              GlFuncs.sin(GlFloatVal("iGlobalTime")) * 4f,
              2f,
              GlFuncs.cos(GlFloatVal("iGlobalTime")) * 4f)
            ),
            GlAssign.init(GlVar("lightIntensity", GlVec3Type()), GlVec3Val(0.4f, 0.4f, 0.4f)),
            GlAssign.incr(GlVar("color", GlVec3Type()), GlCall(
              "phongContribForLight", GlVec3Type(),
              GlVec3Val("k_d"),
              GlVec3Val("k_s"),
              GlFloatVal("alpha"),
              GlVec3Val("point"),
              GlVec3Val("eye"),
              GlVec3Val("lightPos"),
              GlVec3Val("lightIntensity")
            )),
            GlReturn(GlVec3Val("color"))
          )
        ),
        GlFunction(
          "shortestDistance", GlFloatType(),
          GlArgument("eye", GlVec3Type()),
          GlArgument("dir", GlVec3Type()),
          GlBlock(
            GlAssign.init(GlVar("depth", GlFloatType()), minDist),
            GlAssign.init(GlVar("end", GlFloatType()), maxDist),
            GlForLoop(GlVar("i", GlIntType()), marchingSteps, GlBlock(
              GlAssign.init(GlVar("dist", GlFloatType()), GlCall(
                "sdfDist", GlFloatType(),
                GlVar("eye", GlVec3Type()) + GlVar("depth", GlVec3Type()) * GlVar("dir", GlFloatType())
              )),
              /* if (i < epsilon) */
              GlIf(
                GlCompare(GlVar("dist", GlFloatType()), epsilon, "<"),
                GlBlock(
                  GlReturn(GlVar("depth", GlFloatType()))
                )
              ),
              /* Increment depth */
              GlAssign.incr(GlVar("depth", GlFloatType()), GlVar("dist", GlFloatType())),
              GlIf(
                GlCompare(GlVar("depth", GlFloatType()), GlVar("end", GlFloatType()), ">="),
                GlBlock(
                  /* Moved beyond end */
                  GlReturn(GlVar("end", GlFloatType()))
                )
              )
            )),
            GlReturn(GlVar("end", GlFloatType()))
          )
        )
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
          getColor
        )
      )
    )
  }

  def getColor: GlValue[GlVec4Type] = {
    GlFuncs.mix(Colors.greenBlue, Colors.white, smoothBorder(sdfShape()))
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

  def layered(sdf1: GlValue[GlFloatType],
              color: GlValue[GlVec4Type],
              bgColor: GlValue[GlVec4Type] = GlVec4Val(1.0f, 1.0f, 1.0f, 1.0f),
              mixFactor: GlValue[GlFloatType] = GlFloatVal(1f)): GlValue[GlVec4Type] = {
    GlFuncs.mix(
      bgColor,
      color,
      GlVec4Val.v3f(GlVec3Val(mixFactor, mixFactor, mixFactor) * GlBraces(1f - smoothBorder(sdf1)), 1f)
    )
  }

  def colorSdf(sdf: GlValue[GlFloatType],
               color: GlValue[GlVec4Type] = Colors.black,
               bgColor: GlValue[GlVec4Type] = Colors.white): GlValue[GlVec4Type] = {
    GlFuncs.mix(
      color,
      bgColor,
      smoothBorder(sdf)
    )
  }

  def pointTranslate(translation: GlValue[GlVec2Type],
                     point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlBraces(
      point - translation
    )
  }

  def pointScale(scaleX: GlValue[GlFloatType], scaleY: GlValue[GlFloatType],
                     point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlBraces(point * GlVec2Val(scaleX, scaleY))
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

  def pointRotateAroundOrigin(rotation: GlValue[GlFloatType],
                              originX: GlValue[GlFloatType],
                              originY: GlValue[GlFloatType],
                              point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlBraces(
      GlVec3Val(
        GlVec3Val(point, 1f) *
        GlMat3Val(
          1f, 0f, originX * -1f,
          0f, 1f, originY * -1f,
          0f, 0f, 1f
        ) *
        GlMat3Val(
          GlFuncs.cos(rotation), GlFuncs.sin(rotation) * GlFloatVal(-1f), 0f,
          GlFuncs.sin(rotation), GlFuncs.cos(rotation), 0f,
          0f, 0f, 1f
        ) *
        GlMat3Val(
          1f, 0f, originX,
          0f, 1f, originY,
          0f, 0f, 1f
        )
      ).xy
    )
  }

  def repeatPoint(distance: GlValue[GlVec2Type],
                  point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlFuncs.mod(point, distance) - GlBraces(distance * 0.5f)
  }

  def animateFloat(constant: GlValue[GlFloatType] = GlFloatVal(0f),
                   coef: GlValue[GlFloatType] = GlFloatVal(1f),
                   tAdjustment: GlValue[GlFloatType] = GlFloatVal(0f)): GlValue[GlFloatType] = {

    constant + GlBraces(coef * GlFuncs.sin(GlVar("iGlobalTime", GlFloatType()) + tAdjustment))
  }

  def incrementFloat(constant: GlValue[GlFloatType] = GlFloatVal(0f),
                   coef: GlValue[GlFloatType] = GlFloatVal(1f),
                   tAdjustment: GlValue[GlFloatType] = GlFloatVal(0f)): GlValue[GlFloatType] = {

    constant + GlBraces(coef * GlBraces(GlVar("iGlobalTime", GlFloatType()) + tAdjustment))
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