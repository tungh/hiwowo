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

          $(".colm dl").hover(function(){
              $(this).addClass("hover")
          },function(){
              $(this).removeClass("hover")
          })

    })
})