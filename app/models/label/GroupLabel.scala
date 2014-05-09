package models.label

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-17
 * Time: 下午11:09
 */
import play.api.db.slick.Config.driver.simple._


case class GroupLabel(
                       id: Option[Long],
                       groupId: Long,
                       labelId: Long
                       )

class GroupLabels(tag: Tag) extends Table[GroupLabel](tag, "group_label") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def groupId = column[Long]("group_id")
  def labelId = column[Long]("label_id")

  def * = (id.?,groupId,labelId) <>(GroupLabel.tupled, GroupLabel.unapply)


}

