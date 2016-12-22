package ui

import app.Scenes

import scala.scalajs.js
import org.scalajs.dom
import ui.font.{FontFiles, TextLayout}
import org.scalajs.dom.raw.{MessageEvent, WebSocket}
import ui.math.{Vec3, Vec4}
import ui.program.{Attribute, DataType, Program, Uniform}
import ui.scene._
import ui.sdf.SdfScene
import ui.shader.Shader
import ui.shader.builder.types.{GlFloatType, GlVec4Type}
import ui.shader.builder.value.GlValue
import ui.texture.Texture

import scala.scalajs.js.annotation.JSExport

object ClientMain extends js.JSApp {

  private var webSocket: WebSocket = _

  @JSExport
  def main(): Unit = {
    val glContext = new GLContext("main-canvas")
    glContext.init()

    val test = "156284532k"

    createSdf3dScene(glContext)

    /*
    */

    // Websocket
    /*
    webSocket = new WebSocket("ws://localhost:9000/websocket")
    webSocket.onmessage = onWebsocketMessage _
    webSocket.onopen = onWebsocketConn _
    */

  }

  def createSdf3dScene(context: GLContext): Unit = {
    val scene = new Scene(context)
    val sdfScene = new SdfScene(true) {
      override def sdfShape3d: GlValue[GlFloatType] = Scenes.main3d
    }
    scene.items += sdfScene.createSceneItem(context)
    scene.sceneModules += new Navigator(
      pos = Vec3(8.0, 5.0, 5.0),
      direction = Vec3(0.0, 2.0, -1.0)
    )
    scene.init()
    scene.draw()
  }

  def createSdfScene(context: GLContext): Unit = {
    val scene = new Scene(context)
    val sdfScene = new SdfScene {
      override def getColor: GlValue[GlVec4Type] = Scenes.main
    }
    scene.items += sdfScene.createSceneItem(context)
    scene.init()
    scene.draw()
  }


  def createBezierScene(context: GLContext): Unit = {
    Shader.request("bezier", (vertShader, fragShader) => {
      val scene = new Scene(context)
      val sceneItem = Plane(
        context,
        new Program(context,
          vertShader,
          fragShader,
          Seq(Attribute("position", DataType.GlFloat, 2))
        )
      )
      sceneItem.textures += ("dataTexture" -> new Texture(Texture.seqToPixels(Seq(
        5, 10, 25, 23,
        4, 7, 8, 5,
        8, 11, 17, 26,
        3, 9, 4, 2
      )), 4, 4))
      scene.items += sceneItem
      scene.init()
      scene.draw()
    })
  }

  def createGraphScene(glContext: GLContext): Unit = {
    Shader.request("graph-lines", (vertShader, fragShader) => {
      val scene = new Scene(glContext)
      val sceneItem = Plane(
        glContext,
        new Program(glContext,
          vertShader,
          fragShader,
          Seq(Attribute("position", DataType.GlFloat, 2))
        )
      )
      sceneItem.textures += ("dataTexture" -> new Texture(Texture.seqToPixels(Seq(
        5, 10, 25, 23,
        4, 7, 8, 5,
        8, 11, 17, 26,
        3, 9, 4, 2
      )), 4, 4))
      scene.items += sceneItem
      scene.init()
      scene.draw()
    })
  }

  def createFontScene(glContext: GLContext): Unit = {
    FontFiles.request("arial", (files) => {
      val scene = new Scene(glContext, Vec4(0.2f, 0.2f, 0.2f, 1f))
      scene.items += SdfText(glContext, "Test 12345", files)
      val bm2 = SdfText(glContext, "123", files)
      bm2.translate.x -= 80
      bm2.translate.y += 1
      bm2.scale.x = 1
      bm2.scale.y = 1
      bm2.rotate.y = ((2*3.14) / 360).toFloat * 45

      scene.items += bm2
      scene.init()
      scene.draw()
    })
  }

  def onWebsocketMessage(message: MessageEvent): Unit = {
    dom.console.log(message)
  }

  def onWebsocketConn(event: Any): Unit = {
    webSocket.send("Test from client")
  }
}
