package models.diagram.dao

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
object DiagramSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def updateViewNum(diagramId:Long,num:Int)=database.withDynSession{
    sqlu"update diagram set view_num = view_num+$num where id =$diagramId".first

  }

  def updateDiscussNum(diagramId:Long,num:Int)=database.withDynSession{
   sqlu"update diagram set discuss_num =discuss_num+$num where id =$diagramId".first
  }
  def updateLoveNum(diagramId:Long,num:Int)=database.withDynSession{
    sqlu"update diagram set love_num =love_num+$num where id =$diagramId".first
  }
  def updateHateNum(diagramId:Long,num:Int)=database.withDynSession{
    sqlu"update diagram set hate_num =hate_num+$num where id =$diagramId".first
  }
}
