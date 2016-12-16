package ui.binary

import scala.scalajs.js
import scala.scalajs.js.typedarray.Uint8Array

// From https://github.com/coolaj86/TextEncoderLite/blob/master/index.js


object TextEncoder {

  def utf8ToBytes(string: String): Uint8Array = {
    var codePoint: Int = 0
    val length = string.length
    var units = new UnitsCounter(length * 6)
    var leadSurrogate: Int = 0
    val bytes = new js.Array[Int]
    var continue: Boolean = false
    var break: Boolean = false
    for (i <- 0 until length if !break) {
      codePoint = string.codePointAt(i)
      continue = false
      if (codePoint > 0xD7FF && codePoint < 0xE000) {
        // Is surrogate component
        if (leadSurrogate != 0) {
          if (codePoint < 0xDC00) {
            // 2 leads in a row
            if (units.decr(3) > -1) {
              bytes.push(0xEF, 0xBF, 0xBD)
            }
            leadSurrogate = codePoint
            continue = true
          } else {
            // Valid surrogate pair
            codePoint = leadSurrogate - 0xD800 << 10 | codePoint - 0xDC00 | 0x10000
            leadSurrogate = 0
          }
        } else {
          // No lead yet
          if (codePoint > 0xDBFF) {
            // Unexpected trail
            if (units.decr(3) > -1) {
              bytes.push(0xEF, 0xBF, 0xBD)
            }
            continue = true
          } else if (i + 1 == length) {
            // Unpaired lead
            if (units.decr(3) > -1) {
              bytes.push(0xEF, 0xBF, 0xBD)
              continue = true
            } else {
              // valid lead
              leadSurrogate = codePoint
              continue = true
            }
          }
        }
      } else if(leadSurrogate != 0) {
        // valid bmp char, but last char was a lead
        if (units.decr(3) > -1) {
          bytes.push(0xEF, 0xBF, 0xBD)
          leadSurrogate = 0
        }
      }
      if (!continue) {
        // Encode utf-8
        if (codePoint < 0x80) {
          if (units.decr() < 0) {
            break = true
          } else {
            bytes.push(codePoint)
          }
        } else if (codePoint < 0x800) {
          if (units.decr(2) < 0) {
            break = true
          } else {
            bytes.push(
              codePoint >> 0x6 | 0xC0,
              codePoint & 0x3F | 0x80)
          }
        } else if (codePoint < 0x10000) {
          if (units.decr(3) < 0) {
            break = true
          } else {
            bytes.push(
              codePoint >> 0xC | 0xE0,
              codePoint >> 0x6 & 0x3F | 0x80,
              codePoint & 0x3F | 0x80
            )
          }
        } else if (codePoint < 0x200000) {
          if (units.decr(4) < 0) {
            break = true
          } else {
            bytes.push(
              codePoint >> 0x12 | 0xF0,
              codePoint >> 0xC & 0x3F | 0x80,
              codePoint >> 0x6 & 0x3F | 0x80,
              codePoint & 0x3F | 0x80
            )
          }
        } else {
          throw new Exception("Invalid code point")
        }
      }
    }
    new Uint8Array(bytes)
  }

}

// Utility to ease some code in utf8ToBytes
private class UnitsCounter(var units: Int) {
  /**
    * Decrements units and return the new units
    * @param num
    * @return
    */
  def decr(num: Int = 1): Int = {
    units -= num
    units
  }
}
