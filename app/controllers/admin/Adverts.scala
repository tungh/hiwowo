package controllers.admin

import play.api.mvc.Controller
import models.advert.dao.AdvertDao
import java.sql.Timestamp
import play.api.data.Form
import play.api.data.Forms._
import java.util.Date


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-5-6
 * Time: 下午9:05
 */
case class AdvertBatchFormData(
                                action:Int,
                                ids:Seq[Long],
                                url:Option[String],
                                nums:Seq[Int]
                                )

case class AdvertEditFormData(
                           id: Option[Long],
                           code: String,
                           typeId: Int,
                           status:Int,
                           title: String,
                           link: String,
                           pic: Option[String],
                           width: Int,
                           height: Int,
                           startTime: Date,
                           endTime: Date,
                           note: Option[String],
                           intro: Option[String]
                           )
case class AdvertFilterFormData(
                                 code: Option[String],
                                 typeId:Option[Int],
                                 title:Option[String],
                                 startTime:Option[Date],
                                 endTime:Option[Date],
                                 currentPage:Option[Int],
                                 pageSize:Option[Int]
                                 )
object Adverts extends Controller {

  val advertEditForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "code" -> nonEmptyText,
      "typeId" -> number,
      "status" -> number,
      "title" -> nonEmptyText,
      "link" -> nonEmptyText,
      "pic" ->optional(text),
      "width" ->number,
      "height" ->number,
      "startTime" ->date("yyyy-MM-dd HH:mm"),
      "endTime" ->date("yyyy-MM-dd HH:mm"),
      "note" -> optional(text),
      "intro" -> optional(text)
    )(AdvertEditFormData.apply)(AdvertEditFormData.unapply)
  )
  val advertFilterForm = Form(
    mapping(
      "code" -> optional(text),
      "typeId" -> optional(number),
      "title" -> optional(text),
      "startTime" ->optional(date("yyyy-MM-dd HH:mm")),
      "endTime" ->optional(date("yyyy-MM-dd HH:mm")),
     "currentPage" ->optional(number),
      "pageSize" ->optional(number)
    )(AdvertFilterFormData.apply)(AdvertFilterFormData.unapply)
  )

  val advertBatchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "url"->optional(text),
      "nums"->seq(number)
    )(AdvertBatchFormData.apply)(AdvertBatchFormData.unapply)
  )


  def list(p:Int,size:Int) =  Admin.AdminAction{user => implicit request =>
    val page = AdvertDao.findAdverts(p,size)
    Ok(views.html.admin.adverts.list(user,page,advertFilterForm))
  }

  def edit(id:Long) = Admin.AdminAction{user => implicit request =>
       if(id ==0){
           Ok(views.html.admin.adverts.edit(user,advertEditForm,""))
       }else{
          val advert =AdvertDao.findAdvert(id)
         Ok(views.html.admin.adverts.edit(user,advertEditForm.fill(AdvertEditFormData(advert.id,advert.code,advert.typeId,advert.status,advert.title,advert.link,advert.pic,advert.width,advert.height,new Date(advert.startTime.getTime),new Date(advert.endTime.getTime),advert.note,advert.intro)),""))
       }

  }

  def save = Admin.AdminAction{user => implicit request =>
      advertEditForm.bindFromRequest.fold(
      formWithErrors => Ok(formWithErrors.toString),
      data => {
          if(data.id.isEmpty){
         AdvertDao.addAdvert(data.code,data.typeId,data.title,data.link,data.pic,data.width,data.height,new Timestamp(data.startTime.getTime),new Timestamp(data.endTime.getTime),0,data.status,data.note,data.intro)
          }else{
             AdvertDao.modifyAdvert(data.id.get,data.code,data.typeId,data.title,data.link,data.pic,data.width,data.height,new Timestamp(data.startTime.getTime),new Timestamp(data.endTime.getTime),0,data.status,data.note,data.intro)
          }
        Ok(views.html.admin.adverts.edit(user,advertEditForm.fill(data),"添加成功"))
      }
      )

  }

  def delete = Admin.AdminAction{user => implicit request =>

    Ok("todo")
  }

  def filter = Admin.AdminAction{ user => implicit request =>
     advertFilterForm.bindFromRequest.fold(
         formWithErrors => Ok(formWithErrors.toString),
         data => {
            val page = AdvertDao.filterAdverts(data.code,data.typeId,data.title,data.startTime,data.endTime,data.currentPage.getOrElse(1),data.pageSize.getOrElse(20))
           Ok(views.html.admin.adverts.list(user,page,advertFilterForm.fill(data)))
         }
     )
  }

  def batch = Admin.AdminAction { user => implicit request =>
     advertBatchForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        batch => {
          if (batch.action == 0) {
            for (id <- batch.ids) {
             AdvertDao.modifyAdvertState(id,0)
            }
          } else if (batch.action == 1) {
            for (id <- batch.ids) {
              AdvertDao.modifyAdvertState(id,1)
            }
          } else if (batch.action == 3) {
            for (id <- batch.ids) {
              AdvertDao.deleteAdvert(id)
            }
          }else if(batch.action == 4){
            for ((id,i) <- batch.ids.view.zipWithIndex) {
              AdvertDao.modifyAdvertSortNum(id,batch.nums.apply(i))
            }
          }


          Redirect(request.headers.get("REFERER").getOrElse("/admin/adverts/list"))
        }
      )
  }
}