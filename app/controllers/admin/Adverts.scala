package controllers.admin

import play.api.mvc.Controller
import models.advert.dao.AdvertDao
import java.sql.Timestamp
import play.api.data.Form
import play.api.data.Forms._
import controllers.admin.DiagramEditFormData

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-5-6
 * Time: 下午9:05
 */
case class AdvertEditFormData(
                           id: Option[Long],
                           code: String,
                           typeId: Int,
                           title: String,
                           link: String,
                           pic: Option[String],
                           width: Int,
                           height: Int,
                           startTime: String,
                           endTime: String,
                           note: Option[String]
                           )
object Adverts extends Controller {

  val advertEditForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "code" -> nonEmptyText,
      "typeId" -> number,
      "title" -> nonEmptyText,
      "link" -> nonEmptyText,
      "pic" ->optional(text),
      "width" ->number,
      "height" ->number,
      "startTime" ->text,
      "endTime" ->text,
      "note" -> optional(text)
    )(AdvertEditFormData.apply)(AdvertEditFormData.unapply)
  )

  def list(p:Int,size:Int) =  Admin.AdminAction{user => implicit request =>
    val adverts = AdvertDao.findAdverts(p,size)
    Ok(views.html.admin.adverts.list(user,adverts))
  }

  def edit(id:Long) = Admin.AdminAction{user => implicit request =>
       if(id ==0){
           Ok(views.html.admin.adverts.edit(user,advertEditForm,""))
       }else{
          val advert =AdvertDao.findAdvert(id)
         Ok(views.html.admin.adverts.edit(user,advertEditForm.fill(AdvertEditFormData(advert.id,advert.code,advert.typeId,advert.title,advert.link,advert.pic,advert.width,advert.height,advert.startTime.toString,advert.endTime.toString,advert.note)),""))
       }

  }

  def save = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def delete = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def filter = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def batch = Admin.AdminAction { user => implicit request =>
      BatchFormData.batchForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        batch => {
          if (batch.action == 2) {
            for (id <- batch.ids) {
              //  println("update" + id)
           //
            }
          } else if (batch.action == 3) {
            for (id <- batch.ids) {

            }
          } else if (batch.action == 4) {
            for (id <- batch.ids) {

            }
          }

          Redirect(batch.url.getOrElse("/admin/adverts/list"))
        }
      )
  }
}