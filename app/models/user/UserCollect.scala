package models.user

import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp

case class UserCollect (
                    id: Option[Long],
                    uid:Long,
                    loveId:Long,
                    typeId:Long,
                    addTime:Option[Timestamp]
                    )
class UserCollects(tag:Tag) extends Table[UserCollect](tag,"user_collect") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def loveId = column[Long]("collectId")
  def typeId = column[Long]("typeId")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid, loveId,typeId,addTime.?)  <>(UserCollect.tupled, UserCollect.unapply)



}
