package controllers.admin
import play.api.mvc.Controller
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-5-6
 * Time: 下午8:55
 */
object Cmses extends Controller {

  def list(p:Int,size:Int) =  Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }

  def edit(id:Long) = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def save = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def delete = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }
}
