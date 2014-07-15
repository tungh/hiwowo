package utils.global

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:13-2-18下午7:19
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future

object Global extends GlobalSettings {
  override def doFilter(next: EssentialAction): EssentialAction = {
    Filters(super.doFilter(next), OauthFilter)
  }
  override def onStart(app: Application) {
   val schedule = Play.maybeApplication.flatMap(_.configuration.getString("schedule")).getOrElse("off")
     if(schedule=="on"){

     }

  }
//  When an exception occurs in your application, the onError operation will be called
/*
 override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(InternalServerError(
      views.html.common.global.error()
    ))
  }
 //   If the framework doesn’t find an Action for a request, the onHandlerNotFound operation will be called:
  override def onHandlerNotFound(request: RequestHeader) = {
   Future.successful( NotFound(
   //   views.html.common.common.notFound()
      views.html.common.global.error()
    ) )
  }

 //  The onBadRequest operation will be called if a route was found, but it was not possible to bind the request parameters
  override def onBadRequest(request: RequestHeader, error: String) = {
   Future.successful( BadRequest(
    //  views.html.common.global.badRequest()
      views.html.common.global.error()
    ))
  }
*/


}