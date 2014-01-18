package models.user


import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午2:36
 * ***********************
 * description:用于类的说明  用户喜欢的小站
 */

case class UserLoveSite (
                          id: Option[Long],
                          uid:Long,
                          siteId:Long,
                          addTime:Option[Timestamp]
                          )
object UserLoveSites extends Table[UserLoveSite]("user_love_site") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def siteId = column[Long]("siteId")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ siteId  ~ addTime.?  <>(UserLoveSite, UserLoveSite.unapply _)

  def autoInc = uid  ~ siteId   returning id


}




