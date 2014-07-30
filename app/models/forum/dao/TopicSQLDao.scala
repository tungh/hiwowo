package models.forum.dao


import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._

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
 

  def updateViewNum(topicId:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update topic set view_num = view_num+$num where id =$topicId".first

  }

  def updateLoveNum(topicId:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update topic set love_num = love_num+$num where id =$topicId".first

  }

  def updateDiscussNum(topicId:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
   sqlu"update topic set discuss_num =discuss_num+$num where id =$topicId".first

  }
}
