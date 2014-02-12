package models.site


import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description: 小镇
 * status 0 未审核  1 通过审核 2 优质
 * typeID 0 album 1 pet
 */

case class Site(
                id: Option[Long],
                uid:Long,
                typeId:Int,
                title:String,
                pic:String,
                intro: String,
                tags:String,
                status:Int,
                notice:Option[String],
                blogNum:Int,
                picNum:Int,
                videoNum:Int,
                modifyTime:Option[Timestamp],
                addTime:Option[Timestamp]
                )


class Sites(tag:Tag) extends Table[Site](tag,"site") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def typeId = column[Int]("type_id")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def intro     = column[String]("intro")
  def tags    = column[String]("tags")
  def status = column[Int]("status")
  def notice  = column[String]("notice")
  def blogNum  = column[Int]("blog_num")
  def picNum  = column[Int]("pic_num")
  def videoNum  = column[Int]("video_num")
  def modifyTime=column[Timestamp]("modify_time")
  def addTime=column[Timestamp]("add_time")

  def * =(id.?,uid,typeId,title,pic,intro,tags,status,notice.?,blogNum,picNum,videoNum,modifyTime.?,addTime.?)<>(Site.tupled, Site.unapply)




}
