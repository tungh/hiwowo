package models.user

import play.api.db.slick.Config.driver.simple._

import java.sql.Timestamp
  /*
  * type id :0 图说   1 forum topic
  * */
case class UserCollect (
                    id: Option[Long],
                    uid:Long,
                    typeId:Int,
                    collectId:Long,
                    addTime:Option[Timestamp]
                    )
class UserCollects(tag:Tag) extends Table[UserCollect](tag,"user_collect") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def typeId = column[Int]("type_id")
  def collectId = column[Long]("collect_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid,typeId,collectId,addTime.?)  <>(UserCollect.tupled, UserCollect.unapply)



}
