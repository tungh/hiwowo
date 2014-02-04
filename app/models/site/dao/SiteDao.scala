package models.site.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.Page
import models.user._
import models.site._
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-3
 * Time: 下午7:22
 */
object SiteDao {
  implicit val database = Database.forDataSource(DB.getDataSource())
  val users = TableQuery[Users]
  val sites = TableQuery[Sites]
  /* add site */
  def addSite(uid:Long,title:String,pic:String,intro:String,tags:String) = database.withDynSession {
    val sitesAutoInc = sites.map( s => (s.uid, s.title, s.pic, s.intro, s.tags)) returning sites.map(_.id) into {
      case (_, id) => id
    }
    sitesAutoInc.insert(uid,title,pic,intro,tags)
  }

  def modifySite(sid:Long,title:String,pic:String,intro:String,tags:String) = database.withDynSession {
    (for(s <- sites if s.id === sid )yield (s.title,s.pic,s.intro,s.tags)).update((title,pic,intro,tags))
  }

  def modifySiteStatus(sid:Long,status:Int)  = database.withDynSession {
    ( for(s<-sites if s.id === sid)yield s.status ).update(status)
  }
  def modifySiteNotice(sid:Long,notice:String)  = database.withDynSession {
    ( for(s<-sites if s.id ===sid)yield s.notice ).update(notice)
  }

  def deleteSite(id:Long)  = database.withDynSession {
    ( for(s<-sites if s.id === id)yield s ).delete
  }

  def countSite  = database.withDynSession {
    Query(sites.length).first()
  }
  def countSite(time:Timestamp)  = database.withDynSession {
    Query(sites.filter(_.addTime > time ).length).first()
  }

  /* 查找 site by id */
  def findSite(sid:Long)  = database.withDynSession {
    ( for{
      s<- sites
      u<- users
    if s.uid === u.id
    if s.id === sid
    }yield (s,u) ).first
  }

  /* 查找 所有site */
  def findAllSites(currentPage:Int,pageSize:Int):Page[Site]  = database.withDynSession {
    val totalRows=Query(sites.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val list =  (for(c<-sites.drop(startRow).take(pageSize)  )yield c).list()

    Page[Site](list,currentPage,totalPages)
  }

  /* 筛选 sites */
  def filterSites(uid:Option[Long],title:Option[String],status:Option[Int],startDate:Option[Timestamp],endDate:Option[Timestamp],currentPage:Int,pageSize:Int):Page[Site]  = database.withDynSession {

    var query =for(c<-sites)yield c
    if(!uid.isEmpty) query = query.filter(_.uid === uid)
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    if(!status.isEmpty) query = query.filter(_.status === status)
    if(!startDate.isEmpty) query = query.filter(_.addTime > new Timestamp(startDate.get.getTime) )
    if(!endDate.isEmpty) query = query.filter(_.addTime <  new Timestamp(endDate.get.getTime) )
    val totalRows=query.list().length
    val totalPages=(totalRows + pageSize - 1) / pageSize
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val msgs:List[Site]=  query.drop(startRow).take(pageSize).list()
    Page[Site](msgs,currentPage,totalPages)
  }
}
