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

class SiteVideos(tag:Tag) extends Table[SiteVideo](tag,"site_video") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def sid = column[Long]("sid")
  def url = column[String]("url")
  def intro = column[String]("intro")
  def tags = column[String]("tags")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.? , sid , url , intro.? , tags.? , isTop , addTime.?)  <>(SiteVideo.tupled, SiteVideo.unapply)




}
