package models.msg

import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:16
 * ***********************
 * description:用于类的说明
 */

case class AtMsg (
            id: Option[Long],
            senderId: Long,
            senderName:String,
            content: String,
            receiverId:Long,
            receiverName:String,
            status:Int,
            addTime:Option[Timestamp]
            )

class AtMsgTable(tag:Tag) extends Table[AtMsg](tag,"at_msg") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def senderId =column[Long]("sender_id")
  def senderName =column[String]("sender_name")
  def content = column[String]("content")
  def receiverId =column[Long]("receiver_id")
  def receiverName =column[String]("receiver_name")
  def status = column[Int]("status")
  def addTime=column[Timestamp]("add_time")
  def * = (id.?,senderId,senderName,content,receiverId,receiverName,status,addTime.?) <> (AtMsg.tupled, AtMsg.unapply)
}




