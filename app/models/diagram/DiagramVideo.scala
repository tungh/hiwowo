package models.diagram

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-8-1
 * Time: 下午9:04
 */
case class DiagramVideo(
                         id: Option[Long],
                         uid:Long,
                         diagramId:Long,
                         url:String,
                         title:String,
                         intro: Option[String],
                         sortNum:Int,
                         addTime:Option[Timestamp]
                         )
class DiagramVideoTable(tag:Tag) extends Table[DiagramVideo](tag,"diagram_video") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def diagramId = column[Long]("diagram_id")
  def url = column[String]("url")
  def title  = column[String]("title")
  def intro  = column[String]("intro")
  def sortNum = column[Int]("sort_num")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,uid,diagramId,url,title,intro.?,sortNum,addTime.?) <> (DiagramVideo.tupled, DiagramVideo.unapply)

}

