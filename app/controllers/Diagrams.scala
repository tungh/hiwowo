package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import play.api.libs.json.Json
import models.user.dao.UserDao
import models.diagram.dao.{DiagramDiscussSQLDao, DiagramSQLDao, DiagramDao}
import models.diagram.Diagram

import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import models.user.User
import java.io.File
import com.sksamuel.scrimage.Image
import scala.collection.JavaConversions._

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

case class DiagramFormData(
                                id:Option[Long],
                                title:String,
                                intro:Option[String],
                                typeId:Int,
                                urls:Seq[String],
                                alts: Seq[Option[String]]
                                )
object  Diagrams extends Controller {



  val diagramForm = Form(
    mapping(
      "id"->optional(longNumber),
      "title" ->nonEmptyText ,
      "intro"->optional(text),
      "typeId"->number,
      "urls" ->seq(text),
      "alts" ->seq(optional(text))
    )(DiagramFormData.apply)(DiagramFormData.unapply)
  )

  /* 图说 1 判断图说是否存在 ，不存在，则显示no-diagram,存在，如果是草稿状态，不是本人浏览，则显示no-diagram,否则显示diagram */
  def diagram(id:Long) = Users.UserAction{ user => implicit request =>
    val component = DiagramDao.findDiagram(id)
     if(component.isEmpty){
       Ok(views.html.diagrams.diagramInvalid(user))
     }else if(component.get._1.status==1 && user.isEmpty){
       Ok(views.html.diagrams.diagramInvalid(user))
     }else if(component.get._1.status==1 && component.get._1.uid != user.get.id.get ){
       Ok(views.html.diagrams.diagramInvalid(user))
     }else {
    Ok(views.html.diagrams.diagram(user,DiagramComponent(component.get._1,component.get._2)))
     }
  }

  /* 创建图说 */
  def add = Users.UserAction{ user => implicit request =>
    if(user.isEmpty){
      Redirect(controllers.users.routes.UsersRegLogin.login)        // 用户未登录，提醒去登录
    } else{
      if(user.get.status == 3){
        Ok(views.html.diagrams.add(user,diagramForm))  // 只有是vip 用户才能创建
      } else{
        Redirect(controllers.users.routes.UsersAccount.vip)
      }
    }

  }

  def save  = Users.UserAction{ user => implicit request =>

      diagramForm.bindFromRequest.fold(
        formWithErrors => BadRequest(formWithErrors.errors.toString),
        data =>{
          var diagramId:Long=0L
          val pic = data.urls.head
          var content =""
          for((url,i) <- data.urls.view.zipWithIndex){
            val suffix = url.substring(url.lastIndexOf("."))
           val pic = new File("/opt/static"+url)
           val rawPicUrl = url+suffix
           val rawPic = new File("/opt/static"+rawPicUrl)
            content +="<img class='img-upload lazy'  data-original='"+ url +"' width='"+Image(pic).width+"'height='"+Image(pic).height+"' data-raw='"+rawPicUrl+"'data-rawwidth='"+Image(rawPic).width+"'data-rawheight='"+Image(rawPic).height+"'alt='"+ data.alts.get(i).getOrElse("") +"'  '><span>"+data.alts.get(i).getOrElse("")+"</span>"
          }
          if(data.id.isEmpty){
            diagramId = DiagramDao.addDiagram(user.get.id.get,data.typeId,data.title,pic,data.intro,Some(content),1)

          }else{
            diagramId = data.id.get
            DiagramDao.deleteDiagramPics(diagramId)
            DiagramDao.modifyDiagram(data.id.get,user.get.id.get,data.typeId,data.title,pic,data.intro,Some(content),1)

          }
          /* 保存 diagram pic */
          for((url,i) <- data.urls.view.zipWithIndex){
            val suffix = url.substring(url.lastIndexOf("."))
            val pic = new File("/opt/static"+url)
            val width = Image(pic).width
            val height = Image(pic).height
            val rawPicUrl = url+suffix
            val rawPic = new File("/opt/static"+rawPicUrl)
            val rawWidth = Image(rawPic).width
            val rawHeight = Image(rawPic).height
            DiagramDao.addDiagramPic(user.get.id.get,diagramId,url,width,height,rawPicUrl,rawWidth,rawHeight,data.alts.get(i))
          }
          Redirect(controllers.routes.Diagrams.diagram(diagramId))
        })


  }

