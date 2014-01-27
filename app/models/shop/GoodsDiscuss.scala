package models.goods

import java.sql.Timestamp
import play.api.db._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._
import models.Page
import models.user.{User,Users}

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:10
 * ***********************
 * description:宝贝的评论
 */

case class GoodsDiscuss(
                        id: Option[Long],
                        uid:Long,
                        goodsId:Long,
                        content: String,
                        isWorth:Boolean,
                        isBought: Boolean,
                        checkState:Int,
                        addTime:Option[Timestamp]
                        )

class GoodsDiscusses(tag:Tag) extends Table[GoodsDiscuss](tag,"goods_discuss") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def goodsId = column[Long]("goods_id")
  def content = column[String]("content")
  def isWorth =column[Boolean]("is_worth")
  def isBought =column[Boolean]("is_bought")
  def checkState = column[Int]("check_state")
  def addTime=column[Timestamp]("add_time")
  def * = (id.?, uid,  goodsId , content, isWorth, isBought, checkState, addTime.?) <>(GoodsDiscuss.tupled, GoodsDiscuss.unapply)

}

