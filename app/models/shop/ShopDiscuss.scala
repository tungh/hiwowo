package models.shop




import scala.slick.driver.MySQLDriver.simple._
import java.sql.Timestamp

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:14
 * ***********************
 * description:主题评论
 */

case class ShopDiscuss(
                        id: Option[Long],
                        uid:Long,
                        shopId:Long,
                        quoteContent:Option[String],
                        content:String,
                        checkState:Int,
                        addTime:Option[Timestamp]
                        )
class ShopDiscusses(tag:Tag)  extends  Table[ShopDiscuss](tag,"shop_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Long]("uid")
  def shopId  =column[Long]("shop_id")
  def quoteContent = column[String]("quote_content")
  def content = column[String]("content")
  def checkState = column[Int]("check_state")
  def addTime = column[Timestamp]("add_time")
  
  def * =(id.? , uid, shopId , quoteContent.?, content, checkState, addTime.? ) <>(ShopDiscuss.tupled, ShopDiscuss.unapply )

}



