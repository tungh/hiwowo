package controllers.admin
import play.api.mvc.Controller
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._
import models.label.dao.LabelDao

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


  def list(p:Int,size:Int)  =  Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def core(p:Int,size:Int)  = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def edit(id:Long)  = Admin.AdminAction{user => implicit request =>

    Ok("todo")

  }
  def check  = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def save  = Admin.AdminAction{user => implicit request =>
   Ok("todo")
  }

  def delete  = Admin.AdminAction{user => implicit request =>
   Ok("todo")
  }

  def filter = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def batch = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  /* 标签组管理  */

  def groups(p:Int,size:Int) =  Admin.AdminAction{user => implicit request =>
    val page = LabelDao.findGroups(p,size)
    Ok(views.html.admin.labels.groups(user,page))
  }

  def editGroup(id:Long) = Admin.AdminAction{user => implicit request =>
    if(id == 0) { Ok(views.html.admin.labels.editGroup(user,editGroupForm)) }
    else{
      val group = LabelDao.findGroup(id)
      Ok(views.html.admin.labels.editGroup(user,editGroupForm.fill((group.id,group.name,group.intro,group.status))))
    }
  }

  def saveGroup = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def deleteGroup = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }


}
