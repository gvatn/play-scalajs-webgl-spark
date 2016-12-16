package ui.program

object DataType {
  sealed trait GlDataType
  case object GlFloat extends GlDataType
  case object GlInt extends GlDataType
}
