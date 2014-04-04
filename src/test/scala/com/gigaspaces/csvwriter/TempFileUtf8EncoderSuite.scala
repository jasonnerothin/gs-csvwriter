package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.mock.MockitoSugar
import java.nio.charset.Charset
import java.io.File

class TempFileUtf8EncoderSuite extends FunSuite with TempFileCreation with MockitoSugar with BeforeAndAfterAll {

  val mockCLP = mock[CommandLineProcessing]
  val mockEncodingDetection = mock[EncodingDetection]
  val mockCharset = mock[Charset]

  var tempFile:File = null

  var testInstance: TempFileUtf8Encoder = null

  override def beforeAll(): Unit = {

    tempFile = newRandFile(6, deleteLater = true)

//    doReturn(tempFile).when(mockCLP.inputFile())
//    doReturn(mockCharset).when(mockEncodingDetection.detectEncoding(tempFile))

  }

  ignore("encoding detection") {

    val result = testInstance.encodeToTemp(deleteTempFile =true)

  }

}