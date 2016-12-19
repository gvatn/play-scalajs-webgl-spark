package ui.shader

import ui.loader.{AjaxLoader, AjaxTextResult, TextAsset}

object Shader {
  def request(name: String, callback: (Shader, Shader) => Unit): Unit = {
    val vertText = TextAsset(s"shaders/$name/vert.glsl")
    val fragText = TextAsset(s"shaders/$name/frag.glsl")
    AjaxLoader.loadFiles(Seq(
      vertText,
      fragText
    ), (result) => {
      callback(
        new Shader(result(vertText).asInstanceOf[AjaxTextResult].text),
        new Shader(result(fragText).asInstanceOf[AjaxTextResult].text)
      )
    })
  }
}

class Shader(var source: String) {

}
