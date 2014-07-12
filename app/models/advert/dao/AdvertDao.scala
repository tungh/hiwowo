package models.advert.dao

import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._
import models.Page
import models.advert.{Advert, Adverts}
import play.api.Play.current
import play.api.libs.Codecs
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-5
 * Time: 下午9:51
 */
object AdvertDao {
    val adverts = TableQuery[Adverts]

  def addAdvert(code:String,typeId:Int,title:String,link:String,pic:Option[String],width:Int,height:Int,startTime:Timestamp,endTime:Timestamp,sortNum:Int,note:Option[String]) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val advertAutoInc = adverts.map( c =>(c.code,c.typeId,c.title,c.link,c.pic.?,c.width,c.height,c.startTime,c.endTime,c.sortNum,c.note.?))  returning adverts.map(_.id) into {
      case (_, id) => id
    }
    advertAutoInc.insert(code,typeId,title,link,pic,width,height,startTime,endTime,sortNum,note)
  }

  def findAdvert(id:Long):Advert = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-adverts if c.id === id) yield c).first
  }

  def deleteAdvert(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-adverts if c.id === id) yield c).delete
  }

  def modifyAdvert(id:Long,code:String,typeId:Int,title:String,link:String,pic:Option[String],width:Int,height:Int,startTime:Timestamp,endTime:Timestamp,sortNum:Int,note:Option[String])   = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-adverts if c.id === id )yield(c.code,c.typeId,c.title,c.link,c.pic.?,c.width,c.height,c.startTime,c.endTime,c.sortNum,c.note.?)).update(code,typeId,title,link,pic,width,height,startTime,endTime,sortNum,note)
  }

  def findAdverts(currentPage:Int,pageSize:Int):Page[Advert]  = play.api.db.slick.DB.withSession{ implicit session:Session =>
  val totalRows = Query(adverts.length).first
  val totalPages = (totalRows + pageSize - 1) / pageSize
  val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
   val list = ( for(c<-adverts)yield c).drop(startRow).take(pageSize).list()
    Page[Advert](list,currentPage,totalPages)
  }

  def filterAdverts(code:Option[String],typeId:Option[Int],title:Option[String],startTime:Option[Timestamp],endTime:Option[Timestamp],currentPage:Int,pageSize:Int)  = play.api.db.slick.DB.withSession{ implicit session:Session =>
    var query = for( c<- adverts ) yield c
    val now = new Timestamp(System.currentTimeMillis())
    if(!code.isEmpty) query = query.filter(_.code like "%"+code.get+"%")
    if(!typeId.isEmpty) query = query.filter(_.typeId === typeId.get)
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    if(!startTime.isEmpty) query = query.filter(_.startTime < now)
    if(!endTime.isEmpty) query = query.filter(_.endTime > now)
    query = query.sortBy(_.sortNum desc)
    val totalRows:Int = query.list().length
    val totalPages:Int = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }

    val list:List[Advert] = query.drop(startRow).take(pageSize).list()
    Page[Advert](list,currentPage,totalPages)

  }

}
