package com.gigaspaces.csvwriter

import org.slf4j.LoggerFactory
import com.gigaspaces.document.SpaceDocument
import scala.annotation.tailrec

object Main {

  private val logger = LoggerFactory.getLogger(getClass)

  val creator = new Object with TempFileCreation
  val settings = new Object with AppSettings

  def main(args: Array[String]): Unit = {

    // make an encoded temp file copy of the specified input file
    val processing = new CommandLineProcessing(args)
    val inFile = processing.inputFile()
    val reading = new EncodedCopying(processing, creator)
    val tempFile = reading.encodedTempFile

    // let user know what we're about to do...
    logger.info("Importing file: {} ...", inFile.getAbsolutePath)
    logger.trace("... as {} .", tempFile.getAbsolutePath)
    val dataType = processing.documentDataType()
    logger.info("Generating {} SpaceDocuments...", dataType)

    val reader = new DocumentReader(tempFile, dataType)

    var spaceDocs = List[SpaceDocument]()
    for( i <- 0 to settings.batchSize){
    }

    processing.inputFile()
  }


  def dispatch(spaceDocs: List[SpaceDocument]): Unit = {

  }

}
