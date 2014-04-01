package com.gigaspaces.csvwriter

import scala.util.Random

/**
  * A mixin for generating random strings.
  */
trait RandomStrings {

  private val rand = new Random(System.currentTimeMillis())

  /**
    * @return an alphabetic character
    */
  def randChar(): Character = {
    (rand.nextInt(26) + 65).toChar
  }

  /**
    * @param len length of returned string
    * @return a random string, length len, comprised of alphabetic characters
    */
  def randString(len: Int): String = {
    val builder = new StringBuilder
    for (i <- 1 to len ) builder.append(randChar())
    builder.toString()
  }

}