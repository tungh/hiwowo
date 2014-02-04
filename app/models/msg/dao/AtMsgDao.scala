package models.msg.dao
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.msg.{AtMsg, AtMsgs}
import models.Page
import java.sql.Timestamp
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 上午9:25
 */
object AtMsgDao {
  lazy val database = Database.forDataSource(DB.getDataSource())
     val atMsgs = TableQuery[AtMsgs]

 def addMsg( senderId:Long,senderName:String,content:String,receiverId:Long,receiverName:String):Long = database.withDynSession {
   val atMsgsAutoInc = atMsgs.map( u => (u.senderId, u.senderName, u.content, u.receiverId, u.receiverName)) returning atMsgs.map(_.id) into {
     case (_, id) => id
   }
   atMsgsAutoInc.insert(senderId, senderName, content, receiverId,receiverName)
  }

  def deleteMsg(id:Long) = database.withDynSession {
    (for(c <- atMsgs if c.id ===id)yield c).delete
  }

  def countAtMsg = database.withDynSession {
    Query(atMsgs.length).first()
  }
  def countAtMsg(time:Timestamp) = database.withDynSession {
    Query(atMsgs.filter(_.addTime > time ).length).first()
  }

  def findAll(currentPage:Int,pageSize:Int):Page[AtMsg] = database.withDynSession {
    val totalRows=Query(atMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- atMsgs.drop(startRow).take(pageSize)  )yield c
    val msgs:List[AtMsg]=  q.list()
    Page[AtMsg](msgs,currentPage,totalPages)
  }

  def findMsgBySender(senderId:Long,currentPage:Int,pageSize:Int):Page[AtMsg] = database.withDynSession {
    val totalRows=Query(atMsgs.filter(_.senderId === senderId).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q = for(c<-atMsgs.filter(_.senderId === senderId ).drop(startRow).take(pageSize)  )yield c
    val msgs:List[AtMsg]=  q.list()
    Page[AtMsg](msgs,currentPage,totalPages)
  }
  def findMsgByReceiver(receiverId:Long,currentPage:Int,pageSize:Int):Page[AtMsg] = database.withDynSession {
    val totalRows=Query(atMsgs.filter(_.receiverId === receiverId).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-atMsgs.filter(_.receiverId === receiverId ).drop(startRow).take(pageSize)  )yield c
    val msgs:List[AtMsg]=  q.list()
    Page[AtMsg](msgs,currentPage,totalPages)
  }

  }
