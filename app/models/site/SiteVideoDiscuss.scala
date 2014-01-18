package models.site

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-17
 * Time: 下午9:17
 */
import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

case class SiteVideoDiscuss (
                            id: Option[Long],
                            uid:Long,
                            vid:Long,
                            quoteContent:Option[String],
                            content:String,
                            status:Int,
                            addTime:Option[Timestamp]
                            )


