package models.cms.dao
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-4
 * Time: 下午5:43
 */
object CmsSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())


  def updateViewNum(id:Long,num:Int)=database.withDynSession{
    sqlu"update cms set view_num = view_num+$num where id =$id".first

  }
}
