/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:16
 *
 */
define(function (require) {
    var $ = jQuery = require("jquery");
     require("uploadify")

    $(function(){
         /* index page */
          $(".index .colm dl").hover(function(){
              $(this).addClass("hover")
          },function(){
              $(this).removeClass("hover")
          })


        /* diagram page */
        $("#J_diagram_sort").dropDown({
            classNm: ".diagram-dropdown"
        })

        /* pets page */
        $("#J_num").hover(function(){
            $(this).hide()
        },function(){
            $(this).show()
        })

       /* uploadify */

        $('#file_upload').uploadify({
            'fileObjName' : 'fileData',
            'swf'      : '/assets/js/uploadify/uploadify.swf',
            'uploader' : '/uploadImage',
            'onUploadSuccess' : function(file, data, response) {
                alert('The file ' + file.name + ' was successfully uploaded with a response of ' + response + ':' + data);
            }
            // Put your options here
        });

        /* uploadify */


    })
})