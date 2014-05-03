package models.msg.dao



import play.api.Play.current
import models.msg._
import models.Page
import play.api.db.slick.Config.driver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-29
 * Time: 下午3:59
 */
object DiscussMsgDao {
  
  val discussMsgs = TableQuery[DiscussMsgs]

  def addMsg(discusserId:Long,discusserName:String,discussType:Int,thirdId:Long,content:String,ownerId:Long):Long  = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val discussMsgsAutoInc = discussMsgs.map( u => (u.discusserId, u.discusserName,u.discussType, u.thirdId, u.content, u.ownerId)) returning discussMsgs.map(_.id) into {
      case (_, id) => id
    }
    discussMsgsAutoInc.insert(discusserId, discusserName,discussType,thirdId,content,ownerId)
}
  def findMsgByLovedId(ownerId:Long,currentPage:Int,pageSize:Int):Page[DiscussMsg]  = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows=Query(discussMsgs.filter(_.ownerId === ownerId ).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<- discussMsgs.filter(_.ownerId === ownerId).drop(startRow).take(pageSize)  )yield c
    //println(" q sql "+q.selectStatement)
    val msgs:List[DiscussMsg]=  q.list()
    Page[DiscussMsg](msgs,currentPage,totalPages)
  }
  def findAll(currentPage:Int,pageSize:Int):Page[DiscussMsg] = play.api.db.slick.DB.withSession{ implicit session:Session =>
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
