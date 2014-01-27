package models.msg.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{DiscussMsg, DiscussMsgs}
import models.Page

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object DiscussMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
/*
  def addMsg( replierId:Long,replierName:String,replyType:Int,thirdId:Long,content:String,ownerId:Long):Long = database.withSession {  implicit session:Session =>
    DiscussMsgs.autoInc2.insert(replierId,replierName,replyType,thirdId,content,ownerId)
}
  def findMsgByLovedId(ownerId:Long,currentPage:Int,pageSize:Int):Page[DiscussMsg] = database.withSession {  implicit session:Session =>
    val totalRows=Query(DiscussMsgs.filter(_.ownerId === ownerId ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-DiscussMsgs.filter(_.ownerId === ownerId).sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[DiscussMsg]=  q.list()
    Page[DiscussMsg](msgs,currentPage,totalPages)
  }
  def findAll(currentPage:Int,pageSize:Int):Page[DiscussMsg] = database.withSession {  implicit session:Session =>
    val totalRows=Query(DiscussMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-DiscussMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[DiscussMsg]=  q.list()
    Page[DiscussMsg](msgs,currentPage,totalPages)
  }*/
}
