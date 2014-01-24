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


object Users extends Table[User]("user") {
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
  def * = id.? ~ name ~ password ~ email.? ~ credits ~ pic  ~ title.?  ~ intro.?  ~ status ~ comeFrom ~ openId.?  ~ tags.? ~ province.?  ~ modifyTime.?  <>(User, User.unapply _)
  def autoInc = id.? ~ name ~ password ~ email.? ~ credits ~ pic  ~ title.?  ~ intro.?  ~ status ~ comeFrom ~ openId.?  ~ tags.? ~ province.?  ~ modifyTime.? <>(User, User.unapply _) returning id
  def autoInc2 = name ~ password ~ email returning id
  def autoInc3 = name ~ comeFrom ~ openId ~ pic returning id
  /* count  */
  def count()(implicit session: Session):Int = {
    Query(Users.length).first()
  }
  /* count  */
  def count(comeFrom:Int)(implicit session: Session):Int = {
    Query(Users.filter(_.comeFrom === comeFrom).length).first()
  }
  /* 验证用户登陆email 和 密码是否正确 */
  def authenticate(email: String, password: String)(implicit session: Session): Option[User] = {
    (for(u<-Users if u.email === email && u.password===Codecs.sha1("hiwowo"+password))yield(u) ).firstOption
  }
  /* 根据用户id 查找 */
  def findById(uid:Long)(implicit session: Session):Option[User] = {
    Query(Users).filter(_.id === uid).firstOption
  }
  /* 根据用户email 查找 */
  def findByEmail(email:String)(implicit session: Session):Option[User] = {
    Query(Users).filter(_.email === email).firstOption
  }
  /*验证第三方登陆用户*/
  def findSnsUser(comeFrom:Int,openId:String)(implicit session: Session): Option[User] = {
    (for(u<-Users if u.comeFrom === comeFrom && u.openId=== openId )yield(u) ).firstOption
  }



}





