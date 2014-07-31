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


   /* 嗨喔喔 精选 */
   def weixin(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
     val adverts = AdvertDao.findAdverts("weixin-banner",3)
     val pages = WeixinDiagramDao.findDiagrams(currentPage,pageSize)
     Ok(views.html.pages.weixin(user,pages,adverts))
   }
   /* 嗨喔喔 专辑 */
  def album(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("daren-banner",3)
    val pages = DiagramDao.findDiagrams("new",0,2,currentPage,pageSize)
    Ok(views.html.pages.album(user,pages,adverts))
  }
    /* 嗨喔喔 达人 */
  def daren(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("daren-banner",3)
    val pages = DiagramDao.findDiagrams("new",0,2,currentPage,pageSize)
    Ok(views.html.pages.daren(user,pages,adverts))
  }

  /* 萌宠 */
  def pets(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.pets(user,pages,adverts))
  }

  /* 萌宠 -- 自言自语 */
  def talk(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.talk(user,pages,adverts))
  }
  /* 萌宠 --- gif 神兽 */
  def gif(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.gif(user,pages,adverts))
  }

  /* 萌宠 --- 表情帝 */
  def emotion(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.emotion(user,pages,adverts))
  }

  /*  视频 */
  def videos(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.videos(user,pages,adverts))
  }

  /* 视频 开心宠物 */
  def funny(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.funny(user,pages,adverts))
  }
  /* 视频  萌童逗宠 */
  def kid(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.kid(user,pages,adverts))
  }
  /* 视频 二货主人 */
  def adult(currentPage:Int,pageSize:Int) = Users.UserAction{ user => implicit request =>
    val adverts = AdvertDao.findAdverts("owners-banner",3)
    val pages = DiagramDao.findDiagrams("new",1,2,currentPage,pageSize)
    Ok(views.html.pages.adult(user,pages,adverts))
  }


}
