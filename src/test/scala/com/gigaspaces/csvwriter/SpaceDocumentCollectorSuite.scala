package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import com.gigaspaces.document.SpaceDocument
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar

class SpaceDocumentCollectorSuite extends FunSuite with BeforeAndAfterEach with MockitoSugar{

  val reader = mock[DocumentReader]

  override def beforeEach(): Unit = {

  }

  test("mockito works like i expect it to") {

    val mockDoc = mock[SpaceDocument]
    val lameoType = "hiya coww-boyyyy"
    when(mockDoc.getTypeName).thenReturn(lameoType)
    when(reader.nextDocument())
      .thenReturn(Some(mockDoc))
      .thenReturn(Some(mockDoc))
      .thenReturn(None)

    for( i <- 0 to 1 ) {
      val doc = reader nextDocument()
      println(s"i: $i")
      assert(doc.get.getTypeName === lameoType)
    }

    assert(reader.nextDocument() === None)

  }

}