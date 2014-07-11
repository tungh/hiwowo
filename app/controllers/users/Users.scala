package controllers.users

import play.api.mvc._
import play.api.libs.json.Json
import models.user._
import models.user.User
import models.user.dao.{UserSQLDao, UserDao}
import models.Page
import models.diagram.dao.{DiagramDao, DiagramSQLDao}
import play.api.db.slick.DBAction


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-19
 * Time: 上午9:37
 * ***********************
 * description:用户页面显示，采用类似于权限控制的规则，对外显示的页面和用户能够看到的页面一致，只是在局部上 某些功能有额外的处理
 */

case class UserComponent(
                         user:User,
                         profile:UserProfile,
                         stat:UserStatic
                          )

object Users extends Controller {

 /* 欢迎新用户 */
 def welcome(uid:Long) = Users.UserAction{ user => implicit request =>
     Ok(views.html.users.welcome(user))
 }


  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def UserAction(f:Option[User] => Request[AnyContent] => Result) = {
    Action{ request =>
       val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
        f(user)(request)
      }
  }




  /*user 首页*/
  def home(uid:Long) =UserAction { user => implicit request =>
    Redirect(controllers.users.routes.Users.diagrams(uid,1))
  }

  /* 我的图说 */
  def diagrams(uid:Long,p:Int,size:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findUser(uid)
    val page = DiagramDao.findUserDiagrams(uid,p,10)
    Ok(views.html.users.diagrams(user,UserComponent(author._1,author._2,author._3),page))
  }

  /* 我收藏的 */
  def collects(uid:Long,p:Int,size:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findUser(uid)
    val page = UserDao.findCollectDiagrams(uid,p,size)
    Ok(views.html.users.collects(user,UserComponent(author._1,author._2,author._3),page))
  }
  /* 我订阅的 */
  def subscribes(uid:Long,p:Int,size:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findUser(uid)
    val page = UserDao.findSubscribeDiagrams(uid,p,size)
    Ok(views.html.users.subscribes(user,UserComponent(author._1,author._2,author._3),page))
  }


  /* 我关注的  翻页以后出来  */
  def follow(uid:Long,p:Int,size:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findUser(uid)
    val followPage = UserDao.findUserFollows(uid,p,size)
    val fansPage  = UserDao.findUserFans(uid,p,size)
    Ok(views.html.users.follow(user,UserComponent(author._1,author._2,author._3),followPage,fansPage) )
  }




  /* 添加收藏 */
   def addCollect = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止收藏"))
    else {
      val collectId = (request.body \ "collectId").asOpt[Long]
      val typeId = (request.body \ "typeId").asOpt[Int]
      if(collectId.isEmpty || collectId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"collect id is not correct"))
      }else{
        val userCollect = UserDao.findUserCollect(user.get.id.get,typeId.get,collectId.get)
        if(!userCollect.isEmpty){
          Ok(Json.obj("code" -> "101", "message" ->"exists"))
        }else{
          UserDao.addUserCollect(user.get.id.get,typeId.getOrElse(0),collectId.get) // 在user collect 中记录
          UserSQLDao.updateCollectNum(user.get.id.get,1)                             // update user_static 中的 collect num
        if(typeId.getOrElse(0)==0) { DiagramSQLDao.updateCollectNum(collectId.get,1) }           // 如果type id ==0,则update diagram 中的collect num
          Ok(Json.obj("code" -> "100", "message" ->"success"))
        }
      }
    }
  }
   /* 取消收藏 */
  def removeCollect = Action(parse.json){  implicit request =>
     val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
     if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
     else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止收藏"))
     else {
       val collectId = (request.body \ "collectId").asOpt[Long]
       val typeId = (request.body \ "typeId").asOpt[Int]
       if(collectId.isEmpty || collectId.getOrElse(0) ==0 ){
         Ok(Json.obj("code" -> "104", "message" ->"collect id is not correct"))
       }else{
         UserDao.deleteUserCollect(user.get.id.get,typeId.get,collectId.get)
         UserSQLDao.updateCollectNum(user.get.id.get,-1)
         Ok(Json.obj("code" -> "100", "message" ->"success"))
       }
     }
  }

  /* 添加订阅 */
  def addSubscribe = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止收藏"))
    else {
      val labelId = (request.body \ "labelId").asOpt[Long]
      if(labelId.isEmpty || labelId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"subscribe id is not correct"))
      }else{
        val userSubscribe = UserDao.findUserSubscribe(user.get.id.get,labelId.get)
        if(!userSubscribe.isEmpty){
          Ok(Json.obj("code" -> "101", "message" ->"exists"))
        }else{
          UserDao.addUserSubscribe(user.get.id.get,labelId.get) // 在user subscribe 中记录
          UserSQLDao.updateSubscribeNum(user.get.id.get,1)                             // update user_static 中的 subscribe num
          Ok(Json.obj("code" -> "100", "message" ->"success"))
        }
      }
    }
  }
  /* 取消订阅 */
  def removeSubscribe = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止收藏"))
    else {
      val labelId = (request.body \ "labelId").asOpt[Long]
      if(labelId.isEmpty || labelId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"subscribe id is not correct"))
      }else{
        UserDao.deleteUserSubscribe(user.get.id.get,labelId.get)
        UserSQLDao.updateSubscribeNum(user.get.id.get,-1)
        Ok(Json.obj("code" -> "100", "message" ->"success"))
      }
    }
  }

  /* 添加关注  */
  def addFollow = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止收藏"))
    else {
      val followId = (request.body \ "followId").asOpt[Long]
      if(followId.isEmpty || followId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"follow id is not correct"))
      }else{
        val userFollow = UserDao.findUserFollow(user.get.id.get,followId.get)
        if(!userFollow.isEmpty){
          Ok(Json.obj("code" -> "101", "message" ->"exists"))
        }else{
          UserDao.addUserFollow(user.get.id.get,followId.get) // 在user follow 中记录
          UserSQLDao.updateFollowNum(user.get.id.get,1)                             // update user_static 中的 follow num
          UserSQLDao.updateFansNum(followId.get,1)
          Ok(Json.obj("code" -> "100", "message" ->"success"))
        }
      }
    }
  }
  /* 取消关注 */
  def removeFollow = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止收藏"))
    else {
      val followId = (request.body \ "followId").asOpt[Long]
      if(followId.isEmpty || followId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"follow id is not correct"))
      }else{
        UserDao.deleteUserFollow(user.get.id.get,followId.get)
        UserSQLDao.updateFollowNum(user.get.id.get,-1)
        UserSQLDao.updateFansNum(user.get.id.get,-1)
        Ok(Json.obj("code" -> "100", "message" ->"success"))
      }
    }
  }
}
