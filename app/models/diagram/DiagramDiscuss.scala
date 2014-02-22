package models.diagram
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: 01345096
 * Date: 14-2-21
 * Time: 下午3:27
 * 讨论
 */
case class DiagramDiscuss  (
                             id: Option[Long],
                             uid:Long,
                             diagramId:Long,
                             thirdId:Long,
                             typeId:Int,
                             quoteContent:Option[String],
                             content:String,
                             checkState:Int,
                             addTime:Option[Timestamp]
                             )

class DiagramDiscusses(tag:Tag) extends Table[DiagramDiscuss](tag,"diagram_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def diagramId = column[Long]("uid")
  def thirdId = column[Long]("third_id")
  def typeId = column[Int]("type_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,diagramId,thirdId,typeId,quoteContent.?, content, checkState, addTime.? ) <> (DiagramDiscuss.tupled, DiagramDiscuss.unapply)


}

