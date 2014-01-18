package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-17
 * Time: 下午9:15
 */
case class SitePicDiscuss (
                             id: Option[Long],
                             uid:Long,
                             pid:Long,
                             quoteContent:Option[String],
                             content:String,
                             status:Int,
                             addTime:Option[Timestamp]
                             )

