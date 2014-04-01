package com.gigaspaces.csvwriter

import java.io.{FileInputStream, File}
import java.nio.charset.{UnsupportedCharsetException, Charset}

trait EncodingDetection {

  private val bufferSize = 4096
  private val detector = new org.mozilla.universalchardet.UniversalDetector(null)

  /** Detects a file's encoding using a universal detector. See [[https://code.google.com/p/juniversalchardet/ this doc]] for details.
    * @param in a non-zero-length non-directory
    * @return the encoding String
    */
  def detectEncoding(in: File): Charset = {

    require(in != null, "Can't detect encoding on a null file handle!")
    val path = in.getAbsolutePath
    require(in.isFile, String.format("Can't detect encoding on a non-file reference [%s].", path))
    require(in.getTotalSpace > 0, String.format("Can't detect encoding on a file of size 0 [%s].", path))

    val input = new FileInputStream(in)
    val buffer = new Array[Byte](bufferSize)

    while (!detector.isDone) {
      val numBytesRead = input.read(buffer)
      detector.handleData(buffer, 0, numBytesRead)
    }

    input.close()

    asCharset(detector.getDetectedCharset, in)

  }

  private def asCharset(enc: String, in: File): Charset = {
    try {
      Charset.forName(enc)
    } catch {
      case u: UnsupportedCharsetException => throw new IllegalStateException(
        String.format("Provided csv file [%s] uses an unsupported character set [%s] and cannot be processed.", in.getAbsolutePath, enc)
      )
    }
  }

}