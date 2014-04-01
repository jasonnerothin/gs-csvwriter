package com.gigaspaces.csvwriter

import java.io.File
import org.apache.commons.cli.{CommandLine, GnuParser, Options}
import scala.Predef.String
import scala.Predef.require
import org.openspaces.core.space.UrlSpaceConfigurer
import org.openspaces.core.{GigaSpace, GigaSpaceConfigurer}

class CommandLineProcessing(args: Array[String]) {

  private val inputFileOption = "in"
  private val inputFileDescription = "csv file"
  private val spaceUrlOption = "url"
  private val spaceUrlDescription = "Space URL (see http://wiki.gigaspaces.com/wiki/display/XAP9/Space+URL for details)"
  private val documentDataTypeName = "dt"
  private val documentDataTypeDescription = "Type of data stored in document"

  private val options: Options = new Options()
    .addOption(inputFileOption, true, inputFileDescription)
    .addOption(spaceUrlOption, true, spaceUrlDescription)
    .addOption(documentDataTypeName, true, documentDataTypeDescription)

  /** @return parse the command line, and return a java.io.File according to the args
    */
  def inputFile(): File = {
    val p = inputFilePath
    val f = new File(p)
    require(!f.isDirectory, String.format("Input %s must not be a directory, but [%s] is one.", inputFileDescription, p))
    require(f.isFile, String.format("Input %s must exist, but [%s] does not.", inputFileDescription, p))
    f
  }

  private def inputFilePath: String = stringValFromOpt(inputFileOption)

  private def commandLine(): CommandLine = {
    require(args != null, options.toString)
    require(args.length > 1, options.toString)
    val parser = new GnuParser
    parser.parse(options, args)
  }

  private def spaceUrlString: String = stringValFromOpt(spaceUrlOption)

  private def stringValFromOpt(opt: String) = commandLine().getOptionValue(opt)

  /** @return a GigaSpace, properly construed
    */
  def gigaSpace(): GigaSpace = {

    val url = spaceUrlString
    require(url != null, options.toString)
    require(url.length > 0, options.toString)

    new GigaSpaceConfigurer(new UrlSpaceConfigurer(url)).gigaSpace()

  }

  private def documentDataTypeString = stringValFromOpt(documentDataTypeName)

  def documentDataType() : String = {

    val dataType = documentDataTypeString
    require( dataType != null, options.toString )
    require( dataType.length > 0, options.toString )

    dataType
  }

}