/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:16
 *
 */
define(function (require) {
    var $ = jQuery = require("jquery");


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




    })
})