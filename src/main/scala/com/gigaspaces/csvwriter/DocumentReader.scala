package com.gigaspaces.csvwriter

import au.com.bytecode.opencsv.CSVReader
import com.gigaspaces.document.{DocumentProperties, SpaceDocument}
import java.io.FileReader
import scala.collection.convert.Wrappers.{MutableMapWrapper}

class DocumentReader(processing: CommandLineProcessing) {

  private val reader = new CSVReader(new FileReader(processing.inputFile()))
  private val columnNames: Array[String] = reader.readNext() // readFirst is really what it is...

  /** Takes the next line in the CSV file and turns it into a [SpaceDocument], keys from the
    * column names
    * @return a SpaceDocument, populated with the next row's worth of data
    */
  def nextDocument(): Option[SpaceDocument] = {
    val map = scala.collection.mutable.Map[String, AnyRef]()
    val values: Array[String] = reader.readNext()
    if (values != null) {
      val props = new DocumentProperties()
      for (idx <- 0 to columnNames.length - 1)
        props.put(columnNames(idx), values(idx))
      Some(new SpaceDocument(processing.documentDataType(), props))
    }
    else None
  }

}