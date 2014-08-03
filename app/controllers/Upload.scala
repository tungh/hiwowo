package controllers

import _root_.utils.Utils
import play.api.mvc.{Action, Controller}
import java.io.File
import play.api.libs.json.Json
import controllers.users.Users
import play.api.data.Form
import play.api.data.Forms._
import models.user.dao.UserDao
import java.sql.Timestamp
import com.sksamuel.scrimage.Image
/**
 * Created by zuosanshao.
 * email:zuosanshao@qq.com
 * Date: 12-10-12
 * Time: 上午9:36
 * ***********************
 * description:用于类的说明
 */

object Upload extends Controller {



  val picForm = Form(
    tuple(
      "thumb-path" -> nonEmptyText,
      "area-x1" -> number,
      "area-y1" -> number,
      "area-x2" -> number,
      "area-y2" -> number
    )
  )

  def uploadDiagramPic=Action(parse.multipartFormData) { request =>
    request.body.file("file").map { picture =>
      val suffix = picture.filename.substring(picture.filename.lastIndexOf("."))
      val name = System.currentTimeMillis()
      val filename =name+suffix

      if(Utils.isImage(filename)){
        val  now = new Timestamp(System.currentTimeMillis())
        val rawPic = new File( utils.Utils.typeDir(now,"diagram"),filename+suffix)
        val smallPic = new File( utils.Utils.typeDir(now,"diagram"),filename)
        val rawUrl = "/images/"+utils.Utils.getYearMonth(now).toString+"/"+filename+suffix
        val smallUrl = "/images/"+utils.Utils.getYearMonth(now).toString+"/"+filename

        picture.ref.moveTo(rawPic,true)
        if(Image(rawPic).width > 600){
          Image(rawPic).scaleToWidth(600).write(smallPic)
        }else{
          Image(rawPic).write(smallPic)
        }


        Ok(Json.obj("code"->"100","message"->"success","src"->smallUrl))

      }else{
        Ok(Json.obj("code"->"104","message"->"fail"))
      }

    }.getOrElse {
      Ok("File uploaded error")
    }
  }

  def uploadEditorPic =Action(parse.multipartFormData)  {   request =>
    request.body.file("fileData").map { picture =>
      val suffix = picture.filename.substring(picture.filename.lastIndexOf("."))
      val name = System.currentTimeMillis()
      val filename =name+suffix

      if(Utils.isImage(filename)){

        val  now = new Timestamp(System.currentTimeMillis())
        val rawPic = new File( utils.Utils.typeDir(now,"editor"),filename+suffix)
        val smallPic = new File( utils.Utils.typeDir(now,"editor"),filename)
        val rawUrl = "/images/"+utils.Utils.getYearMonth(now).toString+"/editor/"+filename+suffix
        val smallUrl = "/images/"+utils.Utils.getYearMonth(now).toString+"/editor/"+filename

        picture.ref.moveTo(rawPic,true)
        if(Image(rawPic).width > 600){
          Image(rawPic).scaleToWidth(600).write(smallPic)
        }else{
          Image(rawPic).write(smallPic)
        }

       Ok(Json.obj("code"->"100","src"->smallUrl,"message"->"success","title"->"嗨喔喔"))

      }else{
        Ok(Json.obj("code"->"104","message"->"亲，你确定是图片吗"))
      }

    }.getOrElse {
      Ok(Json.obj("code"->"500","message"->"亲，服务器欧巴桑了，请重试"))
    }
  }
  /* 上传的广告图片 */
  def uploadAdvertPic =Action(parse.multipartFormData)  {   request =>
    request.body.file("fileData").map { picture =>
      val suffix = picture.filename.substring(picture.filename.lastIndexOf("."))
      val name = System.currentTimeMillis()
      val filename =name+suffix

      if(Utils.isImage(filename)){
        val  now = new Timestamp(System.currentTimeMillis())

        val pic = new File( utils.Utils.typeDir(now,"advert"),filename)

        val picUrl = "/images/"+utils.Utils.getYearMonth(now).toString+"/editor/"+filename

        picture.ref.moveTo(pic,true)

        Ok(Json.obj("code"->"100","src"->picUrl,"message"->"success","title"->"嗨喔喔"))

      }else{
        Ok(Json.obj("code"->"104","message"->"亲，你确定是图片吗"))
      }

    }.getOrElse {
      Ok(Json.obj("code"->"500","message"->"亲，服务器欧巴桑了，请重试"))
    }
  }

  /*上传 用户头像图片  */
  def uploadImageSelectPic = Action(parse.multipartFormData) {
    request =>
      request.body.file("filedata").map {
        picture =>
          val suffix = picture.filename.substring(picture.filename.lastIndexOf("."))
          val name = System.currentTimeMillis()
          val filename =name+suffix

          if (Utils.isImage(filename)) {

            val  now = new Timestamp(System.currentTimeMillis())
            val pic = new File( utils.Utils.typeDir(now,"user"),filename)
            val picUrl = "/images/"+utils.Utils.getYearMonth(now).toString+"/user/"+filename
            picture.ref.moveTo(pic,true)

            Ok(views.html.common.uploadImageSelectSuccess(true, picUrl))
          } else {
            Ok(views.html.common.uploadImageSelectSuccess(false, "亲，服务器欧巴桑了，请重试"))
          }

      }.getOrElse {
        Ok(views.html.common.uploadImageSelectSuccess(false, "亲，服务器欧巴桑了，请重试"))
      }
  }

  /*上传图片，用户截取图片    */
  def doUploadUserPic = Users.UserAction {
    user => implicit request =>
      picForm.bindFromRequest.fold(
        formWithErrors => BadRequest("something is wrong"),
        fields => {
          val picName: String = fields._1.substring(fields._1.lastIndexOf("/"))
          val pic = new File("/opt/static/" + fields._1)
          Image(pic).subimage(fields._2, fields._3, (fields._4 - fields._2), (fields._5 - fields._3)).write(pic)

          UserDao.modifyPic(user.get.id.get, fields._1)
          Ok(Json.obj("code" -> "100", "src" -> fields._1))
        }
      )
  }


  /* 获得视频地址*/
  def getVideo = Users.UserAction { user => implicit request =>

      Ok(Json.obj("code" -> "100", "src" -> "sss"))
  }




}