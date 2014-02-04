package models.pet
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-3
 * Time: 下午8:18
 */
case class PetVideoDiscuss (
                              id: Option[Long],
                              uid:Long,
                              vid:Long,
                              quoteContent:Option[String],
                              content:String,
                              status:Int,
                              addTime:Option[Timestamp]
                              )


class PetVideoDiscusses(tag:Tag) extends  Table[PetVideoDiscuss](tag,"pet_video_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def videoId  =column[Long]("video_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,videoId,quoteContent.?,content,checkState,addTime.?) <> (PetVideoDiscuss.tupled, PetVideoDiscuss.unapply)

}
