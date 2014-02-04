package models.admin.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import play.api.libs.Codecs
import  models.admin._
import models.Page
import java.sql.{Timestamp,Date }
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-13下午12:27
* ModifyTime :
* ModifyContent:
* http://www.smeite.com/
*
*/
object AdministratorDao {
  implicit val database = Database.forDataSource(DB.getDataSource())
  val administrators = TableQuery[Administrators]

  def findById(id:Long):Administrator = database.withDynSession {
    (for(c<- administrators if c.id === id )yield c ).first
  }

  def authenticate(email: String, password: String):Option[Administrator] = database.withDynSession {
    (for(c<-administrators if c.email === email && c.password === Codecs.sha1("hiwowo"+password))yield c ).firstOption
  }



}
