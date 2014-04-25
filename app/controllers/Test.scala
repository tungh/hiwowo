package controllers

import play.api.mvc.Controller
import controllers.users.Users
import models.user.dao.UserDao

import models.user.User
import models.label.dao.LabelDao
import org.ansj.domain.Term
import org.ansj.splitWord.analysis.ToAnalysis
import java.util
import org.ansj.app.keyword.{Keyword, KeyWordComputer}


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-31
 * Time: 下午1:35
 */
object Test extends Controller {

  def index = Users.UserAction{ user => implicit request =>
  /*  val users = UserDao.findAll(1,10)
    for(user <- users.items){
      println(user.name)
    }
  val (user,userStatic) = UserDao.findUserWithStatic(2)
  println(user.name + " : " + userStatic.uid)*/
//    TermDao.addTerm("hiwowo","嗨喔喔","这里是简介，有可能会被用上，也可以用作为 备注",3)
//val term   = TermDao.checkTerm("hiwowo")
//  if(term.isEmpty) println("term not exists") else println("term exists")
//    TermDao.addTermRelation(1,1,0)
    val parse:util.List[Term] = ToAnalysis.parse("让战士们过一个欢乐祥和的新春佳节。")
  //  println(parse)
    val kwc:KeyWordComputer = new KeyWordComputer(10)
    val title:String = "表情太生动 英国唱歌猫咪红遍网络"
   val content:String = "近日，一则由英国移动运营商 Three 所发布的视频广告在互联网上获得了巨大的人气和超过百万的点击量，原因就是广告中唱歌的喵咪表情太生动了、太丰富了，简直让人难以置信(广告视频)。\n\n　　在这则广告中，一只可爱的猫咪在和一个五岁大的小女孩在一起引吭高歌老电影 Starships 的主题曲 We Built This City，猫咪乖巧地配合唱歌，表情相当丰富且惟妙惟肖，以致许多网友怀疑猫咪是使用电脑合成的。\n\n　　但其实这只猫是真实存在的，它的名字叫 Bronte，来自英国沃里克郡的小镇拉格比，其主人为 Caroline 和 Lyndon Bailey，据说拍摄这则广告时它只有 3 个月大，而现在它已经是红遍网络的大明星了。";
    val result:util.Collection[Keyword] = kwc.computeArticleTfidf(title, content)

    Ok(" Ok! " + result)
  }


}
