package models.diagram
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: 01345096
 * Date: 14-2-21
 * Time: 下午3:26
 * 专辑
 */
case class Album (
                   id: Option[Long],
                   uid:Long,
                   title: String,
                   pic: String,
                   intro: Option[String],
                   status:Int,
                   diagramNum:Int,
                   modifyTime:Option[Timestamp],
                   addTime:Option[Timestamp]
                   )

class AlbumTable(tag:Tag) extends Table[Album](tag,"album") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def title = column[String]("title")
  def pic = column[String]("pic")
  def intro  = column[String]("intro")
  def status = column[Int]("status")
  def diagramNum = column[Int]("diagram_num")
  def modifyTime = column[Timestamp]("modify_time")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,title,pic,intro.?,status,diagramNum,modifyTime.?,addTime.?) <> (Album.tupled, Album.unapply)


}
