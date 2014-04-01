package com.gigaspaces.csvwriter

import au.com.bytecode.opencsv.CSVReader
import com.gigaspaces.document.{DocumentProperties, SpaceDocument}
import java.io.{File, FileReader}

class DocumentReader(inputFile: File, documentDataType: String) {

  private val reader = new CSVReader(new FileReader(inputFile))
  private val columnNames: Array[String] = reader.readNext() // readFirst is really what it is...

  /** Takes the next line in the CSV file and turns it into a [SpaceDocument], keys from the
    * column names
    * @return a SpaceDocument, populated with the next row's worth of data
    */
  def nextDocument(): Option[SpaceDocument] = {
    val values: Array[String] = reader.readNext()
    if (values != null) {
      val props = new DocumentProperties()
      for (idx <- 0 to columnNames.length - 1)
        props.put(columnNames(idx), values(idx))
      Some(new SpaceDocument(documentDataType, props))
    }
    else None
  }

}