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
case  class DiagramFilterFormData(title:Option[String],status:Option[Int],typeId:Option[Int],currentPage:Option[Int])

object Diagrams extends Controller {
  /*检索*/
  val diagramFilterForm =Form(
    mapping(
      "title"->optional(text),
      "status"->optional(number),
      "typeId"->optional(number),
      "currentPage"->optional(number)
    )(DiagramFilterFormData.apply)(DiagramFilterFormData.unapply)
  )

  def list(p:Int,pageSize:Int) = Admin.AdminAction{user => implicit request =>
         val pages = DiagramDao.findAllDiagrams(p,pageSize)
     Ok(views.html.admin.diagrams.list(user,pages))
  }
  def view(id:Long)= Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }
  def delete= Admin.AdminAction{user => implicit request =>
   Ok("todo")
  }
  def check = Admin.AdminAction{user => implicit request =>
    Ok("todo")
  }
  def filter =   Admin.AdminAction{user => implicit request =>
    diagramFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      filterCondition => {
        val page=DiagramDao.filterDiagrams(filterCondition.title,filterCondition.status,filterCondition.typeId,filterCondition.currentPage.getOrElse(1),20)
        Ok(views.html.admin.diagrams.filter(user,page,diagramFilterForm.fill(filterCondition)))
      }
    )
  }
  def batch =   Admin.AdminAction{user => implicit request =>
    BatchFormData.batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 2){
          for(id<-batch.ids){
            println("update" + id)
            DiagramDao.modifyDiagramStatus(id,2)
          }
        }else if (batch.action ==3){
          for(id<-batch.ids){
            DiagramDao.modifyDiagramStatus(id,3)
          }
        } else if (batch.action ==4){
          for(id<-batch.ids){
            DiagramDao.deleteDiagram(id)
          }
        }

        Redirect(batch.url.getOrElse("/admin/diagrams/list"))
      }
    )
  }
}
