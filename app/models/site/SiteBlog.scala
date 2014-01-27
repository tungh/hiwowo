package models.site

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/*
* status 0 草稿  1 发布 2 优质
* */
case  class  SiteBlog(
                  id: Option[Long],
                  uid:Long,
                  sid:Long,
                  comeFrom:Option[String],
                  title:String,
                  pic:Option[String],
                  content: String,
                  tags:Option[String],
                  status:Int,
                  isTop:Int,
                  viewNum:Int,
                  loveNum:Int,
                  replyNum:Int,
                  addTime:Option[Timestamp]
                  )

class SiteBlogs(tag:Tag) extends Table[SiteBlog](tag,"site_blog") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def sid = column[Long]("sid")
  def comeFrom = column[String]("come_from")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def content  = column[String]("content")
  def tags    = column[String]("tags")
  def status = column[Int]("status")
  def isTop = column[Int]("is_top")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def replyNum = column[Int]("reply_num")
  def addTime = column[Timestamp]("add_time")

  // Every table needs a * projection with the same type as the table's type parameter
  def * =(id.?,uid,sid,comeFrom.?,title,pic.?,content,tags.?,status,isTop,viewNum,loveNum,replyNum, addTime.?) <>(SiteBlog.tupled, SiteBlog.unapply)

}