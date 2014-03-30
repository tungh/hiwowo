package controllers.users

import play.api.mvc.{AnyContent, Action, Controller}
import play.api.data._
import play.api.data.Forms._
import models.user.User
import  models.user.dao.UserDao
import play.api.cache.Cache
import play.api.Play.current
import play.api.libs.json.Json._
import play.api.libs.json.Json
import play.api.Play
import org.apache.http.client.methods.{HttpGet, HttpPost}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils


/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-15上午9:15
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/

object UsersSnsLogin extends Controller {
  private def taobaoAppkey = Play.maybeApplication.flatMap(_.configuration.getString("application.taobao_appkey")).getOrElse("21136607")

  /*
  * 第三方登录 目前只开发qq weibo taobao
  * */
  def snsLogin(snsType:String,backType:String,id:Long)=Action{ implicit  request=>
    if(snsType=="qzone"){
      Redirect(" https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101050057&state=qq&redirect_uri=http://hiwowo.com/user/qzone/registed/"+backType+"/"+id)
    }else if (snsType=="sina"){
      Redirect("https://api.weibo.com/oauth2/authorize?client_id=1464940004&response_type=code&redirect_uri=http://hiwowo.com/user/sina/registed/"+backType+"/"+id)
    }
    else{
      Ok("亲，我们只支持qq帐号登录和新浪微博登陆哦…… ")
    }

  }
  /* i  inviteId */
  def  registed(snsType:String,code:String,backType:String,i:Long): Action[AnyContent] =Action{   implicit request =>

      /*
      * qzone 登录 第一步获取token 第二步 获取用户的openId  第三步 获取用户信息  第四步 处理用户信息
      * */
    if(snsType=="qzone"){
       val get = new HttpGet("https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101050057&client_secret=6bf5c992099d3449721eafe263525ef0&code="+code+"&redirect_uri=http://hiwowo.com/user/qzone/registed&state=qq")
       val client =new DefaultHttpClient()
       val resp= client.execute(get)
       val entity=resp.getEntity
       val r=EntityUtils.toString(entity)
    //   val json=Json.parse(r)
       val token =r.split("&").head.split("=").last


       val getMe = new HttpGet("https://graph.qq.com/oauth2.0/me?access_token="+token)
       val client3 =new DefaultHttpClient()
       val resp3 = client3.execute(getMe)
       val entity3=resp3.getEntity
       val me=EntityUtils.toString(entity3)
       val json =Json.parse(me.split(" ")(1))
       val openId = (json \ "openid").as[String]

       val getInfo = new HttpGet("https://graph.qq.com/user/get_user_info?access_token="+token+"&oauth_consumer_key=101050057&openid="+openId+"&format=json")
       getInfo.addHeader("charset","UTF-8")
       val client2 =new DefaultHttpClient()
       val resp2= client2.execute(getInfo)
   //    resp2.getAllHeaders.foreach(x=>println (x.getName  + " : " +x.getValue))

       val entity2=resp2.getEntity
       //println("ssssssssssssssssssssssssssss"+EntityUtils.getContentCharSet(entity2))
       val info=EntityUtils.toString(entity2,"UTF-8")

       val json2 =Json.parse(info)
       val nickName= (json2 \ "nickname").as[String]
       val pic = (json2 \ "figureurl_1").as[String]
       /*查找用户信息*/
       val user =UserDao.checkSnsUser(1,openId)
       var uid:Long = 0
       if (user.isEmpty){
        uid = UserDao.addSnsUser(nickName,1,openId,pic,i)
       }else {
         uid = user.get.id.get
       }
        /* 处理result */
       if (backType=="asyn"){
         Ok(views.html.users.snsLogin.asynLogin("qzone")).withSession("user" -> uid.toString)
       }else{
         Ok(views.html.users.snsLogin.synLogin("qzone")).withSession("user" -> uid.toString)
       }
     }
     /* 新浪微博登陆 */
     else if(snsType=="sina"){
       val get = new HttpPost("https://api.weibo.com/oauth2/access_token?client_id=1464940004&client_secret=52cb22ab45f9764a3b329578c70b89df&grant_type=authorization_code&redirect_uri=http://hiwowo.com/user/sina/registed&state=sina&code="+code)
       val client =new DefaultHttpClient();
       val resp= client.execute(get)
       val entity=resp.getEntity;
       val r=EntityUtils.toString(entity)
       val json =Json.parse(r);
       val assessToken=(json \ "access_token").as[String];
       val openId =(json \ "uid").as[String];
       val getInfo = new HttpGet("https://api.weibo.com/2/users/show.json?access_token="+assessToken+"&uid="+openId)
       val client2 =new DefaultHttpClient();
       val resp2= client2.execute(getInfo)
       val entity2=resp2.getEntity;
       //println("ssssssssssssssssssssssssssss "+EntityUtils.getContentCharSet(entity2))
       val r2=EntityUtils.toString(entity2)
       //println(r2)
       val json2 =Json.parse(r2)
       val nickName = (json2 \ "name").as[String]
       val pic = (json2 \ "profile_image_url").as[String]

        /* 查找用户信息 */
        /*查找用户信息*/
        val user =UserDao.checkSnsUser(2,openId)
       var uid:Long = 0
       if (user.isEmpty){
       uid =  UserDao.addSnsUser(nickName,2,openId,pic,i)
       }else {
         uid = user.get.id.get
       }
       /* 处理result */
       if (backType=="asyn"){
         Ok(views.html.users.snsLogin.asynLogin("sina")).withSession("user" -> uid.toString)
       }else{
         Ok(views.html.users.snsLogin.synLogin("sina")).withSession("user" -> uid.toString)
       }

     } else{
       Ok("可能获取token出错了")
     }

  }


}
