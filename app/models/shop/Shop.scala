package models.shop



import java.sql.{Timestamp }
import scala.slick.driver.MySQLDriver.simple._



/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:11
 * ***********************
 * description: 所在地区域
 */

case class Shop(
                  id: Option[Long],
                  uid:Long,
                  cid:Int,
                  name: String,
                  intro:String,
                  isVisible:Int,
                  pic:String,
                  tags:Option[String],
                  loveNum:Int,
                  discussNum:Int,
                  goodsNum:Int,
                  province: Option[String],
                  city: Option[String],
                  town: Option[String],
                  street: Option[String],
                  modifyTime:Option[Timestamp],
                  addTime:Option[Timestamp]
                  )


object Shops extends Table[Shop]("shop") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def cid = column[Int]("cid")
  def name = column[String]("name")
  def intro = column[String]("intro")
  def isVisible = column[Int]("is_visible")
  def pic =   column[String]("pic")
  def tags = column[String]("tags")
  def loveNum = column[Int]("love_num")
  def discussNum = column[Int]("discuss_num")
  def goodsNum = column[Int]("goods_num")
  def province = column[String]("province")
  def city = column[String]("city")
  def town = column[String]("town")
  def street = column[String]("street")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ uid  ~ cid  ~ name ~ intro  ~ isVisible  ~ pic ~ tags.? ~ loveNum ~ discussNum ~ goodsNum ~ province.? ~ city.? ~ town.? ~ street.?  ~ modifyTime.? ~ addTime.? <>(Shop, Shop.unapply _)
  def autoInc = id.? ~ uid  ~ cid  ~ name ~ intro  ~ isVisible  ~ pic ~ tags.? ~ loveNum ~ discussNum ~ goodsNum ~ province.? ~ city.? ~ town.? ~ street.?  ~ modifyTime.? ~ addTime.? <>(Shop, Shop.unapply _) returning id

  def autoInc2 = name ~ uid  ~ addTime  returning id
  def autoInc3 = name ~ intro.? ~ uid ~ cid ~ tags.? ~ addTime returning id



}


