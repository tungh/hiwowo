package models.msg.dao



import play.api.Play.current
import models.msg.{SystemMsgReceivers, SystemMsg, SystemMsgs}
import models.Page
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-2
 * Time: 下午10:43
 */
object SystemMsgDao {

  val systemMsgs = TableQuery[SystemMsgs]
  val systemMsgReceivers = TableQuery[SystemMsgReceivers]

   def addMsg(title:String,content:String):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
     val systemMsgsAutoInc = systemMsgs.map( u => (u.title, u.content )) returning systemMsgs.map(_.id) into {
       case (_, id) => id
     }
     systemMsgsAutoInc.insert(title,content)
   }
   def modifyMsg(id:Long,title:String,content:String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
     (for(c <- systemMsgs if c.id === id )yield (c.title,c.content)).update(title,content)
   }
  def deleteMsg(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c <- systemMsgs if c.id === id) yield  c).delete
  }
  def modifyMsgStatus(id:Long,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c <- systemMsgs if c.id === id)yield c.status).update(status)
  }

  def findMsg(id:Long):Option[SystemMsg] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c <- systemMsgs if c.id === id)yield c ).firstOption
  }

  def findAll(currentPage:Int,pageSize:Int):Page[SystemMsg] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows=Query(systemMsgs.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- systemMsgs.sortBy(_.id desc).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[SystemMsg]=  q.list()
    Page[SystemMsg](msgs,currentPage,totalPages)
  }

  def filterMsgs(title:Option[String],currentPage:Int,pageSize:Int):Page[SystemMsg] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    var query =for(c<-systemMsgs)yield c
    if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
    val totalRows=query.list().length
    val totalPages=((totalRows + pageSize - 1) / pageSize);
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val msgs:List[SystemMsg]=  query.drop(startRow).take(pageSize).list()
    Page[SystemMsg](msgs,currentPage,totalPages);
  }


  /* msg receiver */
  def addMsgReceiver(msgId:Long,receiverId:Long):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val systemMsgReceiversAutoInc = systemMsgReceivers.map( u => (u.msgId, u.receiverId )) returning systemMsgs.map(_.id) into {
      case (_, id) => id
    }
    systemMsgReceiversAutoInc.insert(msgId,receiverId)
  }

  def modifyMsgReceiverStatus(id:Long,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c<- systemMsgReceivers if c.id === id )yield c.status ).update(status)
  }

  def modifyMsgReceiverStatus(msgId:Long,receiverId:Long,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c<- systemMsgReceivers.filter(_.msgId === msgId ).filter(_.receiverId === receiverId ) )yield c.status ).update(status)
  }

  def deleteMsgReceiver(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c<- systemMsgReceivers if c.id === id  )yield c).delete
  }

  def deleteMsgReceiver(msgId:Long,receiverId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c<- systemMsgReceivers.filter(_.msgId === msgId ).filter(_.receiverId === receiverId ) )yield c ).delete
  }
 /* 查找所有的 system msg receiver */
  def findAllMsgReceivers(currentPage:Int,pageSize:Int):Page[(Long,Long,Int,String,String,Timestamp)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows=Query(systemMsgReceivers.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q =  for{
      c<- systemMsgReceivers
      sm <- systemMsgs
      if sm.id === c.msgId
    }yield (c.id,c.receiverId,c.status,sm.title,sm.content,c.addTime)
    val msgs:List[(Long,Long,Int,String,String,Timestamp)]=  q.drop(startRow).take(pageSize).list()
    Page[(Long,Long,Int,String,String,Timestamp)](msgs,currentPage,totalPages)
  }

  /* 查找某个receiver 收到的 信息 */
    def findReceiverMsgs(receiverId:Long,currentPage:Int,pageSize:Int):Page[(Long,Long,Int,String,String,Timestamp)] =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows=Query(systemMsgReceivers.filter(_.receiverId === receiverId).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<- systemMsgReceivers
      sm <- systemMsgs
     if c.receiverId === receiverId
      if sm.id === c.msgId
    }yield (c.id,c.receiverId,c.status,sm.title,sm.content,c.addTime)
    val msgs:List[(Long,Long,Int,String,String,Timestamp)]=  q.drop(startRow).take(pageSize).list()
    Page[(Long,Long,Int,String,String,Timestamp)](msgs,currentPage,totalPages)
  }
}
