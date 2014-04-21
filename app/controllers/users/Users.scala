package controllers.users

import play.api.mvc._
import play.api.libs.json.Json
import models.user._
import models.user.User
import models.user.dao.{UserSQLDao, UserDao}
import models.Page
import models.diagram.dao.DiagramSQLDao


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-19
 * Time: 上午9:37
 * ***********************
 * description:用户页面显示，采用类似于权限控制的规则，对外显示的页面和用户能够看到的页面一致，只是在局部上 某些功能有额外的处理
 */

object Users extends Controller {



  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def UserAction(f:Option[User] => Request[AnyContent] => Result) = {
      Action{ request =>
       val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
        f(user)(request)
      }
  }




  /*user 首页*/
  def home(id:Long) =UserAction { user => implicit request =>
   val author = UserDao.findById(id)
    Ok(views.html.users.home(user,author) )
  }

  /* 我的图说 */
  def diagrams(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.diagrams(user,author) )
  }

  /* 我收藏的 */
  def collects(id:Long,p:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.collects(user,author) )
  }
  /* 我订阅的 */
  def subscribes(id:Long,p:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.subscribes(user,author) )
  }

  /* 我讨论的 */
  def discusses(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.discusses(user,author) )
   
  }

  /* 我喜欢的 */
  def loves(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.loves(user,author) )
  }

  /* 我关注的 */
  def follow(id:Long,p:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.follow(user,author) )
  }
  def fans(id:Long,p:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.fans(user,author) )
  }

  /* 我的动态 */
  def records(id:Long,p:Int)  = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.records(user,author) )
  }


  /* 添加收藏 */
   def saveCollect = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止评论"))
    else {
      val collectId = (request.body \ "collectId").asOpt[Long]
      val typeId = (request.body \ "typeId").asOpt[Int]
      if(collectId.isEmpty || collectId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"collect id is not correct"))
      }else{
        val userCollect = UserDao.findUserCollect(collectId.get)
        if(!userCollect.isEmpty){
          Ok(Json.obj("code" -> "102", "message" ->"exists"))
        }else{
          UserDao.addUserCollect(user.get.id.get,typeId.getOrElse(0),collectId.get) // 在user collect 中记录
          UserSQLDao.updateCollectNum(user.get.id.get,1)                             // update user_static 中的 collect num
        if(typeId.getOrElse(0)==0) { DiagramSQLDao.updateCollectNum(collectId.get,1) }           // 如果type id ==0,则update diagram 中的collect num
          Ok(Json.obj("code" -> "100", "message" ->"success"))
        }


      }
    }
  }

}
