package models.cms.dao

import java.sql.Timestamp
import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.cms._
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-4
 * Time: 下午5:43
 */
object CmsDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val cmses = TableQuery[Cmses]

  def deleteCms(id:Long) =  database.withDynSession {
    ( for( c<- cmses if c.id === id) yield c).delete
  }
  def modifyCms(cms:Cms) = database.withDynSession {
    (for( c<- cmses if c.id === cms.id) yield c ).update(cms)
  }

}
