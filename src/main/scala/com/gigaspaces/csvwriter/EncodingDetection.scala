package com.gigaspaces.csvwriter

import java.io.{FileInputStream, File}
import java.nio.charset.{UnsupportedCharsetException, Charset}
import org.slf4j.{Logger, LoggerFactory}

trait EncodingDetection {

  private val logger:Logger = LoggerFactory.getLogger(getClass)
  private val bufferSize = 4096

  /** Detects a file's encoding using a universal detector.
    * See [[https://code.google.com/p/juniversalchardet/ this doc]] for details.
    *
    * The returned name is converted to a Charset so that we have a
    * typesafe result.
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
    val detector = new org.mozilla.universalchardet.UniversalDetector(null)

    var numBytesRead = 1
    while (numBytesRead > 0 && !detector.isDone) {
      numBytesRead = input.read(buffer)
      if( numBytesRead > 0 ) {
        detector.handleData(buffer, 0, numBytesRead)
        logger.trace(numBytesRead + " bytes read.")
      }
    }
    input.close()

    var cs = Charset.defaultCharset()
    if( detector.isDone ) cs = asCharset(detector.getDetectedCharset, in)

    logger.debug("Detected charset {}.", cs.displayName)

    cs

  }

  private def asCharset(enc: String, in: File): Charset = {
    if( enc == null ){
      logger.trace("*> Null encoding was passed to me.")
      return Charset.defaultCharset()
    }
    logger.trace("*> Encoding string of [{}] was passed to me.", enc)
    try {
      Charset.forName(enc)
    } catch {
      case u: UnsupportedCharsetException => throw new IllegalStateException(
        String.format("Provided csv file [{}] uses an unsupported character set [{}] and cannot be processed.", in.getAbsolutePath, enc)
      )
    }
  }

}