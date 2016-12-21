package ui.shader.builder

import ui.shader.builder.types.GlType
import ui.shader.builder.value.GlValue

import scala.collection.mutable.ListBuffer

class GlModule[T <: GlType](val result: GlValue[T],
                            val commands: ListBuffer[GlCommand],
                            val functions: ListBuffer[GlFunction[GlType]]) {

}
