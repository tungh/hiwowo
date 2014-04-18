package models.user

import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-18
 * Time: 下午11:11
 * 用户订阅的内容  标签订阅
 */
case class UserSubscribe(
                          id: Option[Long],
                          uid:Long,
                          labelId:Long,
                          addTime:Option[Timestamp]
                          )
class UserSubscribes(tag:Tag) extends Table[UserSubscribe](tag,"user_subscribe") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def labelId = column[Long]("label_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid, labelId,addTime.?)  <>(UserSubscribe.tupled, UserSubscribe.unapply)

}
