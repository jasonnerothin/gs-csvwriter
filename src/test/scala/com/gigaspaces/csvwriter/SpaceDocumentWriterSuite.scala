package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.openspaces.core.GigaSpace
import com.gigaspaces.document.SpaceDocument
import scala.util.Random
import org.mockito.Mockito._
import org.mockito.Matchers._

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 4/11/14
 * Time: 8:23 PM
 */
class SpaceDocumentWriterSuite extends FunSuite with MockitoSugar with BeforeAndAfterEach {

  val rand = new Random(System.currentTimeMillis())
  val gigaSpace = mock[GigaSpace]

  var writer: SpaceDocumentWriter = null
  var docs: Seq[SpaceDocument] = null

  override def beforeEach() {

    var list = List[SpaceDocument](mock[SpaceDocument])
    for(x <- 0 to rand.nextInt(5)){
      val mockDoc = mock[SpaceDocument]
      list = list :+ mockDoc
    }
    docs = list

    when(gigaSpace.write(isA(classOf[SpaceDocument]))).thenReturn(null)

    writer = new SpaceDocumentWriter(gigaSpace)

  }

  test("all docs are written") {

    writer.write(docs)

    docs.foreach {
      doc => verify(gigaSpace).write(doc)
    }

  }


}
