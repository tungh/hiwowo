package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users

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

  def forum = Action{
    Ok(views.html.pages.forum())
  }



}