   /* 编辑 图说 */
  def edit(id:Long) = Users.UserAction{ user => implicit request =>
    if(user.isEmpty){
      Redirect(controllers.users.routes.UsersRegLogin.login)        // 用户未登录，提醒去登录
    } else{
      if(user.get.status == 3){
        if(id == 0)    Ok(views.html.diagrams.add(user,diagramForm))   // 只有是vip 用户才能edit
        else{
          val diagram = DiagramDao.findDiagramById(id)
          if(diagram.isEmpty || diagram.get.uid != user.get.id.get){        // 只有图说的作者才能修改
              Ok("图说不存在，或者你没有权限编辑")
          }else{
            val pics = DiagramDao.findDiagramPics(diagram.get.id.get)
            Ok(views.html.diagrams.edit(user,diagramForm,diagram.get,pics))
          }
        }
      } else{
        Redirect(controllers.users.routes.UsersAccount.vip)
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
          Ok(Json.obj("code" -> "100", "message" ->"图说已删除"))
        }else{
          Ok(Json.obj("code" -> "444", "message" ->"你没有权限删除哦"))
        }
      }

    }
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
  def favorDiagram = Action(parse.json){  implicit request =>

      val diagramId = (request.body \ "diagramId").asOpt[Long]
      if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
        Ok(Json.obj("code" -> "104", "message" ->"diagram id is not correct"))
      }else{
       if(!request.session.get("pet_"+diagramId.get).isEmpty){
          Ok(Json.obj("code" -> "102", "message" ->"loved"))
        } else {
        DiagramSQLDao.updateLoveNum(diagramId.get,1)
        val key ="pet_"+diagramId.get.toString
        val value=diagramId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( request.session + (key -> value))
        }
      }
  }

  /* add hate num */
  def disfavorDiagram = Action(parse.json){  implicit request =>

    val diagramId = (request.body \ "diagramId").asOpt[Long]
    if(diagramId.isEmpty || diagramId.getOrElse(0) ==0 ){
      Ok(Json.obj("code" -> "104", "message" ->"diagram id is not correct"))
    }else{
      if(!request.session.get("pet_"+diagramId.get).isEmpty){
        Ok(Json.obj("code" -> "102", "message" ->"hated"))
      } else {
        DiagramSQLDao.updateHateNum(diagramId.get,1)
        val key ="pet_"+diagramId.get.toString
        val value=diagramId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( request.session + (key -> value))
      }
    }
  }
  /* add like num 在一个session 里 只能 点击一次 */
  def favorDiscuss = Action(parse.json){  implicit request =>

    val discussId = (request.body \ "discussId").asOpt[Long]
    if(discussId.isEmpty || discussId.getOrElse(0) ==0 ){
      Ok(Json.obj("code" -> "104", "message" ->"discuss id is not correct"))
    }else{
      if(!request.session.get("discuss_"+discussId.get).isEmpty){
        Ok(Json.obj("code" -> "104", "message" ->"support"))
      } else {
        DiagramDiscussSQLDao.updateLoveNum(discussId.get,1)
        val key ="discuss_"+discussId.get.toString
        val value=discussId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( request.session + (key -> value))
      }
    }
  }

  /* add hate num */
  def disfavorDiscuss = Action(parse.json){  implicit request =>

    val discussId = (request.body \ "discussId").asOpt[Long]
    if(discussId.isEmpty || discussId.getOrElse(0) ==0 ){
      Ok(Json.obj("code" -> "104", "message" ->"discuss id is not correct"))
    }else{
      if(!request.session.get("discuss_"+discussId.get).isEmpty){
        Ok(Json.obj("code" -> "104", "message" ->"support"))
      } else {
        DiagramDiscussSQLDao.updateHateNum(discussId.get,1)
        val key ="discuss_"+discussId.get.toString
        val value=discussId.get.toString
        Ok(Json.obj("code" -> "100", "message" ->"success")).withSession( request.session + (key -> value))
      }
    }
  }





}
