package controllers

import _root_.utils.Utils
import play.api.mvc.{Action, Controller}
import java.io.File
import java.util.regex.Pattern
import play.api.libs.json.Json
import net.coobird.thumbnailator.Thumbnails
import controllers.users.Users
import java.net.URL
import play.api.data.Form
import play.api.data.Forms._
import models.user.dao.UserDao

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
    request.body.file("fileData").map { picture =>
      val filename =System.currentTimeMillis()+ picture.filename.substring(picture.filename.lastIndexOf("."))
      if(Utils.isImage(filename)){
        picture.ref.moveTo(new File("public/uploadImage/temp/"+filename),true)
        val src ="/uploadImage/temp/"+filename
       // println(src)

        Ok(Json.obj("code"->"100","message"->"success","src"->src))

      }else{
        Ok(Json.obj("code"->"104","message"->"fail"))
      }

    }.getOrElse {
      Ok("File uploaded error")
    }
  }

  def uploadUmeditorPic(editorid:String) =Action(parse.multipartFormData)  {   request =>
    request.body.file("upfile").map { picture =>
      val filename =System.currentTimeMillis()+ picture.filename.substring(picture.filename.lastIndexOf("."))
      if(Utils.isImage(filename)){
        picture.ref.moveTo(new File("public/uploadImage/temp/"+filename),true)
        val picSrc ="/uploadImage/temp/"+filename
       // Ok(Json.obj("url"->picSrc,"state"->"SUCCESS","title"->"嗨喔喔"))
         Ok(views.html.common.umeditorUploadPic(picSrc,"SUCCESS",editorid))
      }else{
        Ok(Json.obj("code"->"104","filelink"->"亲，服务器欧巴桑了，请重试"))
      }

    }.getOrElse {
      Ok(Json.obj("code"->"104","filelink"->"亲，服务器欧巴桑了，请重试"))
    }
  }

  /*上传 用户头像图片  */
  def uploadImageSelectPic = Action(parse.multipartFormData) {
    request =>
      request.body.file("filedata").map {
        picture =>
          val filename = System.currentTimeMillis() + picture.filename.substring(picture.filename.lastIndexOf("."))
          if (Utils.isImage(filename)) {
          //  picture.ref.moveTo(new File("/opt/static/images/temp/" + filename), true)
          //  Thumbnails.of(new File("/opt/static/images/temp/" + filename)).size(300, 300).toFile(new File("/opt/static/images/temp/" + filename))
          //  val picSrc = "/images/temp/" + filename
              picture.ref.moveTo(new File("public/uploadImage/temp/" + filename), true)
              Thumbnails.of(new File("public/uploadImage/temp/" + filename)).size(300, 300).toFile(new File("public/uploadImage/temp/" + filename))
              val picSrc = "/uploadImage/temp/" + filename
            Ok(views.html.common.uploadImageSelectSuccess(true, picSrc))
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
          val src = "public" + fields._1
          Thumbnails.of(src).sourceRegion(fields._2, fields._3, (fields._4 - fields._2), (fields._5 - fields._3)).size((fields._4 - fields._2), (fields._5 - fields._3)).toFile("public/uploadImage/" + picName)
          val picSrc: String = "/uploadImage/" + picName
          UserDao.modifyPic(user.get.id.get, picSrc)
          Ok(Json.obj("code" -> "100", "src" -> picSrc))
        }
      )
  }


  /* 获得视频地址*/
  def getVideo = Users.UserAction {
    user => implicit request =>

      Ok(Json.obj("code" -> "100", "src" -> "sss"))
  }


}