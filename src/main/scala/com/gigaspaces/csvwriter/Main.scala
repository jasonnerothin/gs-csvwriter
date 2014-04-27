package com.gigaspaces.csvwriter

import org.slf4j.LoggerFactory
import com.gigaspaces.document.SpaceDocument
import scala.collection.parallel.mutable.ParArray
import scala.concurrent.Future
import akka.actor.ActorDSL._
import akka.actor.Actor
import akka.pattern.ask

object Main {

  private val logger = LoggerFactory.getLogger(getClass)

  val creator = new Object with TempFileCreation
  val settings = new Object with AppSettings
  val moxy = new Object with GigaSpacesMoxy

  def main(args: Array[String]): Unit = {

    // make an encoded temp file copy of the specified input file
    val processing = new CommandLineProcessing(args)

    val inFile = processing.inputFile()
    val reading = new EncodedCopying(processing, creator)
    val tempFile = reading.encodedTempFile()
    val dataType = processing.documentDataType()

    // let user know what we're about to do...
    logger.info("Importing file: {} ...", inFile.getAbsolutePath)
    logger.trace("... as {} .", tempFile.getAbsolutePath)
    logger.info("Generating {} SpaceDocuments...", dataType)

    val reader = new DocumentReader(tempFile, dataType)
    val collector = new SpaceDocumentCollector(reader)

    val spaceDocs = collector.collect(ParArray[SpaceDocument]()).toList
    logger.debug("Created {} SpaceDocuments...", spaceDocs.size)

    val gigaSpace = moxy.gigaSpace()


    var writeCount = 0
    spaceDocs.grouped(settings.batchSize).foreach {
      case batch: Seq[SpaceDocument] =>
        ask(actor, new Future {
          new SpaceDocumentWriter(gigaSpace).write(batch)
          writeCount = writeCount + settings.batchSize
          logger.info("Wrote {} documents...", writeCount)
        }
        )
      case _ =>
    }

  }

  def anActor(): Actor = {
    actor(new Act {
      whenStarting(testActor ! "Starting actor")
      whenStopping(testActor ! "Stopping actor")
    })
  }

}
