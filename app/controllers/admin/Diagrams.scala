package controllers.admin

import play.api.mvc.Controller
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._
import org.jsoup.Jsoup
import models.label.dao.LabelDao
import models.weixin.dao.WeixinDiagramDao




/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-11
 * Time: 下午11:33
 */
case class DiagramFilterFormData(
                                  title: Option[String],
                                  status: Option[Int],
                                  typeId: Option[Int],
                                  currentPage: Option[Int]
                                  )

case class DiagramEditFormData(
                               id:Long,
                               uid:Long,
                               typeId: Int,
                               title: String,
                               pic: String,
                               intro: Option[String],
                               content: Option[String],
                               labels: Option[String],
                               status: Int
                                )
case class DiagramPicsFormData(
                                diagramId:Long,
                                uid:Long,
                                urls:Seq[String],
                                intros: Seq[Option[String]]
                                )
case class WeixinDiagramsFormData(
                                  id: Long,
                                  diagramsId:String,
                                  period:Long
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
      "id" -> longNumber,
      "uid" -> longNumber,
      "typeId" -> number,
      "title" -> text,
      "pic" -> text,
      "intro" -> optional(text),
      "content" -> optional(text),
      "labels" -> optional(text),
      "status" -> number
    )(DiagramEditFormData.apply)(DiagramEditFormData.unapply)
  )

  val addWeixinDiagramsForm =Form(
    mapping(
      "id"->longNumber,
      "diagramsId" ->text,
      "period"->longNumber
    )(WeixinDiagramsFormData.apply)(WeixinDiagramsFormData.unapply)
  )

  def list(p: Int, pageSize: Int) = Admin.AdminAction {
    user => implicit request =>
      val pages = DiagramDao.findAllDiagrams(p, pageSize)
      Ok(views.html.admin.diagrams.list(user, pages, diagramFilterForm))
  }

  def edit(id: Long,msg:String) = Admin.AdminAction { user => implicit request =>
      val (diagram,author) = DiagramDao.findDiagram(id).get
      Ok(views.html.admin.diagrams.edit(user,author,diagram.id.get,diagramEditForm.fill(DiagramEditFormData(diagram.id.get,diagram.uid,diagram.typeId,diagram.title,diagram.pic,diagram.intro,diagram.content,diagram.labels,diagram.status)),msg))

  }
     /*
      * 1、首先保存diagram
      * 2、处理labels 和 label-diagram，先删除所有的diagram的label，在保存diagram-label
      * 3、如果保存的类型是图说，则需要抽取pic保存，然后删除所有的diagram pic,在保存diagram-pic
      *  */
  def saveDiagram = Admin.AdminAction { user => implicit request =>
    diagramEditForm.bindFromRequest.fold(
      formWithErrors => Ok("something wrong"),
      data => {

          DiagramDao.modifyDiagram(data.id,data.uid,data.typeId,data.title,data.pic,data.intro,data.content,2)
        // 处理label
        LabelDao.deleteLabelDiagramByDiagramId(data.id)
        if(!data.labels.isEmpty){
         for(name <- data.labels.get.split(",")){
           var labelId = 0l
            val label = LabelDao.findLabelByName(name)
           if(label.isEmpty){ labelId = LabelDao.addLabel(name,1) }else{ labelId = label.get.id.get  }
            LabelDao.addLabelDiagram(labelId,data.id)
         }
        }


        Redirect(controllers.admin.routes.Diagrams.edit(data.id,"保存Diagram成功"))
      }
    )
  }



  def delete = Admin.AdminAction { user => implicit request =>
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
        data => {
          val page = DiagramDao.filterDiagrams(data.title, data.status, data.typeId, data.currentPage.getOrElse(1), 20)
          Ok(views.html.admin.diagrams.list(user, page, diagramFilterForm.fill(data)))
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

       //   Redirect(batch.url.getOrElse("/admin/diagrams/list"))
          Redirect(request.headers.get("REFERER").get)
        }
      )
  }



 /* 微信精选 */
  def weixin(p:Int,size:Int) = Admin.AdminAction { user => implicit request =>
      val page = WeixinDiagramDao.findWeixinDiagrams(p,size)
     Ok(views.html.admin.diagrams.weixin(user,page))
 }
  def addWeixinDiagrams(id:Long) = Admin.AdminAction { user => implicit request =>
    if(id!=0){
       val weixinDiagrams = WeixinDiagramDao.findWeixinDiagrams(id)
      var ids=""
      weixinDiagrams.foreach(x =>{ ids += x.diagramId+" "}  )
      Ok(views.html.admin.diagrams.addWeixinDiagrams(user,id,addWeixinDiagramsForm.fill(WeixinDiagramsFormData(id,ids,weixinDiagrams.head.period))))
    }else{
        Ok(views.html.admin.diagrams.addWeixinDiagrams(user,id,addWeixinDiagramsForm))
    }

  }
  def saveWeixinDiagrams  = Admin.AdminAction { user => implicit request =>
    addWeixinDiagramsForm.bindFromRequest.fold(
      formWithErrors => {
        Ok("something wrong")
      },
      data => {
      if( data.id == 0 ){
        for( id <- data.diagramsId.split(",")){
          WeixinDiagramDao.addDiagram(id.toLong,data.period)
        }
      }else{
        for( id <- data.diagramsId.split(",")){
          WeixinDiagramDao.updateDiagram(data.id,id.toLong)
        }
      }
        Redirect(controllers.admin.routes.Diagrams.weixin(1,50))
      }
    )
  }

}
