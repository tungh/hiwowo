package models.tag



import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import models.goods.{GoodsAssesses, Goodses, Goods}
import models.user.{Users,User}
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description:标签
 */

case class Tag(
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


object Tags extends Table[Tag]("tag") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def  addNum = column[Int]("add_num")
  def isTop = column[Boolean]("is_top")
  def isHighlight = column[Boolean]("is_highlight")
  def sortNum  = column[Int]("sort_num")
  def checkState = column[Int]("check_state")
  def seoTitle = column[String]("seo_title")
  def seoKeywords = column[String]("seo_keywords")
  def seoDesc = column[String]("seo_desc")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ name ~ addNum ~ isTop ~ isHighlight ~ sortNum ~ checkState  ~ seoTitle.? ~ seoKeywords.? ~seoDesc.?  ~ modifyTime.? ~addTime.? <>(Tag, Tag.unapply _)
  def autoInc = id.? ~ name ~ addNum ~ isTop ~ isHighlight ~ sortNum ~ checkState  ~ seoTitle.? ~ seoKeywords.? ~seoDesc.?  ~ modifyTime.? ~addTime.? <>(Tag, Tag.unapply _) returning id





}


