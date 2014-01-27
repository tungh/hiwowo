package models.advert

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-27
 * Time: 下午1:28
 * ***********************
 * description:用于类的说明
 */

case class AdvertPosition(
                           id:Option[Int]=None,
                           position:String,
                           name: String,
                           code:String,
                           advertType:Int,
                           addTime:Option[Timestamp]
                           )

class AdvertPositions(tag:Tag) extends Table[AdvertPosition](tag,"advert_position") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def position = column[String]("position")
  def name = column[String]("name")
  def code = column[String]("code")
  def advertType = column[Int]("advert_type")
  def addTime = column[Timestamp]("add_time")
  def * = (id.? , position , name , code , advertType , addTime.?) <>(AdvertPosition.tupled, AdvertPosition.unapply )
}



