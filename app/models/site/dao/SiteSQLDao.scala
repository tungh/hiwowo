package models.site.dao

import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-27下午8:05
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/
object SiteSQLDao {
  lazy val database = Database.forDataSource(DB.getDataSource())

 /* 添加  blog num */
  def updateSiteBlogNum(id:Long,num:Int)=database.withDynSession {
    sqlu"update site set blog_num = blog_num+$num where id =$id".first
  }
  def updateSitePicNum(id:Long,num:Int)=database.withDynSession {
    sqlu"update site set pic_num = pic_num+$num where id =$id".first
  }
  def updateSiteVideoNum(id:Long,num:Int)=database.withDynSession {
    sqlu"update site set video_num = video_num+$num where id =$id".first
  }
}
