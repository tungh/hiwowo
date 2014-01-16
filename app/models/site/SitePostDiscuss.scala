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
                          pid:Long,
                          cid:Int,
                          quoteContent:Option[String],
                          content:String,
                          status:Int,
                          addTime:Option[Timestamp]
                          )

object SitePostDiscusses extends Table[SiteBlogDiscuss]("site_blog_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def pid = column[Long]("pid")
  def cid =column[Int]("cid")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def status =column[Int]("status")
  def addTime=column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ pid ~ cid  ~ quoteContent.?  ~ content ~ status ~ addTime.?  <>(SiteBlogDiscuss, SiteBlogDiscuss.unapply _)
  def autoInc  = id.? ~ uid  ~ pid ~ cid  ~ quoteContent.?  ~ content ~ status ~ addTime.?  <>(SiteBlogDiscuss, SiteBlogDiscuss.unapply _) returning id
  def autoInc2 = uid  ~ pid ~ cid  ~ quoteContent.?  ~ content  returning id


}
