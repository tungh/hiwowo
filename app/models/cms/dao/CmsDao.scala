package models.cms.dao

import java.sql.Timestamp
import play.api.Play.current

import models.Page
import models.cms.{Cmses, Cms}
import play.api.db.slick.Config.driver.simple._

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-4
 * Time: 下午5:43
 */
object CmsDao {
  val cmses = TableQuery[Cmses]
  def deleteCms(id:Long) =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( c <- cmses if c.id === id) yield c).delete
  }
  def modifyCms(cms:Cms) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c <- cmses if c.id === cms.id) yield c ).update(cms)
  }

}
