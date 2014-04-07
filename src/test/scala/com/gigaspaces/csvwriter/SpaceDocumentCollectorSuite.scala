package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import com.gigaspaces.document.SpaceDocument
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.slf4j.LoggerFactory
import scala.collection.parallel.mutable.ParArray

class SpaceDocumentCollectorSuite extends FunSuite with BeforeAndAfterEach with MockitoSugar {

  val logger = LoggerFactory.getLogger(getClass)
  val reader = mock[DocumentReader]

  var collector: SpaceDocumentCollector = null

  override def beforeEach(): Unit = {
    collector = new SpaceDocumentCollector(reader)
  }

  test("mockito works like i expect it to") {

    val mockDoc = mock[SpaceDocument]
    val lameoType = "hiya coww-boyyyy"
    when(mockDoc.getTypeName).thenReturn(lameoType)
    when(reader.nextDocument())
      .thenReturn(Some(mockDoc))
      .thenReturn(Some(mockDoc))
      .thenReturn(None)

    for (i <- 0 to 1) {
      val doc = reader nextDocument()
      logger.trace(s"i: $i")
      assert(doc.get.getTypeName === lameoType)
    }

    assert(reader.nextDocument() === None)

  }

  // the JVM stack size limit, by default, is lower than this
  val testRecursionDepth = 1200

  /** If this call is tail-recursive, recursing a large number of times
    * will not exceed some JVM stack-size limit
    *
    * This test does take a little while to run. An alternative configuration would be to drop
    * the test depth down to a smaller number and have the test run with a -Xss=[lower number]
    */
  test("collect is tail recursive") {

    var mockDoc = mock[SpaceDocument]
    var cnt = 0
    when(mockDoc.getTypeName).thenReturn(s"Type $cnt")
    var stubResult = when(reader.nextDocument()).thenReturn(Some(mockDoc))

    for (stackSize <- 0 to testRecursionDepth - 1) {
      mockDoc = mock[SpaceDocument]
      cnt = cnt + 1
      when(mockDoc.getTypeName).thenReturn(s"Type $cnt")
      stubResult = stubResult.thenReturn(Some(mockDoc))
    }
    stubResult.thenReturn(None)

    val result = collector.collect(new ParArray[SpaceDocument](0))

    assert(result.size === testRecursionDepth + 1)

  }

}