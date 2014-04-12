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
  val users = TableQuery[Users]

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
  def addPic(uid:Long,url:String,intro:Option[String],status:Int) = database.withDynSession{
    val picAutoInc = pics.map( c => (c.uid,c.url,c.intro.?,c.status)) returning pics.map(_.id) into {
      case (_, id) => id
    }
    picAutoInc.insert(uid,url,intro,status)
  }

  def deletePic(id:Long) = database.withDynSession{
    ( for(c<-pics if c.id === id) yield c ).delete
  }
  def modifyPic(id:Long,url:String,intro:Option[String],status:Int) = database.withDynSession{
    ( for(c<-pics if c.id === id) yield(c.url,c.intro.?,c.status)).update(url,intro,status)
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
 def addDiagram(uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],ps:Option[String],tags:Option[String],status:Int):Long = database.withDynSession{
    val diagramAutoInc = diagrams.map( c => (c.uid,c.title,c.pic,c.intro.?,c.content.?,c.ps.?,c.tags.?,c.status)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,title,pic,intro,content,ps,tags,status)
  }

  def addDiagram(uid:Long,typeId:Int,title: String,pic: String,intro: Option[String],tags:Option[String],status:Int):Long = database.withDynSession{
    val diagramAutoInc = diagrams.map(c => (c.uid,c.typeId,c.title,c.pic,c.intro.?,c.tags.?,c.status)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,typeId,title,pic,intro,tags,status)
  }

  def deleteDiagram(id:Long) = database.withDynSession{
    ( for(c<-diagrams if c.id === id) yield c ).delete
  }
  def modifyDiagram(id:Long,uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],ps:Option[String],tags:Option[String],status:Int) = database.withDynSession{
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.title,c.pic,c.intro.?,c.content.?,c.ps.?,c.tags.?,c.status) ).update(uid,title,pic,intro,content,ps,tags,status)
  }
  def modifyDiagram(id:Long,uid:Long,typeId:Int,title: String,pic: String,intro: Option[String],tags:Option[String],status:Int) = database.withDynSession{
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.typeId,c.title,c.pic,c.intro.?,c.tags.?,c.status) ).update(uid,typeId,title,pic,intro,tags,status)
  }
  def modifyDiagramStatus(id:Long,status:Int) = database.withDynSession{
    (for(c<-diagrams if c.id === id) yield c.status ).update(status)
  }
  def findDiagramById(id:Long):Option[Diagram] = database.withDynSession{
    (for(c<-diagrams if c.id === id) yield c).firstOption
  }
  def findDiagram(id:Long):Option[(Diagram,User)] = database.withDynSession{
    (
      for{
        c<-diagrams
        u<-users
         if c.uid === u.id
        if c.id === id } yield (c,u)
      ).firstOption
  }
    def findDiagrams(sortBy:String,status:Int,currentPage:Int,pageSize:Int):Page[(Diagram,User)] = database.withDynSession{
    val totalRows = Query(diagrams.filter(_.status ===status).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
      var query = for{
        c<-diagrams
        u<-users
        if c.status === status
        if c.uid === u.id
      }yield(c,u)
      if(sortBy == "new") query = query.sortBy(_._1.addTime desc)
      if(sortBy == "hot") query = query.sortBy(_._1.loveNum desc)
      val list = query.drop(startRow).take(pageSize).list()
    Page[(Diagram,User)](list,currentPage,totalPages)
  }



  def findDiagrams(sortBy:String,typeId:Int,status:Int,currentPage:Int,pageSize:Int):Page[(Diagram,User)] = database.withDynSession{
    val totalRows = Query(diagrams.filter(_.status === status).filter(_.typeId === typeId).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    var query = for{
      c<-diagrams
      u<-users
      if c.typeId === typeId
      if c.status === status
      if c.uid === u.id
    }yield(c,u)
    if(sortBy == "new") query = query.sortBy(_._1.addTime desc)
    if(sortBy == "hot") query = query.sortBy(_._1.loveNum desc)
    val list = query.drop(startRow).take(pageSize).list()
    Page[(Diagram,User)](list,currentPage,totalPages)
  }
  def findAllDiagrams(currentPage:Int,pageSize:Int):Page[Diagram] = database.withDynSession{
    val totalRows = Query(diagrams.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
     val list =  ( for(c <- diagrams ) yield c).drop(startRow).take(pageSize).list()
    Page[Diagram](list,currentPage,totalPages)
  }

  /* 管理后台 筛选 */
  def filterDiagrams(title: Option[String], status: Option[Int], typeId: Option[Int], currentPage: Int, pageSize: Int):Page[Diagram] = database.withDynSession {
    var query = for (c <- diagrams) yield c
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    if(!status.isEmpty) query = query.filter(_.status === status.get)
    if(!typeId.isEmpty) query = query.filter(_.typeId === typeId.get)
    query = query.sortBy(_.id desc)
    val totalRows = query.list().length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val ts: List[Diagram] = query.drop(startRow).take(pageSize).list()
    Page[Diagram](ts, currentPage, totalPages)
  }

  /*
   *
   *    diagram pic dao
   *  */

 def addDiagramPic(diagramId:Long,picId:Long) = database.withDynSession{
   ( for( c<- diagramPics)yield (c.diagramId,c.picId) ).insert(diagramId,picId)
 }
 def findDiagramPic(diagramId:Long,picId:Long):Option[DiagramPic] = database.withDynSession{
   ( for( c<- diagramPics if c.diagramId === diagramId if c.picId === picId ) yield c).firstOption
 }
  def deleteDiagramPic(diagramId:Long,picId:Long) = database.withDynSession{
    ( for(c<- diagramPics if c.diagramId === diagramId if c.picId === picId)yield c).delete
  }
  def findDiagramPics(id:Long):List[Pic] = database.withDynSession{
    ( for{
      p  <- pics
      dp <- diagramPics
      if p.id === dp.picId
      if dp.diagramId === id
    }yield p ).list()

  }
  /*
  *
  * diagram discuss
  * */

def addDiagramDiscuss(uid:Long,diagramId:Long,quoteContent:Option[String],content:String,checkState:Int) = database.withDynSession{
  val diagramDiscussAutoInc = diagramDiscusses.map( c => (c.uid,c.diagramId,c.quoteContent.?,c.content,c.checkState)) returning diagramDiscusses.map(_.id) into {
    case (_, id) => id
  }
  diagramDiscussAutoInc.insert(uid,diagramId,quoteContent,content,checkState)
}

}
