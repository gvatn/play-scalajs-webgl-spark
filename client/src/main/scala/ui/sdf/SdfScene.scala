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

class SdfScene(val shader3d: Boolean = false) {
  val sdfItems: ListBuffer[SdfItem] = ListBuffer[SdfItem]()

  def createSceneItem(glContext: GLContext): SceneItem = {
    val program = new GlProgram(
      createVertexShader(),
      if (shader3d) create3dFragmentShader() else createFragmentShader()
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
        GlAssign(GlGlobals.Position, GlVec4Val(GlVar("position", GlVec2Type()), 0d, 1d)),
        GlAssign(GlVar("vPosition", GlVec2Type()), GlVar("position", GlVec2Type()))
      )
    )
  }

  def sdfShape3d: GlValue[GlFloatType] = {
    GlFuncs.length(GlVar("point", GlVec3Type())) - 1d
  }

  def create3dFragmentShader(): GlFragmentShader = {
    val epsilon = GlFloatVal(0.0001d)
    val minDist = GlFloatVal(1d)
    val maxDist = GlFloatVal(200d)
    val marchingSteps = GlIntVal(300)
    new GlFragmentShader(
      ListBuffer[GlAttribute[GlType]](),
      ListBuffer[GlUniform[GlType]](
        GlUniform("iGlobalTime", GlFloatType()),
        GlUniform("uCameraPos", GlVec3Type()),
        GlUniform("uCameraDir", GlVec3Type()),
        GlUniform("uViewport", GlVec2Type())
      ),
      ListBuffer[GlVarying[GlType]](
        GlVarying("vPosition", GlVec2Type())
      ),
      GlBlock(
        GlAssign.init(GlVar("viewDir", GlVec3Type()), GlCall(
          "rayDirection", GlVec3Type(),
          GlVec2Val("vPosition") / GlVec2Val(GlVec2Val("uViewport").y / GlVec2Val("uViewport").x, 1.0)
        )),
        GlAssign.init(GlVar("eye", GlVec3Type()), GlVec3Val("uCameraPos")),
        GlAssign.init(GlVar("viewToWorld", GlMat4Type()), GlCall(
          "viewMatrix", GlMat4Type(),
          GlVec3Val("eye"),
          GlVec3Val("uCameraPos") + GlBraces(GlVec3Val("uCameraDir") * 4.0),
          GlVec3Val(0.0, 1.0, 0.0)
        )),
        GlAssign.init(GlVar("worldDir", GlVec3Type()), GlVec4Val(
          GlMat4Val("viewToWorld").matrixMult(GlVec4Val.v3f(GlVec3Val("viewDir"), 0d))
        ).xyz),
        GlAssign.init(GlVar("dist", GlFloatType()), GlCall("shortestDistance", GlFloatType(),
          GlVar("eye", GlVec3Type()),
          GlVar("worldDir", GlVec3Type()))
        ),
        GlIf(
          GlFloatVal("dist") > GlBraces(maxDist - epsilon),
          GlBlock(
            /* No shape in ray */
            GlAssign(GlGlobals.Color, Colors.black)
          ),
          Some(GlBlock(
            GlAssign.init(GlVar("p", GlVec3Type()), GlVec3Val("eye") + GlFloatVal("dist") * GlVec3Val("worldDir")),
            GlAssign.init(GlVar("K_a", GlVec3Type()), GlVec3Val(0.2d, 0.2d, 0.2d)),
            GlAssign.init(GlVar("K_d", GlVec3Type()), GlVec3Val(0.2d, 0.3d, 0.6d)),
            GlAssign.init(GlVar("K_s", GlVec3Type()), GlVec3Val(1d, 1d, 1d)),
            GlAssign.init(GlVar("shininess", GlFloatType()), GlFloatVal(10d)),
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
              GlVec4Val.v3f(GlVec3Val("color"), 1d)
            )
          ))
        )
      ),
      ListBuffer[GlFunction[GlType]](
        GlFunction(
          "sdfDist", GlFloatType(),
          GlArgument("point", GlVec3Type()),
          GlBlock(
            GlReturn(sdfShape3d)
          )
        ),
        GlFunction(
          "rayDirection", GlVec3Type(),
          GlArgument("canvasPosition", GlVec2Type()),
          GlBlock(
            GlReturn(GlFuncs.normalize(GlVec3Val(
              GlVar("canvasPosition", GlVec2Type()),
              GlFloatVal(2d) / GlFuncs.tan(GlFuncs.radians(45d) / 2d) * -1d
            )))
          )
        ),
        GlFunction(
          "viewMatrix", GlMat4Type(),
          GlArgument("eye", GlVec3Type()),
          GlArgument("center", GlVec3Type()),
          GlArgument("up", GlVec3Type()),
          GlBlock(
            /* look at */
            GlAssign.init(GlVar("f", GlVec3Type()), GlFuncs.normalize(GlVec3Val("center") - GlVec3Val("eye"))),
            GlAssign.init(GlVar("s", GlVec3Type()), GlFuncs.cross(GlVec3Val("f"), GlVec3Val("up"))),
            GlAssign.init(GlVar("u", GlVec3Type()), GlFuncs.cross(GlVec3Val("s"), GlVec3Val("f"))),
            GlReturn(GlMat4Val(
              GlVec4Val.v3f(GlVec3Val("s"), 0.0),
              GlVec4Val.v3f(GlVec3Val("u"), 0.0),
              GlVec4Val.v3f(GlVec3Val("f") * -1.0, 0.0),
              GlVec4Val(0.0, 0.0, 0.0, 1.0)
            ))
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
            GlAssign.init(GlVar("R", GlVec3Type()), GlFuncs.reflect(GlVec3Val("L") * -1d, GlVec3Val("N"))),
            GlAssign.init(GlVar("dotLN", GlFloatType()), GlFuncs.dot(GlVec3Val("L"), GlVec3Val("N"))),
            GlAssign.init(GlVar("dotRV", GlFloatType()), GlFuncs.dot(GlVec3Val("R"), GlVec3Val("V"))),
            GlIf(GlFloatVal("dotLN") < 0d, GlBlock(
              /* Light not visible from this point on the surface */
              GlReturn(GlVec3Val(0d, 0d, 0d))
            )),
            GlIf(GlFloatVal("dotRV") < 0d, GlBlock(
              /* Light reflection in opposite direction as viewer, apply diffuse component */
              GlReturn(GlVec3Val("lightIntensity") * GlBraces(GlVec3Val("k_d") * GlFloatVal("dotLN")))
            )),
            GlReturn(GlVec3Val("lightIntensity") * GlBraces(
              GlVec3Val("k_d") * GlFloatVal("dotLN") + GlVec3Val("k_s") * GlFuncs.pow(GlFloatVal("dotRV"), GlFloatVal("alpha")))
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
            GlAssign.init(GlVar("ambientLight", GlVec3Type()), GlVec3Val(0.25d, 0.2d, 0.2d)),
            GlAssign.init(GlVar("color", GlVec3Type()), GlVec3Val("ambientLight") * GlVec3Val("k_a")),
            GlAssign.init(GlVar("lightPos", GlVec3Type()), GlVec3Val(
              GlVec3Val("uCameraPos").x + GlFuncs.sin(GlFloatVal("iGlobalTime") * 0.7) * 4d,
              GlVec3Val("uCameraPos").y + 2d,
              GlVec3Val("uCameraPos").z + GlFuncs.cos(GlFloatVal("iGlobalTime") * 0.7) * 4d)
            ),
            GlAssign.init(GlVar("lightIntensity", GlVec3Type()), GlVec3Val(0.4d, 0.4d, 0.4d)),
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
      circle(animateFloat(0.25d, 0.2d), pointTranslate(GlVec2Val(-0.2d, 0.0d))),
      box(0.4d, 0.4d, pointRotate(animateFloat(0.25d, 0.2d)))
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
          GlVec2Val(0d, 0d)
        )
      )
    )
  }

  def box3d(width: GlValue[GlFloatType], height: GlValue[GlFloatType], depth: GlValue[GlFloatType],
            rounding: GlValue[GlFloatType] = GlFloatVal(0d),
          point: GlValue[GlVec3Type] = GlVar("point", GlVec3Type())): GlValue[GlFloatType] = {

    GlBraces(
      GlFuncs.length(GlFuncs.max(GlFuncs.abs(point) - GlVec3Val(width,height,depth), GlVec3Val(0d,0d,0d))) - rounding
    )
  }

  def layered(sdf1: GlValue[GlFloatType],
              color: GlValue[GlVec4Type],
              bgColor: GlValue[GlVec4Type] = GlVec4Val(1.0d, 1.0d, 1.0d, 1.0d),
              mixFactor: GlValue[GlFloatType] = GlFloatVal(1d)): GlValue[GlVec4Type] = {
    GlFuncs.mix(
      bgColor,
      color,
      GlVec4Val.v3f(GlVec3Val(mixFactor, mixFactor, mixFactor) * GlBraces(1d - smoothBorder(sdf1)), 1d)
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
        GlFuncs.cos(rotation), GlFuncs.sin(rotation) * GlFloatVal(-1d),
        GlFuncs.sin(rotation), GlFuncs.cos(rotation)
      )
    )
  }

  def pointTranslate3d(translation: GlValue[GlVec3Type],
                     point: GlValue[GlVec3Type] = GlVec3Val("point")): GlValue[GlVec3Type] = {
    GlBraces(
      point - translation
    )
  }

  def pointScale3d(scaleX: GlValue[GlFloatType], scaleY: GlValue[GlFloatType], scaleZ: GlValue[GlFloatType],
                 point: GlValue[GlVec3Type] = GlVec3Val("point")): GlValue[GlVec3Type] = {
    GlBraces(point * GlVec3Val(scaleX, scaleY, scaleZ))
  }

  def pointRotate3d(rotation: GlValue[GlFloatType],
                  point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlBraces(
      point * GlMat2Val(
        GlFuncs.cos(rotation), GlFuncs.sin(rotation) * GlFloatVal(-1d),
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
        GlVec3Val(point, 1d) *
        GlMat3Val(
          1d, 0d, originX * -1d,
          0d, 1d, originY * -1d,
          0d, 0f, 1d
        ) *
        GlMat3Val(
          GlFuncs.cos(rotation), GlFuncs.sin(rotation) * GlFloatVal(-1d), 0d,
          GlFuncs.sin(rotation), GlFuncs.cos(rotation), 0d,
          0d, 0d, 1d
        ) *
        GlMat3Val(
          1d, 0d, originX,
          0d, 1d, originY,
          0d, 0d, 1d
        )
      ).xy
    )
  }

  def repeatPoint(distance: GlValue[GlVec2Type],
                  point: GlValue[GlVec2Type] = GlVar("vPosition", GlVec2Type())): GlValue[GlVec2Type] = {
    GlFuncs.mod(point, distance) - GlBraces(distance * 0.5d)
  }

  def repeatPoint3d(distance: GlValue[GlVec3Type],
                  point: GlValue[GlVec3Type] = GlVar("point", GlVec3Type())): GlValue[GlVec3Type] = {
    GlFuncs.mod(point, distance) - GlBraces(distance * 0.5d)
  }

  def animateFloat(constant: GlValue[GlFloatType] = GlFloatVal(0d),
                   coef: GlValue[GlFloatType] = GlFloatVal(1d),
                   tAdjustment: GlValue[GlFloatType] = GlFloatVal(0d)): GlValue[GlFloatType] = {

    constant + GlBraces(coef * GlFuncs.sin(GlVar("iGlobalTime", GlFloatType()) + tAdjustment))
  }

  def floatWave(from: GlValue[GlFloatType] = GlFloatVal(0d),
                to: GlValue[GlFloatType] = GlFloatVal(1d),
                tAdjustment: GlValue[GlFloatType] = GlFloatVal(0d)): GlValue[GlFloatType] = {

    from + GlBraces(GlBraces(to - from) * GlBraces(GlFuncs.sin(GlVar("iGlobalTime", GlFloatType()) + tAdjustment) * 0.5 + 0.5))
  }

  def incrementFloat(constant: GlValue[GlFloatType] = GlFloatVal(0d),
                   coef: GlValue[GlFloatType] = GlFloatVal(1d),
                   tAdjustment: GlValue[GlFloatType] = GlFloatVal(0d)): GlValue[GlFloatType] = {

    constant + GlBraces(coef * GlBraces(GlVar("iGlobalTime", GlFloatType()) + tAdjustment))
  }

  def smoothBorder(glValue: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.smoothstep(0d, 0.005d, glValue)
  }

  def union(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.min(sdf1, sdf2)
  }

  def subtract(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.max(GlMultiply(GlFloatVal(-1d), sdf1), sdf2)
  }

  def intersect(sdf1: GlValue[GlFloatType], sdf2: GlValue[GlFloatType]): GlValue[GlFloatType] = {
    GlFuncs.max(sdf1, sdf2)
  }

  def sphere(radius: GlValue[GlFloatType] = GlFloatVal(1d),
             point: GlValue[GlVec3Type] = GlVec3Val("point")): GlValue[GlFloatType] = {
    GlFuncs.length(point) - radius
  }

}