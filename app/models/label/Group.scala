package models.label

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-17
 * Time: 下午11:09
 */

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

case class Group (
                   id: Option[Long],
                   name: String,
                   intro:Option[String],
                   isHot:Int,
                   status:Int,
                   addTime:Option[Timestamp]
                   )

class Groups(tag:Tag) extends Table[Group](tag,"group") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def intro = column[String]("intro")
  def isHot = column[Int]("is_hot")
  def status = column[Int]("status")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,name,intro.?,isHot,status,addTime.?) <> (Group.tupled, Group.unapply)


}

