package ui.font

import org.scalajs.dom.raw.HTMLImageElement
import ui.binary.BinaryBuffer
import ui.loader._


object FontFiles {
  def request(name: String, callback: FontFiles => Unit): Unit = {
    val binAsset = BinaryAsset("fonts/arial-sdf.bin")
    val imgAsset = ImageAsset("fonts/arial-sdf.png")
    AjaxLoader.loadFiles(Seq(
      binAsset,
      imgAsset
    ), (results) => {
      val bin = results(binAsset).asInstanceOf[AjaxBinaryResult]
      val image = results(imgAsset).asInstanceOf[AjaxImageResult]
      val fontFiles = new FontFiles(bin.bin, image.image)
      callback(fontFiles)
    })

  }
}

class FontFiles(val bin: BinaryBuffer, val image: HTMLImageElement) {

}
