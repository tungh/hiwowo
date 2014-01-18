package models.goods



import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import java.sql.{ Timestamp}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:11
 * ***********************
 * description: 宝贝
 */

case class Goods(
                  id: Option[Long],
                  shopId:Long,
                  numIid:Long,
                  name: String,
                  intro: String,
                  content:Option[String],
                  price:String,
                  pic: String,
                  itemPics: String,
                  detailUrl: String,
                  loveNum: Int,
                  status: Int,
                  isMember: Boolean,
                  modifyTime:Option[Timestamp],
                  addTime:Option[Timestamp]
                  )

object Goodses extends Table[Goods]("goods") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def shopId = column[Long]("shop_id")
  def numIid = column[Long]("num_iid")
  def name = column[String]("name")
  def intro = column[String]("intro")
  def content = column[String]("content")
  def price =column[String]("price")
  def pic = column[String]("pic")
  def itemPics = column[String]("item_pics")
  def detailUrl = column[String]("detail_url")
  def loveNum = column[Int]("love_num")
  def status = column[Int]("status")
  def isMember = column[Boolean]("is_member")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ shopId ~ numIid  ~ name ~ intro ~ content.? ~ price ~ pic ~ itemPics ~ detailUrl ~ loveNum  ~ status ~ isMember ~ modifyTime.? ~ addTime.? <>(Goods, Goods.unapply _)
  def autoInc =  shopId ~ numIid  ~ name ~ intro ~ price ~ pic ~ itemPics   returning id


}
