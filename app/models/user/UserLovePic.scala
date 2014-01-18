package models.user

import scala.slick.driver.MySQLDriver.simple._
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-16
 * Time: 下午11:18
 * description : 用户喜欢的图片
 */
case class UserLovePic (
                          id: Option[Long],
                          uid:Long,
                          picId:Long,
                          addTime:Option[Timestamp]
                          )
object UserLovePics extends Table[UserLoveBlog]("user_love_blog") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def picId = column[Long]("blogId")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = id.? ~ uid  ~ picId  ~ addTime.?  <>(UserLoveBlog, UserLoveBlog.unapply _)

  def autoInc = uid  ~ picId  returning id


}