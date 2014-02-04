package models.pet

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-3
 * Time: 下午8:03
 */
case class Pet(
                id: Option[Long],
                uid:Long,
                title:String,
                pic:String,
                intro: String,
                tags:String,
                status:Int,
                modifyTime:Option[Timestamp],
                addTime:Option[Timestamp]
                )
class Pets(tag:Tag) extends Table[Pet](tag,"pet") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def intro     =column[String]("intro")
  def tags    =column[String]("tags")
  def status = column[Int]("status")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")

  def * =(id.?,uid,title,pic,intro,tags,status,modifyTime.?,addTime.?)<>(Pet.tupled, Pet.unapply)


}
