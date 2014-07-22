package models.user

/**
 * Created by zuosanshao.
 * User: zuosanshao
 * Date: 12-9-30
 * Time: 下午9:41
 * Email:zuosanshao@qq.com
 *  用户状态说明
 */

object UserStatus extends Enumeration {
  val  NEWMAN   = Value("注册用户")
  val  NORMAL   = Value("普通用户")
  val ACTIVE  = Value("申请VIP用户")
  val VIP = Value("VIP用户")
  val BLACK = Value("黑名单用户")
}
