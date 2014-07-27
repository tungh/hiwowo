package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.forum.dao.TopicDao
import models.diagram.dao.DiagramDao
import models.weixin.dao.WeixinDiagramDao
import models.label.dao.LabelDao
import models.advert.dao.AdvertDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:17
 */
object Pages extends Controller {



  /* 首页 */
  def index(currentPage:Int,pageSize:Int)  = Users.UserAction{ user => implicit request =>
    val notices = AdvertDao.findAdverts("index-notices",3)
    val pages = DiagramDao.findDiagrams("new",2,currentPage,pageSize)
    Ok(views.html.pages.index(user,pages,notices))
  }


   /* 微信精选 */
   def weixin(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
     val adverts = AdvertDao.findAdverts("weixin-banner",3)
     val pages = WeixinDiagramDao.findDiagrams(currentPage,pageSize)
     Ok(views.html.pages.weixin(user,pages,adverts))
   }

  def owners(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",0,2,currentPage,pageSize)
    Ok(views.html.pages.owners(user,pages,adverts))
  }

  /* 宠物 */
  def pets(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.pets(user,pages,adverts))
  }







}
