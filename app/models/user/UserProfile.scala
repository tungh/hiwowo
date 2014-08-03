package models.user



import play.api.db._
import play.api.Play.current
import  java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 下午4:00
 * ***********************
 * description:用于类的说明   用户的详细数据
 */

case class UserProfile (
                         id: Option[Long],
                         uid: Long,
                         inviteId: Option[Long],
                         gender: Int,
                         birth: Option[String],
                         qq: Option[String],
                         receiver: Option[String],
                         province: Option[String],
                         city: Option[String],
                         town: Option[String],
                         street: Option[String],
                         postCode: Option[String],
                         phone: Option[String],
                         blog: Option[String],
                         loginTime:Option[Timestamp],
                         loginNum:Int,
                         loginIP: Option[String]
                         )
class UserProfileTable(tag:Tag) extends Table[UserProfile](tag,"user_profile") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc) // This is the primary key column
  def uid = column[Long]("uid")
  def inviteId = column[Long]("invite_id")
  def gender = column[Int]("gender")
  def birth = column[String]("birth")
  def qq = column[String]("qq")
  def receiver = column[String]("receiver")
  def province = column[String]("province")
  def city = column[String]("city")
  def town = column[String]("town")
  def street = column[String]("street")
  def postCode = column[String]("post_code")
  def phone = column[String]("phone")
  def blog = column[String]("blog")
  def loginTime = column[Timestamp]("login_time")
  def loginNum=column[Int]("login_num")
  def loginIP = column[String]("login_ip")

  def * = (id.?, uid, inviteId.? , gender , birth.?, qq.?, receiver.?, province.?, city.?, town.?, street.?, postCode.?, phone.?, blog.?, loginTime.?, loginNum, loginIP.?) <>(UserProfile.tupled, UserProfile.unapply)

}



