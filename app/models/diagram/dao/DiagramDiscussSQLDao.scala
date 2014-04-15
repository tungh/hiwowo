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
object DiagramDiscussSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def updateLoveNum(discussId:Long,num:Int)=database.withDynSession{
    sqlu"update diagram_discuss set love_num =love_num+$num where id =$discussId".first
  }
  def updateHateNum(discussId:Long,num:Int)=database.withDynSession{
    sqlu"update diagram_discuss set hate_num =hate_num+$num where id =$discussId".first
  }
}
