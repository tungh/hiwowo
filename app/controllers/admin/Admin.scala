package controllers.admin

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import models.user.User
import models.user.dao.UserDao


/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-10-3
 * Time: 下午4:08
 * Email:zuosanshao@qq.com
 */

object Admin extends Controller {




  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def AdminAction(f: User => Request[AnyContent] => Result) = {
    Action {
      request =>
        val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
        if (user.isEmpty) { Redirect(controllers.users.routes.UsersRegLogin.login()) }
        else if(user.get.isAdmin == 0){
             Ok("不是管理员账号")
        } else { f(user.get)(request) }
    }
  }



  /* 用户退出  清除缓存*/
  def logout = Action {  implicit request =>
      if (!session.get("user").isEmpty) {
        Cache.remove(session.get("user").get)
      }
      Redirect("/").withNewSession
  }




  /*admin 首页*/
  def index = AdminAction { user => implicit request =>

      Ok(views.html.admin.admin.index(user))
  }

  /*缓存管理*/
  def cache = AdminAction{    user => implicit request =>
      Ok(views.html.admin.admin.cache(user))
  }



  /*推送消息、信息管理*/
  def pushMsg = AdminAction{    user => implicit request =>
    Ok("向用户推送消息 todo ")
  }

  def check =AdminAction{   user => implicit request =>
    Ok("主要检测是否有恶意用户 todo ")
  }





}
