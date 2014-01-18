package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._


/* aid  : Album id 小镇相册的ID */

case class SitePic (
                        id: Option[Long],
                        sid:Long,
                        bid: Option[Long],
                        intro:Option[String],
                        tags:Option[String],
                        pic:String,
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )


