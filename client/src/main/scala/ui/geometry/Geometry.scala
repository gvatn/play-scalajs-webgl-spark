package ui.geometry

import ui.arrayBuffer.ArrayBuffer

import scala.scalajs.js.typedarray.Uint16Array

class Geometry(val vertices: Seq[ArrayBuffer]) {
}

object Geometry {
  def apply(vertices: Seq[ArrayBuffer]): Geometry = new Geometry(vertices)

  // https://github.com/Jam3/quad-indices/blob/master/index.js
  def quadIndices(count: Int, ccw: Boolean = false, start: Int = 0): Uint16Array = {
    val tpl = if (ccw) Seq(2, 1, 3) else Seq(0, 2, 3)
    val totalIndices = count * 6
    val indices = new Uint16Array(totalIndices)
    var i, j = 0
    while (i < totalIndices) {
      val off = i + start
      indices(off + 0) = j + 0
      indices(off + 1) = j + 1
      indices(off + 2) = j + 2
      indices(off + 3) = j + tpl(0)
      indices(off + 4) = j + tpl(1)
      indices(off + 5) = j + tpl(2)
      i += 6
      j += 4
    }
    indices
  }
}
