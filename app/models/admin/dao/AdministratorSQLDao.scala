package models.admin.dao


import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-27下午8:05
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object AdministratorSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def loginRecord(id:Long,ip:String,num:Int,loginTime:Timestamp,lastLoginTime:Timestamp) = database.withDynSession {
    sqlu"update administrator set login_ip=$ip ,login_num = login_num+$num ,login_time = $loginTime ,last_login_time =$lastLoginTime  where id =$id".first
  }


}
