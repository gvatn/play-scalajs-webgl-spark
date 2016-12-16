package ui.font

import org.scalajs.dom
import org.scalajs.dom.raw.HTMLImageElement
import ui.binary.BinaryBuffer
import ui.loader._

import scala.scalajs.js.typedarray.Uint8Array

object FontFiles {
  def request(name: String, callback: (FontFiles) => Unit): Unit = {
    AjaxLoader.loadFiles(Seq(
      BinaryAsset("fonts/arial-sdf.bin"),
      ImageAsset("fonts/arial-sdf.png")
    ), (results) => {
      val bin = results(BinaryAsset("fonts/arial-sdf.bin")).asInstanceOf[AjaxBinaryResult]
      val image = results(ImageAsset("fonts/arial-sdf.png")).asInstanceOf[AjaxImageResult]
      val fontFiles = new FontFiles(bin.bin, image.image)
      callback(fontFiles)
    })

  }
}

class FontFiles(val bin: BinaryBuffer, val image: HTMLImageElement) {

}
