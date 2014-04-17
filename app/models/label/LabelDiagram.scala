package models.label

import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-24
 * Time: 下午11:41
 * type id 0 图说，1 用户
 */
case class LabelDiagram (
                id: Option[Long],
                labelId: Long,
                diagramId: Long,
                checkState:Int
                )
class LabelDiagrams(tag:Tag) extends Table[LabelDiagram](tag,"label_diagram") {
def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
def labelId = column[Long]("label_id")
def diagramId = column[Long]("diagram_id")
def checkState = column[Int]("check_state")
def * =(id.?,labelId,diagramId,checkState) <> (LabelDiagram.tupled, LabelDiagram.unapply)


}


