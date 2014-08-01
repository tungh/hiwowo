package models.diagram

/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-9-20
 * Time: 上午11:18
 * ***********************
 * description:话题类型
 */

object TushuoCatalog extends Enumeration{
  val TUSHUO = Value("图说")
  val TALK = Value("自言自语")
  val GIF = Value("gif神兽")
  val EMOTION = Value("表情帝")
  val OTHER = Value("其他")
}
