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
             'onUploadSuccess' : function(file, data, response) {

             }
             // Put your options here
         });


         $(".b-textarea").jqte({
             format: false,
             fsize:false,
             color:false
         })
     })

 })