package models.user


import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午2:36
 * ***********************
 * description:用于类的说明  用户喜欢的话题
 */

case class UserLoveVideo (
                          id: Option[Long],
                          uid:Long,
                          videoId:Long,
                          addTime:Option[Timestamp]
                          )
object UserLoveVideos extends Table[UserLoveBlog]("user_love_video") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def videoId = column[Long]("videoId")
  def addTime = column[Timestamp]("add_time")

  def * = id.? ~ uid  ~ videoId  ~ addTime.?  <>(UserLoveBlog, UserLoveBlog.unapply _)

  def autoInc = uid  ~ videoId   returning id


}




