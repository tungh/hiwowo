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
 * description:用于类的说明  用户喜欢的话题
 */

case class UserLoveTopic (
                           id: Option[Long],
                           uid:Long,
                           topicId:Long,
                           addTime:Option[Timestamp]
                           )
class UserLoveTopics(tag:Tag) extends Table[UserLoveTopic](tag,"user_love_topic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def topicId = column[Long]("topic_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?,uid,topicId,addTime.?)  <> (UserLoveTopic.tupled, UserLoveTopic.unapply)


}




