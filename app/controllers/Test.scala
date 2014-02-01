package controllers

import play.api.mvc.Controller
import controllers.users.Users
import models.user.dao.UserDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-31
 * Time: 下午1:35
 */
object Test extends Controller {

  def index = Users.UserAction{ user => implicit request =>
    val users = UserDao.findAll(1,10)
    for(user <- users.items){
      println(user.name)
    }
  val (user,userStatic) = UserDao.findUserWithStatic(2)
  println(user.name + " : " + userStatic.uid)
    Ok("Ok")
  }
}
