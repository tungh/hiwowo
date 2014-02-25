package models.term.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import models.term._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-25
 * Time: 下午10:30
 */
object TermDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val terms = TableQuery[Terms]
  val termRelations = TableQuery[TermRelations]

  /* term dao */
  def addTerm(name:String,alias:String,intro:String,status:Int) = database.withDynSession {
    val termsAutoInc = terms.map( t => (t.name, t.alias, t.status,t.intro)) returning terms.map(_.id) into {
      case (_, id) => id
    }
    termsAutoInc.insert(name,alias,status,intro)
  }
  def checkTerm(name:String):Option[Term] = database.withDynSession {
    ( for(t <- terms if t.name === name ) yield t).firstOption
  }
  def modifyTermStatus(id:Long,status:Int) = database.withDynSession {
    ( for( t <- terms if t.id === id )yield t.status ).update(status)
  }
  def modifyTermStatus(name:String,status:Int) = database.withDynSession {
    ( for( t <- terms if t.name === name )yield t.status ).update(status)
  }
  def modifyTerm(id:Long,name:String,alias:String,intro:String,status:Int) =  database.withDynSession {
    ( for(t <- terms if t.id === id) yield (t.name,t.alias,t.intro,t.status) ).update(name,alias,intro,status)
  }
  def deleteTerm(id:Long) = database.withDynSession {
    (for( t<- terms if t.id === id ) yield t ).delete
    ( for(c <- termRelations if c.id === id)yield c ).delete
  }

  def findTermById(id:Long) = database.withDynSession{
    (for(t <- terms if t.id === id) yield t ).first
  }
  def findTermByName(name:String) = database.withDynSession{
    ( for(t <- terms if t.name === name ) yield t ).first
  }

  /* term relation ship */
  def addTermRelation(termId:Long,thirdId:Long,typeId:Int) = database.withDynSession{
    (for( t<-termRelations ) yield (t.termId,t.thirdId,t.typeId)).insert(termId,thirdId,typeId)
  }
  def deleteTermRelation(termId:Long,thirdId:Long,typeId:Int) = database.withDynSession{
    ( for( t<- termRelations if t.termId === termId if t.thirdId === thirdId if t.typeId === typeId) yield t ).delete
  }





}
