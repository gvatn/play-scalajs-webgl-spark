package ui.binary

import ui.font.Incrementer
import org.scalajs.dom

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.scalajs.js.typedarray.Uint8Array
import scala.scalajs.js

object BinaryBuffer {
  // https://github.com/beatgammit/base64-js/blob/master/index.js
  def fromBase64(base64: Uint8Array): BinaryBuffer = {
    val len = base64.length
    if (len % 4 > 0) {
      throw new Exception("Invalid base64 string. Must be multiple of 4")
    }
    val lookup = new mutable.HashMap[Int, Char]()
    val revLookup = new mutable.HashMap[Int, Int]()
    for ((char, index) <- "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".zipWithIndex) {
      lookup += index -> char
      revLookup += char.toString.codePointAt(0) -> index
    }
    revLookup += "-".codePointAt(0) -> 62
    revLookup += "_".codePointAt(0) -> 63

    // There might be placeholders on the end of the string "="
    val equalPoint = "=".codePointAt(0)
    val placeholders =
      if (base64(len-2) == equalPoint) 2
      else if (base64(len-1) == equalPoint) 1
      else 0
    val bin = new Uint8Array(len * 3 / 4 - placeholders)
    // If there are placeholders, only get up to the last complete 4 chars
    val l = if (placeholders > 0) len - 4 else len

    var L = new Incrementer()
    var i, j = 0
    while (i < l) {
      val tmp =
        (revLookup(base64(i)) << 18) |
          (revLookup(base64(i + 1)) << 12) |
          (revLookup(base64(i + 2)) << 6) |
          revLookup(base64(i + 3))

      bin(L.inc()) = ((tmp >> 16) & 0xFF).toShort
      bin(L.inc()) = ((tmp >> 8) & 0xFF).toShort
      bin(L.inc()) = (tmp & 0xFF).toShort
      i += 4
      j += 3
    }
    if (placeholders == 2) {
      val tmp = (revLookup(base64(i)) << 2) | (revLookup(base64(i + 1)) >> 4)
      bin(L.inc()) = (tmp & 0xFF).toShort
    } else if (placeholders == 1) {
      val tmp = (revLookup(base64(i)) << 10) | (revLookup(base64(i + 1)) << 4) | (revLookup(base64(i + 2)) >> 2)
      bin(L.inc()) = ((tmp >> 8) & 0xFF).toShort
      bin(L.inc()) = (tmp & 0xFF).toShort
    }

    new BinaryBuffer(bin)
  }
}

// https://github.com/feross/buffer/blob/master/index.js
class BinaryBuffer(val bytes: Uint8Array) {

  def length: Int = bytes.length

  def readUInt32LE(offset: Int): Long = {
    bytes(offset) |
      (bytes(offset + 1) << 8) |
      (bytes(offset + 2) << 16) +
      (bytes(offset + 3) * 0x1000000)
  }

  def readInt32LE(offset: Int): Long = {
    bytes(offset) |
      bytes(offset + 1) << 8 |
      bytes(offset + 2) << 16 |
      bytes(offset + 3) << 24
  }

  def readUInt16LE(offset: Int): Int = {
    bytes(offset) |
      bytes(offset + 1) << 8
  }

  def readInt16LE(offset: Int): Int = {
    val read = bytes(offset) | bytes(offset + 1) << 8
    if ((read & 0x8000) == 0) read else read | 0xFFFF0000
  }

  def readUInt8(offset: Int): Int = {
    bytes(offset)
  }

  def readInt8(offset: Int): Int = {
    val read = bytes(offset)
    if ((read & 0x80) == 0) read else (0xff - bytes(offset) + 1) * -1
  }

  def readStringNT(offset: Int): String = {
    var pos = offset
    while (pos < bytes.length && bytes(pos) != 0x00) {
      pos += 1
    }
    utf8Slice(offset, pos)
  }

  def utf8Slice(start: Int, end: Int): String = {
    val res: ListBuffer[Int] = new ListBuffer[Int]
    var i = start
    while (i < end) {
      val firstByte = bytes(i)
      var codePoint: Int = -1
      val bytesPerSequence =
        if (firstByte > 0xEF) 4
        else if (firstByte > 0xDF) 3
        else if (firstByte > 0xBF) 2
        else 1
      if (i + bytesPerSequence <= end) {
        bytesPerSequence match {
          case 1 =>
            if (firstByte < 0x80) {
              codePoint = firstByte
            }
          case 2 =>
            val secondByte = bytes(i + 1)
            if ((secondByte & 0xC0) == 0x80) {
              val tempCodePoint = (firstByte & 0x1F) << 0x6 | (secondByte & 0x3F)
              if (tempCodePoint > 0x7F) {
                codePoint = tempCodePoint
              }
            }
          case 3 =>
            val secondByte = bytes(i + 1)
            val thirdByte = bytes(i + 2)
            if ((secondByte & 0xC0) == 0x80 && (thirdByte & 0xC0) == 0x80) {
              val tempCodePoint = (firstByte & 0xF) << 0xC | (secondByte & 0x3F) << 0x6 | (thirdByte & 0x3F)
              if (tempCodePoint > 0x7FF && (tempCodePoint < 0xD800 || tempCodePoint > 0xDFFF)) {
                codePoint = tempCodePoint
              }
            }
          case 4 =>
            val secondByte = bytes(i + 1)
            val thirdByte = bytes(i + 2)
            val fourthByte = bytes(i + 3)
            if ((secondByte & 0xC0) == 0x80 && (thirdByte & 0xC) == 0x80 && (fourthByte & 0xC0) == 0x80) {
              val tempCodePoint =
                (firstByte & 0xF) << 0x12 |
                (secondByte & 0x3F) << 0xC |
                (thirdByte & 0x3F) << 0x6 |
                (fourthByte & 0x3F)
              if (tempCodePoint > 0xFFFF && tempCodePoint < 0x110000) {
                codePoint = tempCodePoint
              }
            }
        }
      }
      if (codePoint == -1) {
        // No valid codePoint generated
        // Insert replacement char
        codePoint = 0xFFFD
        res += codePoint
        i += 1
      } else {
        // Encode to utf16 (surrogate dance)
        codePoint -= 0x10000
        res += codePoint >>> 10 & 0x3FF | 0xD800
        codePoint = 0xDC00 | codePoint & 0x3FF
        res += codePoint
        i += bytesPerSequence
      }
    }
    // Decode codePoints
    res.map(codePoint => {
      js.Dynamic.global.String.fromCharCode(codePoint).asInstanceOf[String]
    }).foldLeft("")((a,b) => a + b)
  }
}
