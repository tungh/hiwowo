package models.site

import scala.slick.driver.MySQLDriver.simple._
import java.sql.Timestamp


/*
*
*
* */

case class SiteDiscuss (
                          id: Option[Long],
                          uid:Long,
                          siteId:Long,
                          quoteContent:Option[String],
                          content:String,
                          status:Int,
                          addTime:Option[Timestamp]
                          )

class SiteDiscusses(tag:Tag) extends  Table[SiteDiscuss](tag,"site_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def siteId  =column[Long]("site_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,siteId,quoteContent.?,content,checkState,addTime.?) <> (SiteDiscuss.tupled, SiteDiscuss.unapply )


}
