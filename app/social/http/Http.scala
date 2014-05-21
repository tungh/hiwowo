package social.http


import org.apache.http.{Header, HttpResponse, HttpEntity, NameValuePair}
import java.util
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.apache.http.client.methods.{HttpPost, HttpGet, HttpRequestBase}
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.message.BasicNameValuePair


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-5-20
 * Time: 上午12:04
 */


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

  def addParameter(params: util.ArrayList[NameValuePair], name:String, value: String) = {
    params.add(new BasicNameValuePair(name, value.toString))
  }
  /* 封装常用的方法 */
  def get(uri:String,params:util.ArrayList[NameValuePair]):String={
    var url: String = uri
    if (params != null) {
      val param: String = params.toArray().toList.mkString("&")
      if (url.contains("?")) {
        url = url + "&" + param
      }
      else {
        url = url + "?" + param
      }
    }
    get(url)
  }
  def get(url:String):String ={
    get(url)
  }

  def get(uri:String, headers: Header*): String = {
    val request: HttpGet = new HttpGet(uri)
    if (headers != null) {
      for (header <- headers) {
        request.addHeader(header)
      }
    }
     execute(request)
  }

  def post(url: String, params: util.ArrayList[NameValuePair]): String = {
    post(url, params, "UTF-8")

  }
  def post(uri: String, postBody: HttpEntity,headers:Header*):String ={
    val request: HttpPost = new HttpPost(uri)
    if (postBody != null) {
      request.setEntity(postBody)
    }
    if (headers != null) {
      for (header <- headers) {
        request.addHeader(header)
      }
    }
    execute(request)
  }

  def post(uri:String, params: util.ArrayList[NameValuePair], charset:String, headers: Header*):String= {
    val request: HttpPost = new HttpPost(uri)
    if (params != null) {
      val parameters: util.ArrayList[NameValuePair] = new util.ArrayList[NameValuePair]
      parameters.addAll(params)

        val entity: HttpEntity = new UrlEncodedFormEntity(parameters, charset)
        request.setEntity(entity)
    }
    if (headers != null) {
      for (header <- headers) {
        request.addHeader(header)
      }
    }
    return execute(request)
  }

  def post(uri: String): String = {
    return post(uri, null.asInstanceOf[HttpEntity])
  }


}
