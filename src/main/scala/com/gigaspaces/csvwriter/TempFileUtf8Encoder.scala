package com.gigaspaces.csvwriter

import java.io._
import java.nio.charset.Charset

class TempFileUtf8Encoder(clp: CommandLineProcessing) extends EncodingDetection with TempFileCreation {

  private val filenameLen = 12
  private val utf8Charset = Charset.forName("UTF-8")
  private val bufferSize = 4096

  /** Generates a temp file containing the contents of the [[com.gigaspaces.csvwriter.CommandLineProcessing]]
    * input file, encoded in UTF-8
    * @return a reference to the file in temp
    */
  def encodeToTemp(): File = {

    val src = clp.inputFile()
    val inputCharset = detectEncoding(src)
    val reader = new InputStreamReader(new FileInputStream(src), inputCharset)

    val inputBuffer = new Array[Char](bufferSize)

    val dest = newRandFile(filenameLen, deleteLater = true)
    val writer = new OutputStreamWriter(new FileOutputStream(dest), utf8Charset)

    while(reader.ready()) {
      val numBytesRead = reader.read(inputBuffer)
      writer.write(inputBuffer, 0, numBytesRead)
    }

    reader.close()
    writer.close()

    dest

  }


}