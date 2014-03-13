package models.diagram
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp

case class Pic (
                        id: Option[Long],
                        uid:Long,
                        src:String,
                        intro: Option[String],
                        sortNum:Int,
                        addTime:Option[Timestamp]
                        )

class Pics(tag:Tag) extends Table[Pic](tag,"pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def src = column[String]("pic")
  def intro  = column[String]("intro")
  def sortNum = column[Int]("sort_num")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,uid,src,intro.?,sortNum,addTime.?) <> (Pic.tupled, Pic.unapply)


}
