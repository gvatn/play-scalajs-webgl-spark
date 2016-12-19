package ui.shader.builder

import scala.collection.mutable.ListBuffer

class GlBlock(val commands: ListBuffer[GlCommand]) {

  def toGlsl: String = {
    s"{\n\t${commands.foldLeft("")(_ + "\n" + _.toGlsl)}\n}"
  }
}

object GlBlock {
  def apply(commands: ListBuffer[GlCommand]): GlBlock = {
    new GlBlock(commands)
  }

  def apply(commands: GlCommand*): GlBlock = {
    new GlBlock(commands.to[ListBuffer])
  }
}
