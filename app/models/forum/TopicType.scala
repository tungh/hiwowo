package models.forum

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:18
 * ***********************
 * description:话题类型
 */

object TopicType extends Enumeration{
  val Normal = Value("普通")
  val Knowledge = Value("知识")
  val Ask  =  Value("问答")
  val Activity =Value("活动")
}
