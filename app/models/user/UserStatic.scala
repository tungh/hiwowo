package models.user

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午3:57
 * ***********************
 * description:用于类的说明    用户的统计
 */



import play.api.db._
import play.api.Play.current
import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._

case class UserStatic(
                      id: Option[Long],
                      uid: Long,
                      fansNum: Int,
                      followNum: Int,
                      loveSiteNum: Int,
                      loveBlogNum: Int,
                      lovePicNum: Int,
                      loveVideoNum: Int,
                      loveShopNum: Int,
                      ownSiteNum: Int,
                      ownBlogNum: Int,
                      ownPicNum: Int,
                      ownVideoNum: Int,
                      ownShopNum: Int,
                      ownTopicNum: Int
                      )
class UserStatics(tag:Tag) extends Table[UserStatic](tag,"user_static") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def fansNum = column[Int]("fans_num")
  def followNum = column[Int]("follow_num")
  def loveSiteNum = column[Int]("love_site_num")
  def loveBlogNum = column[Int]("love_blog_num")
  def lovePicNum = column[Int]("love_pic_num")
  def loveVideoNum = column[Int]("love_video_num")
  def loveShopNum = column[Int]("love_shop_num")
  def ownSiteNum = column[Int]("own_site_num")
  def ownBlogNum = column[Int]("own_blog_num")
  def ownPicNum = column[Int]("own_pic_num")
  def ownVideoNum = column[Int]("own_video_num")
  def ownShopNum = column[Int]("own_shop_num")
  def ownTopicNum = column[Int]("own_topic_num")
  // Every table needs a * projection with the same type as the table's type parameter
  def * =(id.?, uid, fansNum, followNum , loveSiteNum, loveBlogNum, lovePicNum, loveVideoNum, loveShopNum, ownSiteNum, ownBlogNum, ownPicNum, ownVideoNum, ownShopNum, ownTopicNum)  <>(UserStatic.tupled, UserStatic.unapply)
  
}



