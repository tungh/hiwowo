package controllers.users

import play.api.mvc.{Action, Controller}
import models.msg.dao.SystemMsgDao

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-23
 * Time: 下午6:09
 * Email:zuosanshao@qq.com
 */

object UsersMsg extends Controller {
  /*系统通知*/
  def system(p:Int) = Users.UserAction {
    user => implicit request =>
      if (user.isEmpty) Redirect(controllers.users.routes.UsersRegLogin.login)
      else {
       val page = SystemMsgDao.findReceiverMsgs(user.get.id.get,p,10)
        Ok(views.html.users.msg.system(user,page))
      }

  }

  /*@我的*/
  def  at =Users.UserAction{     user => implicit request =>
    Ok(views.html.users.msg.at(user))
  }

}
