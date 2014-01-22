package controllers

import play.api.mvc.{Action, Controller}
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-22
 * Time: 下午11:29
 */
object Forums extends Controller {

  def view(id:Long) = Action{
    Ok(views.html.forums.view())
  }
}
