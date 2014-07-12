package models.advert.dao

import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._

/**
* Created by zuosanshao.
* Email:zuosanshao@qq.com
* Since:12-12-26下午7:16
* ModifyTime :
* ModifyContent:
* http://www.hiwowo.com/
*
*/
object AdvertSQLDao {


  def updateClickNum(advertId:Long,num:Int)=play.api.db.slick.DB.withSession{ implicit session:Session =>
    sqlu"update advert set click_num =click_num+$num where id =$advertId".first
  }

}
