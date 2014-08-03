package models.msg

import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
 /*
 * discuss type 0 帖子回复 1 宝贝回复 2 主题回复
 * */
case class DiscussMsg(
                     id: Option[Long],
                     discusserId:Long,
                     discusserName: String,
                     discussType:Int,
                     thirdId:Long,
                     content:String,
                     ownerId:Long,
                     addTime:Option[Timestamp]
                     )

class DiscussMsgTable(tag:Tag) extends Table[DiscussMsg](tag,"discuss_msg") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def discusserId =column[Long]("discusser_id")
  def discusserName =column[String]("discusser_name")
  def discussType = column[Int]("discuss_type")
  def thirdId =column[Long]("third_id")
  def content = column[String]("content")
  def ownerId = column[Long]("owner_id")
  def addTime = column[Timestamp]("add_time")
  def * = (id.?,discusserId,discusserName,discussType,thirdId,content,ownerId,addTime.? )<>(DiscussMsg.tupled, DiscussMsg.unapply )
  
}
