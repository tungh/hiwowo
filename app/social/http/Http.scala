package utils.social.http


import org.apache.http.{HttpResponse, HttpEntity, NameValuePair}
import java.util
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.{HttpGet, HttpRequestBase}


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-5-20
 * Time: 上午12:04
 */
class Http {
   /* 封装常用的方法 */
  def get(url:String,params:util.ArrayList[NameValuePair]):String={

     "todo"
   }
  def get(url:String):String ={
    "todo"
  }


 def post(url: String, postBody: HttpEntity):String ={
   "todo"
 }
  def post(url:String,params:util.ArrayList[NameValuePair]):String={

    "todo"
  }
}

object  Http{

  /* 创建 httpClient */
  def httpClient:HttpClient={ new DefaultHttpClient()  }

  /* 以UTF-8方式 */
  def responseToString(response:HttpResponse):String={
    val entity: HttpEntity = response.getEntity
    EntityUtils.toString(entity,"UTF-8")
  }
  /* execute request */
 def  execute(request: HttpRequestBase)={
        val response: HttpResponse = httpClient.execute(request)
         responseToString(response)
    }



}
