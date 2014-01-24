package models.admin


import play.api.libs.Codecs
import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import java.sql.{Timestamp }
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:26
 * ***********************
 * description:用于类的说明
 */

case class Administrator (
                     id:Option[Long]=None,
                     email: String,
                     password: String,
                     name:String,
                     department:String,
                     phone:String,
                     loginTime:Timestamp,
                     loginNum:Int,
                     loginIP: Option[String],
                     lastLoginTime:Timestamp,
                     addTime:Timestamp,
                     roleId:Int,
                     roleName:String
                     )


object Administrators extends Table[Administrator]("administrator") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def email = column[String]("email")
  def password = column[String]("password")
  def name = column[String]("name")
  def department = column[String]("department")
  def phone = column[String]("phone")
  def loginTime = column[Timestamp]("login_time")
  def loginNum=column[Int]("login_num")
  def loginIP = column[String]("login_ip")
  def lastLoginTime = column[Timestamp]("last_login_time")
  def addTime = column[Timestamp]("add_time")
  def roleId=column[Int]("role_id")
  def roleName=column[String]("role_name")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ email ~ password ~ name ~ department ~ phone ~loginTime~ loginNum ~ loginIP.? ~ lastLoginTime ~ addTime ~ roleId ~ roleName <>(Administrator, Administrator.unapply _)


}


