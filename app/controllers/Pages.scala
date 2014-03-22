package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.forum.dao.TopicDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:17
 */
object Pages extends Controller {

  /* 首页 */
  def index = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.index(user))
  }

  /* 首页2 */
  def index2  = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.index2(user))
  }

   /* 图说 */
   def diagrams = Users.UserAction{ user => implicit request =>
     Ok(views.html.pages.diagrams(user))
   }
   /* 微信精选 */
   def weixin = Users.UserAction{ user => implicit request =>
     Ok(views.html.pages.weixin(user))
   }
  /* 发现更多 */
  def find = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.find(user))
  }

  /* 宠物乐园 */
  def pets = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.pets(user))
  }







}
