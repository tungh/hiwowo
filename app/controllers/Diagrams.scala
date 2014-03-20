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

  /* editor 1先判断用户是否登陆 2 判断用户的status 是否等于 3 ，只用等于3的用户，才能发表图说。3、判断id 是否为0，只用不为零的图说，可以进入编辑状态 */
  def edit(id:Long) = Users.UserAction{ user => implicit request =>
    if(user.isEmpty){
        Redirect(controllers.users.routes.UsersRegLogin.login)
    } else{
      if(user.get.status == 3){
          if(id == 0) Ok(views.html.diagrams.createDiagram(user))
          else{
            val diagram = DiagramDao.findDiagramById(id)
            val pics = DiagramDao.findPicsByDiagramId(id)
            Ok(views.html.diagrams.editDiagram(user,diagram.get,pics))
          }
      } else{
        Redirect(controllers.users.routes.UsersAccount.vip)
      }
    }

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
        Ok(Json.obj("code" -> "100", "message" ->"success","picUrl" ->picUrl,"picId"->id))
      }else{
        DiagramDao.modifyPic(picId.get,picUrl.get,picIntro,0,1)
        Ok(Json.obj("code" -> "100", "message" ->"success","picUrl" ->picUrl,"picId"->picId.get))
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
      val diagramStatus =  (request.body \ "diagramStatus").asOpt[Int]
      val  picIds = (request.body \ "picIds").asOpt[String]
      val ids=picIds.get.split(",").map(x=>x.toLong)
      if(diagramTitle.isEmpty){
        Ok(Json.obj("code"->"104","message"->"diagram title is empty"))
      } else {
        if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
          val dId = DiagramDao.addDiagram(user.get.id.get,diagramTitle.get,diagramPic.get,diagramIntro,diagramContent,diagramPs,diagramTags,diagramStatus.getOrElse(0))
          for (id<-ids) DiagramDao.addDiagramPic(dId,id)
          Ok(Json.obj("code" -> "100", "message" ->"success","diagramId"->dId))
        }else{
          DiagramDao.modifyDiagram(diagramId.get,user.get.id.get,diagramTitle.get,diagramPic.get,diagramIntro,diagramContent,diagramPs,diagramTags,diagramStatus.getOrElse(0))
          for (id<-ids) DiagramDao.addDiagramPic(diagramId.get,id)
          Ok(Json.obj("code" -> "100", "message" ->"success","diagramId"->diagramId.get))
        }

      }
    }
  }

  def publishSave = Users.UserAction{ user => implicit request =>

    Ok("OK")
  }

}
