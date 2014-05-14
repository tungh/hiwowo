package controllers.admin

import play.api.mvc.Controller
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-11
 * Time: 下午11:33
 */
case class DiagramFilterFormData(title: Option[String], status: Option[Int], typeId: Option[Int], currentPage: Option[Int])

case class DiagramEditFormData(
                               id:Option[Long],
                               typeId: Int,
                               title: String,
                               pic: String,
                               intro: Option[String],
                               content: Option[String],
                               labels: Option[String],
                               status: Int
                                )

object Diagrams extends Controller {
  /*检索*/
  val diagramFilterForm = Form(
    mapping(
      "title" -> optional(text),
      "status" -> optional(number),
      "typeId" -> optional(number),
      "currentPage" -> optional(number)
    )(DiagramFilterFormData.apply)(DiagramFilterFormData.unapply)
  )
  val diagramEditForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "typeId" -> number,
      "title" -> text,
      "pic" -> text,
      "intro" -> optional(text),
      "content" -> optional(text),
      "labels" -> optional(text),
      "status" -> number
    )(DiagramEditFormData.apply)(DiagramEditFormData.unapply)
  )
  def list(p: Int, pageSize: Int) = Admin.AdminAction {
    user => implicit request =>
      val pages = DiagramDao.findAllDiagrams(p, pageSize)
      Ok(views.html.admin.diagrams.list(user, pages))
  }

  def edit(id: Long) = Admin.AdminAction { user => implicit request =>
      val (diagram,author) = DiagramDao.findDiagram(id).get
      Ok(views.html.admin.diagrams.edit(user,author,diagramEditForm.fill(DiagramEditFormData(diagram.id,diagram.typeId,diagram.title,diagram.pic,diagram.intro,diagram.content,diagram.labels,diagram.status))))

  }

  def saveDiagram = Admin.AdminAction { user => implicit request =>

    Ok("todo")
  }

  def saveDiagramPic = Admin.AdminAction { user => implicit request =>

    Ok("todo")
  }

  def delete = Admin.AdminAction {
    user => implicit request =>
      Ok("todo")
  }

  def check = Admin.AdminAction {
    user => implicit request =>
      Ok("todo")
  }

  def filter = Admin.AdminAction {
    user => implicit request =>
      diagramFilterForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        filterCondition => {
          val page = DiagramDao.filterDiagrams(filterCondition.title, filterCondition.status, filterCondition.typeId, filterCondition.currentPage.getOrElse(1), 20)
          Ok(views.html.admin.diagrams.filter(user, page, diagramFilterForm.fill(filterCondition)))
        }
      )
  }

  def batch = Admin.AdminAction {
    user => implicit request =>
      BatchFormData.batchForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        batch => {
          if (batch.action == 2) {
            for (id <- batch.ids) {
              //  println("update" + id)
              DiagramDao.modifyDiagramStatus(id, 2)
            }
          } else if (batch.action == 3) {
            for (id <- batch.ids) {
              DiagramDao.modifyDiagramStatus(id, 3)
            }
          } else if (batch.action == 4) {
            for (id <- batch.ids) {
              DiagramDao.deleteDiagram(id)
            }
          }

          Redirect(batch.url.getOrElse("/admin/diagrams/list"))
        }
      )
  }
}
