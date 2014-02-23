package models.diagram.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import models.user._
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import models.diagram._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午2:26
 */
object DiagramDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val diagrams = TableQuery[Diagrams]
  val albums = TableQuery[Albums]
  val diagramDiscusses = TableQuery[DiagramDiscusses]
  val diagramPics = TableQuery[DiagramPics]

}
