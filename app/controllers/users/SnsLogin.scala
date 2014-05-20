package controllers.users

import play.api.mvc.{Action, Controller}

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-5-19
 * Time: 下午9:12
 */
object SnsLogin extends Controller {

  def login(comeFrom:String,backType:String)=Action{ implicit  request=>
    if(comeFrom=="qq"){

      Ok("todo")
    }else if (comeFrom=="weibo"){
      val getInfo = new HttpGet("https://api.weibo.com/oauth2/authorize?client_id=464981938&response_type=code&redirect_uri=/user/sns/redirect")
      val client2 =new DefaultHttpClient()
      val resp2= client2.execute(getInfo)
      val entity2=resp2.getEntity
      val r2=EntityUtils.toString(entity2)
      println(r2)
      Ok("todo")
    }
    else{
      Ok("亲，我们只支持qq帐号登录和新浪微博登陆哦…… ")
    }
  }
  def redirect(code:String,comeFrom:String,backType:String)=Action{ implicit  request=>

    Ok("todo")
  }

}
