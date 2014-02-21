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




  def forum(typeId:Int,sortBy:String,p:Int,size:Int) = Users.UserAction{ user => implicit request =>
      val page = TopicDao.filterTopics(typeId,sortBy,p,size)
    Ok(views.html.pages.forum(user,typeId,sortBy,page))
  }


}
