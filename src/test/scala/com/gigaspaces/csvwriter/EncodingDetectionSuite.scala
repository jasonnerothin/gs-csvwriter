package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import java.io.File
import java.nio.charset.Charset
import org.slf4j.{Logger, LoggerFactory}

class EncodingDetectionSuite extends FunSuite with EncodingDetection with TempFileCreation with BeforeAndAfterAll {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  ignore("detect windows 1252") {

    val win1252bytes = 0x80
    val utf8bytes = 0x80

    val testFile = newRandFile(10, deleteLater = true)

  }

  test("a real file comes back with default encoding") {

    val testFile = new File(new File("."), "/src/test/resources/100Rows.txt")
    logger.debug("Test file = {}", testFile.getAbsolutePath)

    val encoding = detectEncoding(testFile)
    logger.debug("Encoding is {}", encoding.displayName())

    assert(Charset.defaultCharset() == encoding)
  }

}