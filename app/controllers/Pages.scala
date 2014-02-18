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

  def index = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.index(user))
  }

  def sites = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.sites(user))
  }



  def forum(typeId:Int,sortBy:String,p:Int,size:Int) = Users.UserAction{ user => implicit request =>
      val page = TopicDao.filterTopics(typeId,sortBy,p,size)
    Ok(views.html.pages.forum(user,typeId,sortBy,page))
  }


}
