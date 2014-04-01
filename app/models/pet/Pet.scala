package models.pet

import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-1
 * Time: 下午11:11
 */
case class Pet (
                 id: Option[Long],
                 uid:Long,
                 url:String,
                 intro: Option[String],
                 status:Int,
                 typeId:Int,
                 tags:Option[String],
                 viewNum:Int,
                 loveNum:Int,
                 discussNum:Int,
                 collectNum:Int,
                 comeFromSite:Option[String],
                 comeFromUrl:Option[String],
                 modifyTime:Option[Timestamp],
                 addTime:Option[Timestamp]
                 )

class Pets(tag:Tag) extends Table[Pet](tag,"pet") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def url = column[String]("url")
  def intro  = column[String]("intro")
  def status = column[Int]("status")
  def typeId = column[Int]("type_id")
  def tags  = column[String]("tags")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def discussNum = column[Int]("discuss_num")
  def collectNum = column[Int]("collect_num")
  def comeFromSite = column[String]("come_from_site")
  def comeFromUrl = column[String]("come_from_url")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,uid,url,intro.?,status,typeId,tags.?,viewNum,loveNum,discussNum,collectNum,comeFromSite.?,comeFromUrl.?,modifyTime.?,addTime.?) <> (Pet.tupled, Pet.unapply)


}

