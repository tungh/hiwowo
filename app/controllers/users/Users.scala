package controllers.users

import play.api.mvc._
import play.api.libs.json.Json
import models.user._
import models.user.User
import models.user.dao.{UserSQLDao, UserDao}
import models.Page


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-19
 * Time: 上午9:37
 * ***********************
 * description:用户页面显示，采用类似于权限控制的规则，对外显示的页面和用户能够看到的页面一致，只是在局部上 某些功能有额外的处理
 */

object Users extends Controller {



  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def UserAction(f:Option[User] => Request[AnyContent] => Result) = {
      Action{ request =>
       val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
        f(user)(request)
      }
  }




  /*user 首页*/
  def home(id:Long) =UserAction { user => implicit request =>
   val author = UserDao.findById(id)
    Ok(views.html.users.home(user,author) )
  }

  /* 我的部落 */
  def site(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.site(user,author) )

  }
  /* 我的那些事 */
  def blog(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.blog(user,author) )

  }
  /* 我讨论的 */
  def discuss(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.discuss(user,author) )
   
  }

  /* 我喜欢的 */
  def love(id:Long,p:Int) = UserAction{ user => implicit request =>

    val author = UserDao.findById(id)
    Ok(views.html.users.love(user,author) )
  }

  /* 我关注的 */
  def follow(id:Long,p:Int) = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.follow(user,author) )
  }

  /* 我的动态 */
  def record(id:Long,p:Int)  = UserAction{ user => implicit request =>
    val author = UserDao.findById(id)
    Ok(views.html.users.record(user,author) )
  }






}
