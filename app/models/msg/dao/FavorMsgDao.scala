package models.msg.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{FavorMsg, FavorMsgs}
import models.Page
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object FavorMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val favorMsgs = TableQuery[FavorMsgs]

 def addMsg(loverId:Long,loverName:String,favorType:Int,thirdId:Long,content:String,lovedId:Long)  = database.withDynSession {
   val favorMsgsAutoInc = favorMsgs.map( u => (u.loverId, u.loverName,u.favorType, u.thirdId, u.content, u.lovedId)) returning favorMsgs.map(_.id) into {
     case (_, id) => id
   }
   favorMsgsAutoInc.insert(loverId, loverName,favorType,thirdId,content,lovedId)
}
  def findMsgByLovedId(lovedId:Long,currentPage:Int,pageSize:Int):Page[FavorMsg]  = database.withDynSession {
    val totalRows=Query(favorMsgs.filter(_.lovedId === lovedId ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- favorMsgs.filter(_.lovedId === lovedId).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[FavorMsg]=  q.list()
    Page[FavorMsg](msgs,currentPage,totalPages)
  }

  def findAll(currentPage:Int,pageSize:Int) =  database.withDynSession {
    val totalRows=Query(favorMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- favorMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    val msgs:List[FavorMsg]=  q.list()
    Page[FavorMsg](msgs,currentPage,totalPages)
  }

}
