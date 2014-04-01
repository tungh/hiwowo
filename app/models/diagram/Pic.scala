package models.diagram
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
 /*
 * status 0 表示未使用  1 表示使用
 * */
case class Pic (
                        id: Option[Long],
                        uid:Long,
                        url:String,
                        intro: Option[String],
                        status:Int,
                        addTime:Option[Timestamp]
                        )

class Pics(tag:Tag) extends Table[Pic](tag,"pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def url = column[String]("url")
  def intro  = column[String]("intro")
  def status = column[Int]("status")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,uid,url,intro.?,status,addTime.?) <> (Pic.tupled, Pic.unapply)


}
