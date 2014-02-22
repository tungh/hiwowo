package models.user

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:08
 * ***********************
 * description:用户
 */
import  java.sql.Timestamp

import scala.slick.driver.MySQLDriver.simple._


case class User(
                 id: Option[Long],
                 openId:Option[String],
                 comeFrom:Int,
                 name: String,
                 password: String,
                 email: Option[String],
                 credits:Int,
                 pic: String,
                 title:Option[String],
                 intro: Option[String],
                 status:Int,
                 province:Option[String],
                 weixin:Option[String],
                 qrcode:Option[String],
                 tags:Option[String],
                 modifyTime:Option[Timestamp]
                 )


class Users(tag:Tag) extends Table[User](tag,"user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def openId = column[String]("open_id")
  def comeFrom = column[Int]("come_from")
  def name = column[String]("name")
  def password = column[String]("password")
  def email = column[String]("email")
  def credits = column[Int]("credits")
  def pic = column[String]("pic")
  def title = column[String]("title")
  def intro     = column[String]("intro")
  def status = column[Int]("status")
  def province     = column[String]("province")
  def weixin     = column[String]("weixin")
  def qrcode     = column[String]("qrcode")
  def tags     = column[String]("tags")
  def modifyTime = column[Timestamp]("modify_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?,openId.?,comeFrom, name, password, email.?, credits, pic , title.? , intro.? , status,province.?,weixin.?,qrcode.?, tags.?, modifyTime.?)  <>(User.tupled, User.unapply)
 


}





