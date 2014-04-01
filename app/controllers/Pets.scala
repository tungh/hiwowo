package controllers
import play.api.mvc.{Action, Controller}
import controllers.users.Users
import play.api.libs.json.Json
import models.user.dao.UserDao
import models.user.User
import models.pet.dao.{ PetSQLDao, PetDao}
import models.diagram.Diagram
import play.api.cache.Cache
import play.api.Play.current
import models.pet.Pet

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-1
 * Time: 下午11:46
 */
case class PetComponent(
                             getPet:Pet,
                             getUser:User
                             )
object Pets extends Controller {

  def pet(id:Long)  = Users.UserAction{ user => implicit request =>
    val petWithUser = PetDao.findPet(id)
    val defaultUser = User(Some(0),Some("1"),1,"hiwowo","",Some(""),1,"",Some(""),Some(""),0,Some(""),Some(""),Some(""),Some(""),None)
    if(petWithUser.isEmpty ||( petWithUser.get._1.status==0 && petWithUser.get._1.uid != user.getOrElse(defaultUser).id.get)){
      Ok(views.html.pets.petInvalid(user))
    } else {
      Ok(views.html.pets.pet(user,PetComponent(petWithUser.get._1,petWithUser.get._2)))
    }

  }

  def edit(id:Long) = Users.UserAction{ user => implicit request =>
    Ok(views.html.pets.editPet(user))
  }


}
