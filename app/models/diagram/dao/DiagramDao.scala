package models.diagram.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs

import play.api.db.slick.Config.driver.simple._
import play.api.cache.Cache
import models.Page
import models.user._
import models.diagram._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午2:26
 */
object DiagramDao {

  val albums = TableQuery[AlbumTable]
  val diagrams = TableQuery[DiagramTable]
  val diagramPics = TableQuery[DiagramPicTable]
  val diagramDiscusses = TableQuery[DiagramDiscussTable]
  val users = TableQuery[UserTable]

  /*
   *
   * album 处理
   * */
  /*  add album */
  def addAlbum(uid:Long,title:String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val albumsAutoInc = albums.map( c =>(c.uid,c.title))  returning albums.map(_.id) into {
      case (_, id) => id
    }
    albumsAutoInc.insert(uid,title)
  }
  def addAlbum( uid:Long,title: String, pic: String, intro:String,status:Int,diagramNum:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val albumsAutoInc = albums.map( c => (c.uid,c.title,c.pic,c.intro,c.status,c.diagramNum)) returning albums.map(_.id) into {
      case (_, id) => id
    }
    albumsAutoInc.insert(uid,title,pic,intro,status,diagramNum)
  }

  def deleteAlbum(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-albums if c.id === id) yield c).delete
  }
  def modifyAlbum(id:Long,title:String,pic:String,intro:String,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-albums if c.id === id ) yield(c.title,c.pic,c.intro,c.status)).update(title,pic,intro,status)
  }

  def findAlbumById(id:Long):Option[Album] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-albums if c.id === id) yield c).firstOption
  }


   /*
   *
   *  diagram images
   *
   * */

  def addDiagramPic(uid:Long,diagramId:Long, url:String, intro: Option[String]):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val imageAutoInc = diagramPics.map( c => (c.uid,c.diagramId,c.url,c.intro.?)) returning diagramPics.map(_.id) into {
      case (_, id) => id
    }
    imageAutoInc.insert(uid,diagramId,url,intro)
  }
  def addDiagramPic(uid:Long,diagramId:Long, url:String,width:Int,height:Int,rawUrl:String,rawWidth:Int,rawHeight:Int,intro: Option[String]):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val imageAutoInc = diagramPics.map( c => (c.uid,c.diagramId,c.url,c.width,c.height,c.rawUrl,c.rawWidth,c.rawHeight,c.intro.?)) returning diagramPics.map(_.id) into {
      case (_, id) => id
    }
    imageAutoInc.insert(uid,diagramId,url,width,height,rawUrl,rawWidth,rawHeight,intro)
  }
  def deleteDiagramPic(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagramPics if c.id === id) yield c ).delete
  }
  def deleteDiagramPics(diagramId:Long) =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagramPics if c.diagramId === diagramId) yield c ).delete
  }

  def modifyDiagramPicIntro(id:Long,intro:Option[String]) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagramPics if c.id === id ) yield c.intro.? ).update(intro)
  }

  def findDiagramPics(diagramId:Long):List[DiagramPic] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagramPics if c.diagramId === diagramId)yield c).list
  }

  /*
  *
  * diagram 处理
  *
  * */

  def addDiagram(uid:Long,typeId:Int,title: String,pic: String,intro: Option[String],content:Option[String],status:Int):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val diagramAutoInc = diagrams.map(c => (c.uid,c.typeId,c.title,c.pic,c.intro.?,c.content.?,c.status)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,typeId,title,pic,intro,content,status)
  }


  def deleteDiagram(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagrams if c.id === id) yield c ).delete
  }
  def modifyDiagram(id:Long,uid:Long,typeId:Int,title: String,pic: String,intro: Option[String],content: Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.typeId,c.title,c.pic,c.intro.?,c.content.?,c.status) ).update(uid,typeId,title,pic,intro,content,status)
  }

  def modifyDiagramLabels(id:Long,labels:String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-diagrams if c.id ===id)yield c.labels ).update(labels)
  }
  def modifyDiagramStatus(id:Long,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-diagrams if c.id === id) yield c.status ).update(status)
  }
  def findDiagramById(id:Long):Option[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-diagrams if c.id === id) yield c).firstOption
  }
  def findDiagram(id:Long):Option[(Diagram,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for{
        c<-diagrams
        u<-users
         if c.uid === u.id
        if c.id === id
      } yield (c,u)
      ).firstOption
  }
  def findUserDiagrams(uid:Long,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(diagrams.filter(_.uid === uid).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
  val list =  (for{
      c <- diagrams
      if c.uid === uid
    } yield c).drop(startRow).take(pageSize).list
    Page[Diagram](list,currentPage,totalPages)
  }
    def findDiagrams(sortBy:String,status:Int,currentPage:Int,pageSize:Int):Page[(Diagram,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(diagrams.filter(_.status ===status).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
      var query = for{
        c<-diagrams
        u<-users
        if c.status === status
        if c.uid === u.id
      }yield(c,u)
      if(sortBy == "new") query = query.sortBy(_._1.id desc)
      if(sortBy == "hot") query = query.sortBy(_._1.loveNum desc)
      val list = query.drop(startRow).take(pageSize).list
    Page[(Diagram,User)](list,currentPage,totalPages)
  }



  def findDiagrams(sortBy:String,typeId:Int,status:Int,currentPage:Int,pageSize:Int):Page[(Diagram,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(diagrams.filter(_.status === status).filter(_.typeId === typeId).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize}
    var query = for{
      c<-diagrams
      u<-users
      if c.typeId === typeId
      if c.status === status
      if c.uid === u.id
    }yield(c,u)
    if(sortBy == "new") query = query.sortBy(_._1.addTime desc)
    if(sortBy == "hot") query = query.sortBy(_._1.loveNum desc)
    val list = query.drop(startRow).take(pageSize).list
    Page[(Diagram,User)](list,currentPage,totalPages)
  }
  def findAllDiagrams(currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(diagrams.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
     val list =  ( for(c <- diagrams ) yield c).drop(startRow).take(pageSize).list
    Page[Diagram](list,currentPage,totalPages)
  }

  /* 管理后台 筛选 */
  def filterDiagrams(title: Option[String], status: Option[Int], typeId: Option[Int], currentPage: Int, pageSize: Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    var query = for (c <- diagrams) yield c
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    if(!status.isEmpty) query = query.filter(_.status === status.get)
    if(!typeId.isEmpty) query = query.filter(_.typeId === typeId.get)
    query = query.sortBy(_.id desc)
    val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val ts: List[Diagram] = query.drop(startRow).take(pageSize).list
    Page[Diagram](ts, currentPage, totalPages)
  }


  /*
  *
  * diagram discuss
  * */

def addDiagramDiscuss(uid:Long,diagramId:Long,quoteContent:Option[String],content:String,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
  val diagramDiscussAutoInc = diagramDiscusses.map( c => (c.uid,c.diagramId,c.quoteContent.?,c.content,c.checkState)) returning diagramDiscusses.map(_.id) into {
    case (_, id) => id
  }
  diagramDiscussAutoInc.insert(uid,diagramId,quoteContent,content,checkState)
}

def findDiagramDiscusses(diagramId:Long,sortBy:String,currentPage:Int,pageSize:Int):Page[(DiagramDiscuss,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
  val totalRows = Query(diagramDiscusses.filter(_.diagramId === diagramId).filter(_.checkState === 1).length).first
  val totalPages = (totalRows + pageSize - 1) / pageSize
  val startRow = if (currentPage < 1 || currentPage > totalPages) { 0} else {(currentPage - 1) * pageSize }
  var query = for {
    c <- diagramDiscusses
    u <- users
  if c.diagramId === diagramId
  if c.checkState === 1
    if c.uid === u.id
  }yield(c,u)

  if(sortBy == "new") query = query.sortBy(_._1.addTime desc)
  if(sortBy == "hot") query = query.sortBy(_._1.loveNum desc)
  val list = query.drop(startRow).take(pageSize).list
  Page[(DiagramDiscuss,User)](list, currentPage,totalPages)
}

}
