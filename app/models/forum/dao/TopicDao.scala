package models.forum.dao

import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.db.DB
import scala.slick.driver.MySQLDriver.simple._
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import models.user._
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import models.forum._

/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
 * Since:13-1-13下午12:35
 * ModifyTime :
 * ModifyContent:
 * http://www.hiwowo.com/
 * add  delete update find  filter list
 *
 */
object TopicDao {

  lazy val database = Database.forDataSource(DB.getDataSource())
  val topics = TableQuery[Topics]
  val topicDiscusses = TableQuery[TopicDiscusses]
  val users = TableQuery[Users]

  /* 增加一个话题 */
  def addTopic(uid: Long, title: String, content: String, intro: String, pics: String, typeId: Int, checkState: Int) = database.withDynSession {
    val topicsAutoInc = topics.map(t => (t.uid, t.title, t.content, t.intro, t.pics, t.typeId, t.checkState)) returning topics.map(_.id) into {
      case (_, id) => id
    }
    topicsAutoInc.insert(uid, title, content, intro, pics, typeId, checkState)
  }

  /*删除topic的同时，需要把topic 下的reply 给删除*/
  def deleteTopic(id: Long) = database.withDynSession {
    (for (c <- topics if c.id === id) yield c).delete
    (for (c <- topicDiscusses if c.topicId === id) yield c).delete
  }

  /* 修改 topic */
  def modifyTopic(topic: Topic) = database.withDynSession {
      (for (c <- topics if c.id === topic.id) yield c).update(topic)
  }

  def modifyTopicCheckState(id: Long, checkState: Int) = database.withDynSession {
    (for (c <- topics if c.id === id) yield c.checkState).update(checkState)
  }

  def modifyTopicTop(id: Long, isTop: Boolean) = database.withDynSession {
    (for (c <- topics if c.id === id) yield c.isTop).update(isTop)
  }

  def modifyTopicBest(id: Long, isBest: Boolean) = database.withDynSession {
    (for (c <- topics if c.id === id) yield c.isBest).update(isBest)
  }

  /* 统计 topic  */
  def countTopic = database.withDynSession {
    Query(topics.length).first()
  }

  def countTopic(time: Timestamp) = database.withDynSession {
    Query(topics.filter(_.addTime > time).length).first()
  }

