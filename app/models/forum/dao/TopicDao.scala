package models.forum.dao

import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.Play.current
import models.forum._
import models.Page
import  models.user._
import java.sql.Timestamp

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-1-13下午12:35
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
 *  add  delete update find  filter list
*
*/
object TopicDao {

  lazy val database = Database.forDataSource(DB.getDataSource())

 /* /* 增加一个话题 */
  def addTopic(topic:Topic):Long =database.withSession {  implicit session:Session =>
      Topics.autoInc.insert(topic)

  }
  def addTopic(uid:Long,title:String,content:String,typeId:Int,checkState:Int)(implicit  session:Session)={
    Topics.autoInc2.insert(uid,title,content,typeId,checkState)
  }

  /*删除topic的同时，需要把topic 下的reply 给删除*/
  def deleteTopic(id:Long)=database.withSession {  implicit session:Session =>
    (for(c<-Topics if c.id === id)yield c ).delete
    (for(c<-TopicDiscusses  if c.topicId === id)yield c ).delete
  }
  /* 修改 topic */
  def modifyTopic(topic:Topic)= database.withSession{ implicit session:Session =>
    (for(c<-Topics if c.id === topic.id)yield c ).update(topic)
  }
  def modifyTopicCheckState(id:Long,checkState:Int)=database.withSession{implicit session:Session =>
    (for (c<-Topics if c.id === id)yield c.checkState ).update(checkState)
  }
  def modifyTopicTop(id:Long,isTop:Boolean)=database.withSession{implicit session:Session =>
    (for (c<-Topics if c.id === id)yield c.isTop ).update(isTop)
  }
  def modifyTopicBest(id:Long,isBest:Boolean)=database.withSession{implicit session:Session =>
    (for (c<-Topics if c.id === id)yield c.isBest ).update(isBest)
  }
  /* 统计 topic  */
  def countTopic=database.withSession{implicit session:Session =>
    Query(Topics.length).first()
  }
  def countTopic(time:Timestamp)=database.withSession{implicit session:Session =>
    Query(Topics.filter(_.addTime > time ).length).first()
  }
  /* 查找 topic ，分页显示*/
  def findTopics(currentPage: Int, pageSize: Int): Page[Topic] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.filter(_.checkState === 1).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.drop(startRow).take(pageSize)  if c.checkState === 1 ) yield c
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages)
  }

  /* user topics 分页显示*/
  def  findUserTopics(uid:Long,currentPage: Int = 1, pageSize: Int = 10): Page[Topic] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.filter(_.uid === uid).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.drop(startRow).take(pageSize) if c.uid===uid  ) yield c
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages)
  }


  /*查找*/
  def findAll(currentPage: Int , pageSize: Int): Page[Topic] = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.drop(startRow).take(pageSize)  ) yield c
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages)
  }
  /*search for 前端的square的 forum eatLocal*/
  def search(typeId:Int,text:String, currentPage: Int = 1, pageSize: Int = 10) = database.withSession {  implicit session:Session =>
    val totalRows=Query(Topics.filter(_.title like("%"+text+"%")).length).first()
    val totalPages=(totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-Topics.sortBy(c=>(c.isBest,c.addTime,c.discussNum.desc)).drop(startRow).take(pageSize)    if c.title.like("%"+text+"%") if c.checkState ===1   ) yield(c)
    //println(" q sql "+q.selectStatement)
    val topics:List[Topic]=  q.list()
    Page[Topic](topics,currentPage,totalPages)

  }


  /* find byid with user*/
  def findById(topicId:Long)  =  database.withSession {  implicit session:Session =>
    (for{
      c<-Topics.sortBy(_.addTime desc)
      u<-Users
      if c.id === topicId
      if c.uid === u.id
    }yield(u,c)).firstOption
  }


  /* topic discuss */
  def addDiscuss(discuss:TopicDiscuss) =database.withSession {  implicit session:Session =>
    TopicSQLDao.updateDiscussNum(discuss.topicId)
    TopicDiscusses.autoInc.insert(discuss)

  }
  def addDiscuss(uid:Long,topicId:Long,quoteContent:Option[String],content:String,checkState:Int) =database.withSession {  implicit session:Session =>
    TopicSQLDao.updateDiscussNum(topicId)
    TopicDiscusses.autoInc2.insert(uid,topicId,quoteContent,content,checkState)

  }

  def countDiscuss = database.withSession{implicit session:Session =>
    Query(TopicDiscusses.length).first()
  }
  def countDiscuss(time:Timestamp)=database.withSession{implicit session:Session =>
    Query(TopicDiscusses.filter(_.addTime > time ).length).first()
  }


  def deleteDiscuss(id:Long)=database.withSession {  implicit session:Session =>
    (for(c<-TopicDiscusses if c.id === id)yield c ).delete
  }

  def modifyReplyCheckState(id:Long,checkState:Int)=database.withSession{implicit session:Session =>
    (for (c<-TopicDiscusses if c.id === id)yield c.checkState ).update(checkState)
  }

  /*根据topic分类 分页显示*/
  def findDiscusses(topicId:Long,currentPage: Int, pageSize: Int): Page[TopicDiscuss] = database.withSession {  implicit session:Session =>
    val totalRows =Query(TopicDiscusses.filter(_.topicId === topicId).length).first
    val totalPages=(totalRows + pageSize - 1) / pageSize
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-TopicDiscusses.drop(startRow).take(pageSize) if c.topicId===topicId  ) yield c
    //println(" q sql "+q.selectStatement)
    val discusses:List[TopicDiscuss]=  q.list()
    Page[TopicDiscuss](discusses,currentPage,totalPages)
  }
   /* 获取比较全的 user 和 discuss */
  def findTopicDiscusses(topicId:Long,currentPage:Int,pageSize:Int):Page[(String,String,Long,Long,Option[String],String,Int,Timestamp)]  = database.withSession {  implicit session:Session =>
    val totalRows =Query(TopicDiscusses.filter(_.topicId === topicId).length).first
    val totalPages=(totalRows + pageSize - 1) / pageSize
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for{
      c<-TopicDiscusses
      u<-Users
     if c.uid === u.id
     if c.topicId===topicId
     } yield(u.name,u.pic,u.id,c.id,c.quoteContent.?,c.content,c.checkState,c.addTime)
    val discusses:List[(String,String,Long,Long,Option[String],String,Int,Timestamp)]=  q.sortBy(_._8 desc).drop(startRow).take(pageSize).list()
    Page[(String,String,Long,Long,Option[String],String,Int,Timestamp)](discusses,currentPage,totalPages)

  }

  def findAllDiscusses(currentPage: Int, pageSize: Int): Page[TopicDiscuss] = database.withSession {  implicit session:Session =>
    val totalRows =Query(TopicDiscusses.length).first
    val totalPages=(totalRows + pageSize - 1) / pageSize
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
    val q=  for(c<-TopicDiscusses.drop(startRow).take(pageSize)  ) yield c
    //println(" q sql "+q.selectStatement)
    val discusses:List[TopicDiscuss]=  q.list()
    Page[TopicDiscuss](discusses,currentPage,totalPages)

  }

  def filterTopics(name:Option[String],checkState:Option[Int],typeId:Option[Int],isBest:Option[Boolean],isTop:Option[Boolean],currentPage:Int,pageSize:Int)= database.withSession {  implicit session:Session =>
     var query = for(c<-Topics)yield c
     if(!name.isEmpty) query = query.filter(_.title like "%"+name.get+"%")
     if(!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
     if(!typeId.isEmpty) query = query.filter(_.typeId === typeId.get)
     if(!isTop.isEmpty) query = query.filter(_.isTop === isTop.get)
     if(!isBest.isEmpty) query = query.filter(_.isBest === isBest.get)
    query = query.sortBy(_.id desc)
     //println("sql " +query.selectStatement)
     val totalRows=query.list().length
     val totalPages=(totalRows + pageSize - 1) / pageSize
     val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
     val topics:List[Topic]= query.drop(startRow).take(pageSize).list()
     Page[Topic](topics,currentPage,totalPages)
  }



  def filterDiscusses(checkState:Option[Int],currentPage:Int,pageSize:Int)= database.withSession {  implicit session:Session =>
       var query = for(c<-TopicDiscusses)yield c
       if(!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
       query = query.sortBy(_.id desc)
       //println("sql " +query.selectStatement)
       val totalRows=query.list().length
       val totalPages=(totalRows + pageSize - 1) / pageSize
       val startRow= if (currentPage < 1 || currentPage > totalPages ) { 0 } else {(currentPage - 1) * pageSize }
       val discusses:List[TopicDiscuss]= query.drop(startRow).take(pageSize).list()
       Page[TopicDiscuss](discusses,currentPage,totalPages)
  }*/
}
