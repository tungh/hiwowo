package models.user

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-19
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
object CreditSetting {
    /* 连续签到 获得额外积分 */
  val checkIn7Days:Int = 15
  val checkIn15Days:Int = 30
  val checkIn30Days:Int = 60
  val checkIn60Days:Int = 100
  val checkIn100Days:Int = 200
  val checkIn200Days:Int = 400
  val checkIn365Days:Int = 1000

  /*发布优质日志 10*/
  val postHotBlog:Int = 10
  /*发布优质图片 10*/
  val postHotPic:Int = 1
  /*创建优质视频 10*/
  val postHotVideo:Int = 5

}
