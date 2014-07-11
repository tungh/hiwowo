package controllers

import play.api.mvc.{Action, Controller}
import controllers.users.Users
import models.user.dao.UserDao

import models.user.User
import models.label.dao.LabelDao
import org.ansj.domain.Term
import org.ansj.splitWord.analysis.ToAnalysis
import java.util
import org.ansj.app.keyword.{Keyword, KeyWordComputer}
import play.mvc.Http
import org.apache.http.NameValuePair
import play.api.libs.Crypto
import java.sql.Timestamp


/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-31
 * Time: 下午1:35
 */
object Test extends Controller {

  def test = Users.UserAction{ user => implicit request =>
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

 //   val result = UserDao.findUser(1)
      val interval= utils.Utils.getInterval(new Timestamp(1000000000))
    Ok(" Ok! code:  "+interval)
  }

  def ui =  Users.UserAction{ user => implicit request =>
    Ok("ok")
  }

}
