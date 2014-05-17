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
define(function (require) {
    var $ = jQuery = require("jquery");
    var pin = require("pin")

    $(function(){
        // 右侧内容定位
      $(".pin").pin({
            containerSelector: ".diagram"
         //   ,padding: {top: 60, bottom: 20}
        })
         // 隐藏长图片
        $(".pics-list").each(function(){
            var picsHeight = $(this).height()
            if(picsHeight>500){
               $(this).parent(".pics").addClass("pics-too-high")
            }
        })


       /* 处理长图片 */
        $(".pics-hidden").click(function(){
            $(this).parent(".pics").removeClass("pics-too-high")
            $(this).parent(".pics").next(".pics-too-high-close").show()
          /* $(this).parent(".pics").parent(".diagram-content").parent(".diagram").find(".pin").pin({
             containerSelector: ".diagram",
             padding: {top: 60, bottom:0}
             })*/
            $(".pin").pin({
                containerSelector: ".diagram"
              //  ,padding: {top: 60, bottom:20}
            })
        })
        $(".pics-too-high-close").click(function(){
            $(this).hide()
            $(this).prev(".pics").addClass("pics-too-high")

         /*  $(this).parent(".diagram-content").parent(".diagram").find(".pin").pin({
             containerSelector: ".diagram"
             ,padding: {top: 60, bottom: 0}
             })*/
            $(".pin").pin({
                containerSelector: ".diagram"
               // ,padding: {top: 60, bottom:20}
            })
        })
          /* 处理图说 中间的图片 */
        $(".pics .colm  dl").hover(function(){
            $(this).addClass("hover")
        },function(){
            $(this).removeClass("hover")
        })
    })
})