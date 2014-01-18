package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/*
*
*
* */

case class SiteBlogDiscuss (
                          id: Option[Long],
                          uid:Long,
                          bid:Long,
                          quoteContent:Option[String],
                          content:String,
                          status:Int,
                          addTime:Option[Timestamp]
                          )


