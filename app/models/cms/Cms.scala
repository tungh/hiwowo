package models.cms

import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-4
 * Time: 下午5:36
 */
case class Cms (
                 id: Option[Long],
                 aid:Long,
                 title: String,
                 content:String,
                 viewNum:Int,
                 status:Int,
                 seoTitle:Option[String],
                 seoKeywords:Option[String],
                 seoDesc:Option[String],
                 modifyTime:Option[Timestamp],
                 addTime:Option[Timestamp]
                 )

class CmsTable(tag:Tag) extends Table[Cms](tag,"cms") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def aid = column[Long]("aid")
  def title = column[String]("title")
  def content = column[String]("content")
  def viewNum = column[Int]("view_num")
  def status = column[Int]("status")
  def seoTitle = column[String]("seo_title")
  def seoKeywords = column[String]("seo_keywords")
  def seoDesc = column[String]("seo_desc")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,aid,title,content,viewNum,status,seoTitle.?,seoKeywords.?,seoDesc.?,modifyTime.?,addTime.?) <> (Cms.tupled, Cms.unapply)


}

