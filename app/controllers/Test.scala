package controllers

import play.api.mvc.Controller
import controllers.users.Users
import models.user.dao.UserDao
import models.site.dao.SiteDao
import models.user.User
import models.site.Site

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-31
 * Time: 下午1:35
 */
object Test extends Controller {

  def index = Users.UserAction{ user => implicit request =>
  /*  val users = UserDao.findAll(1,10)
    for(user <- users.items){
      println(user.name)
    }
  val (user,userStatic) = UserDao.findUserWithStatic(2)
  println(user.name + " : " + userStatic.uid)*/
   val id = SiteDao.addSite(1l,"我的小站","/images/hiwowo.png","我的小站欢迎您","可爱 萌")
     val (site:Site,user:User) = SiteDao.findSite(1)
    println("site title " +site.title + " created by  " + user.name )
    Ok(" Ok! " +  site.title + " created by " + user.name)
  }


}
