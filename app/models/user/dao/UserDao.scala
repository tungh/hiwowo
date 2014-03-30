package models.user.dao

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

/*
*
* Dao 访问数据，可能来自数据库 也可能来自缓存
* */

object UserDao {

  /*从connection pool 中 获取jdbc的connection*/
  implicit val database = Database.forDataSource(DB.getDataSource())
  val users = TableQuery[Users]
  val userStatics = TableQuery[UserStatics]
  val userProfiles = TableQuery[UserProfiles]

  /* 验证 */
  def authenticate(email: String, password: String): Option[User] = database.withDynSession {
    val user = (for (u <- users if u.email === email && u.password === Codecs.sha1("hiwowo" + password)) yield u).firstOption
    if (!user.isEmpty) {
      Cache.set("user_" + user.get.id.get, user.get)
    }
    user

  }

  /*find By id*/
  def findById(uid: Long): User = database.withDynSession {
    Cache.getOrElse[User]("user_" + uid) {
      val user = (for (u <- users if u.id === uid) yield u).firstOption
      if (!user.isEmpty) {
        Cache.set("user_" + uid, user.get)
      }
      user.get
    }
  }

  /*find by email */
  def findByEmail(email: String): Option[User] = database.withDynSession {
    val user = (for (u <- users if u.email === email) yield u).firstOption
    if (!user.isEmpty) {
      Cache.set("user_" + user.get.id.get, user.get)
    }
    user
  }

  def findUserWithStatic(uid: Long): (User, UserStatic) = database.withDynSession {
    val query = for {
      c <- users
      s <- userStatics
      if c.id === s.uid
      if c.id === uid
    } yield (c, s)
 //   println(query.selectStatement)
    query.first
  }


  /* count user */
  def countUser = database.withDynSession {
    Query(users.length).first
  }

  def countUser(time: Timestamp) = database.withDynSession {
    Query(users.filter(_.modifyTime > time).length).first
  }

  def countUser(days: Int): Int = database.withDynSession {
    val now = new Timestamp(System.currentTimeMillis())
    val before = new Timestamp(now.getTime - 1000 * 60 * 60 * 24 * days)

    Query(users.filter(_.modifyTime between(before, now)).length).first

  }

  /* 检查第三方用户是否存在 */
  /*查找sns 帐号的用户*/
  def checkSnsUser(comeFrom: Int, openId: String): Option[User] = database.withDynSession {
    val user = (for (u <- users if u.comeFrom === comeFrom && u.openId === openId) yield u).firstOption
    if (!user.isEmpty) {
      Cache.set("user_" + user.get.id.get, user)
    }
    user
  }

  /* 第三方用户初次登陆 */
  def addSnsUser(name: String, comeFrom: Int, openId: String, pic: String, inviteId: Long):Long = database.withDynSession {
    val usersAutoInc = users.map(u => (u.name, u.comeFrom, u.openId, u.pic)) returning users.map(_.id) into {
      case (_, id) => id
    }
    val id = usersAutoInc.insert(name, comeFrom, openId, pic)
    userStatics.map(u => u.uid).insert(id)
    userProfiles.map(u => (u.uid, u.inviteId, u.loginTime, u.registTime, u.loginIP)).insert(id, inviteId, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "sns")
    id
  }

  /*用户通过网站注册 * */
  def addHiwowoUser(name: String, password: String, email: String, inviteId: Long, ip: String) = database.withDynSession {
    val usersAutoInc = users.map(u => (u.name, u.password, u.email)) returning users.map(_.id) into {
      case (_, id) => id
    }
    val id = usersAutoInc.insert(name, Codecs.sha1("hiwowo" + password), email)

    userStatics.map(u => u.uid).insert(id)
    userProfiles.map(u => (u.uid, u.inviteId, u.loginTime, u.registTime, u.loginIP)).insert(id, inviteId, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), ip)
    id
  }

  /*修改密码*/
  def modifyPassword(uid: Long, password: String) = database.withDynSession {
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield c.password).update(Codecs.sha1("hiwowo" + password))
  }

  /*保存地址*/
  def modifyAddr(uid: Long, receiver: String, province: String, city: String, street: String, postCode: String, phone: String) = database.withDynSession {

    (for (c <- userProfiles if c.uid === uid) yield (c.receiver, c.province, c.city, c.street, c.postCode, c.phone)).update((receiver, province, city, street, postCode, phone))
  }

  /*修改user pic*/
  def modifyPic(uid: Long, pic: String) = database.withDynSession {
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield c.pic).update(pic)
  }

  /*修改 user email*/
  def modifyEmail(uid: Long, email: String) = database.withDynSession {
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield c.email).update(email)
  }

  /*保存基本信息*/
  def modifyBase(uid: Long, name: String, email: String, intro: String, gender: Int, birth: String, blog: String, qq: String, weixin: String) = database.withDynSession {
    Cache.remove("user_" + uid)
    (for (c <- users if c.id === uid) yield (c.name, c.email, c.intro,c.weixin)).update((name, email, intro, weixin))
    (for (c <- userProfiles if c.uid === uid) yield (c.birth, c.gender, c.blog, c.qq)).update((birth, gender, blog,qq))
  }

  /* 修改用户状态 */
  def modifyStatus(uid: Long, status: Int) = database.withDynSession {
      Cache.remove("user_" + uid)
      (for (u <- users if u.id === uid) yield u.status).update(status)
  }

  /*  list  user */
  def findAll(currentPage: Int, pageSize: Int): Page[User] = database.withDynSession {
    val totalRows = Query(users.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    Page[User](users.drop(startRow).take(pageSize).list(), currentPage, totalPages)
  }


  /* find by id with profile */
  def findWithProfile(uid: Long) = database.withDynSession {
    (for {
      u <- users
      p <- userProfiles
      if u.id === p.uid
      if u.id === uid} yield (u, p)).first
  }

  /* find profile*/
  def findProfile(uid: Long): UserProfile = database.withDynSession {
    {
      for (c <- userProfiles if c.uid === uid) yield c
    }.first
  }

  /*用户筛选*/
  def filterUsers(name: Option[String], status: Option[Int],title: Option[String], comeFrom: Option[Int], creditsOrder: String, addTimeOrder: String, currentPage: Int, pageSize: Int) = database.withDynSession {
    var query = for (u <- users) yield u
    if (!name.isEmpty) query = query.filter(_.name like "%" + name.get + "%")
    if (!status.isEmpty) query = query.filter(_.status === status.get)
    if (!title.isEmpty) query = query.filter(_.title like "%" + title.get + "%")
    if (!comeFrom.isEmpty) query = query.filter(_.comeFrom === comeFrom.get)
    if (addTimeOrder == "desc") query = query.sortBy(_.id desc) else query = query.sortBy(_.id asc)
    if (creditsOrder == "desc") query = query.sortBy(_.credits desc) else query = query.sortBy(_.credits asc)
    //println("sql " +query.selectStatement)
    val totalRows = query.list().length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val groups: List[User] = query.drop(startRow).take(pageSize).list()
    Page[User](groups, currentPage, totalPages)
  }


}
