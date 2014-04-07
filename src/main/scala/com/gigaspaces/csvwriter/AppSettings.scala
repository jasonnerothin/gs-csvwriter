package com.gigaspaces.csvwriter

trait AppSettings{

  def batchSize : Int = 50

  def threadPoolSize : Int = 1

}