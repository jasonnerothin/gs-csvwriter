import com.gigaspaces.csvwriter.{TempFileCreation, CommandLineProcessing}
import java.io.{FileWriter, File}
import org.openspaces.core.space.CannotCreateSpaceException
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import scala.util.Random

class CommandLineProcessingSuite extends FunSuite with TempFileCreation with BeforeAndAfterEach {

  val aDirectoryPath: File = tempDir()

  var notAFile: File = null
  var aFile: File = null

  val rand = new Random(System.currentTimeMillis())

  val mtUrl = ""
  var garbageUrl: String = ""

  def testInstance(in: String, url: String, dt: String) = new CommandLineProcessing(Array("-in", in, "-url", url, "-dt", dt))

  override def beforeEach(): Unit = {
    aFile = newRandFile(8)
    notAFile = newRandFile(12)
    notAFile.delete()
    garbageUrl = String.format("%s", randString(6))
  }

  override def afterEach(): Unit = {
    aFile.deleteOnExit()
    notAFile.deleteOnExit()
    garbageUrl = ""
  }

  private def newRandFile(fileNameLen: Int): File = {
    def randFile = new File(tempDir(), randString(fileNameLen))
    val aFile = randFile
    //    println(String.format("Creating file: %s", aFile.getAbsolutePath))
    aFile.createNewFile
    writeTo(aFile)
    aFile
  }

  private def writeTo(someFile: File) = {
    val writer = new FileWriter(someFile)
    writer.write("foo")
    writer.write("\n")
    writer.close()
  }

  test("inputFile -in aFilePath returns a file reference") {
    val actual = testInstance(in = aFile.getAbsolutePath, url = randString(4), dt = randString(3)).inputFile()
    assert(actual.isFile)
  }

  test("inputFile -in notAFilePath throws") {
    try {
      testInstance(in = notAFile.getAbsolutePath, url = randString(4), dt = randString(3)).inputFile()
    } catch {
      case e: IllegalArgumentException =>
      case e: Throwable => fail("Expected IllegalArgumentException instead.", e)
    }

  }

  test("inputFile -in aDirectoryPath throws") {
    try {
      testInstance(in = aDirectoryPath.getAbsolutePath, url = randString(4), dt = randString(3)).inputFile()
    } catch {
      case e: IllegalArgumentException =>
      case e: Throwable => fail("Expected IllegalArgumentException instead.", e)
    }
  }

  test("gigaSpace -url mtUrl throws") {
    try {
      val actual = testInstance(url = mtUrl, in = randString(4), dt = randString(3))
      actual.gigaSpace()
    } catch {
      case e: IllegalArgumentException =>
      case e: Throwable => fail("Expected IllegalArgumentException instead.", e)
    }
  }

  test("gigaSpace -url garbageUrl throws") {
    try {
      val actual = testInstance(url = randString(4), in = aFile.getAbsolutePath, dt = randString(3))
      actual.gigaSpace()
    } catch {
      case e: CannotCreateSpaceException =>
      case e: Throwable => fail("Expected CannotCreateSpaceException instead.", e)
    }
  }

  test("documentDataType -dt, when empty, throws"){
    try{
      testInstance(url = randString(4), in = aFile.getAbsolutePath, dt = "").documentDataType()
    } catch{
      case e: IllegalArgumentException =>
      case e: Throwable => fail("Expected IllegalArgumentException instead", e)
    }
  }

  test("documentDataType -dt something, returns something"){
    assert( testInstance(url=randString(3), in = aFile.getAbsolutePath, dt = "something").documentDataType() === "something" )
  }

}