package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import java.io.{FileOutputStream, OutputStreamWriter, FileWriter, BufferedWriter}
import scala.util.Random
import scala.io.Source

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 4/7/14
 * Time: 5:18 PM
 */
class EncodedCopyingSuite extends FunSuite with BeforeAndAfterEach with TempFileCreation with RandomStrings with MockitoSugar {

  var copying: EncodedCopying = null

  val creation = mock[TempFileCreation]
  val processing = mock[CommandLineProcessing]

  var testInput = newRandFile(12, deleteLater = true)
  var anotherFile = newRandFile(12, deleteLater = true)

  var windowsEncoding = "windows-1252"
  var magicString = randString(8)
  var windowsChar = 0x081.toChar
  // character that exists in cp1252, but not utf8
  var strings: Array[String] = Array(randString(12), randString(7), magicString + windowsChar, randString(4))

  override def beforeEach() {

    when(processing.inputFile()).thenReturn(testInput)
    when(creation.newRandFile(anyObject(), anyObject())).thenReturn(anotherFile)

    val writer = new OutputStreamWriter(new FileOutputStream(testInput), windowsEncoding)
    writer.write(strings(0))
    writer.write(strings(1))
    writer.write(strings(2))
    writer.write(strings(3))
    writer.close()

    copying = new EncodedCopying(processing, creation)

  }

  test("not the same file") {

    val result = copying.encodedTempFile()

    assert(result != testInput)
    assert(result.getAbsolutePath != testInput.getAbsolutePath)

  }


  test("file is utf8 encoded") {

    val lines = Source.fromFile(copying.encodedTempFile(), windowsEncoding)

    val e = lines.exists { ch =>
      ch == windowsChar
    }

  }

}
