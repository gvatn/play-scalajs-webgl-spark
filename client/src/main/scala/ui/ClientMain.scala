package ui

import org.scalajs.dom
import shared.SharedMessages

import scala.scalajs.js
import org.scalajs.dom
import dom.document
import ui.arrayBuffer.{ArrayBuffer, ElementBuffer, Float32Buffer}
import ui.binary.{BMFont, BinaryBuffer, TextEncoder}
import ui.font.{FontFiles, TextLayout}
import loadFile.LoadFile
import org.scalajs.dom.raw.{HTMLImageElement, ImageData, MessageEvent, WebSocket}
import ui.geometry._
import ui.loader.{AjaxLoader, BinaryAsset, ImageAsset}
import ui.math.Vec2
import ui.program.{Attribute, DataType, Program, Uniform}
import ui.scene.{BitmapText, Scene, SceneItem, SdfText}
import ui.shader.Shader
import ui.texture.Texture

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.typedarray.Float32Array

object ClientMain extends js.JSApp {

  private var webSocket: WebSocket = _

  @JSExport
  def main(): Unit = {
    val glContext = new GLContext("main-canvas")
    glContext.init()
    val scene = new Scene(glContext)

    val test = "156184532k"

    FontFiles.request("arial", (files) => {

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


    /*
    sceneItem.textures += ("dataTexture" -> new Texture(Texture.seqToPixels(Seq(
      5, 10, 25, 23,
      4, 7, 8, 5,
      8, 11, 17, 26,
      3, 6, 4, 2
    )), 4, 4))
    */

    // Websocket
    /*
    webSocket = new WebSocket("ws://localhost:9000/websocket")
    webSocket.onmessage = onWebsocketMessage _
    webSocket.onopen = onWebsocketConn _
    */

  }

  def onWebsocketMessage(message: MessageEvent): Unit = {
    dom.console.log(message)
  }

  def onWebsocketConn(event: Any): Unit = {
    webSocket.send("Test from client")
  }
}
