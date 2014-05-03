package models.diagram
import play.api.db.slick.Config.driver.simple._

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
                             quoteContent:Option[String],
                             content:String,
                             loveNum:Int,
                             hateNum:Int,
                             reportNum:Int,
                             checkState:Int,
                             addTime:Option[Timestamp]
                             )

class DiagramDiscusses(tag:Tag) extends Table[DiagramDiscuss](tag,"diagram_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def diagramId = column[Long]("diagram_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def loveNum = column[Int]("love_num")
  def hateNum = column[Int]("hate_num")
  def reportNum = column[Int]("report_num")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,diagramId,quoteContent.?, content, loveNum, hateNum, reportNum,checkState, addTime.? ) <> (DiagramDiscuss.tupled,DiagramDiscuss.unapply)


}

