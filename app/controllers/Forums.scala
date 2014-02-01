package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.forum.dao.TopicDao
import play.api.data.Form
import play.api.data.Forms._
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import models.forum.Topic

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-22         hiwowo.com
 * Time: 下午11:29
 */
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
          val (t,u) = TopicDao.findById(id)
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
        val goodsImages =doc.body().getElementsByClass("img-goods")
        val uploadImages= doc.body().getElementsByClass("img-upload")
        var pics =""
        val it=uploadImages.iterator()
        while(it.hasNext){
          pics +=it.next().attr("src")+","
        }
        val it2= goodsImages.iterator()
        while(it2.hasNext){
          pics +=it2.next().attr("src")+","
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

  def view(id: Long) = Users.UserAction{ user => implicit request =>
      val topic=TopicDao.findById(id)
      Ok(views.html.forums.view(user,topic))

  }


  def search = Users.UserAction{ user => implicit request =>
         Ok(" forum search ")
  }
}
