package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import java.io._
import org.slf4j.{Logger, LoggerFactory}
import java.nio.charset.Charset

class EncodingDetectionSuite extends FunSuite with EncodingDetection with TempFileCreation with BeforeAndAfterAll {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  val cp1252ToUnicodeByteMap = Map[Char, Char](0x0080.toChar -> 0x20ac.toChar, 0x0082.toChar -> 0x201a.toChar)
  val cp1252BytesWithoutUnicodeAnalogues = List[Char](0x0081.toChar, 0x008d.toChar)

  test("detect windows 1252") {

    val winFile = newRandFile(6, deleteLater = true)

    val writer = new BufferedWriter(new FileWriter(winFile))
    writer.write(cp1252String)
    writer.write("\n")
    writer.close()

    val str = cp1252String

    logger.trace("Special string is [{}].", str)
    val encoding = detectEncoding(winFile)
    logger.debug("Encoding is {}.", encoding.displayName)

    assert("windows-1252" === encoding.displayName)

  }

  /** The special bytes in this string have a code-point in cp 1252 but
    * have no code-point in UTF-8 or UTF-16
    * @return the String containing bytes: ["hello",0x0081,0x008d,"earth"]
    */
  def cp1252String = {

    val hi = "hello"
    val planet = "earth"

    var chars = Array[Char]()
    for (b <- hi) chars = chars :+ b
    chars = chars :+ cp1252BytesWithoutUnicodeAnalogues(0)
    chars = chars :+ cp1252BytesWithoutUnicodeAnalogues(1)
    for (b <- planet) chars = chars :+ b

    // even though we're encoding it in "Unicode", it contains two
    // bytes that are present in cp1252 (Windows-1252) that do
    // *not* have a corresponding code point in UTF-16BE - i.e.
    // this is a "Windows-1252" string

    var bytes = Array[Byte]()
    for (ch <- chars) bytes = bytes ++ asBytes(ch)

    new String(bytes, "UTF-16BE")

  }

  def asBytes(ch: Char): Array[Byte] = {
    Array[Byte]((ch / 256).toByte, ch.toByte)
  }

  test("asBytes works") {
    val ch = 'a'
    val arr = asBytes(ch)

    val b0 = arr(0)
    val b1 = arr(1)

    val ch0 = b0.toChar
    val ch1 = b1.toChar

    assert(ch0 === 0x00)
    assert(ch1 === 'a')
  }

    test("a real file comes back with default encoding") {

      val testFile = new File(new File("."), "/src/test/resources/100Rows.txt")
      logger.debug("Test file = {}", testFile.getAbsolutePath)

      val encoding = detectEncoding(testFile)
      logger.debug("Encoding is {}", encoding.displayName())

      assert(Charset.defaultCharset() == encoding)
    }

}