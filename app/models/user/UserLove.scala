package models.user
import play.api.db.slick.Config.driver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-12
 * Time: 下午8:16
 * typeId 0 diagram  1 topic
 */
case class UserLove (
                          id: Option[Long],
                          uid:Long,
                          loveId:Long,
                          typeId:Int,
                          addTime:Option[Timestamp]
                          )
class UserLoveTable(tag:Tag) extends Table[UserLove](tag,"user_love") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def loveId = column[Long]("love_id")
  def typeId = column[Int]("type_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid, loveId,typeId,addTime.?)  <>(UserLove.tupled, UserLove.unapply)

}
