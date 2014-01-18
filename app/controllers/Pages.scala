package controllers

import play.api.mvc.{Action, Controller}

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:17
 */
object Pages extends Controller {

  def index = Action {
    Ok(views.html.pages.index())
  }
}
