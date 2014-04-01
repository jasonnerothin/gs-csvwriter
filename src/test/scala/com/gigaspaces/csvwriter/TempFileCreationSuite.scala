package com.gigaspaces.csvwriter

import org.scalatest.FunSuite
import scala.util.Random
import java.io.File

class TempFileCreationSuite extends FunSuite with TempFileCreation{

  val rand = new Random(System.currentTimeMillis())
  val javaIoTmpDir = new File(System.getProperty("java.io.tmpdir"))
  val tmpDir = new File("/tmp")

  test("newRandFile has correct filename length"){

    val len = rand.nextInt(4) + 1

    val result = newRandFile(len, deleteLater = true)

    assert(result.getName.length === len )

  }

  private val unexpectedTempDir = "Not pointing at *either* of the supported temp dirs."

  test("newRandFile is in the temp directory"){

    val jitd = javaIoTmpDir
    val td = tmpDir

    val result = newRandFile(rand.nextInt(4)+1, deleteLater = true)
    val parent = result.getParentFile

    assert( parent == td || parent == jitd, unexpectedTempDir)

  }


  ignore("newRandFile delete flag is used"){
    // pain to test - maybe with def afterAll() or something, but even then, we have to
    // make sure individual tests are run in parallel
  }

  test("tempDir points to tempDir"){

    val jitd = javaIoTmpDir
    val td = tmpDir

    val result = tempDir()

    assert( result == jitd || result == td, unexpectedTempDir )

  }

}