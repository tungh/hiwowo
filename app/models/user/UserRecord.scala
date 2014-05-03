package models.user

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import models.Page
import models.Page._
import play.api.cache.Cache
import play.api.Play.current

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-9下午7:19
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/


case class UserRecord (
id: Option[Long],
uid: Long,
actionName:String,
actionId:Long,
actionUrl:String,
actionContent:String,
addTime: Option[Timestamp]
)

class UserRecords(tag:Tag) extends Table[UserRecord](tag,"user_record") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def actionName = column[String]("action_name")
  def actionId = column[Long]("action_id")
  def actionUrl = column[String]("action_url")
  def actionContent = column[String]("action_content")
  def addTime = column[Timestamp]("add_time")
  def * = (id.?,uid,actionName,actionId,actionUrl,actionContent,addTime.?) <> (UserRecord.tupled, UserRecord.unapply )

}


