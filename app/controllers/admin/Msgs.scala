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



  def systemMsgs(currentPage:Int)=Administrators.AdminAction{ administrator => implicit request =>
    val page=SystemMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.systemMsgs(administrator,page))
  }
  /*  编辑 系统站内信 */
  def editSystemMsg(id:Long) = Administrators.AdminAction{ administrator => implicit request =>
    if (id==0) Ok(views.html.admin.msgs.editSystemMsg(administrator,systemMsgForm))
    else {
      val systemMsg=SystemMsgDao.findMsg(id)
      if(systemMsg.isEmpty) Ok(views.html.admin.msgs.editSystemMsg(administrator,systemMsgForm))
      else  Ok(views.html.admin.msgs.editSystemMsg(administrator,systemMsgForm.fill(SystemMsgFormData(systemMsg.get.id,systemMsg.get.title,systemMsg.get.content))))
    }
  }
  def saveSystemMsg = Administrators.AdminAction{ administrator => implicit request =>
    systemMsgForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.admin.msgs.editSystemMsg(administrator,formWithErrors)),
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

  /* 筛选 系统站内信 */
   def filterSystemMsgs = Administrators.AdminAction{ administrator => implicit request =>
    systemMsgFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      msg => {
        val page=SystemMsgDao.filterMsgs(msg.title,msg.currentPage.getOrElse(1),50)
        Ok(views.html.admin.msgs.filterSystemMsgs(administrator,page,systemMsgFilterForm.fill(msg)))
      }
    )

  }
  /* 批量处理站内信 */
  def batchSystemMsgs  = Administrators.AdminAction{ administrator => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            SystemMsgDao.modifyMsgStatus(id,0)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            SystemMsgDao.modifyMsgStatus(id,1)
          }
        }else if(batch.action==2){
          for(id<-batch.ids){
            SystemMsgDao.deleteMsg(id)
          }
        }
        Redirect(request.headers.get("REFERER").getOrElse("/admin/msgs/systemMsgs"))
      }
    )
  }

  def systemMsgReceivers(currentPage:Int) = Administrators.AdminAction{ administrator => implicit request =>
    val page = SystemMsgDao.findAllMsgReceivers(currentPage,50)
    Ok(views.html.admin.msgs.systemMsgReceivers(administrator,page))
  }

  def atMsgs(currentPage:Int)=Administrators.AdminAction{ administrator => implicit request =>
   val page = AtMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.atMsgs(administrator,page))
  }
  def favorMsgs(currentPage:Int) =Administrators.AdminAction{ administrator => implicit request =>
    val page = FavorMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.favorMsgs(administrator,page))
  }

  def discussMsgs(currentPage:Int) =Administrators.AdminAction{ administrator => implicit request =>
    val page = DiscussMsgDao.findAll(currentPage,50)
    Ok(views.html.admin.msgs.discussMsgs(administrator,page))
  }


}
