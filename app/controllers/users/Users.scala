package controllers.users

import play.api.mvc._
import play.api.libs.json.Json
import models.user._
import models.forum.Topic
import models.forum.dao.TopicDao
import models.goods.Goods
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

    Ok(views.html.users.home(user) )
  }


  /*user   讨论吧 广场 */
  def forum(id:Long,t:String,p:Int) = UserAction{ user => implicit request =>

    Ok("todo")
   
  }







}
