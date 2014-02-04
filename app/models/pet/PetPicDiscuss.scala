package models.pet
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-3
 * Time: 下午8:14
 */
case class PetPicDiscuss (
                            id: Option[Long],
                            uid:Long,
                            picId:Long,
                            quoteContent:Option[String],
                            content:String,
                            status:Int,
                            addTime:Option[Timestamp]
                            )

class PetPicDiscusses(tag:Tag)  extends  Table[PetPicDiscuss](tag,"pet_pic_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def picId  =column[Long]("pic_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?  , uid , picId  , quoteContent.? , content , checkState , addTime.?)  <>(PetPicDiscuss.tupled, PetPicDiscuss.unapply)


}
