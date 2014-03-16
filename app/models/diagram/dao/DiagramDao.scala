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
  val albums = TableQuery[Albums]
  val diagrams = TableQuery[Diagrams]
  val diagramDiscusses = TableQuery[DiagramDiscusses]
  val diagramPics = TableQuery[DiagramPics]
  val pics = TableQuery[Pics]


  /*
   *
   * album 处理
   * */
  /*  add album */
  def addAlbum(uid:Long,title:String) = database.withDynSession{
    val albumsAutoInc = albums.map( c =>(c.uid,c.title))  returning albums.map(_.id) into {
      case (_, id) => id
    }
    albumsAutoInc.insert(uid,title)
  }
  def addAlbum( uid:Long,title: String, pic: String, intro:String,status:Int,diagramNum:Int) = database.withDynSession{
    val albumsAutoInc = albums.map( c => (c.uid,c.title,c.pic,c.intro,c.status,c.diagramNum)) returning albums.map(_.id) into {
      case (_, id) => id
    }
    albumsAutoInc.insert(uid,title,pic,intro,status,diagramNum)
  }

  def deleteAlbum(id:Long) = database.withDynSession{
    (for(c<-albums if c.id === id) yield c).delete
  }
  def modifyAlbum(id:Long,title:String,pic:String,intro:String,status:Int) = database.withDynSession{
    (for(c<-albums if c.id === id ) yield(c.title,c.pic,c.intro,c.status)).update(title,pic,intro,status)
  }

  def findAlbumById(id:Long):Option[Album] = database.withDynSession{
    (for(c<-albums if c.id === id) yield c).firstOption
  }

 /*
  *
  *   图片处理
  */
  def addPic(uid:Long,url:String,intro:Option[String],status:Int,sortNum:Int) = database.withDynSession{
    val picAutoInc = pics.map( c => (c.uid,c.url,c.intro.?,c.status,c.sortNum)) returning pics.map(_.id) into {
      case (_, id) => id
    }
    picAutoInc.insert(uid,url,intro,status,sortNum)
  }

  def deletePic(id:Long) = database.withDynSession{
    ( for(c<-pics if c.id === id) yield c ).delete
  }
  def modifyPic(id:Long,url:String,intro:Option[String],status:Int,sortNum:Int) = database.withDynSession{
    ( for(c<-pics if c.id === id) yield(c.url,c.intro.?,c.status,c.sortNum)).update(url,intro,status,sortNum)
  }
  def modifyPicStatus(id:Long,status:Int) = database.withDynSession{
    ( for(c<-pics if c.id === id) yield c.status ).update(status)
  }

  def findPicById(id:Long):Option[Pic]= database.withDynSession{
    ( for(c<-pics if c.id === id) yield c).firstOption
  }


  /*
  *
  * diagram 处理
  *
  * */
 def addDiagram(uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],ps:Option[String],tags:Option[String],status:Int) = database.withDynSession{
    val diagramAutoInc = diagrams.map( c => (c.uid,c.title,c.pic,c.intro.?,c.content.?,c.ps.?,c.tags.?,c.status)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,title,pic,intro,content,ps,tags,status)
  }
  def deleteDiagram(id:Long) = database.withDynSession{
    ( for(c<-diagrams if c.id === id) yield c ).delete
  }
  def modifyDiagram(id:Long,uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],ps:Option[String],tags:Option[String],status:Int) = database.withDynSession{
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.title,c.intro.?,c.content.?,c.ps.?,c.tags.?,c.status) ).update(uid,title,intro,content,ps,tags,status)
  }
  def findDiagramById(id:Long):Option[Diagram] = database.withDynSession{
    (for(c<-diagrams if c.id === id) yield c).firstOption
  }

  /*
   *
   *    diagram pic dao
   *  */

 def addDiagramPic(diagramId:Long,picId:Long) = database.withDynSession{
   ( for( c<- diagramPics)yield (c.diagramId,c.picId) ).insert(diagramId,picId)
 }
  def deleteDiagramPic(diagramId:Long,picId:Long) = database.withDynSession{
    ( for(c<- diagramPics if c.diagramId === diagramId if c.picId === picId)yield c).delete
  }

  /*
  *
  * diagram discuss
  * */

def addDiagramDiscuss(uid:Long,diagramId:Long,thirdId:Long,typeId:Int,quoteContent:Option[String],content:String,checkState:Int) = database.withDynSession{
  val diagramDiscussAutoInc = diagramDiscusses.map( c => (c.uid,c.diagramId,c.thirdId,c.typeId,c.quoteContent.?,c.content,c.checkState)) returning diagramDiscusses.map(_.id) into {
    case (_, id) => id
  }
  diagramDiscussAutoInc.insert(uid,diagramId,thirdId,typeId,quoteContent,content,checkState)
}

}
