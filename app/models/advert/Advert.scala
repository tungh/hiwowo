package models.advert


import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-27
 * Time: 上午11:49
 * ***********************
 * description:用于类的说明
 */

case class Advert(
                   id:Option[Long],
                   code:String,
                   typeId:Int,
                   title:String,
                   link:String,
                   pic:Option[String],
                   width:Int,
                   height:Int,
                   startTime:Timestamp,
                   endTime:Timestamp,
                   note:Option[String],
                   sortNum:Int,
                   clickNum:Int,
                   addTime:Option[Timestamp]
                   )
class Adverts(tag: Tag) extends Table[Advert](tag,"advert") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def code = column[String]("code")
  def typeId = column[Int]("type_id")
  def title = column[String]("title")
  def link = column[String]("link")
  def pic = column[String]("pic")
  def width = column[Int]("width")
  def height = column[Int]("height")
  def startTime = column[Timestamp]("start_time")
  def endTime = column[Timestamp]("end_time")
  def note=column[String]("note")
  def sortNum=column[Int]("sort_num")
  def clickNum=column[Int]("click_num")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = ( id.?,code,typeId,title,link,pic.?,width,height,startTime,endTime,note.?,clickNum,sortNum,addTime.? ) <> (Advert.tupled, Advert.unapply)
}

