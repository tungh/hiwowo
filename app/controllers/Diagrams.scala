package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午11:37
 */
object  Diagrams extends Controller {

  /* 图说 */
  def diagram(id:Long) = Users.UserAction{ user => implicit request =>
    Ok(views.html.diagrams.diagram(user))
  }

  /* editor */
  def edit(id:Long) = Users.UserAction{ user => implicit request =>
    Ok(views.html.diagrams.edit(user))
  }
}
