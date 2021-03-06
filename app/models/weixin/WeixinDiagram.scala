package models.weixin

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-3-31
 * Time: 下午11:32
 */
case class WeixinDiagram (
                      id: Option[Long],
                      diagramId:Long,
                      period:Long,
                      addTime:Option[Timestamp]
                      )

class WeixinDiagramTable(tag:Tag) extends Table[WeixinDiagram](tag,"weixin_diagram") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def diagramId = column[Long]("diagram_id")
  def period = column[Long]("period")
  def addTime = column[Timestamp]("add_time")
  def * =(id.?,diagramId,period,addTime.?) <> (WeixinDiagram.tupled, WeixinDiagram.unapply)


}


