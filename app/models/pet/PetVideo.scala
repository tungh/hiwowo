package models.pet
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-3
 * Time: 下午8:08
 */
case class PetVideo (
                       id: Option[Long],
                       petId:Long,
                       url:String,
                       intro:Option[String],
                       tags:Option[String],
                       isTop:Int,
                       addTime:Option[Timestamp]
                       )

class PetVideos(tag:Tag) extends Table[PetVideo](tag,"pet_video") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def petId = column[Long]("pet_id")
  def url = column[String]("url")
  def intro = column[String]("intro")
  def tags = column[String]("tags")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.? , petId , url , intro.? , tags.? , isTop , addTime.?)  <>(PetVideo.tupled, PetVideo.unapply)




}
