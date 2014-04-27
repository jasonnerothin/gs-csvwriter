package com.gigaspaces.csvwriter

import org.openspaces.core.{GigaSpaceConfigurer, GigaSpace}
import org.openspaces.core.space.UrlSpaceConfigurer

/**
 * Created by IntelliJ IDEA.
 * User: jason
 * Date: 4/17/14
 * Time: 6:38 AM
 */
trait GigaSpacesMoxy {

  implicit val appSettings : AppSettings

  /**
    * @return a newly minted [[GigaSpace]] proxy
    */
  def gigaSpace(): GigaSpace = {
    new GigaSpaceConfigurer(new UrlSpaceConfigurer(appSettings.jiniUrl)).gigaSpace()
  }

}
