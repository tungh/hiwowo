package models.term




import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description:标签
 * status 0 未审核 1 审核 3 核心  4 死
 * count
 */

case class Term(
                id: Option[Long],
                name: String,
                alias:Option[String],
                intro:Option[String],
                status:Int,
                count:Int,
                addNum:Int
                )

class Terms(tag:Tag) extends Table[Term](tag,"term") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def name = column[String]("name")
  def alias = column[String]("alias")
  def intro = column[String]("intro")
  def status = column[Int]("status")
  def count = column[Int]("count")
  def addNum = column[Int]("add_num")


  def * =(id.?,name,alias.?,intro.?,status,count,addNum) <> (Term.tupled, Term.unapply)


}


