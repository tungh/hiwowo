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
                          blogId:Long,
                          quoteContent:Option[String],
                          content:String,
                          status:Int,
                          addTime:Option[Timestamp]
                          )


class SiteBlogDiscusses(tag:Tag) extends Table[SiteBlogDiscuss](tag,"site_blog_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def blogId  =column[Long]("blog_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * = (id.?,uid,blogId,quoteContent.?,content,checkState,addTime.?) <> (SiteBlogDiscuss.tupled,SiteBlogDiscuss.unapply)


}