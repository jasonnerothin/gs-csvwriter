package com.gigaspaces.csvwriter

import org.springframework.transaction.annotation.{Propagation, Transactional}
import com.gigaspaces.document.SpaceDocument
import org.openspaces.core.GigaSpace

import com.gigaspaces.csvwriter.AppSettings._

/**
  * Created by IntelliJ IDEA.
  * User: jason
  * Date: 4/11/14
  * Time: 8:00 PM
  */
class SpaceDocumentWriter(gigaSpace: GigaSpace){

  @Transactional(propagation = Propagation.REQUIRED)
  def write(spaceDocs: Seq[SpaceDocument]): Unit = {
    val docsSize = spaceDocs.size
    require(docsSize <= batchSize, s"Batch size too large for space docs: $docsSize")
    spaceDocs.foreach{doc => gigaSpace.write(doc)}
  }

}
