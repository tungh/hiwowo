package models.label.dao
import java.sql.Timestamp
import play.api.Play.current
import play.api.libs.Codecs
import play.api.cache.Cache
import play.api.Play.current
import models.Page
import play.api.db.slick.Config.driver.simple._
import models.label._
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

  /* label group */
  def addGroup(name:String,intro:Option[String],status:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val groupAutoInc = groups.map( t => (t.name,t.intro.?,t.status)) returning labels.map(_.id) into {
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
  def findGroups = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<-groups)yield c).list()
  }

  /* label dao */
  def addLabel(name:String,level:Int,intro:Option[String],checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val labelsAutoInc = labels.map( t => (t.name, t.level, t.intro.?,t.checkState)) returning labels.map(_.id) into {
      case (_, id) => id
    }
    labelsAutoInc.insert(name,level,intro,checkState)
  }
  def addLabel(name:String,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    val labelsAutoInc = labels.map( t => (t.name,t.checkState)) returning labels.map(_.id) into {
      case (_, id) => id
    }
    labelsAutoInc.insert(name,checkState)
  }

  def modifyLabelCheckState(id:Long,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t <- labels if t.id === id )yield t.checkState ).update(checkState)
  }
  def modifyLabelCheckState(name:String,checkState:Int) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t <- labels if t.name === name )yield t.checkState ).update(checkState)
  }
  def modifyLabel(id:Long,name:String,level:Int,intro:Option[String],checkState:Int) =  play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(t <- labels if t.id === id) yield (t.name,t.level,t.intro.?,t.checkState) ).update(name,level,intro,checkState)
  }
  def deleteLabel(id:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( t<- labels if t.id === id ) yield t ).delete
    ( for(c <- labelDiagrams if c.id === id)yield c ).delete
  }
  def findLabelById(id:Long):Option[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(t <- labels if t.id === id) yield t ).firstOption
  }
  def findLabelByName(name:String):Option[Label] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for(t <- labels if t.name === name ) yield t ).firstOption
  }


  /* add group label */
  def addGroupLabel(groupId:Long,labelId:Long)= play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(t <- groupLabels) yield (t.groupId,t.labelId)).insert(groupId,labelId)
  }
  def deleteGroupLabel(groupId:Long,labelId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- groupLabels if t.groupId === groupId if t.labelId === labelId) yield t ).delete
  }

  /* label diagram ship */
  def addLabelDiagram(labelId:Long,diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for( t<- labelDiagrams ) yield (t.labelId,t.diagramId)).insert(labelId,diagramId)
  }

  def findLabelDiagram(labelId:Long,diagramId:Long):Option[LabelDiagram] = play.api.db.slick.DB.withSession{ implicit session:Session =>
    (for(c<- labelDiagrams if c.labelId === labelId  if c.diagramId === diagramId) yield c ).firstOption
  }


  def deleteLabelDiagram(labelId:Long,diagramId:Long) = play.api.db.slick.DB.withSession{ implicit session:Session =>
    ( for( t<- labelDiagrams if t.labelId === labelId if t.diagramId === diagramId) yield t ).delete
  }





}
