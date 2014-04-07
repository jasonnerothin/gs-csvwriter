package com.gigaspaces.csvwriter

import scala.io.{BufferedSource, Source, Codec}
import java.io.{BufferedWriter, FileWriter, File}

class EncodedCopying(processing: CommandLineProcessing, creation: TempFileCreation) {

  implicit val codec = Codec("UTF-8")
  val tempFileNameLength = 12
  val deleteTempFile = false

  def encodedTempFile: File = {

    def errorMsg(action: String, path: String): String = {
      String.format("Need to be able to %s temp file [ %s ].", action, path)
    }

    val file: File = creation.newRandFile(tempFileNameLength, deleteLater = deleteTempFile)

    val path = file.getAbsolutePath
    require(file.canWrite, errorMsg("read from", path))
    require(file.canRead, errorMsg("write to", path))

    // copy
    val writer = new BufferedWriter(new FileWriter(file, true))
    Source.fromFile(processing.inputFile())(codec).foreach { line => writer.write(line) }
    writer.flush()
    writer.close()

    file
  }

}