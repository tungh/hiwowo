package controllers.admin

import play.api.mvc.Controller
import play.api.mvc._
import play.api.data.Forms._
import play.api.data.Form
import models.Page
import play.api.libs.json.Json
import models.forum.dao.TopicDao

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-11-11
 * Time: 上午1:20
 * ***********************
 * description:用于类的说明
 */
case class TopicBatchFormData(action:Int,ids:Seq[Long],typeIds:Seq[Int],url:Option[String])
case  class TopicFilterFormData(name:Option[String],checkState:Option[Int],typeId:Option[Int],isBest:Option[Boolean],isTop:Option[Boolean],currentPage:Option[Int])
case  class DiscussFilterFormData(checkState:Option[Int],currentPage:Option[Int])

object Forums extends Controller {
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "typeIds"->seq(number),
      "url"->optional(text)
    )(TopicBatchFormData.apply)(TopicBatchFormData.unapply)
  )
  /*检索标签*/
  val topicFilterForm =Form(
      mapping(
        "name"->optional(text),
        "checkState"->optional(number),
        "typeId"->optional(number),
        "isBest"->optional(boolean),
        "isTop"->optional(boolean),
        "currentPage"->optional(number)
      )(TopicFilterFormData.apply)(TopicFilterFormData.unapply)
    )
  /*检索标签*/
  val discussFilterForm =Form(
    mapping(
      "checkState"->optional(number),
      "currentPage"->optional(number)
    )(DiscussFilterFormData.apply)(DiscussFilterFormData.unapply)
  )


  def topics(p:Int) = Administrators.AdminAction{administrator => implicit request =>
      val page=TopicDao.findAll(p,50)
      Ok(views.html.admin.forums.topics(administrator,page))
  }
  
  def delete(id:Long) = Administrators.AdminAction{administrator => implicit request =>
    val result =TopicDao.deleteTopic(id)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" ->"删除成功"))
    else Ok(Json.obj("code" -> "101", "message" -> "删除失败" ))
  }
  
  def check(id:Long,checkState:Int) = Administrators.AdminAction{administrator => implicit request =>
    val result =TopicDao.modifyTopicCheckState(id,checkState)
    if (result >0) Ok(Json.obj("code" -> "100", "message" ->"审核成功" ))
    else Ok(Json.obj("code" -> "101", "message" ->"审核不通过，请直接删除"))
  }


  
  def discusses(p:Int)=  Administrators.AdminAction{administrator => implicit request =>
    val page=TopicDao.findAllDiscusses(p,50)
    Ok(views.html.admin.forums.discusses(administrator,page))
  }
  def deleteDiscuss(id:Long)  = Administrators.AdminAction{administrator => implicit request =>
    val result =TopicDao.deleteDiscuss(id)
    if (result >0) Ok(Json.obj("code" -> "100", "message" ->"删除成功"))
    else Ok(Json.obj("code" -> "101", "message" ->"删除失败" ))
  }

  def checkDiscuss(id:Long,state:Int)= Administrators.AdminAction{administrator => implicit request =>
    val result =TopicDao.modifyDiscussCheckState(id,state)
    if (result >0) Ok(Json.obj( "code" -> "100", "message" ->"审核成功" ))
    else Ok(Json.obj("code" -> "101", "message" -> "审核不通过，请直接删除"))
  }

  /* 批量处理 */
  def batchTopics=Administrators.AdminAction{administrator => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            TopicDao.modifyTopicCheckState(id,1)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            TopicDao.modifyTopicCheckState(id,2)
          }
        }else if (batch.action ==2){
          for(id<-batch.ids){
            TopicDao.modifyTopicTop(id,true)
          }
        }else if (batch.action ==3){
          for(id<-batch.ids){
            TopicDao.modifyTopicTop(id,false)
          }
        }else if (batch.action ==4){
          for(id<-batch.ids){
            TopicDao.modifyTopicBest(id,true)
          }
        }else if (batch.action ==5){
          for(id<-batch.ids){
            TopicDao.modifyTopicBest(id,false)
          }
        }else if (batch.action ==6){
          for(id<-batch.ids){
            TopicDao.deleteTopic(id)
          }
        }

        Redirect(batch.url.getOrElse("/admin/forums/list"))
      }
    )
  }

  /*用户过滤*/
  def filterTopics = Administrators.AdminAction{ administrator => implicit request =>
    topicFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      topic => {
        val page=TopicDao.filterTopics(topic.name,topic.checkState,topic.typeId,topic.isTop,topic.isBest,topic.currentPage.getOrElse(1),50)
        Ok(views.html.admin.forums.filterTopics(administrator,page,topicFilterForm.fill(topic)))
      }
    )

  }

  /* 批量处理 */
  def batchDiscusses=Administrators.AdminAction{administrator => implicit request =>
    batchForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      batch => {
        if(batch.action == 0){
          for(id<-batch.ids){
            TopicDao.modifyDiscussCheckState(id,1)
          }
        }else if (batch.action ==1){
          for(id<-batch.ids){
            TopicDao.modifyDiscussCheckState(id,2)
          }
        }else if(batch.action==2){
          for(id<-batch.ids){
            TopicDao.deleteDiscuss(id)
          }
        }

        Redirect(batch.url.getOrElse("/admin/forums/discusses"))
      }
    )
  }

  /*用户过滤*/
  def filterDiscusses = Administrators.AdminAction{ administrator => implicit request =>
    discussFilterForm.bindFromRequest.fold(
      formWithErrors =>Ok("something wrong"),
      discuss => {
        val page=TopicDao.filterDiscusses(discuss.checkState,discuss.currentPage.getOrElse(1),50)
        Ok(views.html.admin.forums.filterDiscusses(administrator,page,discussFilterForm.fill(discuss)))
      }
    )

  }



}
