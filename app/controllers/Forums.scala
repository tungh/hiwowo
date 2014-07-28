package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.forum.dao.{TopicSQLDao, TopicDao}
import play.api.data.Form
import play.api.data.Forms._
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import models.forum.Topic
import play.api.cache.Cache
import play.api.Play.current
import models.user.User
import models.user.dao.UserDao
import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-22         hiwowo.com
 * Time: 下午11:29
 */

case class TopicComponent(
                     topic:Topic,
                     user:User
                           )

object Forums extends Controller {

  val topicForm = Form(
    tuple(
      "id"->optional(longNumber),
      "title" ->nonEmptyText ,
      "typeId" ->number,
      "content"->nonEmptyText
    )
  )
  val discussForm =Form(
    tuple(
      "topicId"->number,
      "content"->nonEmptyText,
      "quoteContent"->optional(text)
    )
  )
  var searchForm =Form(
    tuple(
      "typeId"->number,
      "text"->text
    )
  )
  /*编辑或者新增一个topic*/
  def edit(id: Long) = Users.UserAction { user => implicit request =>
     if (user.isEmpty) {
       Redirect(controllers.users.routes.UsersRegLogin.login)
     } else {
        if (id == 0) Ok(views.html.forums.edit(user, topicForm))
        else {
          val (t,u) = TopicDao.findTopic(id)
          Ok(views.html.forums.edit(user, topicForm.fill((t.id, t.title, t.typeId, t.content))))
        }
      }

  }

  /*保存 帖子*/
  def save = Users.UserAction{ user => implicit request =>
    topicForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.forums.edit(user,formWithErrors)),
      fields =>{
        var intro =Jsoup.clean(fields._4,Whitelist.none())
        if(intro.length()>100) intro =intro.substring(0,100)+"..."
        val  doc =Jsoup.parseBodyFragment(fields._4)
        val uploadImages =doc.body().getElementsByTag("img")
        var pics =""
        val it=uploadImages.iterator()
        while(it.hasNext){
          pics +=it.next().attr("src")+","
        }
        if (fields._1.isEmpty){
          val id= TopicDao.addTopic(user.get.id.get,fields._2,fields._4,intro,pics,fields._3,0)
          Redirect(controllers.routes.Forums.view(id))
        }else{
          TopicDao.modifyTopic(Topic(fields._1,user.get.id.get,fields._2,fields._4,intro,Some(pics.trim),fields._3,false,false,0,0,0,0,None))
          Redirect(controllers.routes.Forums.view(fields._1.get))
        }


      }
    )

  }
 /* topic view */
  def view(id: Long,p:Int,size:Int) = Users.UserAction{ user => implicit request =>
    /* 记录每个页面的点击次数，先放在cache里，当大于9时，记录到数据库中 */
      val (topic,author)=TopicDao.findTopic(id)
     val pageDiscusses = TopicDao.findDiscusses(id,p,size)

      Ok(views.html.forums.view(user,TopicComponent(topic,author),pageDiscusses))

  }
  /* add discuss */
  def addDiscuss =Action(parse.json){  implicit request =>
    val user:Option[User] =request.session.get("user").map(u=>UserDao.findById(u.toLong))
    if(user.isEmpty)Ok(Json.obj("code" -> "200", "message" ->"亲，你还没有登录哦" ))
    else if(user.get.status==4)Ok(Json.obj("code" -> "444", "message" -> "亲，你违反了社区规定，目前禁止评论"))
    else {
      val topicId = (request.body \ "topicId").asOpt[Long]
      val  content =(request.body \ "content").asOpt[String]
      val  quoteContent=(request.body \ "quoteContent").asOpt[String]
      if(content.isEmpty || topicId.isEmpty ){
        Ok(Json.obj("code" -> "101", "message" ->"亲，是不是没有输入内容？请重新提交试试"))
      }else{
        TopicDao.addDiscuss(user.get.id.get,topicId.get,quoteContent,content.get,1)
        Ok(Json.obj("code" -> "100", "message" ->"亲，评论成功"))
      }
    }

  }

  def forum(typeId:Int,sortBy:String,p:Int,size:Int) = Users.UserAction{ user => implicit request =>
    val page = TopicDao.filterTopics(typeId,sortBy,p,size)
    Ok(views.html.forums.forum(user,typeId,sortBy,page))
  }

  def search = Users.UserAction{ user => implicit request =>
         Ok(" forum search ")
  }
}
