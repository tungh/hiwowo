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
                  cid:Int,
                  title:String,
                  pic:Option[String],
                  content: String,
                  tags:Option[String],
                  status:Int,
                  isTop:Int,
                  viewNum:Int,
                  loveNum:Int,
                  replyNum:Int,
                  extraAttr1:Option[String],
                  extraAttr2:Option[String],
                  extraAttr3:Option[String],
                  extraAttr4:Option[String],
                  extraAttr5:Option[String],
                  extraAttr6:Option[String],
                  addTime:Option[Timestamp]
                  )

object SiteBlogs extends Table[SiteBlog]("site_blog") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def sid = column[Long]("sid")
  def cid = column[Int]("cid")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def content     =column[String]("content")
  def tags    =column[String]("tags")
  def status = column[Int]("status")
  def isTop = column[Int]("is_top")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def replyNum = column[Int]("reply_num")
  def extraAttr1 = column[String]("extra_attr1")
  def extraAttr2 = column[String]("extra_attr2")
  def extraAttr3 = column[String]("extra_attr3")
  def extraAttr4 = column[String]("extra_attr4")
  def extraAttr5 = column[String]("extra_attr5")
  def extraAttr6 = column[String]("extra_attr6")
  def addTime=column[Timestamp]("add_time")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid ~ sid ~ cid ~ title ~ pic.? ~ content ~ tags.? ~ status ~ isTop ~ viewNum ~ loveNum ~ replyNum ~ extraAttr1.? ~ extraAttr2.? ~ extraAttr3.? ~ extraAttr4.? ~ extraAttr5.? ~ extraAttr6.?  ~addTime.? <>(SiteBlog, SiteBlog.unapply _)
  def autoInc = id.? ~ uid ~ sid ~ cid ~ title ~ pic.? ~ content ~ tags.? ~ status ~ isTop ~ viewNum ~ loveNum ~ replyNum ~ extraAttr1.? ~ extraAttr2.? ~ extraAttr3.? ~ extraAttr4.? ~ extraAttr5.? ~ extraAttr6.?  ~addTime.? <>(SiteBlog, SiteBlog.unapply _) returning id
  def autoInc2 = uid ~ sid ~ cid ~ title ~ pic.? ~ content ~ tags.? ~ status ~ extraAttr1.? ~ extraAttr2.? ~ extraAttr3.? ~ extraAttr4.? ~ extraAttr5.? ~ extraAttr6.?  returning id

}