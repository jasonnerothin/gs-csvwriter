package com.gigaspaces.csvwriter

import org.openspaces.core.GigaSpace

trait AppSettings{

  def batchSize : Int = 50

  def threadPoolSize : Int = 1

  def jiniUrl : String = "jini://*/csvWriterSpace"

}

object AppSettings extends AppSettings {}