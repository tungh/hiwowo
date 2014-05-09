/**
 * Created by zuosanshao.
 * User: hiwowo.com
 * Email:zuosanshao@qq.com
 * @contain:
 * @depends:
 * Includes:
 * Since: 2014-5-3  上午11:35
 * ModifyTime :
 * ModifyContent:
 * http://hiwowo.com/
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
            'onUploadSuccess' : function(file, data, response) {
                 $("#J_pic").val(JSON.parse(data).src)
                $("#J_picShow").html("<img src='"+JSON.parse(data).src+"'with='580'> ")

            }
            // Put your options here
        });



    })

})