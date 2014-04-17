package models.label




import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description:标签
 * check_state 0 未审核 1 审核
 * level 1 普通  2 扩展  3 核心
 */

case class Label(
                id: Option[Long],
                name: String,
                level:Int,
                intro:Option[String],
                isHot:Int,
                checkState:Int,
                addNum:Int
                )

class Labels(tag:Tag) extends Table[Label](tag,"label") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def level = column[Int]("level")
  def intro = column[String]("intro")
  def isHot = column[Int]("is_hot")
  def checkState = column[Int]("check_state")
  def addNum = column[Int]("add_num")


  def * =(id.?,name,level,intro.?,isHot,checkState,addNum) <> (Label.tupled, Label.unapply)


}


