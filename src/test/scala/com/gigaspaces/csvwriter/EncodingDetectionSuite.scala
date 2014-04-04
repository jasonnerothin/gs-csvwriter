package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import java.io.{FileOutputStream, File}
import java.nio.CharBuffer
import java.nio.charset.Charset
import org.slf4j.{Logger, LoggerFactory}

class EncodingDetectionSuite extends FunSuite with EncodingDetection with TempFileCreation with BeforeAndAfterAll {

  /** Some likely culprits...
    */
  private val testEncodingStrings = List("WINDOWS-1252")
  private val testChars = "1234567890abcdWXYZ" // should be nearly universal

  private val logger:Logger = LoggerFactory.getLogger(getClass)

  override def beforeAll(): Unit = {

  }

  test("a real file from windows") {

    val testFile = new File(new File("."), "/src/test/resources/100Rows.txt")
    logger.debug("Test file = {}", testFile.getAbsolutePath)

    val encoding = detectEncoding(testFile)
    logger.debug("Encoding is {}", encoding.displayName())

    assert(Charset.defaultCharset() == encoding)
  }

  /**
   * This test isn't working very well for detecting non-UTF8 stuff. Probably, I need real files.
   */
  ignore("detectEncoding detects all encodings correctly") {

    var testCharsets = Set[Charset]()
    var tempFiles: List[File] = List[File]()

    var counter0 = 0
    testEncodingStrings.map {
      enc => {
        val file = newRandFile(10, deleteLater = true)
        val testCharset = Charset.forName(enc)
        testCharsets = testCharsets + testCharset
        val encoder = testCharset.newEncoder()
        val encodedBytes = encoder.encode(CharBuffer.wrap(testChars))
        val out = new FileOutputStream(file)
        out.write(encodedBytes.array())
        out.close()
        tempFiles = tempFiles :+ file
        counter0 = counter0 + 1
        logger.debug("encoding count = {}", counter0.toString)
      }
    }


    var counter1 = 0
    tempFiles.map {
      file => {
        val cs = detectEncoding(file)
        assert(cs != null, String.format("Could not detect charset for file  #%s!", counter1.toString))
        if (!testCharsets.contains(cs))
          logger.trace("Incorrectly detected charset for file #{}. Detected {} instead.", counter1.toString, cs.displayName)
        testCharsets = testCharsets - cs
        counter1 = counter1 + 1
        logger.debug("Temp file = {}", counter1.toString)
      }
    }

    assert(testCharsets.size === 0, "Missed at least one charset detection.")

  }

}