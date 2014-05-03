package models.user.dao


import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
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
 


  def updateFansNum(uid:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_static set fans_num = fans_num+$num where id =$uid".first
  }
  def updateFollowNum(uid:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_static set follow_num =follow_num+$num where id =$uid".first
  }

  def updateCollectNum(uid:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_static set collect_num =collect_num+$num where id =$uid".first
  }

  def updateSubscribeNum(uid:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_static set subscribe_num = subscribe_num+$num where id =$uid".first
  }

  def updatePostDiagramNum(uid:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_static set post_diagram_num =post_diagram_num+$num where id =$uid".first
  }
  def updatePostTopicNum(uid:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_static set post_topic_num =post_topic_num+$num where id =$uid".first
  }

  def loginRecord(uid:Long,ip:String,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update user_profile  set login_ip=$ip ,login_num = login_num+$num where uid =$uid".first
  }


}
