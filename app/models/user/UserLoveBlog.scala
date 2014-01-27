package models.user

import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
import models.Page
import models.Page._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午2:36
 * ***********************
 * description:用于类的说明  用户喜欢的日志
 */

case class UserLoveBlog (
                           id: Option[Long],
                           uid:Long,
                           blogId:Long,
                           addTime:Option[Timestamp]
                           )
class UserLoveBlogs(tag:Tag) extends Table[UserLoveBlog](tag,"user_love_blog") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def blogId = column[Long]("blogId")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid, blogId, addTime.?)  <>(UserLoveBlog.tupled, UserLoveBlog.unapply)

}




