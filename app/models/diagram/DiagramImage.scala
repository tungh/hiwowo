package models.diagram
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-7-8
 * Time: 下午5:31
 */
case class DiagramImage (
                          id: Option[Long],
                          uid:Long,
                          diagramId:Long,
                          url:String,
                          intro: Option[String],
                          sortNum:Int,
                          addTime:Option[Timestamp]
                          )
class DiagramImages(tag:Tag) extends Table[DiagramImage](tag,"diagram_image") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def diagramId = column[Long]("diagram_id")
  def url = column[String]("url")
  def intro  = column[String]("intro")
  def sortNum = column[Int]("sort_num")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,uid,diagramId,url,intro.?,sortNum,addTime.?) <> (DiagramImage.tupled, DiagramImage.unapply)

}
