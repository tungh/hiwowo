package controllers.admin

import play.api.mvc.Controller
import models.diagram.dao.DiagramDao
import play.api.data.Form
import play.api.data.Forms._
import org.jsoup.Jsoup
import models.label.dao.LabelDao


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
  val diagramPicsForm = Form(
    mapping(
      "diagramId" ->longNumber,
      "uid" ->longNumber,
      "urls" ->seq(text),
      "intros" ->seq(optional(text))
    )(DiagramPicsFormData.apply)(DiagramPicsFormData.unapply)
  )
  def list(p: Int, pageSize: Int) = Admin.AdminAction {
    user => implicit request =>
      val pages = DiagramDao.findAllDiagrams(p, pageSize)
      Ok(views.html.admin.diagrams.list(user, pages))
  }

  def edit(id: Long,msg:String) = Admin.AdminAction { user => implicit request =>
      val (diagram,author) = DiagramDao.findDiagram(id).get
      val pics = DiagramDao.findDiagramPics(id)
      Ok(views.html.admin.diagrams.edit(user,author,pics,diagram.id.get,diagramEditForm.fill(DiagramEditFormData(diagram.id.get,diagram.uid,diagram.typeId,diagram.title,diagram.pic,diagram.intro,diagram.content,diagram.labels,diagram.status)),msg))

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
        val images =Jsoup.parseBodyFragment(data.content.get).body().getElementsByTag("img")
        var pics =""
        val it=images.iterator()
        while(it.hasNext){
          pics +=it.next().attr("src")+","
        }
          DiagramDao.modifyDiagram(data.id,data.uid,data.title,data.pic,data.intro,data.content,Some(pics),2,data.typeId)
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

        /* 处理diagram pics */
        if(data.typeId == 1){
            DiagramDao.deleteDiagramPicByDiagramId(data.id)
           for(url <- pics.split(",")){
              val pic = DiagramDao.findPicByUrl(url)
              var picId =0l
             if(pic.isEmpty){ picId = DiagramDao.addPic(data.uid,url,1) }else{ picId = pic.get.id.get }
             DiagramDao.addDiagramPic(data.id,picId)

           }
        }



        Redirect(controllers.admin.routes.Diagrams.edit(data.id,"保存Diagram成功"))
      }
    )
  }

  def saveDiagramPics = Admin.AdminAction { user => implicit request =>
   diagramPicsForm.bindFromRequest.fold(
      formWithErrors => Ok("something wrong"),
      data => {
        for((url,i) <- data.urls.view.zipWithIndex){
           DiagramDao.modifyPicIntro(url,data.intros.apply(i),1)
        }

        Redirect(controllers.admin.routes.Diagrams.edit(data.diagramId,"保存Diagram pic 成功"))
      }
    )

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
