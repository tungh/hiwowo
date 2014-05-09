package controllers.admin
import play.api.mvc.{Action, Controller}
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._
import models.label.dao.LabelDao
import models.user.User
import models.user.dao.UserDao
import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-18
 * Time: 下午11:35
 */



object Labels extends Controller {
  val editGroupForm =Form(
    tuple(
      "id"->optional(longNumber),
      "name" ->nonEmptyText ,
      "intro"->optional(text),
      "status"->number
    )
  )


  def list(p:Int,size:Int)  =  Admin.AdminAction{ user => implicit request =>

    Ok("todo")
  }

  def core(p:Int,size:Int)  = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def edit(id:Long)  = Admin.AdminAction{ user => implicit request =>

    Ok("todo")

  }
  def check  = Admin.AdminAction{ user => implicit request =>
    Ok("todo")
  }

  def save  = Admin.AdminAction{ user => implicit request =>
   Ok("todo")
  }

  def delete  = Admin.AdminAction{ user => implicit request =>
   Ok("todo")
  }

  def filter = Admin.AdminAction{ user => implicit request =>
    Ok("todo")
  }

  def batch = Admin.AdminAction{ user => implicit request =>
    Ok("todo")
  }

  /* 标签组管理  */

  def groups(p:Int,size:Int) =  Admin.AdminAction{ user => implicit request =>
    val page = LabelDao.findGroups(p,size)
    Ok(views.html.admin.labels.groups(user,page))
  }

  def editGroup(id:Long) = Admin.AdminAction{ user => implicit request =>
    if(id == 0) { Ok(views.html.admin.labels.editGroup(user,editGroupForm)) }
    else{
      val group = LabelDao.findGroup(id)
      Ok(views.html.admin.labels.editGroup(user,editGroupForm.fill((group.id,group.name,group.intro,group.status))))
    }
  }

  def saveGroup = Admin.AdminAction{ user => implicit request =>
    editGroupForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      groupData => {
        if(groupData._1.getOrElse(0) == 0) {
          LabelDao.addGroup(groupData._2,groupData._3,groupData._4)
        } else{
          LabelDao.modifyGroup(groupData._1.get,groupData._2,groupData._3,groupData._4)
        }
       Redirect(controllers.admin.routes.Labels.groups(1,50))
      })
  }

  def deleteGroup = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.isAdmin ==0 ){Ok(Json.obj("code" -> "204", "message" ->"亲，你还不是管理员")) }
    else{
      val groupId =  (request.body \ "groupId").asOpt[Long]
      if(groupId.isEmpty){ Ok(Json.obj("code" -> "104", "message" ->"group id is empty" )) }
      else{
        LabelDao.deleteGroup(groupId.get)
        Ok(Json.obj("code" -> "100", "message" ->"success" ))
      }
    }
  }
      /* 查看group 下面的 labels*/
  def groupLabels(gid:Long)  = Admin.AdminAction{ user => implicit request =>
        val labels= LabelDao.findGroupLabels(gid)
        Ok(views.html.admin.labels.groupLabels(user,labels))
      }

  def addGroupLabel  = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.isAdmin ==0 ){Ok(Json.obj("code" -> "204", "message" ->"亲，你还不是管理员")) }
    else{
      val groupId =  (request.body \ "groupId").asOpt[Long]
      val labelId =  (request.body \ "labelId").asOpt[Long]
      if(groupId.isEmpty || labelId.isEmpty ){ Ok(Json.obj("code" -> "104", "message" ->"group id or labelId is empty" )) }
      else{
        val groupLabel = LabelDao.findGroupLabel(groupId.get,labelId.get)
        if(groupLabel.isEmpty){
          LabelDao.addGroupLabel(groupId.get,labelId.get)
          Ok(Json.obj("code" -> "100", "message" ->"group id or labelId is empty" ))
        }else{
          Ok(Json.obj("code" -> "103", "message" ->"group label is Exists" ))
        }
      }
    }

  }

  def deleteGroupLabel = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.isAdmin ==0 ){Ok(Json.obj("code" -> "204", "message" ->"亲，你还不是管理员")) }
    else{
      val groupId =  (request.body \ "groupId").asOpt[Long]
      val labelId =  (request.body \ "labelId").asOpt[Long]
      if(groupId.isEmpty || labelId.isEmpty ){ Ok(Json.obj("code" -> "104", "message" ->"group id or labelId is empty" )) }
      else{
        LabelDao.deleteGroupLabel(groupId.get,labelId.get)
        Ok(Json.obj("code" -> "100", "message" ->"success" ))
      }
    }
  }

}