  /* find byid with user*/
  def findById(topicId: Long): (Topic, User) = database.withDynSession {
    (for {
      c <- topics
      u <- users
      if c.id === topicId
      if c.uid === u.id
    } yield (c, u)).first
  }
  /* 查找 topic ，分页显示*/
  def findTopics(currentPage: Int, pageSize: Int): Page[Topic] = database.withDynSession {
    val totalRows = Query(topics.filter(_.checkState === 1).length).first()
    val totalPages = (totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for (c <- topics.drop(startRow).take(pageSize) if c.checkState === 1) yield c
    //println(" q sql "+q.selectStatement)
    // val topics:List[Topic]=  q.list()
    Page[Topic](q.list(), currentPage, totalPages)
  }

  /* user topics 分页显示*/
  def findUserTopics(uid: Long, currentPage: Int = 1, pageSize: Int = 10): Page[Topic] = database.withDynSession {
    val totalRows = Query(topics.filter(_.uid === uid).length).first()
    val totalPages = (totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for (c <- topics.drop(startRow).take(pageSize) if c.uid === uid) yield c
    //println(" q sql "+q.selectStatement)
    // val topics:List[Topic]=  q.list()
    Page[Topic](q.list(), currentPage, totalPages)
  }


  /*查找*/
  def findAll(currentPage: Int, pageSize: Int): Page[Topic] = database.withDynSession {
    val totalRows = Query(topics.length).first()
    val totalPages = (totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for (c <- topics.drop(startRow).take(pageSize)) yield c
    //println(" q sql "+q.selectStatement)
    //  val topics:List[Topic]=  q.list()
    Page[Topic](q.list(), currentPage, totalPages)
  }

  /*search for 前端的square的 forum eatLocal*/
  def search( title:String,currentPage: Int = 1, pageSize: Int = 10) = database.withDynSession {
    val totalRows = Query(topics.filter(_.title like ("%" + title + "%")).length).first()
    val totalPages = (totalRows + pageSize - 1) / pageSize
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for (c <- topics.sortBy(c => (c.isBest, c.addTime, c.discussNum.desc)).drop(startRow).take(pageSize) if c.title.like("%" + title + "%") if c.checkState === 1) yield (c)
    //println(" q sql "+q.selectStatement)
    //  val topics:List[Topic]=  q.list()
    Page[Topic](q.list(), currentPage, totalPages)

  }

  def filterTopics(typeId:Int,sortBy:String,currentPage:Int =1,pageSize:Int =10) =  database.withDynSession {
    var query = for (c <- topics) yield c

    if(typeId != -1) query = query.filter(_.typeId === typeId)
    if(sortBy == "new") query = query.sortBy(_.addTime desc)
    if(sortBy == "discuss") query = query.sortBy(_.discussNum desc)
    if(sortBy == "love") query = query.sortBy(_.loveNum desc)
    query.sortBy(_.isBest desc)
    //println("sql " +query.selectStatement)
    val totalRows = query.list().length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val ts: List[Topic] = query.drop(startRow).take(pageSize).list()
    Page[Topic](ts, currentPage, totalPages)

  }



  /* topic discuss */

  def addDiscuss(uid: Long, topicId: Long, quoteContent: Option[String], content: String, checkState: Int) = database.withDynSession {
    TopicSQLDao.updateDiscussNum(topicId)
    val discussesAutoInc = topicDiscusses.map(t => (t.uid, t.topicId, t.quoteContent, t.content, t.checkState)) returning topics.map(_.id) into {
      case (_, id) => id
    }
    discussesAutoInc.insert(uid, topicId, quoteContent.getOrElse(""), content, checkState)

  }

  def countDiscuss = database.withDynSession {
    Query(topicDiscusses.length).first()
  }

  def countDiscuss(time: Timestamp) = database.withDynSession {
    Query(topicDiscusses.filter(_.addTime > time).length).first()
  }


  def deleteDiscuss(id: Long) = database.withDynSession {
    (for (c <- topicDiscusses if c.id === id) yield c).delete
  }

  def modifyDiscussCheckState(id: Long, checkState: Int) = database.withDynSession {
    (for (c <- topicDiscusses if c.id === id) yield c.checkState).update(checkState)
  }

  /*根据topic分类 分页显示*/
  def findDiscusses(topicId: Long, currentPage: Int, pageSize: Int): Page[TopicDiscuss] = database.withDynSession {
    val totalRows = Query(topicDiscusses.filter(_.topicId === topicId).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for (c <- topicDiscusses.drop(startRow).take(pageSize) if c.topicId === topicId) yield c
    //println(" q sql "+q.selectStatement)
    val discusses: List[TopicDiscuss] = q.list()
    Page[TopicDiscuss](discusses, currentPage, totalPages)
  }

  /* 获取比较全的 user 和 discuss */
  def findTopicDiscusses(topicId: Long, currentPage: Int, pageSize: Int): Page[(String, String, Long, Long, Option[String], String, Int, Timestamp)] = database.withDynSession {
    val totalRows = Query(topicDiscusses.filter(_.topicId === topicId).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for {
      c <- topicDiscusses
      u <- users
      if c.uid === u.id
      if c.topicId === topicId
    } yield (u.name, u.pic, u.id, c.id, c.quoteContent.?, c.content, c.checkState, c.addTime)
    val discusses: List[(String, String, Long, Long, Option[String], String, Int, Timestamp)] = q.sortBy(_._8 desc).drop(startRow).take(pageSize).list()
    Page[(String, String, Long, Long, Option[String], String, Int, Timestamp)](discusses, currentPage, totalPages)

  }

  def findAllDiscusses(currentPage: Int, pageSize: Int): Page[TopicDiscuss] = database.withDynSession {
    val totalRows = Query(topicDiscusses.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    //println("totalPages " +totalPages)
    /*获取分页起始行*/
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val q = for (c <- topicDiscusses.drop(startRow).take(pageSize)) yield c
    //println(" q sql "+q.selectStatement)
    val discusses: List[TopicDiscuss] = q.list()
    Page[TopicDiscuss](discusses, currentPage, totalPages)

  }

  def filterTopics(title: Option[String], checkState: Option[Int], typeId: Option[Int], isBest: Option[Boolean], isTop: Option[Boolean], currentPage: Int, pageSize: Int) = database.withDynSession {
    var query = for (c <- topics) yield c
      if(!title.isEmpty) query = query.filter(_.title like "%"+title.get+"%")
      if(!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
      if(!typeId.isEmpty) query = query.filter(_.typeId === typeId.get)
      if(!isTop.isEmpty) query = query.filter(_.isTop === isTop.get)
      if(!isBest.isEmpty) query = query.filter(_.isBest === isBest.get)
     query = query.sortBy(_.id desc)
    //println("sql " +query.selectStatement)
    val totalRows = query.list().length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val ts: List[Topic] = query.drop(startRow).take(pageSize).list()
    Page[Topic](ts, currentPage, totalPages)
  }


  def filterDiscusses(checkState: Option[Int], currentPage: Int, pageSize: Int) = database.withDynSession {
    var query = for (c <- topicDiscusses) yield c
    if (!checkState.isEmpty) query = query.filter(_.checkState === checkState.get)
    query = query.sortBy(_.id desc)
    //println("sql " +query.selectStatement)
    val totalRows = query.list().length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val discusses: List[TopicDiscuss] = query.drop(startRow).take(pageSize).list()
    Page[TopicDiscuss](discusses, currentPage, totalPages)
  }
}
