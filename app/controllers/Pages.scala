package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.forum.dao.TopicDao
import models.diagram.dao.DiagramDao
import models.weixin.dao.WeixinDiagramDao
import models.label.dao.LabelDao

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
    val pages = DiagramDao.findDiagrams("new",2,currentPage,pageSize)
    Ok(views.html.pages.index(user,pages))
  }


   /* 微信精选 */
   def weixin(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
     val pages = WeixinDiagramDao.findDiagrams(currentPage,pageSize)
     Ok(views.html.pages.weixin(user,pages))
   }


  /* 图说频道
   *
    * */
  def pics(p:Int,size:Int) = Users.UserAction{ user => implicit request =>
    val mengPage = LabelDao.findLabelDiagrams(1,1,1,p,size)
    val xiaoPage = LabelDao.findLabelDiagrams(2,1,1,p,size)
    val shengPage = LabelDao.findLabelDiagrams(3,1,1,p,size)
    Ok(views.html.pages.pics(user,mengPage,xiaoPage,shengPage))
  }

  /* 宠物乐园 */
  def pets(typeId:Int,currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val pages = DiagramDao.findDiagrams("new",typeId,2,currentPage,pageSize)
    Ok(views.html.pages.pets(user,pages,typeId))
  }







}
