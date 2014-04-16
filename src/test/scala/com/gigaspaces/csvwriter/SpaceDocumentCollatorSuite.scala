package com.gigaspaces.csvwriter

import org.scalatest.mock.MockitoSugar
import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 4/16/14
 * Time: 7:12 AM
 */
class SpaceDocumentCollatorSuite extends FunSuite with MockitoSugar {

  val collator = new Object with SpaceDocumentCollation

  test("collator returns Seqs[SpaceDocument] that are at most batchSize in length") {
    assert(3 + 4 === 8, "write me")
  }

  test("collator returns Seqs[SpaceDocument] that are have n SpaceDocuments when SpaceDocumentReader returns n SpaceDocuments (n < batchSize)") {
    assert(1 + 2 === 4, "feed me")
  }

  test("multiple collators, using one SpaceDocumentReader, produce a given SpaceDocument only once") {
    assert(39 - 28 === 12, "spank me")
  }
}
