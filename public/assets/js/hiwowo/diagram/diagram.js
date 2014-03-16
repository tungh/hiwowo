/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午11:42
 *
 */
 define(function(require){
     var $ = jQuery = require("jquery")
      require("jqueryte")
     require("uploadify")
     require("pin")
     $(function(){

         $("#J_diagramIntro").jqte({
             format: false,
             fsize:false,
             color:false
         })
         $("#J_psLabel").click(function(){
             $("#J_diagramPs").jqte({
                 format: false,
                 fsize:false,
                 color:false
             })
         })
         $("#J_tagsLabel").click(function(){
             $("#J_diagramTags").removeClass("hidden")
         })
         /* 上传图片*/
         $('#J_uploadify').uploadify({
             'fileObjName' : 'fileData',
             'swf'      : '/assets/js/sea-modules/uploadify.swf',
             'uploader' : '/uploadDiagramPic',
             'buttonText' : '添加图片，支持JPG, GIF, PNG，最多15张',
             'fileSizeLimit' : '5MB',
             'fileTypeExts' : '*.gif; *.jpg; *.png',
             'uploadLimit' : 15,
             'width'    : 580,
             'height':60,
             'itemTemplate' : '<div class="photo_box" id="${fileID}" data-picid="0"><figure> </figure><div  class="uploadify-queue-item">\
					<div class="cancel">\
						<a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a>\
					</div>\
					<span class="fileName">${fileName} (${fileSize})</span><span class="data"></span>\
				</div> <div class="pic_intro"><textarea  class="b-textarea"></textarea> </div>\
                 </div>',
             'removeCompleted' : false,
             'onUploadSuccess' : function(file, data, response) {

                  $("#"+file.id).find("figure").html("<img src='"+JSON.parse(data).src+"'with='580'> ")
                 $("#"+file.id).find(".uploadify-queue-item").hide()
             }
             // Put your options here
         });

         /* 获取所有图片和 说明*/
         function getContent(){
             var content ="";
             $(".photo_box").each(function(){
                 $(this).remove(".uploadify-queue-item")
                 //  var intro= $(this).find("textarea").val()
                 //  $(this).remove("textarea")
                 //   $(this).find(".pic_intro").html(intro)
                 alert($(this).html())
                 content +=$(this).html()
             })
         }
         /* 获取所有图片的Id */
         function getPicIds(){
             var ids="";
             $(".photo_box").each(function(){
                 ids +=$(this).data("picid")+","
             })
         }

         /*保存图片 保存成功后，在pic-content 中生存pic-box 包含pic 和 intro,给diagram使用 */
         function savePhoto($elm){

             var picUrl= $elm.find("figure").find("img").attr("src");
             var picIntro = $elm.find("textarea").val();
             var picId = $elm.data("picid");
             $.ajax({
                 url: "/diagram/savePic",
                 type: "POST",
                 contentType:"application/json; charset=utf-8",
                 dataType: "json",
                 data: JSON.stringify({
                     picId:picId,
                     picUrl: picUrl,
                     picIntro: picIntro
                 }),
                 success: function(data) {
                     if(data.code=="100"){
                         $elm.data("picid",data.picId)
                     }
                 }
             });
         }


         /* 保存到草稿 */
         $("#J_diagramDraftBtn").click(function(){
             /* 第一步 保存 photo_box */
                $(".photo_box").each(function(){
                   savePhoto($(this))
                })

             /*第二步判断标题是否为空 图片至少有一张  */
             if($("#J_diagramTitle")==""){
                 $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                 $.hiwowo.tip.show($(this),"内容不能为空哦！");
                 $(this).attr('disabled',false);
                 return;
             }
             if(getPicIds()==""){
                 $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                 $.hiwowo.tip.show($(this),"至少需要上传一张图片哦");
                 $(this).attr('disabled',false);
                 return;
             }
             /* 第三步，保存 diagram */
             var diagramId =$("#J_diagramId").val()
             var diagramTitle =$("#J_diagramTitle").val()
             var diagramPic = $(".photo_box").first().find("figure img").attr("src")
             var diagramIntro =$("#J_diagramIntro").val()
             var diagramContent=getContent()
             var diagramPs =$("#J_diagramPs").val()
             var diagramTags =$("#J_diagramTags").val()
             var picIds =getPicIds()
             $.ajax({
                 url: "/diagram/saveDraft",
                 type: "POST",
                 contentType:"application/json; charset=utf-8",
                 dataType: "json",
                 data: JSON.stringify({
                     diagramId:diagramId,
                     diagramTitle: diagramTitle,
                     diagramPic:diagramPic,
                     diagramIntro:diagramIntro,
                     diagramContent:diagramContent,
                     diagramPs:diagramPs,
                     diagramTags:diagramTags,
                     picIds:picIds
                 }),
                 success: function(data) {
                     if(data.code=="100"){
                        $("#J_diagramId").val(data.diagramId)
                         $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                         $.hiwowo.tip.show($(this),"保存成功");
                     }
                 }
             });
         })

     })

 })