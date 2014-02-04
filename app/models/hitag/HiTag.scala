package models.hitag




import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description:标签
 */

case class HiTag(
                id: Option[Long],
                name: String,
                addNum:Int,
                isTop:Boolean,
                isHighlight:Boolean,
                sortNum:Int,
                checkState:Int,
                seoTitle:Option[String],
                seoKeywords:Option[String],
                seoDesc:Option[String],
                modifyTime:Option[Timestamp],
                addTime:Option[Timestamp]
                )

class HiTags(tag:Tag) extends Table[HiTag](tag,"hi-tag") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def addNum = column[Int]("add_num")
  def isTop = column[Boolean]("is_top")
  def isHighlight = column[Boolean]("is_highlight")
  def sortNum  = column[Int]("sort_num")
  def checkState = column[Int]("check_state")
  def seoTitle = column[String]("seo_title")
  def seoKeywords = column[String]("seo_keywords")
  def seoDesc = column[String]("seo_desc")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,name,addNum,isTop,isHighlight,sortNum,checkState,seoTitle.?,seoKeywords.?,seoDesc.?,modifyTime.?,addTime.?) <> (HiTag.tupled, HiTag.unapply)


}


