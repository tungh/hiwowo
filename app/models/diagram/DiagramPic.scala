package models.diagram
import scala.slick.driver.MySQLDriver.simple._

import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: 01345096
 * Date: 14-2-21
 * Time: 下午3:27
 * 图片
 */
case class DiagramPic (
                        id: Option[Long],
                        uid:Long,
                        diagramId:Long,
                        pic:String,
                        intro: Option[String],
                        isTop:Int,
                        sortNum:Int,
                        addTime:Option[Timestamp]
                        )

class DiagramPics(tag:Tag) extends Table[DiagramPic](tag,"diagram_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def diagramId = column[Long]("diagram_id")
  def pic = column[String]("pic")
  def intro  = column[String]("intro")
  def isTop = column[Int]("is_top")
  def sortNum = column[Int]("sort_num")
  def addTime = column[Timestamp]("add_time")

  def * =(id.?,uid,diagramId,pic,intro.?,isTop,sortNum,addTime.?) <> (DiagramPic.tupled, DiagramPic.unapply)


}

