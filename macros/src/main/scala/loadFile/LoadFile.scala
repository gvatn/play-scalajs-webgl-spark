package loadFile

import java.io.{BufferedInputStream, FileInputStream}
import java.nio.file.{Files, Paths}

import language.experimental.macros
import scala.reflect.macros.blackbox.Context
import scala.io.Source
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.IOUtils

object LoadFile {
  def syncLoad(fileName: String): String = macro syncLoadImpl

  def syncLoadImpl(c: Context)(fileName: c.Expr[String]): c.Expr[String] = {
    import c.universe._
    val Literal(Constant(fileNameStr)) = fileName.tree
    val fileContents = Source.fromFile("./server/public/" + fileNameStr).getLines.mkString("\n")
    c.Expr[String](Literal(Constant(fileContents)))
  }

  def toBase64(fileName: String): String = macro toBase64Impl

  def toBase64Impl(c: Context)(fileName: c.Expr[String]): c.Expr[String] = {
    import c.universe._
    val Literal(Constant(fileNameStr)) = fileName.tree
    return c.Expr[String](Literal(Constant(new String(""))))
    //val bis = new BufferedInputStream(new FileInputStream("./server/public/" + fileNameStr))
    // Get byte array for base64 encoding
    //val fileBytes = Stream.continually(bis.read).takeWhile(-1 != _).map(_.toByte).toArray
    //val fileBytes = IOUtils.toByteArray(bis)
    val fileBytes = Files.readAllBytes(Paths.get("./server/public/" + fileNameStr))
    c.Expr[String](Literal(Constant(new String(Base64.encodeBase64(fileBytes)))))
  }
}
