package com.gigaspaces.csvwriter

import scala.util.Random

trait RandomStrings {

  private val rand = new Random(System.currentTimeMillis())

  def randChar(): Character = {
    (rand.nextInt(26) + 65).toChar
  }

  def randString(len: Int): String = {
    val builder = new StringBuilder
    for (i <- 1 to len ) builder.append(randChar())
    builder.toString()
  }

}