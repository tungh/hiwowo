package models.pet

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-3
 * Time: 下午8:08
 */
case  class PetPic (
                     id: Option[Long],
                     petId:Long,
                     intro:Option[String],
                     tags:Option[String],
                     pic:String,
                     isTop:Int,
                     addTime:Option[Timestamp]
                     )
class PetPics(tag:Tag) extends Table[PetPic](tag,"pet_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def petId = column[Long]("pet_id")
  def intro = column[String]("intro")
  def tags = column[String]("tags")
  def pic = column[String]("pic")
  def isTop = column[Int]("is_top")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,petId,intro.?,tags.?,pic,isTop,addTime.?) <> (PetPic.tupled, PetPic.unapply)


}

