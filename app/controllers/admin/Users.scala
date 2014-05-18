package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.user.dao.{UserSQLDao, UserDao}


import java.util.Date
import play.api.Play

import scala.collection.JavaConverters._

import java.sql.Timestamp



case class UserBatchFormData(action:Int,ids:Seq[Long],url:Option[String])
case  class UserFilterFormData(name:Option[String],status:Option[Int],title:Option[String],comeFrom:Option[Int],creditsOrder:String,addTimeOrder:String,currentPage:Option[Int])

object Users  extends Controller {
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "url"->optional(text)
    )(UserBatchFormData.apply)(UserBatchFormData.unapply)
  )
  /*检索标签*/
  val userFilterForm =Form(
    mapping(
      "name"->optional(text),
      "status"->optional(number),
      "title"->optional(text),
      "comeFrom"->optional(number),
      "creditsOrder"->nonEmptyText,
      "addTimeOrder"->nonEmptyText,
      "currentPage"->optional(number)
    )(UserFilterFormData.apply)(UserFilterFormData.unapply)
  )

  /*用户管理*/
def users(p:Int,size:Int) = Admin.AdminAction{ user => implicit request =>
    val page =UserDao.findAll(p,size)
  Ok(views.html.admin.users.users(user,page))
}
   /* 用户拉黑处理 */
  def black(uid:Long)= Admin.AdminAction{ user => implicit request =>
       val result = UserDao.modifyStatus(uid,4)
     if (result>0)Ok(Json.obj("code"->"100","message"->"success"))
     else Ok(Json.obj("code"->"104","message"->"更新失败"))
  }

  /* 用户信息修改 */
  def edit(uid:Long)=Admin.AdminAction{user => implicit request =>
       val user =UserDao.findById(uid)
    Ok("succsess")
  }
  /* 用户信息查看*/
  def view(uid:Long)=Admin.AdminAction{user => implicit request =>
    val userWithProfile =UserDao.findWithProfile(uid)
   // val trends = UserDao.findUserTrends(uid)
    Ok(views.html.admin.users.view(user,userWithProfile))
  }


  /* 批量处理 */
  def batchUsers=Admin.AdminAction{user => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 4){
          for(id<-batch.ids){
            UserDao.modifyStatus(id,4)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            UserDao.modifyStatus(id,1)
          }
        }
        Redirect(batch.url.getOrElse("/admin/users/list"))
      }
    )
  }

  /*用户过滤*/
  def filterUsers = Admin.AdminAction{ user => implicit request =>
    userFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      u => {
        val page=UserDao.filterUsers(u.name,u.status,u.title,u.comeFrom,u.creditsOrder,u.addTimeOrder,u.currentPage.getOrElse(1),50)
        Ok(views.html.admin.users.filterUsers(user,page,userFilterForm.fill(u)))
      }
    )
  }


}
