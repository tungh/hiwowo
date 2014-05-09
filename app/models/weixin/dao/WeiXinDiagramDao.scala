package models.weixin.dao


import play.api.Play.current
import models.user._
import models.diagram._
import models.weixin.{WeixinDiagrams, WeixinDiagram}
import play.api.db.slick.Config.driver.simple._
import models.Page
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午2:26
 */
object WeiXinDiagramDao {

  val weixinDiagrams = TableQuery[WeixinDiagrams]
  val diagrams = TableQuery[Diagrams]
  val users = TableQuery[Users]

  /* 专门为微信挑选的diagram */
  def addDiagram(diagramId:Long,period:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams)yield(diagramId,period)).insert(diagramId,period)
  }
  def deleteDiagram(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams if c.id === id) yield c).delete
  }
  def updateDiagram(id:Long,diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams if c.id === id) yield c.diagramId ).update(diagramId)
  }
  /* 根据期数来查找diagram */
  def findDiagrams(period:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
   ( for{
      c<-weixinDiagrams
      d<-diagrams
      u<-users
      if c.period === period
      if c.diagramId === d.id
      if d.uid === u.id
    }yield(d,u)).list()
  }

  def findDiagrams(currentPage:Int,pageSize:Int):Page[(Diagram,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(weixinDiagrams.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
   val list= (for{
      c<-weixinDiagrams
      d<-diagrams
      u<-users
      if c.diagramId === d.id
      if d.uid === u.id
    }yield(d,u)).drop(startRow).take(pageSize).sortBy(_._1.addTime desc).list()

    Page[(Diagram,User)](list,currentPage,totalPages)
  }



}
