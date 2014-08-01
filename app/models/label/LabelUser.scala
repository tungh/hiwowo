package models.label
import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
/**
  * Created with IntelliJ IDEA.
  * User: zuosanshao
  * Date: 14-8-2
  * Time: 上午1:02
  */
case class LabelUser (
                      id: Option[Long],
                      labelId: Long,
                      userId: Long,
                      checkState:Int,
                      addTime:Option[Timestamp]
                      )
class LabelUsers(tag:Tag) extends Table[LabelUser](tag,"label_user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def labelId = column[Long]("label_id")
  def userId = column[Long]("user_id")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,labelId,userId,checkState,addTime.?) <> (LabelUser.tupled, LabelUser.unapply)
}
