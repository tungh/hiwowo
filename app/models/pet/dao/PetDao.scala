package models.pet.dao

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
import models.pet.{Pet, Pets}

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午2:26
 */
object PetDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val pets = TableQuery[Pets]
  val users = TableQuery[Users]



 /*
  *
  *   图片处理
  */
  def addPet(uid:Long,url:String,intro:Option[String],status:Int,typeId:Int) = database.withDynSession{
    val petAutoInc = pets.map( c => (c.uid,c.url,c.intro.?,c.status,c.typeId)) returning pets.map(_.id) into {
      case (_, id) => id
    }
    petAutoInc.insert(uid,url,intro,status,typeId)
  }

  def deletePet(id:Long) = database.withDynSession{
    ( for(c<-pets if c.id === id) yield c ).delete
  }
  def modifyPet(id:Long,url:String,intro:Option[String],status:Int,typeId:Int) = database.withDynSession{
    ( for(c<-pets if c.id === id) yield(c.url,c.intro.?,c.status,c.typeId)).update(url,intro,status,typeId)
  }
  def modifyPetStatus(id:Long,status:Int) = database.withDynSession{
    ( for(c<-pets if c.id === id) yield c.status ).update(status)
  }

  def findPetById(id:Long):Option[Pet]= database.withDynSession{
    ( for(c<-pets if c.id === id) yield c).firstOption
  }

  def findPet(id:Long):Option[(Pet,User)] = database.withDynSession{
    (for{
      c<-pets
      u<-users
      if c.id === id
      if c.uid === u.id
    }yield(c,u) ).firstOption
  }






}
