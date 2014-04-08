/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-1
 * Time: 下午11:53
 *
 */
define(function(require){
    var $ = jQuery = require("jquery")
    require("uploadify")

    $(function(){




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
            'itemTemplate' : ' <div class="post-photo post-item-edit" id="${fileID}">\
                 <div class="move-bar"><a class="move-up" href="javascript:;" style="">上移</a><a class="move-down" href="javascript:;" style="display: none;">下移</a></div>\
                 <div  class="uploadify-queue-item"><div class="cancel"> <a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a></div><span class="fileName">${fileName} (${fileSize})</span><span class="data"></span></div>\
                 <div class="edit-box clearfix"><div class="upload-img"><div class="img"></div></div>\
                 <div class="photo-des"><textarea id="image_desc" name="image_desc" cols="" rows="" class="textarea-normal" label="图片描述(可不填)" style="color: rgb(188, 188, 188);"></textarea></div></div>\
                 <div class="pic-doing-all clearfix"><span class="pic-doing"><a class="img_save_href" href="javascript:;"><ins class="ico-pic-save"></ins>保存</a><a id="img_del_href" href="javascript:;"><ins class="ico-pic-del"></ins>删除</a></span></div>\
                </div>',
            'removeCompleted' : false,
            'onUploadSuccess' : function(file, data, response) {

                $("#"+file.id).find("div.img").html("<img src='"+JSON.parse(data).src+"'with='580'> ")
                $("#"+file.id).find(".uploadify-queue-item").hide()
            }
            // Put your options here
        });



    })

})