package ui.font

import ui.binary.{BMChar, BMFont}
import org.scalajs.dom

import scala.collection.mutable.ListBuffer
import scala.scalajs.js.typedarray.Float32Array

object TextLayout {
  sealed trait Align
  case object AlignLeft extends Align
  case object AlignRight extends Align
  case object AlignCenter extends Align
}


class TextLayout(var text: String,
                 val font: BMFont,
                 val width: Int,
                 val align: TextLayout.Align = TextLayout.AlignLeft,
                 val letterSpacing: Int = 0,
                 val tabSize: Int = 4,
                 val above: Boolean = false,
                 val flipY: Boolean = false) {

  val glyphs: ListBuffer[TextLayoutGlyph] = new ListBuffer[TextLayoutGlyph]

  /*
  val space: BMChar = font.charsById(" ".codePointAt(0))
  val tab: BMChar = font.charsById("\t".codePointAt(0))
  */



  def update(): Unit = {
    val lines = lineWraps(width, 0, text.length)
    val maxLineWidth = lines.foldLeft(0)((max, fitting) => math.max(max, fitting.width))
    // Pen position
    var x = 0
    var y = 0
    val lineHeight = font.common.lineHeight
    val baseline = font.common.base
    val descender = lineHeight - baseline
    val height = lineHeight * lines.length - descender

    for ((line, lineIndex) <- lines.zipWithIndex) {
      var lastGlyph: BMChar = null
      // For each glyph in line
      for (i <- line.start until line.end) {
        val id = text.codePointAt(i)
        val glyph = font.charsById(id)
        if (lastGlyph != null) {
          x += font.getKerning(lastGlyph.id, glyph.id)
        }
        var tx = x
        align match {
          case TextLayout.AlignCenter =>
            tx += (maxLineWidth - line.width) / 2
          case TextLayout.AlignRight =>
            tx += (maxLineWidth - line.width)
          case TextLayout.AlignLeft =>
            tx += 0
        }
        glyphs += TextLayoutGlyph(
          position = TextLayoutGlyphCoords(tx, y),
          data = glyph,
          index = i,
          line = lineIndex
        )
        // Move pen forward
        x += glyph.xAdvance + letterSpacing
        lastGlyph = glyph
      }
      y -= lineHeight
      x = 0
    }
  }

  /**
    * Wraps text to lines and returns indexes and width
    * for each line
    *
    * @param width
    * @param start
    * @param end
    * @return
    */
  def lineWraps(width: Int, start: Int, end: Int): Seq[FittingChars] = {
    val lines = new ListBuffer[FittingChars]
    var curStart = start
    while (curStart < end && curStart < text.length) {
      val nextNewline = if (text.indexOf("\n", curStart) != -1) text.indexOf("\n", curStart) else text.length
      // Strip whitespace at start of line
      while (curStart < nextNewline && text.charAt(curStart) == ' ') {
        curStart += 1
      }
      // Get chars fitting on the current line
      val FittingChars(fitStart, fitEnd, fitWidth) = measureFittingChars(curStart, end, width)
      var lineEnd = curStart + (fitEnd-fitStart)
      var nextStart = lineEnd + "\n".length
      // If limit was reached before newline
      if (lineEnd < nextNewline) {
        // Find char to break on
        while (lineEnd > curStart && text.charAt(lineEnd) != ' ') {
          lineEnd -= 1
        }
        if (lineEnd == curStart) {
          // We went all the way to start without whitespace
          if (nextStart > curStart + "\n".length) {
            nextStart -= 1
          }
          // Show all from measure fitting
          lineEnd = nextStart
        } else {
          nextStart = lineEnd
          // Strip whitespace from end of line
          while (lineEnd > curStart && text.charAt(lineEnd - "\n".length) == ' ') {
            lineEnd -= 1
          }
        }
      }
      if (lineEnd >= curStart) {
        lines += measureFittingChars(curStart, lineEnd, width)
      }
      curStart = nextStart
    }
    lines
  }

  /**
    * Returns start, end, width
    * @param start
    * @param end
    * @param width
    * @return
    */
  def measureFittingChars(start: Int, end: Int, width: Int): FittingChars = {
    var curPen = 0
    var curWidth = 0
    var count = 0
    var lastGlyph: BMChar = null
    import scala.math._
    val calcEnd = min(text.length, end)
    var break = false
    for (i <- start until calcEnd if !break) {
      val id = text.codePointAt(i)
      val glyph: BMChar = font.charsById(id)
      // Move pen forward
      val kerning = if (lastGlyph != null) font.getKerning(lastGlyph.id, glyph.id) else 0
      curPen += kerning

      val nextPen = curPen + glyph.xAdvance + letterSpacing
      var nextWidth = curPen + glyph.width
      if (nextWidth >= width || nextPen >= width) {
        // Limit reached
        break = true
      } else {
        curPen = nextPen
        curWidth = nextWidth
        lastGlyph = glyph
        count += 1
      }
    }
    if (lastGlyph != null) {
      curWidth += lastGlyph.xOffset
    }
    FittingChars(start, start + count, curWidth)
  }

  def vertexData(): Float32Array = {
    val data = new Float32Array(glyphs.length * 4 * 2 * 2)
    val texWidth = font.common.scaleW
    val texHeight = font.common.scaleH
    val i = new Incrementer()
    for (glyph <- glyphs) {
      // Position data
      // Bottom left position
      val x = glyph.position.x.toFloat + glyph.data.xOffset
      val y = glyph.position.y.toFloat + glyph.data.yOffset
      // Quad size
      val w = glyph.data.width.toFloat
      val h = glyph.data.height.toFloat

      // Uv data
      val bitmap = glyph.data
      val bw = bitmap.x + bitmap.width
      val bh = bitmap.y + bitmap.height
      // Top left positions
      val u0 = bitmap.x.toFloat / texWidth
      val u1 = bw.toFloat / texWidth
      val v1 = if (flipY) (texHeight - bitmap.y).toFloat / texHeight else bitmap.y.toFloat / texHeight
      val v0 = if (flipY) (texHeight - bh).toFloat / texHeight else bh.toFloat / texHeight

      // Bottom left
      // Positions
      data(i.inc()) = x
      data(i.inc()) = y
      // Uvs
      data(i.inc()) = u0
      data(i.inc()) = v1

      // Top left
      // Positions
      data(i.inc()) = x
      data(i.inc()) = y + h
      // Uvs
      data(i.inc()) = u0
      data(i.inc()) = v0

      // Top right
      // Positions
      data(i.inc()) = x + w
      data(i.inc()) = y + h
      // Uvs
      data(i.inc()) = u1
      data(i.inc()) = v0

      // Bottom right
      // Positions
      data(i.inc()) = x + w
      data(i.inc()) = y
      // Uvs
      data(i.inc()) = u1
      data(i.inc()) = v1

    }
    data
  }

  def positions(): Float32Array = {
    val positions = new Float32Array(glyphs.length * 4 * 2)
    val i = new Incrementer()
    for (glyph <- glyphs) {
      // Bottom left position
      val x = glyph.position.x + glyph.data.xOffset
      val y = glyph.position.y
      // Quad size
      val w = glyph.data.width
      val h = glyph.data.height

      // Bottom left
      positions(i.inc()) = x
      positions(i.inc()) = y
      // Top left
      positions(i.inc()) = x
      positions(i.inc()) = y + h
      // Top right
      positions(i.inc()) = x + w
      positions(i.inc()) = y + h
      // Bottom right
      positions(i.inc()) = x + w
      positions(i.inc()) = y
    }
    positions
  }

  def uvs(): Float32Array = {
    val uvs = new Float32Array(glyphs.length * 4 * 2)
    val texWidth = font.common.scaleW
    val texHeight = font.common.scaleH
    val i = new Incrementer()
    for (glyph <- glyphs) {
      val bitmap = glyph.data
      val bw = bitmap.x + bitmap.width
      val bh = bitmap.y + bitmap.height
      // Top left positions
      val u0 = bitmap.x.toFloat / texWidth
      val u1 = bw.toFloat / texWidth
      val v0 = if (flipY) (texHeight - bitmap.y).toFloat / texHeight else bitmap.y.toFloat / texHeight
      val v1 = if (flipY) (texHeight - bh).toFloat / texHeight else bh.toFloat / texHeight

      // Bottom left
      uvs(i.inc()) = u0
      uvs(i.inc()) = v1
      // Top left
      uvs(i.inc()) = u0
      uvs(i.inc()) = v0
      // Top right
      uvs(i.inc()) = u1
      uvs(i.inc()) = v0
      // Bottom right
      uvs(i.inc()) = u1
      uvs(i.inc()) = v1
    }
    uvs
  }

}

case class FittingChars(start: Int, end: Int, width: Int)

case class TextLayoutGlyph(position: TextLayoutGlyphCoords,
                      data: BMChar,
                      index: Int,
                      line: Int)

case class TextLayoutGlyphCoords(x: Int, y: Int)

// Helper to increment
class Incrementer(var i: Int = 0) {
  def inc(num: Int = 1): Int = {
    i += 1
    i - 1
  }

  implicit def toInteger(inc: Incrementer): Int = {
    inc.i
  }
}
