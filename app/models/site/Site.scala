package models.site


import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:19
 * ***********************
 * description: 小镇
 * status 0 未审核  1 通过审核 2 优质
 */

case class Site(
                id: Option[Long],
                uid:Long,
                cid:Int,
                title:String,
                pic:String,
                bgImg:String,
                intro: String,
                tags:String,
                status:Int,
                notice:Option[String],
                modifyTime:Option[Timestamp],
                addTime:Option[Timestamp]
                )



