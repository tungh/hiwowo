package models.diagram
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: 01345096
 * Date: 14-2-21
 * Time: 下午3:27
 * 图说
 * ps 后记 postscript
 * typeId 0 图片 1 gif
 * status 0 草稿 1 发布 2 已审核 3 精华
 */
case class Diagram (
                   id: Option[Long],
                   uid:Long,
                   typeId:Int,
                   title: String,
                   pic: String,
                   intro: Option[String],
                   content: Option[String],
                   ps:Option[String],
                   tags:Option[String],
                   status:Int,
                   viewNum:Int,
                   loveNum:Int,
                   discussNum:Int,
                   comeFromSite:Option[String],
                   comeFromUrl:Option[String],
                   modifyTime:Option[Timestamp],
                   addTime:Option[Timestamp]
                   )

class Diagrams(tag:Tag) extends Table[Diagram](tag,"diagram") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def typeId = column[Int]("type_id")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def intro  = column[String]("intro")
  def content  = column[String]("content")
  def ps  = column[String]("ps")
  def tags  = column[String]("tags")
  def status = column[Int]("status")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def discussNum = column[Int]("discuss_num")
  def comeFromSite = column[String]("come_from_site")
  def comeFromUrl = column[String]("come_from_url")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,typeId,title,pic,intro.?,content.?,ps.?,tags.?,status,viewNum,loveNum,discussNum,comeFromSite.?,comeFromUrl.?,modifyTime.?,addTime.?) <> (Diagram.tupled, Diagram.unapply)


}
