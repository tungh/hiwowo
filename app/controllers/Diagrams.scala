package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import play.api.libs.json.Json
import models.user.dao.UserDao
import models.user.User
import models.diagram.dao.DiagramDao


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午11:37
 */
object  Diagrams extends Controller {

  /* 图说 */
  def diagram(id:Long) = Users.UserAction{ user => implicit request =>
    Ok(views.html.diagrams.diagram(user))
  }

  /* editor */
  def edit(id:Long) = Users.UserAction{ user => implicit request =>
    Ok(views.html.diagrams.edit(user))
  }
  /* pic save */
  def savePic =Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止发表图说"))
    else {
      val picId = (request.body \ "picId").asOpt[Long]
      val  picUrl = (request.body \ "picUrl").asOpt[String]
      val  picIntro = (request.body \ "picIntro").asOpt[String]
      if(picUrl.isEmpty){
        Ok(Json.obj("code"->"104","message"->"pic url is empty"))
      } else {
      if(picId.isEmpty || picId.getOrElse(0) ==0 ){
       val id = DiagramDao.addPic(user.get.id.get,picUrl.getOrElse(""),picIntro,0,1)
        Ok(Json.obj("code" -> "100", "message" ->"success","picId"->id))
      }else{
        DiagramDao.modifyPic(picId.get,picUrl.get,picIntro,0,1)
        Ok(Json.obj("code" -> "100", "message" ->"success","picId"->picId.get))
      }

      }
    }

  }

  /* draft save 1保存图片，2保存diagram,并把图片记录在content中 */
  def saveDraft  =Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止发表图说"))
    else {
      val diagramId = (request.body \ "picId").asOpt[Long]
      val diagramTitle = (request.body \ "diagramTitle").asOpt[String]
      val  diagramPic = (request.body \ "diagramPic").asOpt[String]
      val  diagramIntro = (request.body \ "diagramIntro").asOpt[String]
      val  diagramContent = (request.body \ "diagramContent").asOpt[String]
      val  diagramPs = (request.body \ "diagramPs").asOpt[String]
      val  diagramTags = (request.body \ "diagramTags").asOpt[String]
      val  picIds = (request.body \ "picIds").asOpt[String]
      println(picIds + " : " +diagramContent)
      if(diagramTitle.isEmpty){
        Ok(Json.obj("code"->"104","message"->"diagram title is empty"))
      } else {
        if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
          val id = DiagramDao.addDiagram(user.get.id.get,diagramTitle.get,diagramPic.get,diagramIntro,diagramContent,diagramPs,diagramTags,0)
          Ok(Json.obj("code" -> "100", "message" ->"success","diagramId"->id))
        }else{
          DiagramDao.modifyDiagram(diagramId.get,user.get.id.get,diagramTitle.get,diagramPic.get,diagramIntro,diagramContent,diagramPs,diagramTags,0)
          Ok(Json.obj("code" -> "100", "message" ->"success","diagramId"->diagramId.get))
        }

      }
    }
  }

  def publishSave = Users.UserAction{ user => implicit request =>

    Ok("OK")
  }

}
