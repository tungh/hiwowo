package models.pet.dao

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
object PetSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def updateViewNum(petId:Long,num:Int)=database.withDynSession{
    sqlu"update pet set view_num = view_num+$num where id =$petId".first

  }

  def updateDiscussNum(petId:Long,num:Int)=database.withDynSession{
   sqlu"update pet set discuss_num =discuss_num+$num where id =$petId".first
  }
  def updateLoveNum(petId:Long,num:Int)=database.withDynSession{
    sqlu"update pet set love_num =love_num+$num where id =$petId".first
  }
}
