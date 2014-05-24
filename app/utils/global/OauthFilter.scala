package utils.global

import play.api.mvc.Cookie
import play.api.mvc.Cookies
import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.Crypto


object OauthFilter extends Filter {
  def apply(nextFilter: (RequestHeader) => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {

    nextFilter(requestHeader).map {
      result =>

        result.withHeaders("Request-Time" -> "ssss")
    }
  }
}
