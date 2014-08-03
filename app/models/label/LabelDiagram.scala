package models.label

import play.api.db.slick.Config.driver.simple._
import java.sql.Timestamp

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-24
 * Time: 下午11:41
 */
case class LabelDiagram (
                id: Option[Long],
                labelId: Long,
                diagramId: Long,
                checkState:Int,
                addTime:Option[Timestamp]
                )
class LabelDiagramTable(tag:Tag) extends Table[LabelDiagram](tag,"label_diagram") {
def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
def labelId = column[Long]("label_id")
def diagramId = column[Long]("diagram_id")
def checkState = column[Int]("check_state")
def addTime = column[Timestamp]("add_time")
def * =(id.?,labelId,diagramId,checkState,addTime.?) <> (LabelDiagram.tupled, LabelDiagram.unapply)

}


