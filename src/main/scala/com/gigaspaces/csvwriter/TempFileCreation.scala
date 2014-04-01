package com.gigaspaces.csvwriter

import java.io.File

/**
  * A trait for generating files in the temp directory, sometimes temporary in nature.
  */
trait TempFileCreation extends RandomStrings {

  /** @return a convenience feature, which makes it a lot easier to looking at temp files during unit testing
    */
  protected def useSlashTmpInstead = false

  /**
    * @return location of the temp directory
    */
  def tempDir(): File = {
    if (!useSlashTmpInstead) return new File(System.getProperty("java.io.tmpdir"))
    new File("/tmp")
  }

  /**
    * Creates a new (empty) file in the temp directory. Optionally, it's temporary.
    * @param fileNameLen length of the (random) file name to use
    * @param deleteLater by default, random files in the temp directory are ''not'' deleted
    * @return a reference to the file.
    */
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