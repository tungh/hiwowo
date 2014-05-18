package controllers.admin

import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import models.msg.dao._


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 13-7-3
 * Time: 上午12:03
 */

case class SystemMsgFormData(id:Option[Long],title:String,content:String)

case class SystemMsgFilterFormData(title:Option[String],currentPage:Option[Int])

case class MsgBatchFormData(action:Int,ids:Seq[Long])

object Msgs extends Controller {
  val systemMsgForm =Form(
    mapping(
      "id"->optional(longNumber),
      "title" -> nonEmptyText,
      "content" -> nonEmptyText
    )(SystemMsgFormData.apply)(SystemMsgFormData.unapply)
  )

  val systemMsgFilterForm =Form(
    mapping(
      "title"->optional(text),
      "currentPage"->optional(number)
    )(SystemMsgFilterFormData.apply)(SystemMsgFilterFormData.unapply)
  )

  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber)
    )(MsgBatchFormData.apply)(MsgBatchFormData.unapply)
  )



  def systemMsgs(p:Int,size:Int)=Admin.AdminAction{ user => implicit request =>
    val page=SystemMsgDao.findAll(p,size)
    Ok(views.html.admin.msgs.systemMsgs(user,page))
  }
  /*  编辑 系统站内信 */
  def editSystemMsg(id:Long) = Admin.AdminAction{ user => implicit request =>
    if (id==0) Ok(views.html.admin.msgs.editSystemMsg(user,systemMsgForm))
    else {
      val systemMsg=SystemMsgDao.findMsg(id)
      if(systemMsg.isEmpty) Ok(views.html.admin.msgs.editSystemMsg(user,systemMsgForm))
      else  Ok(views.html.admin.msgs.editSystemMsg(user,systemMsgForm.fill(SystemMsgFormData(systemMsg.get.id,systemMsg.get.title,systemMsg.get.content))))
    }
  }
  def saveSystemMsg = Admin.AdminAction{ user => implicit request =>
    systemMsgForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.msgs.editSystemMsg(user,formWithErrors)),
      systemMsg => {
        /*如果group id 为空，则保存数据 ，否则，则update数 */
        if(systemMsg.id.isEmpty){
          SystemMsgDao.addMsg(systemMsg.title,systemMsg.content)
        }else{
          SystemMsgDao.modifyMsg(systemMsg.id.get,systemMsg.title,systemMsg.content)
        }
   //     Redirect(request.headers.get("REFERER").getOrElse("/admin/msgs/systemMsgs"))
        Redirect("/admin/msgs/systemMsgs")
      }
    )
  }






  def atMsgs(p:Int,size:Int)=Admin.AdminAction{ user => implicit request =>
   val page = AtMsgDao.findAll(p,size)
    Ok(views.html.admin.msgs.atMsgs(user,page))
  }



}
