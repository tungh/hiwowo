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
object WeixinDiagramDao {

  val weixinDiagrams = TableQuery[WeixinDiagrams]
  val diagrams = TableQuery[Diagrams]
  val diagramPics = TableQuery[DiagramPics]
  val users = TableQuery[Users]

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
  def findDiagrams(period:Long):List[(Diagram,DiagramPic,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
   ( for{
      c<-weixinDiagrams
      d<-diagrams
      p<-diagramPics
      u<-users
      if c.period === period
      if c.diagramId === d.id
      if d.id === p.diagramId
      if d.uid === u.id
    }yield(d,p,u)).list()
  }

  def findDiagrams(currentPage:Int,pageSize:Int):Page[(Diagram,DiagramPic,User)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
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
      p<-diagramPics
      u<-users
      if c.diagramId === d.id
      if d.id === p.diagramId
      if d.uid === u.id
    }yield(d,p,u)).drop(startRow).take(pageSize).sortBy(_._1.id desc).list()

    Page[(Diagram,DiagramPic,User)](list,currentPage,totalPages)
  }

  def findWeixinDiagrams(id:Long):List[WeixinDiagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-weixinDiagrams if c.id === id )yield c).list()
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
    }yield(w,d)).drop(startRow).take(pageSize).sortBy(_._2.id desc).list()

    Page[(WeixinDiagram,Diagram)](list,currentPage,totalPages)
  }

}
