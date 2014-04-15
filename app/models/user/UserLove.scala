package models.user
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-12
 * Time: 下午8:16
 * typeId 0 site  1 blog 2 pic 3 video 4 shop 5 topic
 */
case class UserLove (
                          id: Option[Long],
                          uid:Long,
                          loveId:Long,
                          typeId:Long,
                          addTime:Option[Timestamp]
                          )
class UserLoves(tag:Tag) extends Table[UserLove](tag,"user_love") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def loveId = column[Long]("love_id")
  def typeId = column[Long]("type_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid, loveId,typeId,addTime.?)  <>(UserLove.tupled, UserLove.unapply)

}
