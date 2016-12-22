package ui.program

object DataType {
  sealed trait GlDataType
  case object GlFloat extends GlDataType
  case object GlInt extends GlDataType
  case object GlVec3 extends GlDataType
  case object GlVec2 extends GlDataType
}
