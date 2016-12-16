package ui.binary

import org.scalajs.dom

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class BMFont {
  var kernings: ListBuffer[BMKerning] = _
  var info: BMInfo = _
  var common: BMCommon = _
  var pages: ListBuffer[String] = _
  var chars: ListBuffer[BMChar] = _
  val charsById: mutable.HashMap[Int,BMChar] = new mutable.HashMap[Int,BMChar]()

  def getKerning(prevId: Int, currId: Int): Int = {
    kernings.find(k => k.first == prevId && k.second == currId) match {
      case Some(kerning) => kerning.amount
      case None => 0
    }
  }
}

object BMFont {
  def parse(buf: BinaryBuffer): BMFont = {
    val font = new BMFont
    var i = 0
    // Verify header
    if (buf.readUInt8(i) != 66 ||
      buf.readUInt8(i + 1) != 77 ||
      buf.readUInt8(i + 2) != 70) {
      throw new Exception("Byte header not found")
    }
    i += 3
    // Check version
    if (buf.readUInt8(i) != 3) {
      throw new Exception("Only version 3 supported")
    }
    i += 1
    // Read five blocks
    for (b <- 0 to 4) {
      i += readBlock(font, buf, i)
    }
    font
  }

  def readBlock(font: BMFont, buf: BinaryBuffer, i: Int): Int = {
    if (i > buf.length - 1) {
      return 0
    }
    val blockID = buf.readUInt8(i)
    val blockSize = buf.readInt32LE(i + 1).toInt
    val blockIndex = i + 5
    blockID match {
      case 1 => font.info = readInfo(buf, blockIndex)
      case 2 => font.common = readCommon(buf, blockIndex)
      case 3 => font.pages = readPages(buf, blockIndex, blockSize)
      case 4 =>
        font.chars = readChars(buf, blockIndex, blockSize)
        // Map id to char
        font.charsById ++= font.chars.map(char => char.id -> char)
      case 5 => font.kernings = readKernings(buf, blockIndex, blockSize)
    }
    5 + blockSize
  }

  def readInfo(buf: BinaryBuffer, i: Int): BMInfo = {
    val bitField = buf.readUInt8(i + 2)
    new BMInfo(size = buf.readInt16LE(i),
      smooth = ((bitField >> 7) & 1) == 1,
      unicode = ((bitField >> 6) & 1) == 1,
      italic = ((bitField >> 5) & 1) == 1,
      bold = ((bitField >> 4) & 1) == 1,
      fixedHeight = ((bitField >> 3) & 1) == 1,
      charset = buf.readUInt8(i + 3),
      stretchH = buf.readUInt16LE(i + 4),
      aa = buf.readUInt8(i + 6),
      padding = Seq(
        buf.readInt8(i + 7),
        buf.readInt8(i + 8),
        buf.readInt8(i + 9),
        buf.readInt8(i + 10)
      ),
      spacing = Seq(
        buf.readInt8(i + 11),
        buf.readInt8(i + 12)
      ),
      outline = buf.readUInt8(i + 13),
      face = buf.readStringNT(i + 14)
    )
  }

  def readCommon(buf: BinaryBuffer, i: Int): BMCommon = {
    new BMCommon(
      lineHeight = buf.readUInt16LE(i),
      base = buf.readUInt16LE(i+2),
      scaleW = buf.readUInt16LE(i+4),
      scaleH = buf.readUInt16LE(i+6),
      pages = buf.readUInt16LE(i+8),
      packed = 0,
      alphaChnl = buf.readUInt8(i+11),
      redChnl = buf.readUInt8(i+12),
      greenChnl = buf.readUInt8(i+12),
      blueChnl = buf.readUInt8(i+13)
    )
  }

  def readPages(buf: BinaryBuffer, i: Int, size: Int): ListBuffer[String] = {
    val pages = new ListBuffer[String]
    val text = buf.readStringNT(i)
    val len = text.length + 1
    val count = size / len
    for (c <- 0 until count) {
      pages += buf.utf8Slice(i + c*len, (i + c*len)+text.length)
    }
    pages
  }

  def readChars(buf: BinaryBuffer, i: Int, blockSize: Int): ListBuffer[BMChar] = {
    val chars = new ListBuffer[BMChar]
    for (c <- 0 until (blockSize / 20)) {
      val off = c*20
      chars += new BMChar(
        id = buf.readUInt32LE(i + 0 + off).toInt,
        x = buf.readUInt16LE(i + 4 + off),
        y = buf.readUInt16LE(i + 6 + off),
        width = buf.readUInt16LE(i + 8 + off),
        height = buf.readUInt16LE(i + 10 + off),
        xOffset = buf.readInt16LE(i + 12 + off),
        yOffset = buf.readInt16LE(i + 14 + off),
        xAdvance = buf.readUInt16LE(i + 16 + off),
        page = buf.readUInt8(i + 18 + off),
        channel = buf.readUInt8(i + 19 + off)
      )
      val test = new BMChar(
        id = buf.readUInt32LE(i + 0 + off).toInt,
        x = buf.readUInt16LE(i + 4 + off),
        y = buf.readUInt16LE(i + 6 + off),
        width = buf.readUInt16LE(i + 8 + off),
        height = buf.readUInt16LE(i + 10 + off),
        xOffset = buf.readInt16LE(i + 12 + off),
        yOffset = buf.readInt16LE(i + 14 + off),
        xAdvance = buf.readUInt16LE(i + 16 + off),
        page = buf.readUInt8(i + 18 + off),
        channel = buf.readUInt8(i + 19 + off)
      )
    }
    chars
  }

  def readKernings(buf: BinaryBuffer, i: Int, blockSize: Int): ListBuffer[BMKerning] = {
    val kernings = new ListBuffer[BMKerning]
    for (c <- 0 until (blockSize / 10)) {
      val off = c*10
      kernings += new BMKerning(
        first = buf.readUInt32LE(i + 0 + off).toInt,
        second = buf.readUInt32LE(i + 4 + off).toInt,
        amount = buf.readInt16LE(i + 8 + off)
      )
    }
    kernings
  }
}

class BMChar(val id: Int,
             val x: Int,
             val y: Int,
             val width: Int,
             val height: Int,
             val xOffset: Int,
             val yOffset: Int,
             val xAdvance: Int,
             val page: Int,
             val channel: Int) {

}

class BMInfo(val size: Int,
             val smooth: Boolean,
             val unicode: Boolean,
             val italic: Boolean,
             val bold: Boolean,
             val fixedHeight: Boolean,
             val charset: Int,
             val stretchH: Int,
             val aa: Int,
             val padding: Seq[Int],
             val spacing: Seq[Int],
             val outline: Int,
             val face: String) {

}


class BMKerning(val first: Int,
                val second: Int,
                val amount: Int) {

}


class BMCommon(val lineHeight: Int,
               val base: Int,
               val scaleW: Int,
               val scaleH: Int,
               val pages: Int,
               val packed: Int,
               val alphaChnl: Int,
               val redChnl: Int,
               val greenChnl: Int,
               val blueChnl: Int)

