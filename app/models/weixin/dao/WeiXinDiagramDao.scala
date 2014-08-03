package models.weixin.dao


import play.api.Play.current
import models.user._
import models.diagram._
import models.weixin.{WeixinDiagramTable, WeixinDiagram}
import play.api.db.slick.Config.driver.simple._
import models.Page
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午2:26
 */
object WeixinDiagramDao {

  val weixinDiagrams = TableQuery[WeixinDiagramTable]
  val diagrams = TableQuery[DiagramTable]
  val diagramPics = TableQuery[DiagramPicTable]
  val users = TableQuery[UserTable]

  /* 专门为微信挑选的diagram */
  def addDiagram(diagramId:Long,period:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val weixinDiagramAutoInc = weixinDiagrams.map( c =>(c.diagramId,c.period))  returning weixinDiagrams.map(_.id) into {
      case (_, id) => id
    }
    weixinDiagramAutoInc.insert(diagramId,period)
  }
  def deleteDiagram(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams if c.id === id) yield c).delete
  }
  def updateDiagram(id:Long,diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams if c.id === id) yield c.diagramId ).update(diagramId)
  }
  /* 根据期数来查找diagram */
  def findDiagrams(period:Long):List[(Diagram,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
   ( for{
      c<-weixinDiagrams
      d<-diagrams
      u<-users
      if c.period === period
      if c.diagramId === d.id
      if d.uid === u.id
    }yield(d,u)).list
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
    }yield(d,u)).drop(startRow).take(pageSize).sortBy(_._1.id desc).list

    Page[(Diagram,User)](list,currentPage,totalPages)
  }

  def findWeixinDiagrams(id:Long):List[WeixinDiagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams if c.id === id )yield c).list
  }
  def findWeixinDiagrams(currentPage:Int,pageSize:Int):Page[(WeixinDiagram,Diagram)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(weixinDiagrams.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val list =(for{
      w <- weixinDiagrams
      d <- diagrams
      if w.diagramId === d.id
    }yield(w,d)).drop(startRow).take(pageSize).sortBy(_._2.id desc).list

    Page[(WeixinDiagram,Diagram)](list,currentPage,totalPages)
  }

}
