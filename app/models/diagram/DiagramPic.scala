package models.diagram
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-7-8
 * Time: 下午5:31
 */
case class DiagramPic (
                          id: Option[Long],
                          uid:Long,
                          diagramId:Long,
                          url:String,
                          width:Int,
                          height:Int,
                          rawUrl:String,
                          rawWidth:Int,
                          rawHeight:Int,
                          intro: Option[String],
                          sortNum:Int,
                          addTime:Option[Timestamp]
                          )
class DiagramPicTable(tag:Tag) extends Table[DiagramPic](tag,"diagram_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def diagramId = column[Long]("diagram_id")
  def url = column[String]("url")
  def width = column[Int]("width")
  def height = column[Int]("height")
  def rawUrl = column[String]("raw_url")
  def rawWidth = column[Int]("raw_width")
  def rawHeight = column[Int]("raw_height")
  def intro  = column[String]("intro")
  def sortNum = column[Int]("sort_num")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,uid,diagramId,url,width,height,rawUrl,width,height,intro.?,sortNum,addTime.?) <> (DiagramPic.tupled, DiagramPic.unapply)

}
