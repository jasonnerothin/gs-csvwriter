package com.gigaspaces.csvwriter

import org.scalatest.FunSuite
import scala.util.Random

class RandomStringsSuite extends FunSuite with RandomStrings {

  val rand = new Random(System.currentTimeMillis())

  test("randomChar produces chars") {

    for (i <- 0 to 26 * 5) {
      val ch = randChar()
      assert(isAlpha(ch), String.format("Produced non alpha char [%s]", ch))
    }

  }

  test("randString gen's strings of the correct length") {

    val len = rand.nextInt(10) + 1

    assert(randString(len).length === len)

  }

  private def isAlpha(ch: Char): Boolean = {
    Character.isAlphabetic(ch.toInt)
  }

  test("randString is composed of only chars, too") {

    for (i <- 0 to 10) {
      val str = randString(rand.nextInt(5) + 1)
      for( ch <- str )
        assert(isAlpha(ch), String.format("There are confusing monsters on Sesame Street [%s]! The alphabet no longer rules!!!", ch.toString))
    }

  }

}