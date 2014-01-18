package models.site

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

/*
* status 0 草稿  1 发布 2 优质
* */
case  class  SiteBlog(
                  id: Option[Long],
                  uid:Long,
                  sid:Long,
                  title:String,
                  pic:Option[String],
                  content: String,
                  tags:Option[String],
                  status:Int,
                  isTop:Int,
                  viewNum:Int,
                  loveNum:Int,
                  replyNum:Int,
                  addTime:Option[Timestamp]
                  )

