package models.cms.dao

import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import play.api.db._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-4
 * Time: 下午5:43
 */
object CmsSQLDao {



  def updateViewNum(id:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update cms set view_num = view_num+$num where id =$id".first

  }
}
