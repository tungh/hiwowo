package models.site

import java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-17
 * Time: 下午9:15
 */
case class SitePicDiscuss (
                             id: Option[Long],
                             uid:Long,
                             picId:Long,
                             quoteContent:Option[String],
                             content:String,
                             status:Int,
                             addTime:Option[Timestamp]
                             )

object SitePicDiscusses  extends  Table[SitePicDiscuss]("site_pic_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def picId  =column[Long]("pic_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =id.?  ~ uid ~ picId  ~ quoteContent.? ~ content ~ checkState ~ addTime.?   <>(SitePicDiscuss, SitePicDiscuss.unapply _)
  def autoInc =id.?  ~ uid ~ picId   ~ quoteContent.?  ~ content ~ checkState ~ addTime.?   <>(SitePicDiscuss, SitePicDiscuss.unapply _) returning id

  def autoInc2 =uid ~ picId  ~ quoteContent.?  ~ content ~ checkState returning id

}