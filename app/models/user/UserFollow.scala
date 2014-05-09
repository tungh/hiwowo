package models.user



import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import models.Page
import models.Page._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:59
 * ***********************
 * description:用于类的说明     用户关注的人
 */

case class UserFollow(
                       id: Option[Long],
                       uid: Long,
                       followId: Long,
                       addTime: Option[Timestamp]
                       )
class UserFollows(tag:Tag) extends Table[UserFollow](tag,"user_follow") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def followId = column[Long]("follow_id")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,followId,addTime.?)<> (UserFollow.tupled, UserFollow.unapply )

}




