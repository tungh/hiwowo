package controllers.admin

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.data.Forms._
import play.api.data.Form
import java.sql.Timestamp
import models.admin.Administrator
import models.admin.dao.{AdministratorSQLDao, AdministratorDao}


/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-10-3
 * Time: 下午4:08
 * Email:zuosanshao@qq.com
 */

object Administrators extends Controller {



  val loginForm = Form(
    tuple(
      "email" -> email,
      "password" -> nonEmptyText
    ) verifying("Email或者密码错误……", fields => fields match {
      case (email, password) => AdministratorDao.authenticate(email, password).isDefined
    })
  )
  /*每个页面 每次访问，都需要知道用户状态，比如是否登录*/
  def AdminAction(f: Administrator => Request[AnyContent] => Result) = {
    Action {
      request =>
        val administrator: Option[Administrator] = request.session.get("administrator").map(m => Cache.getOrElse[Administrator](m) {
          AdministratorDao.findById(m.split("-").last.toLong)
        })
        if (administrator.isEmpty)
          Redirect(controllers.admin.routes.Administrators.login())
        else
          f(administrator.get)(request)
    }
  }

  /* login */
  def login = Action {
    Ok(views.html.admin.login(loginForm))
  }

  /*邮箱验证并cache*/
  def emailLogin = Action {  implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.admin.login(formWithErrors)),
        admin => {
          val administrator:Administrator = AdministratorDao.authenticate(admin._1, admin._2).get
          Cache.set("admin-"+administrator.id.toString, administrator)
          AdministratorSQLDao.loginRecord(administrator.id.get,request.remoteAddress,1,new Timestamp(System.currentTimeMillis()),administrator.loginTime)
          Redirect(controllers.admin.routes.Administrators.index).withSession("administrator" -> ("admin-"+administrator.id.get.toString))
        }
      )
  }

  /* 用户退出  清除缓存*/
  def logout = Action {  implicit request =>
      if (!session.get("administrator").isEmpty) {
        Cache.remove(session.get("administrator").get)
      }
      Redirect(controllers.admin.routes.Administrators.login()).withNewSession
  }

  /*用户设置密码等等*/
  def modify = AdminAction { administrator => implicit request =>
      Ok("todo")
  }

  /*我的账号*/
  def myAccount = AdminAction { administrator => implicit request =>
      Ok("todo")
  }


  /*admin 首页*/
  def index = AdminAction { administrator => implicit request =>

      Ok(views.html.admin.administrators.index(administrator))
  }

  /*缓存管理*/
  def cache = AdminAction{    administrator => implicit request =>
      Ok(views.html.admin.administrators.cache(administrator))
  }



  /*推送消息、信息管理*/
  def pushMsg = AdminAction{    administrator => implicit request =>
    Ok("向用户推送消息 todo ")
  }

  def check =AdminAction{    administrator => implicit request =>
    Ok("主要检测是否有恶意用户 todo ")
  }





}
