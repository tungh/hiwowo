package controllers.users


import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import play.api.libs.functional.syntax._
import play.api.libs.json._


import models.user.dao.UserDao
import java.sql.Timestamp
import models.user.User




/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-21
 * Time: 上午10:35
 * ***********************
 * description:用于类的说明
 */
case  class BaseFormData(
                          nickName:String,
                          email:Option[String],
                          sex:Int,
                          year:Option[String],
                          month:Option[String],
                          day:Option[String],
                          blog:Option[String],
                          qq:Option[String],
                          weixin:Option[String],
                          intro:Option[String]
                          )

object UsersAccount  extends Controller {

  val passwordForm = Form(
    tuple(
      "oldPassword" ->nonEmptyText,
      "password" -> nonEmptyText,
      "password2" -> nonEmptyText
    ) verifying ("两次密码输入不一致……", fields => fields._2 == fields._3)
  )
  val addrForm =Form(
    tuple(
      "trueName" ->optional(text),
      "province" -> optional(text),
      "city" -> optional(text),
      "addressDetail" ->optional(text),
      "postcode" -> optional(text),
      "cellphone" ->optional(text)
    )
  )

  val baseForm =Form(
    mapping(
      "nickname"-> text,
      "email"->optional(text),
      "sex" -> number,
      "year"->optional(text),
      "month"->optional(text),
      "day"->optional(text),
      "blog"->optional(text),
      "qq"->optional(text),
      "weixin"->optional(text),
      "intro" ->optional(text)
    )(BaseFormData.apply)(BaseFormData.unapply)
  )




  /* user account 用户账户 基本信息 */
  def base = Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {

   val profile =UserDao.findProfile(user.get.id.get)
      Ok(views.html.users.account.base(user,baseForm.fill(BaseFormData(user.get.name,user.get.email,profile.gender,profile.birth,None,None,profile.blog,profile.qq,user.get.weixin,user.get.intro))))
    }

  }
  /* user account 用户账号 基本信息 保存*/
  def saveBase =Users.UserAction {user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
      baseForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.users.account.base(user,formWithErrors)) ,
        data =>{
          /*uid:Long,name:String,intro:String,email:String,gender:Int, birth:String, province:String, city:String, blog:String, qq:String*/
          val birth:String=data.year.getOrElse("")+"|"+data.month.getOrElse("")+"|"+data.day.getOrElse("")
          UserDao.modifyBase(user.get.id.get,data.nickName,data.email.getOrElse(""),data.intro.getOrElse(""),data.sex,birth,data.blog.getOrElse(""),data.qq.getOrElse(""),data.weixin.getOrElse(""))
          Ok(views.html.users.account.base(user,baseForm.fill(data),"基本资料保存成功"))
        }
      )

    }
  }

  /* user account 用户账户 修改密码 */
  def password = Users.UserAction { user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else   Ok(views.html.users.account.password(user,passwordForm))
  }
  /* user account 用户账号 修改密码 保存*/
  def modifyPassword  = Users.UserAction { user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else  {
      passwordForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.users.account.password(user,formWithErrors)),
        fields =>{
          /*判断旧密码是否正确*/
          val oldUser =UserDao.authenticate(user.get.email.get,fields._1)
          if(oldUser.isEmpty) Ok(views.html.users.account.password(user,passwordForm,"当前密码不正确"))
          else {
          UserDao.modifyPassword(user.get.id.get,fields._2)
            Ok(views.html.users.account.password(user,passwordForm,"密码修改成功"))
          }
        }
      )
    }
  }




  /* user account 用户账户 收货地址 */
  def address = Users.UserAction {   user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else {
    val profile =UserDao.findProfile(user.get.id.get)
    Ok(views.html.users.account.address(user,addrForm.fill((profile.receiver,profile.province,profile.city,profile.street,profile.postCode,profile.phone))))

    }
  }
  /* user account 用户账户 收货地址  保存*/
  def saveAddress= Users.UserAction {   user => implicit request =>
    if(user.isEmpty)   Redirect(controllers.users.routes.UsersRegLogin.login)
    else  {
      addrForm.bindFromRequest.fold(
        formWithErrors => {
        val profile =UserDao.findProfile(user.get.id.get)
         BadRequest(views.html.users.account.address(user,formWithErrors.fill((profile.receiver,profile.province,profile.city,profile.street,profile.postCode,profile.phone))))
        } ,
        fields =>{
         UserDao.modifyAddr(user.get.id.get, fields._1.getOrElse(""),fields._2.getOrElse(""),fields._3.getOrElse(""),fields._4.getOrElse(""),fields._5.getOrElse(""),fields._6.getOrElse(""))
          Ok(views.html.users.account.address(user,addrForm.fill(fields._1,fields._2,fields._3,fields._4,fields._5,fields._6),"收货地址保存成功"))

        }
      )
    }
  }

  /* user account 用户账户 我的奖品 */
  def myAward = Users.UserAction {    user => implicit request =>
    if(user.isEmpty)  Redirect(controllers.users.routes.UsersRegLogin.login)
    else  Ok(views.html.users.account.myAward(user))

  }


  /* vip 认证 */
  def vip = Users.UserAction{ user => implicit request =>
    Ok(views.html.users.account.vip(user) )
  }


}
