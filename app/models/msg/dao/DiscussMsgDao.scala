package models.msg.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg._
import models.Page
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object DiscussMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
  val discussMsgs = TableQuery[DiscussMsgs]

  def addMsg( replierId:Long,replierName:String,replyType:Int,thirdId:Long,content:String,ownerId:Long):Long  = database.withDynSession {
    val discussMsgsAutoInc = discussMsgs.map( u => (u.replierId, u.replierName,u.replyType, u.thirdId, u.content, u.ownerId)) returning discussMsgs.map(_.id) into {
      case (_, id) => id
    }
    discussMsgsAutoInc.insert(replierId, replierName,replyType,thirdId,content,ownerId)
}
  def findMsgByLovedId(ownerId:Long,currentPage:Int,pageSize:Int):Page[DiscussMsg]  = database.withDynSession {
    val totalRows=Query(discussMsgs.filter(_.ownerId === ownerId ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- discussMsgs.filter(_.ownerId === ownerId).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[DiscussMsg]=  q.list()
    Page[DiscussMsg](msgs,currentPage,totalPages)
  }
  def findAll(currentPage:Int,pageSize:Int):Page[DiscussMsg] = database.withDynSession {
    val totalRows=Query(discussMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for( c<- discussMsgs.drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[DiscussMsg]=  q.list()
    Page[DiscussMsg](msgs,currentPage,totalPages)
  }
}
