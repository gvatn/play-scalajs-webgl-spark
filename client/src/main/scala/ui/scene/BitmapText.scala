package ui.scene

import loadFile.LoadFile
import org.scalajs.dom.raw.HTMLImageElement
import ui.program.{Attribute, DataType, Program, Uniform}
import org.scalajs.dom.document
import ui.GLContext
import ui.arrayBuffer.{ElementBuffer, Float32Buffer}
import ui.binary.{BMFont, BinaryBuffer, TextEncoder}
import ui.font.TextLayout
import ui.geometry.Geometry
import ui.shader.Shader
import ui.texture.Texture

object BitmapText {

  def apply(glContext: GLContext, text: String): SceneItem = {
    val fontBase64 = LoadFile.toBase64("fonts/arial.fnt")
    val fontImage64 = LoadFile.toBase64("fonts/arial_0.png")
    val imgElement: HTMLImageElement = document.createElement("img").asInstanceOf[HTMLImageElement]
    imgElement.src = "data:image/png;base64," + fontImage64
val test =1
    val fontBytes = TextEncoder.utf8ToBytes(fontBase64)
    val binBuffer = BinaryBuffer.fromBase64(fontBytes)
    val font = BMFont.parse(binBuffer)
    val textLayout = new TextLayout(text, font, 80, TextLayout.AlignLeft, 0, 4)
    textLayout.update()
    val glProgram = new Program(
      glContext,
      new Shader(LoadFile.syncLoad("shaders/simple-text/vert.glsl")),
      new Shader(LoadFile.syncLoad("shaders/simple-text/frag.glsl")),
      List(
        Attribute("position", DataType.GlFloat, 2),
        Attribute("uv", DataType.GlFloat, 2)
      ),
      List[Uniform]()
    )
    val sceneItem = new SceneItem(
      glProgram,
      Seq(
        new Float32Buffer(textLayout.positions()),
        new Float32Buffer(textLayout.uvs())),
      new ElementBuffer(Geometry.quadIndices(textLayout.glyphs.length))
    )
    sceneItem.textures += "map" -> new Texture(imgElement)
    sceneItem
  }

}
