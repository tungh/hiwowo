package models.forum



import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.Page._
import models.user.{Users, UserLoveTopics}


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:16
 * ***********************
 * description:用于类的说明
 */

case class Topic(
                 id: Option[Long],
                 uid: Long,
                 title: String,
                 content: String,
                 intro:String,
                 pics:Option[String],
                 typeId:Int,
                 isBest:Boolean,
                 isTop:Boolean,
                 discussNum:Int,
                 viewNum:Int,
                 loveNum:Int,
                 checkState:Int,
                 addTime:Option[Timestamp]
                 )

object Topics extends Table[Topic]("topic") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def title = column[String]("title")
  def content = column[String]("content")
  def intro =column[String]("intro")
  def pics =column[String]("pics")
  def typeId = column[Int]("type_id")
  def isBest = column[Boolean]("is_best")
  def isTop = column[Boolean]("is_top")
  def discussNum = column[Int]("discuss_num")
  def viewNum = column[Int]("view_num")
  def loveNum = column[Int]("love_num")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = id.? ~ uid  ~ title ~ content ~ intro ~ pics.? ~ typeId ~ isBest ~ isTop  ~ discussNum ~ viewNum ~ loveNum ~ checkState ~ addTime.? <>(Topic, Topic.unapply _)
  def autoInc = id.? ~ uid ~ title ~ content ~ intro ~ pics.?  ~ typeId ~ isBest ~ isTop ~ discussNum ~ viewNum ~ loveNum ~ checkState ~ addTime.? <>(Topic, Topic.unapply _) returning id

  def autoInc2 = uid ~ title ~ content  ~ typeId ~ checkState returning id


}




