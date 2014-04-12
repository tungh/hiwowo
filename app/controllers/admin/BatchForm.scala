package controllers.admin

import play.api.data.Form
import play.api.data.Forms._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-12
 * Time: 下午10:03
 */
case class BatchFormData(action:Int,ids:Seq[Long],url:Option[String])
object BatchFormData{
  val batchForm =Form(
    mapping(
      "action"->number,
      "ids"->seq(longNumber),
      "url"->optional(text)
    )(BatchFormData.apply)(BatchFormData.unapply)
  )
}
