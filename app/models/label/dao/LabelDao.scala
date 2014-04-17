package models.label.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import models.label._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-25
 * Time: 下午10:30
 */
object LabelDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val labels = TableQuery[Labels]
  val labelRelations = TableQuery[LabelRelations]

  /* label dao */
  def addLabel(name:String,level:Int,intro:String,checkState:Int) = database.withDynSession {
    val labelsAutoInc = labels.map( t => (t.name, t.level, t.intro,t.checkState)) returning labels.map(_.id) into {
      case (_, id) => id
    }
    labelsAutoInc.insert(name,level,intro,checkState)
  }
  def checkLabel(name:String):Option[Label] = database.withDynSession {
    ( for(t <- labels if t.name === name ) yield t).firstOption
  }
  def modifyLabelCheckState(id:Long,checkState:Int) = database.withDynSession {
    ( for( t <- labels if t.id === id )yield t.checkState ).update(checkState)
  }
  def modifyLabelCheckState(name:String,checkState:Int) = database.withDynSession {
    ( for( t <- labels if t.name === name )yield t.checkState ).update(checkState)
  }
  def modifyLabel(id:Long,name:String,level:Int,intro:String,checkState:Int) =  database.withDynSession {
    ( for(t <- labels if t.id === id) yield (t.name,t.level,t.intro,t.checkState) ).update(name,level,intro,checkState)
  }
  def deleteLabel(id:Long) = database.withDynSession {
    (for( t<- labels if t.id === id ) yield t ).delete
    ( for(c <- labelRelations if c.id === id)yield c ).delete
  }

  def findLabelById(id:Long) = database.withDynSession{
    (for(t <- labels if t.id === id) yield t ).first
  }
  def findLabelByName(name:String) = database.withDynSession{
    ( for(t <- labels if t.name === name ) yield t ).first
  }

  /* label relation ship */
  def addLabelRelation(labelId:Long,diagramId:Long) = database.withDynSession{
    (for( t<- labelRelations ) yield (t.labelId,t.diagramId)).insert(labelId,diagramId)
  }
  def deleteLabelRelation(labelId:Long,diagramId:Long) = database.withDynSession{
    ( for( t<- labelRelations if t.labelId === labelId if t.diagramId === diagramId) yield t ).delete
  }





}
