package models.diagram.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs

import play.api.db.slick.Config.driver.simple._
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import models.user._
import models.diagram._
import play.api.db.slick.Config.driver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午2:26
 */
object DiagramDao {

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
  *   图片处理
  */
  def addPic(uid:Long,url:String,intro:Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val picAutoInc = pics.map( c => (c.uid,c.url,c.intro.?,c.status)) returning pics.map(_.id) into {
      case (_, id) => id
    }
    picAutoInc.insert(uid,url,intro,status)
  }

  def deletePic(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-pics if c.id === id) yield c ).delete
  }
  def modifyPic(id:Long,url:String,intro:Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-pics if c.id === id) yield(c.url,c.intro.?,c.status)).update(url,intro,status)
  }
  def modifyPicStatus(id:Long,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-pics if c.id === id) yield c.status ).update(status)
  }

  def findPicById(id:Long):Option[Pic]= play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-pics if c.id === id) yield c).firstOption
  }




  /*
  *
  * diagram 处理
  *
  * */
 def addDiagram(uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],ps:Option[String],status:Int):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val diagramAutoInc = diagrams.map( c => (c.uid,c.title,c.pic,c.intro.?,c.content.?,c.ps.?,c.status)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,title,pic,intro,content,ps,status)
  }

  def addDiagram(uid:Long,typeId:Int,title: String,pic: String,intro: Option[String],status:Int):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val diagramAutoInc = diagrams.map(c => (c.uid,c.typeId,c.title,c.pic,c.intro.?,c.status)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,typeId,title,pic,intro,status)
  }
  def addDiagram(uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],status:Int,typeId:Int):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val diagramAutoInc = diagrams.map( c => (c.uid,c.title,c.pic,c.intro.?,c.content.?,c.status,c.typeId)) returning diagrams.map(_.id) into {
      case (_, id) => id
    }
    diagramAutoInc.insert(uid,title,pic,intro,content,status,typeId)
  }

  def deleteDiagram(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagrams if c.id === id) yield c ).delete
  }
  def modifyDiagram(id:Long,uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],ps:Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.title,c.pic,c.intro.?,c.content.?,c.ps.?,c.status) ).update(uid,title,pic,intro,content,ps,status)
  }
  def modifyDiagram(id:Long,uid:Long,title: String,pic: String,intro: Option[String],content: Option[String],status:Int,typeId:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.title,c.pic,c.intro.?,c.content.?,c.status,c.typeId) ).update(uid,title,pic,intro,content,status,typeId)
  }
  def modifyDiagram(id:Long,uid:Long,typeId:Int,title: String,pic: String,intro: Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<-diagrams if c.id === id) yield(c.uid,c.typeId,c.title,c.pic,c.intro.?,c.status) ).update(uid,typeId,title,pic,intro,status)
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
    (
      for{
        c<-diagrams
        u<-users
         if c.uid === u.id
        if c.id === id } yield (c,u)
      ).firstOption
  }
  def findUserDiagrams(uid:Long,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(diagrams.filter(_.uid === uid).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
  val list =  (for{
      c <- diagrams
      if c.uid === uid
    } yield c).drop(startRow).take(pageSize).list()
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
      if(sortBy == "new") query = query.sortBy(_._1.addTime desc)
      if(sortBy == "hot") query = query.sortBy(_._1.loveNum desc)
      val list = query.drop(startRow).take(pageSize).list()
    Page[(Diagram,User)](list,currentPage,totalPages)
  }



  def findDiagrams(sortBy:String,typeId:Int,status:Int,currentPage:Int,pageSize:Int):Page[(Diagram,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
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
  def findAllDiagrams(currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
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
  def filterDiagrams(title: Option[String], status: Option[Int], typeId: Option[Int], currentPage: Int, pageSize: Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
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

 def addDiagramPic(diagramId:Long,picId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
   ( for( c<- diagramPics)yield (c.diagramId,c.picId) ).insert(diagramId,picId)
 }
 def findDiagramPic(diagramId:Long,picId:Long):Option[DiagramPic] = play.api.db.slick.DB.withSession{ implicit session:Session =>
   ( for( c<- diagramPics if c.diagramId === diagramId if c.picId === picId ) yield c).firstOption
 }
  def deleteDiagramPic(diagramId:Long,picId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c<- diagramPics if c.diagramId === diagramId if c.picId === picId)yield c).delete
  }
  def findDiagramPics(id:Long):List[Pic] = play.api.db.slick.DB.withSession{ implicit session:Session =>
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

def addDiagramDiscuss(uid:Long,diagramId:Long,quoteContent:Option[String],content:String,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
  val diagramDiscussAutoInc = diagramDiscusses.map( c => (c.uid,c.diagramId,c.quoteContent.?,c.content,c.checkState)) returning diagramDiscusses.map(_.id) into {
    case (_, id) => id
  }
  diagramDiscussAutoInc.insert(uid,diagramId,quoteContent,content,checkState)
}

def findDiagramDiscusses(diagramId:Long,sortBy:String,currentPage:Int,pageSize:Int):Page[(DiagramDiscuss,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>d
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
  val list = query.drop(startRow).take(pageSize).list()
  Page[(DiagramDiscuss,User)](list, currentPage,totalPages)
}

}
