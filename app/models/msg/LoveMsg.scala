package models.msg

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
 /*
 * favorType 0 baobei 1 theme 2 post
 * */
case class LoveMsg(
                     id: Option[Long],
                     loverId:Long,
                     loverName: String,
                     favorType:Int,
                     thirdId:Long,
                     content:String,
                      lovedId:Long,
                     addTime:Option[Timestamp]
                     )
class LoveMsgs(tag:Tag) extends Table[LoveMsg](tag,"love_msg") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def loverId =column[Long]("lover_id")
  def loverName =column[String]("lover_name")
  def favorType = column[Int]("favor_type")
  def thirdId =column[Long]("third_id")
  def content = column[String]("content")
  def lovedId = column[Long]("loved_id")
  def addTime = column[Timestamp]("add_time")
  def * = (id.?,loverId,loverName,favorType,thirdId,content,lovedId,addTime.?) <> (LoveMsg.tupled, LoveMsg.unapply)

}
