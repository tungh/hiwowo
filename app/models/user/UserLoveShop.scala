package models.user

import java.sql.Timestamp
import scala.slick.driver.MySQLDriver.simple._


/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午2:05
 * ***********************
 * description: 用户喜欢的小店
 */

case class UserLoveShop (
                           id: Option[Long],
                           uid:Long,
                           shopId:Long,
                           addTime:Option[Timestamp]
                           )

class UserLoveShops(tag:Tag) extends Table[UserLoveShop](tag,"user_love_shop") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def shopId = column[Long]("shop_id")
  def addTime = column[Timestamp]("add_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?,uid,shopId,addTime.?)  <> (UserLoveShop.tupled, UserLoveShop.unapply )


}

