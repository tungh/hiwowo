package controllers.admin
import play.api.mvc.Controller
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-18
 * Time: 下午11:35
 */



object Labels extends Controller {

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

}
