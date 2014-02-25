package models.term

import scala.slick.driver.MySQLDriver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-24
 * Time: 下午11:41
 * type id 0 图说，1 用户
 */
case class TermRelation (
                id: Option[Long],
                termId: Long,
                thirdId: Long,
                typeId:Int,
                sortNum:Int
                )
class TermRelations(tag:Tag) extends Table[TermRelation](tag,"term_relation") {
def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
def termId = column[Long]("term_id")
def thirdId = column[Long]("third_id")
def typeId = column[Int]("type_id")
def sortNum = column[Int]("sort_num")
def * =(id.?,termId,thirdId,typeId,sortNum) <> (TermRelation.tupled, TermRelation.unapply)


}


