package models.user

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:57
 * ***********************
 * description:用于类的说明    用户的统计
 */



import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._

case class UserStatic(
                      id: Option[Long],
                      uid: Long,
                      fansNum: Int,
                      followNum: Int,
                      collectNum: Int,
                      subscribeNum:Int,
                      postDiagramNum: Int,
                      postTopicNum: Int
                      )
class UserStaticTable(tag:Tag) extends Table[UserStatic](tag,"user_static") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def fansNum = column[Int]("fans_num")
  def followNum = column[Int]("follow_num")
  def collectNum = column[Int]("collect_num")
  def subscribeNum = column[Int]("subscribe_num")
  def postDiagramNum = column[Int]("post_diagram_num")
  def postTopicNum = column[Int]("post_topic_num")

  // Every table needs a * projection with the same type as the table's type parameter
  def * =(id.?, uid, fansNum, followNum , collectNum,subscribeNum, postDiagramNum, postTopicNum)  <>(UserStatic.tupled, UserStatic.unapply)
  
}



