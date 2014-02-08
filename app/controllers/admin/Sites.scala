package controllers.admin

import play.api.mvc.Controller
import models.site.dao.SiteDao

/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-5
 * Time: 下午8:29
 */
object Sites extends Controller {

  def sites(p:Int,size:Int) = Administrators.AdminAction{administrator => implicit request =>
    val page = SiteDao.findAllSites(p,size)
    Ok(views.html.admin.sites.sites(administrator,page))
  }
  def filterSites =  Administrators.AdminAction{administrator => implicit request =>
   Ok("todo")
  }
  def batchSites =  Administrators.AdminAction{administrator => implicit request =>
   Ok("todo")
  }

  def siteDiscusses(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def filterSiteDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def batchSiteDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }

  def blogs(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def filterBlogs =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def batchBlogs =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }

  def blogDiscusses(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def filterBlogDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def batchBlogDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }

  def pics(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
   Ok("todo")
  }
  def filterPics =  Administrators.AdminAction{administrator => implicit request =>
   Ok("todo")
  }
  def batchPics =  Administrators.AdminAction{administrator => implicit request =>
   Ok("todo")
  }

  def picDiscusses(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def filterPicDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def batchPicDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }

  def videos(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def filterVideos =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def batchVideos =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }

  def videoDiscusses(p:Int,size:Int) =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def filterVideoDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }
  def batchVideoDiscusses =  Administrators.AdminAction{administrator => implicit request =>
    Ok("todo")
  }

}
