package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.forum.dao.TopicDao
import models.diagram.dao.DiagramDao
import models.weixin.dao.WeiXinDiagramDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:17
 */
object Pages extends Controller {



  /* 首页 */
  def index(currentPage:Int,pageSize:Int)  = Users.UserAction{ user => implicit request =>
    /* 在首页显示精华的帖子 */
    val pages = DiagramDao.findDiagrams(3,currentPage,pageSize)
    Ok(views.html.pages.index(user,pages))
  }


   /* 微信精选 */
   def weixin(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
     val pages = WeiXinDiagramDao.findDiagrams(currentPage,pageSize)
     Ok(views.html.pages.weixin(user,pages))
   }
  /* 发现 图说 */
  def find = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.find(user))
  }

  /* 宠物乐园 */
  def pets(typeId:Int) = Users.UserAction{ user => implicit request =>
    Ok(views.html.pages.pets(user))
  }







}
