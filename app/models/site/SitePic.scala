package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._


/* aid  : Album id 小镇相册的ID */

case class SitePic (
                        id: Option[Long],
                        sid:Long,
                        intro:Option[String],
                        tags:Option[String],
                        pic:String,
                        isTop:Int,
                        addTime:Option[Timestamp]
                        )

object SitePics extends Table[SitePic]("site_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def intro = column[String]("intro")
  def tags = column[String]("tags")
  def pic = column[String]("pic")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ sid  ~ intro.? ~ tags.? ~ pic ~ isTop ~ addTime.?  <>(SitePic, SitePic.unapply _)
  def autoInc  = id.? ~ sid ~ intro.? ~ tags.? ~ pic ~ isTop ~ addTime.?  <>(SitePic, SitePic.unapply _) returning id
  def autoInc2 = sid  ~ intro.? ~ tags.? ~ pic ~ isTop returning id
  def autoInc3 = sid ~ pic returning id

}
