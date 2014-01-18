package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

case class SiteVideo (
                        id: Option[Long],
                        sid:Long,
                        url:String,
                        intro:Option[String],
                        tags:Option[String],
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )


