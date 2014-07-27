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

case class LabelEditFormData(
                              id: Option[Long],
                              name: String,
                              level: Int,
                              intro: Option[String],
                              isHot: Int,
                              spell: Option[String],
                              checkState: Int
                              )

case class LabelFilterFormData(
                                name: Option[String],
                                level: Option[Int],
                                isHot: Option[Int],
                                spell: Option[String],
                                checkState: Option[Int],
                                currentPage: Option[Int]
                                )

case class LabelDiagramsFilterFormData(
                                  labelId:Long,
                                  title: Option[String],
                                  checkState: Option[Int],
                                  typeId: Option[Int],
                                  currentPage: Option[Int]
                                  )
object Labels extends Controller {
  val groupEditForm = Form(
    tuple(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "intro" -> optional(text),
      "status" -> number
    )
  )
  val labelFilterForm = Form(
    mapping(
      "name" -> optional(text),
      "level" -> optional(number),
      "isHot" -> optional(number),
      "spell" -> optional(text),
      "checkState" -> optional(number),
      "currentPage" -> optional(number)
    )(LabelFilterFormData.apply)(LabelFilterFormData.unapply)
  )
  val labelEditForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> text,
      "level" -> number,
      "intro" -> optional(text),
      "isHot" -> number,
      "spell" -> optional(text),
      "checkState" -> number
    )(LabelEditFormData.apply)(LabelEditFormData.unapply)
  )

  val labelDiagramsFilterForm = Form(
    mapping(
      "labelId" -> longNumber,
      "title" -> optional(text),
      "checkState" -> optional(number),
      "typeId" -> optional(number),
      "currentPage" -> optional(number)
    )(LabelDiagramsFilterFormData.apply)(LabelDiagramsFilterFormData.unapply)
  )


  def list(p: Int, size: Int) = Admin.AdminAction {
    user => implicit request =>
      val page = LabelDao.findAllLabels(p, size)
      Ok(views.html.admin.labels.labels(user, page,labelFilterForm))
  }

  def core(p: Int, size: Int) = Admin.AdminAction {
    user => implicit request =>
      val page = LabelDao.findCoreLabels(3, p, size)
      Ok(views.html.admin.labels.coreLabels(user, page))
  }

  def edit(id: Long) = Admin.AdminAction {  user => implicit request =>
      if (id == 0) {
        Ok(views.html.admin.labels.labelEdit(user, labelEditForm))
      }
      else {
        val label = LabelDao.findLabelById(id)
        Ok(views.html.admin.labels.labelEdit(user, labelEditForm.fill(LabelEditFormData(label.get.id, label.get.name, label.get.level, label.get.intro, label.get.isHot, label.get.spell, label.get.checkState))))
      }

  }


  def save = Admin.AdminAction { user => implicit request =>
      labelEditForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        data => {
          if (data.id.getOrElse(0) == 0) {
            LabelDao.addLabel(data.name, data.level, data.intro, data.isHot, data.spell, data.checkState)
          } else {
            LabelDao.modifyLabel(data.id.get, data.name, data.level, data.intro, data.isHot, data.spell, data.checkState)
          }
          Redirect(controllers.admin.routes.Labels.list(1, 20))
        })
  }

  def check = Admin.AdminAction {
    user => implicit request =>
      Ok("todo")
  }

  def delete = Admin.AdminAction {
    user => implicit request =>
      Ok("todo")
  }

  def filter = Admin.AdminAction { user => implicit request =>
      labelFilterForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        filterCondition => {
          val page = LabelDao.filterLabels(filterCondition.name, filterCondition.level, filterCondition.isHot, filterCondition.spell, filterCondition.checkState, filterCondition.currentPage.getOrElse(1), 20)
          Ok(views.html.admin.labels.labels(user, page, labelFilterForm.fill(filterCondition)))
        }
      )
  }

  def batch = Admin.AdminAction {
    user => implicit request =>
      BatchFormData.batchForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        batch => {
          if (batch.action == 1) {
            for (id <- batch.ids) {
              LabelDao.modifyLabelCheckState(id, 1)
            }
          } else if (batch.action == 2) {
            for (id <- batch.ids) {
              LabelDao.modifyLabelCheckState(id, 2)
            }
          } else if (batch.action == 3) {
            for (id <- batch.ids) {
              LabelDao.deleteLabel(id)
            }
          }


          Redirect(request.headers.get("REFERER").getOrElse("/admin/labels/list"))
        }
      )
  }


  /* 标签组管理  */

  def groups(p: Int, size: Int) = Admin.AdminAction {
    user => implicit request =>
      val page = LabelDao.findGroups(p, size)
      Ok(views.html.admin.labels.groups(user, page))
  }

  def editGroup(id: Long) = Admin.AdminAction {
    user => implicit request =>
      if (id == 0) {
        Ok(views.html.admin.labels.groupEdit(user, groupEditForm))
      }
      else {
        val group = LabelDao.findGroup(id)
        Ok(views.html.admin.labels.groupEdit(user, groupEditForm.fill((group.id, group.name, group.intro, group.status))))
      }
  }

  def saveGroup = Admin.AdminAction {
    user => implicit request =>
      groupEditForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        groupData => {
          if (groupData._1.getOrElse(0) == 0) {
            LabelDao.addGroup(groupData._2, groupData._3, groupData._4)
          } else {
            LabelDao.modifyGroup(groupData._1.get, groupData._2, groupData._3, groupData._4)
          }
          Redirect(controllers.admin.routes.Labels.groups(1, 50))
        })
  }

  def deleteGroup = Action(parse.json) {
    implicit request =>
      val user: Option[User] = request.session.get("user").map(u => UserDao.findById(u.toLong))
      if (user.isEmpty) Ok(Json.obj("code" -> "200", "message" -> "亲，你还没有登录哦"))
      else if (user.get.isAdmin == 0) {
        Ok(Json.obj("code" -> "204", "message" -> "亲，你还不是管理员"))
      }
      else {
        val groupId = (request.body \ "groupId").asOpt[Long]
        if (groupId.isEmpty) {
          Ok(Json.obj("code" -> "104", "message" -> "group id is empty"))
        }
        else {
          LabelDao.deleteGroup(groupId.get)
          Ok(Json.obj("code" -> "100", "message" -> "success"))
        }
      }
  }

  /* 查看group 下面的 labels*/
  def groupLabels(gid: Long) = Admin.AdminAction { user => implicit request =>
      val labels = LabelDao.findGroupLabels(gid)
      Ok(views.html.admin.labels.groupLabels(user, gid, labels))
  }

  def addGroupLabel = Action(parse.json) {
    implicit request =>
      val user: Option[User] = request.session.get("user").map(u => UserDao.findById(u.toLong))
      if (user.isEmpty) Ok(Json.obj("code" -> "200", "message" -> "亲，你还没有登录哦"))
      else if (user.get.isAdmin == 0) {
        Ok(Json.obj("code" -> "204", "message" -> "亲，你还不是管理员"))
      }
      else {
        val groupId = (request.body \ "groupId").asOpt[Long]
        val labelId = (request.body \ "labelId").asOpt[Long]
        if (groupId.isEmpty || labelId.isEmpty) {
          Ok(Json.obj("code" -> "104", "message" -> "group id or labelId is empty"))
        }
        else {
          val groupLabel = LabelDao.findGroupLabel(groupId.get, labelId.get)
          if (groupLabel.isEmpty) {
            LabelDao.addGroupLabel(groupId.get, labelId.get)
            Ok(Json.obj("code" -> "100", "message" -> "group id or labelId is empty"))
          } else {
            Ok(Json.obj("code" -> "103", "message" -> "group label is Exists"))
          }
        }
      }

  }

  def deleteGroupLabel = Action(parse.json) {  implicit request =>
      val user: Option[User] = request.session.get("user").map(u => UserDao.findById(u.toLong))
      if (user.isEmpty) Ok(Json.obj("code" -> "200", "message" -> "亲，你还没有登录哦"))
      else if (user.get.isAdmin == 0) {
        Ok(Json.obj("code" -> "204", "message" -> "亲，你还不是管理员"))
      }
      else {
        val groupId = (request.body \ "groupId").asOpt[Long]
        val labelId = (request.body \ "labelId").asOpt[Long]
        if (groupId.isEmpty || labelId.isEmpty) {
          Ok(Json.obj("code" -> "104", "message" -> "group id or labelId is empty"))
        }
        else {
          LabelDao.deleteGroupLabel(groupId.get, labelId.get)
          Ok(Json.obj("code" -> "100", "message" -> "success"))
        }
      }
  }

  /* label diagrams */
  def labelDiagrams(labelId:Long,p:Int,size:Int) = Admin.AdminAction { user => implicit request =>
    val page = LabelDao.findLabelDiagrams(labelId,p,size)
     Ok(views.html.admin.labels.labelDiagrams(user,page,labelDiagramsFilterForm,labelId))
  }

  def filterLabelDiagrams  = Admin.AdminAction { user => implicit request =>
    labelDiagramsFilterForm.bindFromRequest.fold(
      formWithErrors => Ok("something wrong"),
      data => {
        val page = LabelDao.filterLabelDiagrams(data.labelId, data.title, data.checkState, data.typeId, data.currentPage.getOrElse(1),50)
        Ok(views.html.admin.labels.labelDiagrams(user, page,labelDiagramsFilterForm.fill(data),data.labelId))
      }
    )
  }

  def batchLabelDiagrams = Admin.AdminAction {  user => implicit request =>
      BatchFormData.batchForm.bindFromRequest.fold(
        formWithErrors => Ok("something wrong"),
        batch => {
          if (batch.action == 1) {
            for (id <- batch.ids) {
              LabelDao.modifyLabelDiagramCheckState(batch.thirdId.get,id, 1)
            }
          } else if (batch.action == 2) {
            for (id <- batch.ids) {
              LabelDao.modifyLabelDiagramCheckState(batch.thirdId.get,id, 2)
            }
          } else if (batch.action == 3) {
            for (id <- batch.ids) {
              LabelDao.deleteLabelDiagram(batch.thirdId.get,id)
            }
          }


          Redirect(request.headers.get("REFERER").getOrElse("/admin/labels/labelDiagrams/"+batch.thirdId.get))
        }
      )
  }
}
