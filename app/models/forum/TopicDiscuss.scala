package models.forum

import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import models.Page
import models.Page._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:17
 * ***********************
 * description:话题回复
 */

case class TopicDiscuss (
                        id: Option[Long],
                        uid: Long,
                        topicId: Long,
                        quoteContent: Option[String],
                        content: String,
                        checkState:Int,
                        addTime:Option[Timestamp]
                        )
class TopicDiscusses(tag:Tag) extends Table[TopicDiscuss](tag,"topic_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def topicId = column[Long]("topic_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = (id.?, uid,topicId,quoteContent.?,content,checkState,addTime.?) <> (TopicDiscuss.tupled, TopicDiscuss.unapply)



}


