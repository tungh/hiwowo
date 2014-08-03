package models.label.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import play.api.db.slick.Config.driver.simple._
import models.label._
import models.diagram.{Diagrams, Diagram}
/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-25
 * Time: 下午10:30
 */
object LabelDao {

  val groups = TableQuery[Groups]
  val groupLabels = TableQuery[GroupLabels]
  val labels = TableQuery[Labels]
  val labelDiagrams = TableQuery[LabelDiagrams]
  val diagrams = TableQuery[Diagrams]

  /* label group */
  def addGroup(name:String,intro:Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val groupAutoInc = groups.map( t => (t.name,t.intro.?,t.status)) returning groups.map(_.id) into {
      case (_, id) => id
    }
    groupAutoInc.insert(name,intro,status)
  }
  def modifyGroup(id:Long,name:String,intro:Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-groups if c.id === id) yield(c.name,c.intro.?,c.status)).update(name,intro,status)
  }
  def modifyGroupStatus(id:Long,status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-groups if c.id === id) yield c.status).update(status)
  }

  def deleteGroup(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-groups if c.id === id) yield c ).delete
  }

  def findGroup(id:Long):Group = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-groups if c.id === id) yield c ).first
  }


  def findGroups(currentPage:Int,pageSize:Int):Page[Group] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(groups.length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
   val list = (for( c<-groups )yield c).drop(startRow).take(pageSize).list
    Page[Group](list,currentPage,totalPages)
  }
     /*  查找 group 下 的 标签 */
  def findGroupLabels(gid:Long):List[Label] =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for{
      g <- groups
      gl <- groupLabels
      l <- labels
     if g.id === gl.groupId
     if gl.labelId === l.id
     if g.id === gid
    }yield l).list

  }

  /* label dao */
  def addLabel(name:String,level:Int,intro:Option[String],isHot:Int,spell:Option[String],checkState:Int):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val labelsAutoInc = labels.map( t => (t.name, t.level, t.intro.?,t.isHot,t.spell.?,t.checkState)) returning labels.map(_.id) into {
      case (_, id) => id
    }
    labelsAutoInc.insert(name,level,intro,isHot,spell,checkState)
  }
  def addLabel(name:String,checkState:Int):Long = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val labelsAutoInc = labels.map( t => (t.name,t.checkState)) returning labels.map(_.id) into {
      case (_, id) => id
    }
    labelsAutoInc.insert(name,checkState)
  }

  def modifyLabelCheckState(id:Long,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( c <- labels if c.id === id )yield c.checkState ).update(checkState)
  }
  def modifyLabelCheckState(name:String,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( c <- labels if c.name === name )yield c.checkState ).update(checkState)
  }
  def modifyLabel(id:Long,name:String,level:Int,intro:Option[String],isHot:Int,spell:Option[String],checkState:Int) =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(c <- labels if c.id === id) yield (c.name,c.level,c.intro.?,c.isHot,c.spell.?,c.checkState)).update(name,level,intro,isHot,spell,checkState)
  }
  def deleteLabel(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( c <- labels if c.id === id ) yield c ).delete
    ( for(c <- labelDiagrams if c.id === id)yield c ).delete
  }
  def findLabelById(id:Long):Option[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(t <- labels if t.id === id) yield t ).firstOption
  }
  def findLabelByName(name:String):Option[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(t <- labels if t.name === name ) yield t ).firstOption
  }
 def  findAllLabels(currentPage:Int,pageSize:Int):Page[Label] =  play.api.db.slick.DB.withSession{ implicit session:Session =>
   val totalRows = Query(labels.length).first
   val totalPages = (totalRows + pageSize - 1) / pageSize
   val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
   val list = (for{
    c <- labels
 }yield c ).drop(startRow).take(pageSize).list

   Page[Label](list,currentPage,totalPages)
 }
  def findCoreLabels(level:Int,currentPage:Int,pageSize:Int):Page[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(labels.filter(_.level === level).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
    val list =( for{
      c <- labels
      if c.level === level
    }yield c ).drop(startRow).take(pageSize).list
    Page[Label](list,currentPage,totalPages)

  }
  def filterLabels(name: Option[String],level: Option[Int], isHot: Option[Int],spell: Option[String], checkState: Option[Int], currentPage: Int, pageSize: Int):Page[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>

    var query = for( c<-labels ) yield c
    if(!name.isEmpty) query = query.filter(_.name like "%"+name.get+"%")
    if(!level.isEmpty) query = query.filter(_.level === level.get)
    if(!isHot.isEmpty) query = query.filter(_.isHot === isHot.get)
    if(!spell.isEmpty) query = query.filter(_.spell like "%"+spell.get+"%")
    if(!checkState.isEmpty) query = query.filter(_.checkState === checkState)
    query = query.sortBy(_.id desc)
    val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val ts: List[Label] = query.drop(startRow).take(pageSize).list
    Page[Label](ts, currentPage, totalPages)
  }

  /* add group label */
  def addGroupLabel(groupId:Long,labelId:Long)= play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(t <- groupLabels) yield (t.groupId,t.labelId)).insert(groupId,labelId)
  }
  def deleteGroupLabel(groupId:Long,labelId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- groupLabels if t.groupId === groupId if t.labelId === labelId) yield t ).delete
  }
  def findGroupLabel(groupId:Long,labelId:Long):Option[GroupLabel] =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( c<-groupLabels if c.groupId === groupId if c.labelId === labelId )yield c ).firstOption
  }

  /* label diagram  */
  def addLabelDiagram(labelId:Long,diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( t<- labelDiagrams ) yield (t.labelId,t.diagramId)).insert(labelId,diagramId)
  }

  def findLabelDiagram(labelId:Long,diagramId:Long):Option[LabelDiagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<- labelDiagrams if c.labelId === labelId  if c.diagramId === diagramId) yield c ).firstOption
  }

  def deleteLabelDiagramByDiagramId(diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- labelDiagrams  if t.diagramId === diagramId) yield t ).delete
  }

  def deleteLabelDiagramByLabelId(labelId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- labelDiagrams if t.labelId === labelId) yield t ).delete
  }

  def deleteLabelDiagram(labelId:Long,diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- labelDiagrams if t.labelId === labelId if t.diagramId === diagramId) yield t ).delete
  }
  def modifyLabelDiagramCheckState(labelId:Long,diagramId:Long,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- labelDiagrams if t.labelId === labelId if t.diagramId === diagramId) yield t.checkState ).update(checkState)
  }
   /* 查找label 下的所有diagram */
  def findLabelDiagrams(labelId:Long,currentPage:Int,pageSize:Int):Page[(LabelDiagram,Diagram)] = play.api.db.slick.DB.withSession{ implicit session:Session =>
     val totalRows = Query(labelDiagrams.filter(_.labelId === labelId).length).first
     val totalPages = (totalRows + pageSize - 1) / pageSize
     val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
     val list = ( for{
       ld <- labelDiagrams
       d <- diagrams
       if ld.diagramId === d.id
       if ld.labelId === labelId
     } yield (ld,d) ).drop(startRow).take(pageSize).list
     Page[(LabelDiagram,Diagram)](list,currentPage,totalPages)
   }
  def findLabelDiagrams(labelId:Long,checkState:Int,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val totalRows = Query(labelDiagrams.filter(_.labelId === labelId).filter(_.checkState === checkState).length).first
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
    val list = ( for{
         ld <- labelDiagrams
         d <- diagrams
        if ld.diagramId === d.id
        if ld.checkState === checkState
        if ld.labelId === labelId
    } yield d ).drop(startRow).take(pageSize).list
    Page[Diagram](list,currentPage,totalPages)
  }
  /* 查找label 下 不同type 的 diagram */
  def findLabelDiagrams(labelId:Long,typeId:Int,checkState:Int,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
   val query =(for{
      ld <- labelDiagrams
     d <- diagrams
     if ld.diagramId === d.id
     if ld.labelId === labelId
     if ld.checkState === checkState
     if d.typeId === typeId
   }yield d)
     val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
    val list= query.drop(startRow).take(pageSize).list
    Page[Diagram](list,currentPage,totalPages)
  }

  /* 过滤 label 下的 所有 diagram */
  def filterLabelDiagrams(labelId:Long,title:Option[String],checkState:Option[Int],typeId:Option[Int],currentPage:Int,pageSize:Int):Page[(LabelDiagram,Diagram)] =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    var query =(for{
      ld <- labelDiagrams
      d <- diagrams
      if ld.diagramId === d.id
      if ld.labelId === labelId
    }yield(ld,d) )
    if(!title.isEmpty) query = query.filter(_._2.title like "%"+title.get+"%")
    if(!checkState.isEmpty) query = query.filter(_._1.checkState === checkState.get)
    if(!typeId.isEmpty) query = query.filter(_._2.typeId === typeId.get)
    val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) {
      0
    } else {
      (currentPage - 1) * pageSize
    }
    val ts: List[(LabelDiagram,Diagram)] = query.drop(startRow).take(pageSize).list
    Page[(LabelDiagram,Diagram)](ts, currentPage, totalPages)

  }
  /* 查找group 下的所有diagram */
  def findGroupDiagrams(groupId:Long,checkState:Int,currentPage:Int,pageSize:Int):Page[Diagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
      val query = (for{
            gl <- groupLabels
            ld <- labelDiagrams
            d <- diagrams
          if gl.labelId === ld.labelId
          if ld.diagramId === d.id
          if ld.checkState === checkState
          if gl.groupId === groupId
  }yield d)
      val totalRows = query.list.length
    val totalPages = (totalRows + pageSize - 1) / pageSize
    val startRow = if (currentPage < 1 || currentPage > totalPages) { 0 } else { (currentPage - 1) * pageSize }
    val list= query.drop(startRow).take(pageSize).list

    Page[Diagram](list,currentPage,totalPages)
  }







}
