package controllers

import play.api.mvc._
import play.api.libs.json.Json
import models.user._
import models.user.User
import models.user.dao.{UserSQLDao, UserDao}
import models.Page
import controllers.users.Users


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-19
 * Time: 上午9:37
 * ***********************
 * description
 */

object Sites extends Controller {








  /* site 首页 */
  def site(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.site(user) )
  }

  /* site blog */
  def blog(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.blog(user) )
  }
  /* site  pic */
  def pic(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.pic(user) )
  }
  /* site video */
  def video(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.video(user) )
  }

  /* blog view */
  def blogView(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.blogView(user) )
  }
  /* pic view */
  def picView(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.picView(user) )
  }
  /* video view */
  def videoView(id:Long) =Users.UserAction { user => implicit request =>

    Ok(views.html.sites.videoView(user) )
  }

}
