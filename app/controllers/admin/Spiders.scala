package controllers.admin

import play.api.mvc.Controller

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-7-26
 * Time: 上午12:03
 */
object Spiders extends Controller {

  def diagrams(p:Int,size:Int) =  Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def topics(p:Int,size:Int) =  Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

}
