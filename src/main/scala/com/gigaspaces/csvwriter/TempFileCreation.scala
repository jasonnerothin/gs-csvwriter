package com.gigaspaces.csvwriter

import java.io.File

trait TempFileCreation extends RandomStrings {

  protected def useSlashTmpInstead = true

  def tempDir(): File = {
    if (!useSlashTmpInstead) return new File(System.getProperty("java.io.tmpdir"))
    new File("/tmp")
  }

  def newRandFile(fileNameLen: Int, deleteLater: Boolean = false): File = {
    var aFile = randFile(fileNameLen)
    while (!aFile.createNewFile) aFile = randFile(fileNameLen)
    if (deleteLater) aFile.deleteOnExit()
    aFile
  }

  private def randFile(fileNameLen: Int): File = {
    new File(tempDir(), randString(fileNameLen))
  }

}