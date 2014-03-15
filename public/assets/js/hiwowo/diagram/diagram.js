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
     $(function(){

         $('#J_uploadify').uploadify({
             'fileObjName' : 'fileData',
             'swf'      : '/assets/js/sea-modules/uploadify.swf',
             'uploader' : '/uploadImage',
             'buttonText' : '添加图片，支持JPG, GIF, PNG，最多15张',
             'fileSizeLimit' : '5MB',
             'fileTypeExts' : '*.gif; *.jpg; *.png',
             'uploadLimit' : 15,
             'width'    : 580,
             'height':60,
             'itemTemplate' : '<div class="photo_box" id="${fileID}"><figure> </figure><div  class="uploadify-queue-item">\
					<div class="cancel">\
						<a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a>\
					</div>\
					<span class="fileName">${fileName} (${fileSize})</span><span class="data"></span>\
				</div> <div class="pic_intro" style="margin-top:7px;"><textarea  class="b-textarea"></textarea> </div>\
                 </div>',
             'removeCompleted' : false,
             'onUploadSuccess' : function(file, data, response) {

                  $("#"+file.id).find("figure").html("<img src='"+JSON.parse(data).src+"'with='580'> ")
                 $("#"+file.id).find(".uploadify-queue-item").hide()
             }
             // Put your options here
         });


         $("#J_intro").jqte({
             format: false,
             fsize:false,
             color:false
         })
         $("#J_psLabel").click(function(){
             $("#J_ps").jqte({
                 format: false,
                 fsize:false,
                 color:false
             })
         })
         $("#J_tagsLabel").click(function(){
             $("#J_tags").removeClass("hidden")
         })
     })

 })