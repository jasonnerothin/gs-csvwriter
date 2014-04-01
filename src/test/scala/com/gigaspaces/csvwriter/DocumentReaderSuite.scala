package com.gigaspaces.csvwriter

import org.scalatest.{BeforeAndAfterAll, FunSuite}
import java.io.{FileWriter, File}

class DocumentReaderSuite extends FunSuite with BeforeAndAfterAll {

  val typeName = "ZipperData"
  val testFile: File = new File(new File(System.getProperty("java.io.tmpdir")), "Foo.csv")
  //  var testFile: File = new File(new File("/tmp"), "Foo.csv")
  val clp = new CommandLineProcessing(Array[String]("-in", testFile.getAbsolutePath, "-url", "/./mySpace", "-dt", typeName))

  private def testInstance(): DocumentReader = new DocumentReader(clp)

  override def beforeAll(): Unit = {

    if (testFile.exists()) require(testFile.delete())
    require(testFile.createNewFile())

    val writer = new FileWriter(testFile)
    writer.write("x,y,z,p,d,q\n")
    writer.write("1,2,3,45,67,89\n")
    writer.write("10,98,76,5,4,3\n")
    writer.close()

  }

  override def afterAll(): Unit = {
    testFile.deleteOnExit()
  }

  test("nextDocument") {

    val inst = testInstance()

    val one = inst.nextDocument()
    val doc1 = one.get
    assert(typeName === doc1.getTypeName )

    val props1 = doc1.getProperties
    assert("1" === props1.get("x"))
    assert("2" === props1.get("y"))
    assert("3" === props1.get("z"))
    assert("45" === props1.get("p"))
    assert("67" === props1.get("d"))
    assert("89" === props1.get("q"))

    val two = inst.nextDocument()
    val doc2 = two.get
    assert(typeName === doc2.getTypeName )
    val props2 = doc2.getProperties
    assert("10" === props2.get("x"))
    assert("98" === props2.get("y"))
    assert("76" === props2.get("z"))
    assert("5" === props2.get("p"))
    assert("4" === props2.get("d"))
    assert("3" === props2.get("q"))

    val three = inst.nextDocument()
    assert(three === None)

  }

}