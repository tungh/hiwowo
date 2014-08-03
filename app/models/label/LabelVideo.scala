package models.label
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
/**
  * Created with IntelliJ IDEA.
  * User: zuosanshao
  * Date: 14-8-2
  * Time: 上午1:02
  */
case class LabelVideo (
                      id: Option[Long],
                      labelId: Long,
                      videoId: Long,
                      checkState:Int,
                      addTime:Option[Timestamp]
                      )
class LabelVideoTable(tag:Tag) extends Table[LabelVideo](tag,"label_video") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def labelId = column[Long]("label_id")
  def videoId = column[Long]("video_id")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,labelId,videoId,checkState,addTime.?) <> (LabelVideo.tupled, LabelVideo.unapply)
}
