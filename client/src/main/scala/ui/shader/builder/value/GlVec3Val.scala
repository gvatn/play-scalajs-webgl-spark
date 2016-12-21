package ui.shader.builder.value

import ui.shader.builder.types.{GlFloatType, GlVec2Type, GlVec3Type}

abstract class GlVec3Val extends GlValue[GlVec3Type] {
  def xy: GlValue[GlVec2Type] = {
    GlVec2Val(this)
  }

  def x: GlValue[GlFloatType] = {
    new GlVec3ValX(this)
  }

  def y: GlValue[GlFloatType] = {
    new GlVec3ValY(this)
  }

  def z: GlValue[GlFloatType] = {
    new GlVec3ValZ(this)
  }
}

object GlVec3Val {
  def apply(x: GlValue[GlFloatType],
            y: GlValue[GlFloatType],
            z: GlValue[GlFloatType]): GlVec3Val = {
    new GlVec3ValF(x, y, z)
  }

  def apply(vec2: GlValue[GlVec2Type],
            z: GlValue[GlFloatType]): GlVec3Val = {
    new GlVec3ValV2F(vec2, z)
  }

  def apply(vec3: GlValue[GlVec3Type]): GlVec3Val = {
    new GlVec3ValV3F(vec3)
  }

  def apply(name: String): GlVec3Val = {
    new GlVec3ValVar(name)
  }
}

class GlVec3ValX(val vec3: GlValue[GlVec3Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec3.toGlsl}.x"
  }
}

class GlVec3ValY(val vec3: GlValue[GlVec3Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec3.toGlsl}.y"
  }
}

class GlVec3ValZ(val vec3: GlValue[GlVec3Type]) extends GlValue[GlFloatType] {
  override def toGlsl: String = {
    s"${vec3.toGlsl}.z"
  }
}

class GlVec3ValVar(val name: String) extends GlVec3Val {

  override def toGlsl: String = {
    name
  }

}

class GlVec3ValF(val xVal: GlValue[GlFloatType],
                val yVal: GlValue[GlFloatType],
                val zVal: GlValue[GlFloatType]) extends GlVec3Val {

  override def toGlsl: String = {
    "vec3(" + xVal.toGlsl + ", " + yVal.toGlsl + ", " + zVal.toGlsl + ")"
  }

}

class GlVec3ValV2F(val vec2: GlValue[GlVec2Type],
                   val zVal: GlValue[GlFloatType]) extends GlVec3Val {

  override def toGlsl: String = {
    "vec3(" + vec2.toGlsl + ", " + zVal.toGlsl + ")"
  }
}

class GlVec3ValV3F(val vec3: GlValue[GlVec3Type]) extends GlVec3Val {

  override def toGlsl: String = {
    "vec3(" + vec3.toGlsl + ")"
  }
}
