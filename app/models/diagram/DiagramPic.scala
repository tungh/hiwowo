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
                        diagramId:Long,
                        picId:Long,
                        sortNum:Int
                        )

class DiagramPics(tag:Tag) extends Table[DiagramPic](tag,"diagram_pic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def diagramId = column[Long]("diagram_id")
  def picId = column[Long]("pic_id")
  def sortNum = column[Int]("sort_num")
  def * =(id.?,diagramId,picId,sortNum) <> (DiagramPic.tupled, DiagramPic.unapply)


}

