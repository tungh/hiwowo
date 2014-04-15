package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import play.api.libs.json.Json
import models.user.dao.UserDao
import models.diagram.dao.{DiagramDiscussSQLDao, DiagramSQLDao, DiagramDao}
import models.diagram.Diagram
import play.api.cache.Cache
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import models.diagram.Diagram
import scala.Some
import models.user.User

import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午11:37
 */
case class DiagramComponent(
                   diagram:Diagram,
                   user:User
                       )


object  Diagrams extends Controller {

  val petForm = Form(
    tuple(
      "id"->optional(longNumber),
      "title" ->nonEmptyText ,
      "pic"->nonEmptyText,
      "intro" ->optional(text) ,
      "tags"->optional(text),
      "status"->number,
      "typeId"->number
    )
  )

  /* 图说 1 判断图说是否存在 ，不存在，则显示no-diagram,存在，如果是草稿状态，不是本人浏览，则显示no-diagram,否则显示diagram */
  def diagram(id:Long) = Users.UserAction{ user => implicit request =>
  /* 记录每个页面的点击次数，先放在cache里，当大于9时，记录到数据库中 */
  /*  val viewNum =  Cache.getOrElse[Int]("diagram_"+id) { 1 }
    Cache.set("diagram_"+id,viewNum+1)
    if(viewNum >9) {
      DiagramSQLDao.updateViewNum(id,viewNum)
      Cache.remove("topic_"+id)
    }*/

    val diagramWithUser = DiagramDao.findDiagram(id)
    val defaultUser = User(Some(0),Some("1"),1,"hiwowo","",Some(""),1,"",Some(""),Some(""),0,Some(""),Some(""),Some(""),Some(""),None)
     if(diagramWithUser.isEmpty ||( diagramWithUser.get._1.status==0 && diagramWithUser.get._1.uid != user.getOrElse(defaultUser).id.get)){
       Ok(views.html.diagrams.diagramInvalid(user))
     } else {
    Ok(views.html.diagrams.diagram(user,DiagramComponent(diagramWithUser.get._1,diagramWithUser.get._2)))
     }
  }

  /* editor 1先判断用户是否登陆 2 判断用户的status 是否等于 3 ，只用等于3的用户，才能发表图说。3、判断id 是否为0，只用不为零的图说，可以进入编辑状态 */
  def editDiagram(id:Long) = Users.UserAction{ user => implicit request =>
    if(user.isEmpty){
        Redirect(controllers.users.routes.UsersRegLogin.login)
    } else{
      if(user.get.status == 3){
          if(id == 0) Ok(views.html.diagrams.createDiagram(user))
          else{
            val diagram = DiagramDao.findDiagramById(id)
            val pics = DiagramDao.findDiagramPics(id)
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
       val id = DiagramDao.addPic(user.get.id.get,picUrl.getOrElse(""),picIntro,0)
        Ok(Json.obj("code" -> "100", "message" ->"success","picUrl" ->picUrl,"picId"->id))
      }else{
        DiagramDao.modifyPic(picId.get,picUrl.get,picIntro,0)
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
      val diagramId = (request.body \ "diagramId").asOpt[Long]
      val diagramTitle = (request.body \ "diagramTitle").asOpt[String]
      val  diagramPic = (request.body \ "diagramPic").asOpt[String]
      val  diagramIntro = (request.body \ "diagramIntro").asOpt[String]
      val  diagramContent = (request.body \ "diagramContent").asOpt[String]
      val  diagramPs = (request.body \ "diagramPs").asOpt[String]
      val  diagramTags = (request.body \ "diagramTags").asOpt[String]
      val diagramStatus =  (request.body \ "diagramStatus").asOpt[Int]
      val  picIds = (request.body \ "picIds").asOpt[String]
      val ids=picIds.get.split("-").map(x=>x.toLong)
      if(diagramTitle.isEmpty){
        Ok(Json.obj("code"->"104","message"->"diagram title is empty"))
      } else {
        if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
          val dId = DiagramDao.addDiagram(user.get.id.get,diagramTitle.get,diagramPic.get,diagramIntro,diagramContent,diagramPs,diagramTags,diagramStatus.getOrElse(0))
          for (id<-ids){ if(DiagramDao.findDiagramPic(dId,id).isEmpty){DiagramDao.addDiagramPic(dId,id)}  }
          Ok(Json.obj("code" -> "100", "message" ->"success","diagramId"->dId))
        }else{
          DiagramDao.modifyDiagram(diagramId.get,user.get.id.get,diagramTitle.get,diagramPic.get,diagramIntro,diagramContent,diagramPs,diagramTags,diagramStatus.getOrElse(0))
        /* 查找此diagram pic是否保存 */
          for (id<-ids){ if(DiagramDao.findDiagramPic(diagramId.get,id).isEmpty){ println("sdsdfdddddd"); DiagramDao.addDiagramPic(diagramId.get,id)}  }
          Ok(Json.obj("code" -> "100", "message" ->"success","diagramId"->diagramId.get))
        }

      }
    }
  }

   def  delete =Action(parse.json){  implicit request =>
     val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
     if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
     else{
       val diagramId = (request.body \ "diagramId").asOpt[Long]
       if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
         Ok(Json.obj("code" -> "104", "message" ->"图说不存在"))
       }else{
         val diagram =DiagramDao.findDiagramById(diagramId.get)
          if(user.get.id.get == diagram.get.uid){
            DiagramDao.modifyDiagramStatus(diagramId.get,0)
            Ok(Json.obj("code" -> "104", "message" ->"图说已删除"))
          }else{
            Ok(Json.obj("code" -> "444", "message" ->"你没有权限删除哦"))
          }
       }

     }
   }

