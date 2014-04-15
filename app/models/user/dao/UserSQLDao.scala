package models.user.dao

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-27下午8:05
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/
object UserSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())





  def updateFansNum(uid:Long,num:Int)=database.withDynSession {
    sqlu"update user_static set fans_num = fans_num+$num where id =$uid".first
  }
  def updateFollowNum(uid:Long,num:Int)=database.withDynSession {
    sqlu"update user_static set follow_num =follow_num+$num where id =$uid".first
  }

  def updateCollectNum(uid:Long,num:Int)=database.withDynSession {
    sqlu"update user_static set collect_num =collect_num+$num where id =$uid".first
  }
  def updatePostDiagramNum(uid:Long,num:Int)=database.withDynSession {
    sqlu"update user_static set post_diagram_num =post_diagram_num+$num where id =$uid".first
  }
  def updatePostTopicNum(uid:Long,num:Int)=database.withDynSession {
    sqlu"update user_static set post_topic_num =post_topic_num+$num where id =$uid".first
  }

  def loginRecord(uid:Long,ip:String,num:Int)=database.withDynSession {
    sqlu"update user_profile  set login_ip=$ip ,login_num = login_num+$num where uid =$uid".first
  }


}
