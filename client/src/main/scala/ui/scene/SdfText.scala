package ui.scene

import loadFile.LoadFile
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLImageElement
import ui.GLContext
import ui.arrayBuffer.{ElementBuffer, Float32Buffer}
import ui.binary.{BMFont, BinaryBuffer, TextEncoder}
import ui.font.{FontFiles, TextLayout}
import ui.geometry.Geometry
import ui.program.{Attribute, DataType, Program, Uniform}
import ui.shader.Shader
import ui.texture.Texture

object SdfText {

  def apply(glContext: GLContext, text: String, fontFiles: FontFiles): SceneItem = {
    val font = BMFont.parse(fontFiles.bin)
    val textLayout = new TextLayout(text, font, 80, TextLayout.AlignLeft, 0, 4)
    val test = 6
    textLayout.update()
    val glProgram = new Program(
      glContext,
      new Shader(LoadFile.syncLoad("sdf-text/vert")),
      new Shader(LoadFile.syncLoad("sdf-text/frag")),
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
    sceneItem.textures += "map" -> new Texture(fontFiles.image)
    sceneItem
  }

}
