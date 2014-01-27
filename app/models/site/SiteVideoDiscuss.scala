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


class SiteVideoDiscusses(tag:Tag) extends  Table[SiteVideoDiscuss](tag,"site_video_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def videoId  =column[Long]("video_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,videoId,quoteContent.?,content,checkState,addTime.?) <> (SiteVideoDiscuss.tupled, SiteVideoDiscuss.unapply)

}