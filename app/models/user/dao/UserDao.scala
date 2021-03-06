package models.user.dao

import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import models.user._
import play.api.db.slick.Config.driver.simple._
import models.diagram.{DiagramTable, Diagram}
import models.label._
import java.util.Date
import models.user.UserSubscribe
import models.user.UserStatic
import models.diagram.Diagram
import models.Page
import models.user.UserFollow
import models.user.UserCollect
import models.user.User
import models.label.Label
import models.user.UserProfile

/*
*
* Dao 访问数据，可能来自数据库 也可能来自缓存
* */

object UserDao {

  val users = TableQuery[UserTable]
  val userStatics = TableQuery[UserStaticTable]
  val userProfiles = TableQuery[UserProfileTable]
  val userCollects = TableQuery[UserCollectTable]
  val userLoves = TableQuery[UserLoveTable]
  val userFollows = TableQuery[UserFollowTable]
  val userSubscribes = TableQuery[UserSubscribeTable]
  val userRecords = TableQuery[UserRecordTable]
  val diagrams = TableQuery[DiagramTable]
  val labels = TableQuery[LabelTable]
  val labelDiagrams = TableQuery[LabelDiagramTable]

  /* 验证 */
  def authenticate(email: String, password: String): Option[User] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val user = (for (u <- users if u.email === email && u.password === Codecs.sha1("hiwowo" + password)) yield u).firstOption
    if (!user.isEmpty) {
      Cache.set("user_" + user.get.id.get, user.get)
    }
    user

  }

  /*find By id*/
  def findById(uid: Long): User = play.api.db.slick.DB.withSession{ implicit session:Session =>
    Cache.getOrElse[User]("user_" + uid) {
      val user = (for (u <- users if u.id === uid) yield u).firstOption
      if (!user.isEmpty) {
        Cache.set("user_" + uid, user.get)
      }
      user.get
    }
  }

  /*find by email */
  def findByEmail(email: String): Option[User] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val user = (for (u <- users if u.email === email) yield u).firstOption
    if (!user.isEmpty) {
      Cache.set("user_" + user.get.id.get, user.get)
    }
    user
  }



  /* 检查第三方用户是否存在 */
  /*查找sns 帐号的用户*/
  def checkSnsUser(comeFrom: Int, openId: String): Option[User] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val user = (for (u <- users if u.comeFrom === comeFrom && u.openId === openId) yield u).firstOption
    if (!user.isEmpty) {
      Cache.set("user_" + user.get.id.get, user)
    }
    user
  }

  /* 第三方用户初次登陆 */
  def addSnsUser(name: String, comeFrom: Int, openId: String, pic: String, inviteId: Long):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val usersAutoInc = users.map(u => (u.name, u.comeFrom, u.openId, u.pic,u.registTime)) returning users.map(_.id) into {
      case (_, id) => id
    }
    val id = usersAutoInc.insert(name, comeFrom, openId, pic,new Timestamp(System.currentTimeMillis()))
    userStatics.map(u => u.uid).insert(id)
    userProfiles.map(u => (u.uid, u.inviteId, u.loginTime,u.loginIP)).insert(id, inviteId, new Timestamp(System.currentTimeMillis()), "sns")
    id
  }

  /*用户通过网站注册 * */
  def addHiwowoUser(name: String, password: String, email: String, inviteId: Long, ip: String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val usersAutoInc = users.map(u => (u.name, u.password, u.email,u.registTime)) returning users.map(_.id) into {
      case (_, id) => id
    }
    val id = usersAutoInc.insert(name, Codecs.sha1("hiwowo" + password), email,new Timestamp(System.currentTimeMillis()))

    userStatics.map(u => u.uid).insert(id)
    userProfiles.map(u => (u.uid, u.inviteId, u.loginTime, u.loginIP)).insert(id, inviteId, new Timestamp(System.currentTimeMillis()), ip)
    id
  }

  /*修改密码*/
  def modifyPassword(uid: Long, password: String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield c.password).update(Codecs.sha1("hiwowo" + password))
  }

  /*保存地址*/
  def modifyAddr(uid: Long, receiver: String, province: String, city: String, street: String, postCode: String, phone: String) = play.api.db.slick.DB.withSession{ implicit session:Session =>

    (for (c <- userProfiles if c.uid === uid) yield (c.receiver, c.province, c.city, c.street, c.postCode, c.phone)).update((receiver, province, city, street, postCode, phone))
  }

  /*修改user pic*/
  def modifyPic(uid: Long, pic: String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield c.pic).update(pic)
  }

  /*修改 user email*/
  def modifyEmail(uid: Long, email: String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield c.email).update(email)
  }

  /*保存基本信息*/
  def modifyBase(uid: Long, name: String, email: String, intro: String, gender: Int, birth: String, blog: String, qq: String, weixin: String) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield (c.name, c.email, c.intro,c.weixin)).update((name, email, intro, weixin))
    (for (c <- userProfiles if c.uid === uid) yield (c.birth, c.gender, c.blog, c.qq)).update((birth, gender, blog,qq))
  }

  /* 修改用户状态 */
  def modifyStatus(uid: Long, status: Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
      Cache.remove("user_" + uid)
      (for (u <- users if u.id === uid) yield u.status).update(status)
  }

  /*  list  user */
  def findAll(currentPage: Int, pageSize: Int): Page[User] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(users.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    Page[User](users.drop(startRow).take(pageSize).list, currentPage, totalPages)
  }


  /* find by id with profile */
  def findWithProfile(uid: Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for {
      u <- users
      p <- userProfiles
      if u.id === p.uid
      if u.id === uid} yield (u, p)).first
  }

  /* find profile*/
  def findProfile(uid: Long): UserProfile = play.api.db.slick.DB.withSession{ implicit session:Session =>
    {
      for (c <- userProfiles if c.uid === uid) yield c
    }.first
  }
  def findUserWithStatic(uid: Long): (User,UserStatic) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val query = for {
      c <- users
      s <- userStatics
      if c.id === s.uid
      if c.id === uid
    } yield (c,s)
    //   println(query.selectStatement)
    query.first
  }

  def findUser(uid:Long):(User,UserProfile,UserStatic) =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for {
        u <- users
        p <- userProfiles
        s <- userStatics
      if u.id === p.uid
      if u.id === s.uid
      if u.id === uid
    }yield(u,p,s) ).first
  }
  /*用户筛选*/
  def filterUsers(name: Option[String], status: Option[Int],title: Option[String], comeFrom: Option[Int], startTime:Option[Date],endTime:Option[Date], currentPage: Int, pageSize: Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    var query = for (u <- users) yield u
    if (!name.isEmpty) query = query.filter(_.name like "%" + name.get + "%")
    if (!status.isEmpty) query = query.filter(_.status === status.get)
    if (!title.isEmpty) query = query.filter(_.title like "%" + title.get + "%")
    if (!comeFrom.isEmpty) query = query.filter(_.comeFrom === comeFrom.get)
    if(!startTime.isEmpty) query = query.filter(_.registTime > new Timestamp(startTime.get.getTime))
    if(!endTime.isEmpty) query = query.filter(_.registTime < new Timestamp(endTime.get.getTime))
    //println("sql " +query.selectStatement)
    val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val groups: List[User] = query.drop(startRow).take(pageSize).list
    Page[User](groups, currentPage, totalPages)
  }



  /* user collect */
  def addUserCollect(uid:Long,typeId:Int,collectId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val userCollectAutoInc = userCollects.map(c => (c.uid, c.typeId, c.collectId)) returning userCollects.map(_.id) into {
      case (_, id) => id
    }
    userCollectAutoInc.insert(uid,typeId,collectId)
  }

  def findUserCollect(uid:Long,typeId:Int,collectId:Long):Option[UserCollect] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( c<-userCollects if c.uid === uid  if c.typeId === typeId if c.collectId === collectId ) yield c ).firstOption
  }

  def deleteUserCollect(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c<- userCollects if c.id === id)yield c ).delete
  }

  def deleteUserCollect(uid:Long,typeId:Int,collectId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c<- userCollects if c.uid === uid if c.typeId === typeId if c.collectId === collectId )yield c ).delete
  }

  /* 查找用户收集的 */
  def findCollectDiagrams(uid:Long,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(userCollects.filter(_.uid === uid).filter(_.typeId === 0).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
    val list = (for{
      u<-userCollects
      c<-diagrams
      if u.collectId === c.id
      if u.uid === uid
      if u.typeId === 0
    } yield c ).drop(startRow).take(pageSize).list

    Page[Diagram](list,currentPage,totalPages)

  }

  /* user subscribe 用户订阅 */
  def addUserSubscribe(uid:Long,labelId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val userSubscribeAutoInc = userSubscribes.map(c => (c.uid, c.labelId)) returning userCollects.map(_.id) into {
      case (_, id) => id
    }
    userSubscribeAutoInc.insert(uid,labelId)
  }

  def findUserSubscribe(uid:Long,labelId:Long):Option[UserSubscribe] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c <- userSubscribes if c.uid === uid if c.labelId === labelId )yield c ).firstOption
  }

  def deleteUserSubscribe(uid:Long,labelId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c <- userSubscribes if c.uid === uid if c.labelId === labelId )yield c ).delete
  }
  /* 查找订阅的label */
  def findSubscribeLabels(uid:Long):List[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for{
      u <- userSubscribes
      c <- labels
      if u.labelId === c.id
      if u.uid === uid
    } yield c ).list
  }

  def findSubscribeDiagrams(uid:Long,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
      val query = for{
      s <- userSubscribes
      ld <- labelDiagrams
      d <- diagrams
      if  s.labelId === ld.labelId
      if  ld.diagramId === d.id
      if s.uid === uid
    }yield d

    val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }

    Page[Diagram](query.drop(startRow).take(pageSize).list,currentPage,totalPages)
  }

  /* user subscribe 用户关注 */
  def addUserFollow(uid:Long,followId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val userFollowAutoInc = userFollows.map(c => (c.uid, c.followId)) returning userFollows.map(_.id) into {
      case (_, id) => id
    }
    userFollowAutoInc.insert(uid,followId)
  }

  def findUserFollow(uid:Long,followId:Long):Option[UserFollow] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c <- userFollows if c.uid === uid if c.followId === followId )yield c ).firstOption
  }

  def deleteUserFollow(uid:Long,followId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c <- userFollows if c.uid === uid if c.followId === followId )yield c ).delete
  }

 def findUserFollows(uid:Long,currentPage:Int,pageSize:Int):Page[User] = play.api.db.slick.DB.withSession{ implicit session:Session =>
   val totalRows = Query(userFollows.filter(_.uid === uid).length).first
   val totalPages = (totalRows + pageSize - 1) / pageSize
   val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
   val list = (for{
          c <- userFollows
          u <- users
         if c.uid === u.id
         if c.uid === uid
   }yield u).drop(startRow).take(pageSize).list
   Page[User](list,currentPage,totalPages)
 }

  def findUserFans(uid:Long,currentPage:Int,pageSize:Int):Page[User] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(userFollows.filter(_.followId === uid).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
    val list = (for{
      c <- userFollows
      u <- users
      if c.followId === u.id
      if c.followId === uid
    }yield u).drop(startRow).take(pageSize).list
    Page[User](list,currentPage,totalPages)
  }

  def vipAuth(uid:Long,weixin:String,intro:String)  = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-users if c.id === uid)yield (c.weixin,c.intro,c.status) ).update((weixin,intro,2))
  }
}
