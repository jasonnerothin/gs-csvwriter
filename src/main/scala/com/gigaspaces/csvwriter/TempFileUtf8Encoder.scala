package com.gigaspaces.csvwriter

import java.io._
import java.nio.charset.Charset

class TempFileUtf8Encoder(clp: CommandLineProcessing, encodingDetection: EncodingDetection, tempFileCreation: TempFileCreation) {

  private val utf8Charset = Charset.forName("UTF-8")
  private val bufferSize = 4096
  private val filenameLen = 12

  /** Generates a temp file containing the contents of the [[com.gigaspaces.csvwriter.CommandLineProcessing]]
    * input file, encoded in UTF-8
    * @return a reference to the file in temp
    */
  def encodeToTemp(deleteTempFile: Boolean = true): File = {

    val src = clp.inputFile()
    val inputCharset = encodingDetection.detectEncoding(src)
    val reader = new InputStreamReader(new FileInputStream(src), inputCharset)

    val inputBuffer = new Array[Char](bufferSize)

    val dest = tempFileCreation.newRandFile(filenameLen, deleteLater = deleteTempFile)
    val writer = new OutputStreamWriter(new FileOutputStream(dest), utf8Charset)

    while (reader.ready()) {
      val numBytesRead = reader.read(inputBuffer)
      writer.write(inputBuffer, 0, numBytesRead)
    }

    reader.close()
    writer.close()

    dest

  }


}