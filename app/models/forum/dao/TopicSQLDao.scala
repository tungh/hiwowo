package models.forum.dao

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-26下午7:16
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/
object TopicSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def updateViewNum(topicId:Long,num:Int)=database.withDynSession{
    sqlu"update topic set view_num = view_num+$num where id =$topicId".first

  }

  def updateDiscussNum(topicId:Long,num:Int)=database.withDynSession{
   sqlu"update topic set discuss_num =discuss_num+$num where id =$topicId".first

  }
}
