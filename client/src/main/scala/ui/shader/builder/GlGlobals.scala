package ui.shader.builder

import ui.shader.builder.types.{GlVec3Type, GlVec4Type}
import ui.shader.builder.value.GlVec4Val

object GlGlobals {
  trait GlslGlobal
  object Position extends GlVar("gl_Position", GlVec4Type()) with GlslGlobal
  object Color extends GlVar("gl_FragColor", GlVec4Type()) with GlslGlobal
}
