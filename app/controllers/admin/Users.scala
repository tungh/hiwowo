package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.user.dao.{UserSQLDao, UserDao}
import java.util.Date


import java.util.Date
import play.api.Play

import scala.collection.JavaConverters._

import java.sql.Timestamp
import controllers.users.UserComponent


case class UserBatchFormData(
                              action: Int,
                              ids: Seq[Long]
                              )

case class UserFilterFormData(
                               name: Option[String],
                               status: Option[Int],
                               title: Option[String],
                               comeFrom: Option[Int],
                               creditsOrder: Option[String],
                               startTime:Option[Date],
                               endTime:Option[Date],
                               currentPage: Option[Int],
                               pageSize:Option[Int]
                               )

object Users  extends Controller {
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber)
    )(UserBatchFormData.apply)(UserBatchFormData.unapply)
  )
  /*检索标签*/
  val userFilterForm =Form(
    mapping(
      "name"->optional(text),
      "status"->optional(number),
      "title"->optional(text),
      "comeFrom"->optional(number),
      "creditsOrder"->optional(text),
      "startTime" ->optional(date("yyyy-MM-dd HH:mm")),
      "endTime" ->optional(date("yyyy-MM-dd HH:mm")),
      "currentPage" ->optional(number),
      "pageSize" ->optional(number)
    )(UserFilterFormData.apply)(UserFilterFormData.unapply)
  )

  /*用户管理*/
def users(p:Int,size:Int) = Admin.AdminAction{ user => implicit request =>
    val page =UserDao.findAll(p,size)
  Ok(views.html.admin.users.list(user,page,userFilterForm))
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
    val (user,profile,stat) =UserDao.findUser(uid)
    Ok(views.html.admin.users.view(user,UserComponent(user,profile,stat)))
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
       // Redirect(batch.url.getOrElse("/admin/users/list"))
        Redirect(request.headers.get("REFERER").get)
      }
    )
  }

  /*用户过滤*/
  def filterUsers = Admin.AdminAction{ user => implicit request =>
    userFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok(formWithErrors.toString),
     data => {
        val page=UserDao.filterUsers(data.name,data.status,data.title,data.comeFrom,data.startTime,data.endTime,data.currentPage.getOrElse(1),data.pageSize.getOrElse(20))
        Ok(views.html.admin.users.list(user,page,userFilterForm.fill(data)))
      }
    )
  }


}
