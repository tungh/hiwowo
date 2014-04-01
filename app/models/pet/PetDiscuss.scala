package models.pet

import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: 01345096
 * Date: 14-2-21
 * Time: 下午3:27
 * 讨论
 */
case class PetDiscuss  (
                             id: Option[Long],
                             uid:Long,
                             petId:Long,
                             quoteContent:Option[String],
                             content:String,
                             checkState:Int,
                             addTime:Option[Timestamp]
                             )

class PetDiscusses(tag:Tag) extends Table[PetDiscuss](tag,"pet_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def petId = column[Long]("pet_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,petId,quoteContent.?, content, checkState, addTime.? ) <> (PetDiscuss.tupled,PetDiscuss.unapply)


}