  /* editor 1先判断用户是否登陆 2 判断用户的status 是否等于 3 ，只用等于3的用户，才能发表图说。3、判断id 是否为0，只有不为零的图说，可以进入编辑状态 */
  def editPet(id:Long) = Users.UserAction{ user => implicit request =>
    if(user.isEmpty){
      Redirect(controllers.users.routes.UsersRegLogin.login)
    } else{
      if(user.get.status == 3){
        if(id == 0) Ok(views.html.diagrams.editPet(user,petForm))
        else{
          val diagram = DiagramDao.findDiagramById(id)
          Ok(views.html.diagrams.editPet(user,petForm.fill((diagram.get.id,diagram.get.title,diagram.get.pic,diagram.get.intro,diagram.get.tags,diagram.get.status,diagram.get.typeId))))
        }
      } else{
        Redirect(controllers.users.routes.UsersAccount.vip)
      }
    }

  }

  /*保存 帖子*/
  def save = Users.UserAction{ user => implicit request =>
    petForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.diagrams.editPet(user,formWithErrors)),
      fields =>{

        if (fields._1.isEmpty){
          val id= DiagramDao.addDiagram(user.get.id.get,fields._7,fields._2,fields._3,fields._4,fields._5,fields._6)
          Redirect(controllers.routes.Diagrams.diagram(id))
        }else{
          DiagramDao.modifyDiagram(fields._1.get,user.get.id.get,fields._7,fields._2,fields._3,fields._4,fields._5,fields._6)
          Redirect(controllers.routes.Diagrams.diagram(fields._1.get))
        }
      }
    )

  }

  /* save diagram discuss*/
  def saveDiscuss = Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止评论"))
    else {
      val diagramId = (request.body \ "diagramId").asOpt[Long]
      val quoteContent = (request.body \ "quoteContent").asOpt[String]
      val content = (request.body \ "content").asOpt[String]
        if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
          Ok(Json.obj("code" -> "104", "message" ->"diagram id is not correct"))
        }else{
          DiagramDao.addDiagramDiscuss(user.get.id.get,diagramId.get,quoteContent,content.getOrElse(""),0)
          Ok(Json.obj("code" -> "100", "message" ->"success"))
        }
    }
  }

  /* get diagram discusses */
  def getDiscusses(diagramId:Long,sortBy:String,p:Int)  = Users.UserAction{ user => implicit request =>
      val page = DiagramDao.findDiagramDiscusses(diagramId,sortBy,p,3)
    Ok(views.html.diagrams.discusses(diagramId,page,sortBy))
  }

  /* add like num 在一个session 里 只能 点击一次 */
  def upDiagram = Action(parse.json){  implicit request =>

      val diagramId = (request.body \ "diagramId").asOpt[Long]
      if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"diagram id is not correct"))
      }else{
       if(!session.get("pet_"+diagramId.get).isEmpty){
          Ok(Json.obj("code" -> "104", "message" ->"loved"))
        } else {
        DiagramSQLDao.updateLoveNum(diagramId.get,1)
        val key ="pet_"+diagramId.get.toString
        val value=diagramId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( session + (key -> value))
        }
      }
  }

  /* add hate num */
  def downDiagram = Action(parse.json){  implicit request =>

    val diagramId = (request.body \ "diagramId").asOpt[Long]
    if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
      Ok(Json.obj("code" -> "104", "message" ->"diagram id is not correct"))
    }else{
      if(!session.get("pet_"+diagramId.get).isEmpty){
        Ok(Json.obj("code" -> "104", "message" ->"hated"))
      } else {
        DiagramSQLDao.updateHateNum(diagramId.get,1)
        val key ="pet_"+diagramId.get.toString
        val value=diagramId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( session + (key -> value))
      }
    }
  }
  /* add like num 在一个session 里 只能 点击一次 */
  def upDiscuss = Action(parse.json){  implicit request =>

    val discussId = (request.body \ "discussId").asOpt[Long]
    if(discussId.isEmpty || discussId.getOrElse(0) ==0 ){
      Ok(Json.obj("code" -> "104", "message" ->"discuss id is not correct"))
    }else{
      if(!session.get("discuss_"+discussId.get).isEmpty){
        Ok(Json.obj("code" -> "104", "message" ->"support"))
      } else {
        DiagramDiscussSQLDao.updateLoveNum(discussId.get,1)
        val key ="discuss_"+discussId.get.toString
        val value=discussId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( session + (key -> value))
      }
    }
  }

  /* add hate num */
  def downDiscuss = Action(parse.json){  implicit request =>

    val discussId = (request.body \ "discussId").asOpt[Long]
    if(discussId.isEmpty || discussId.getOrElse(0) ==0 ){
      Ok(Json.obj("code" -> "104", "message" ->"discuss id is not correct"))
    }else{
      if(!session.get("discuss_"+discussId.get).isEmpty){
        Ok(Json.obj("code" -> "104", "message" ->"support"))
      } else {
        DiagramDiscussSQLDao.updateHateNum(discussId.get,1)
        val key ="discuss_"+discussId.get.toString
        val value=discussId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( session + (key -> value))
      }
    }
  }
}
