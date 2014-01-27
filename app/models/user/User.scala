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
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._


case class User(
                 id: Option[Long],
                 name: String,
                 password: String,
                 email: Option[String],
                 credits:Int,
                 pic: String,
                 title:Option[String],
                 intro: Option[String],
                 status:Int,
                 comeFrom:Int,
                 openId:Option[String],
                 tags:Option[String],
                 province:Option[String],
                 modifyTime:Option[Timestamp]
                 )


class Users(tag:Tag) extends Table[User](tag,"user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def password = column[String]("password")
  def email = column[String]("email")
  def credits = column[Int]("credits")
  def pic = column[String]("pic")
  def title = column[String]("title")
  def intro     = column[String]("intro")
  def status = column[Int]("status")
  def comeFrom = column[Int]("come_from")
  def openId = column[String]("open_id")
  def tags     = column[String]("tags")
  def province     = column[String]("province")
  def modifyTime = column[Timestamp]("modify_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, name, password, email.?, credits, pic , title.? , intro.? , status, comeFrom, openId.? , tags.?, province.? , modifyTime.?)  <>(User.tupled, User.unapply)
 


}





