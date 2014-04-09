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
    val pages = DiagramDao.findDiagrams(2,currentPage,pageSize)
    Ok(views.html.pages.index(user,pages))
  }


   /* 微信精选 */
   def weixin(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
     val pages = WeiXinDiagramDao.findDiagrams(currentPage,pageSize)
     Ok(views.html.pages.weixin(user,pages))
   }
  /* 发现 图说 */
  def diagrams(sortBy:String,currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val pages = DiagramDao.findDiagrams(sortBy,0,2,currentPage,pageSize)
    Ok(views.html.pages.diagrams(user,pages,sortBy))
  }

  /* 宠物乐园 */
  def pets(typeId:Int,currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val pages = DiagramDao.findDiagrams("new",typeId,2,currentPage,pageSize)
    Ok(views.html.pages.pets(user,pages,typeId))
  }







}
