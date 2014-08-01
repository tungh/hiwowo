package models.diagram
import play.api.db.slick.Config.driver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: 01345096
 * Date: 14-2-21
 * Time: 下午3:27
 * 图说
 * typeId 0:图文  1 图说
 * status 0 草稿 1 发布 2 审核成功 3 审核失败
 */
case class Diagram (
                   id: Option[Long],
                   uid:Long,
                   typeId:Int,
                   catalogId:Int,
                   title: String,
                   pic: String,
                   intro: Option[String],
                   content: Option[String],
                   labels:Option[String],
                   status:Int,
                   viewNum:Int,
                   loveNum:Int,
                   hateNum:Int,
                   discussNum:Int,
                   collectNum:Int,
                   comeFromSite:Option[String],
                   comeFromUrl:Option[String],
                   modifyTime:Option[Timestamp],
                   addTime:Option[Timestamp]
                   )

class Diagrams(tag:Tag) extends Table[Diagram](tag,"diagram") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def typeId = column[Int]("type_id")
  def catalogId = column[Int]("catalog_id")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def intro  = column[String]("intro")
  def content  = column[String]("content")
  def labels  = column[String]("labels")
  def status = column[Int]("status")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def hateNum = column[Int]("hate_num")
  def discussNum = column[Int]("discuss_num")
  def collectNum = column[Int]("collect_num")
  def comeFromSite = column[String]("come_from_site")
  def comeFromUrl = column[String]("come_from_url")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,typeId,catalogId,title,pic,intro.?,content.?,labels.?,status,viewNum,loveNum,hateNum,discussNum,collectNum,comeFromSite.?,comeFromUrl.?,modifyTime.?,addTime.?) <> (Diagram.tupled, Diagram.unapply)


}
