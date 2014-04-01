package com.gigaspaces.csvwriter

import au.com.bytecode.opencsv.CSVReader
import com.gigaspaces.document.SpaceDocument
import java.io.FileReader
import scala.collection.convert.Wrappers.{MutableMapWrapper}

class DocumentReader(processing: CommandLineProcessing) {

  private val reader = new CSVReader(new FileReader(processing.inputFile()))
  private val columnNames: Array[String] = reader.readNext() // readFirst is really what it is...

  /** Takes the next line in the CSV file and turns it into a [SpaceDocument], keys from the
    * column names
    * @return a SpaceDocument, populated with the next row's worth of data
    */
  def next(): Option[SpaceDocument] = {
    val map = scala.collection.mutable.Map[String, AnyRef]()
    val values: Array[String] = reader.readNext()
    if (values != null) {
      for (idx <- 0 to columnNames.length) {
        map + (columnNames(idx) -> values(idx))
      }
      Some(new SpaceDocument(processing.documentDataType(), new MutableMapWrapper[String, AnyRef](map)))
    }
    None
  }

}