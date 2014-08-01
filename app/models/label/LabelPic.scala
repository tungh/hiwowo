package models.label

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-8-2
 * Time: 上午1:02
 */
case class LabelPic (
                          id: Option[Long],
                          labelId: Long,
                          picId: Long,
                          checkState:Int,
                          addTime:Option[Timestamp]
                          )
class LabelPics(tag:Tag) extends Table[LabelPic](tag,"label_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def labelId = column[Long]("label_id")
  def picId = column[Long]("pic_id")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,labelId,picId,checkState,addTime.?) <> (LabelPic.tupled, LabelPic.unapply)
}
