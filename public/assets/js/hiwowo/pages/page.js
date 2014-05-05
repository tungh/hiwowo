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
        $(".pics-list").each(function(){
            var picsHeight = $(this).height()
            if(picsHeight>700){
               $(this).parent(".pics").addClass("pics-too-high")
            }
        })

       /* 处理长图片 */
        $(".pics-hidden").click(function(){
            $(this).parent(".pics").removeClass("pics-too-high")
            $(this).parent(".pics").next(".pics-too-high-close").show()
        })
        $(".pics-too-high-close").click(function(){
            $(this).hide()
            $(this).prev(".pics").addClass("pics-too-high")
        })
        // 右侧内容定位
        $(".pin").pin({
            containerSelector: ".diagram"
        })


    })
})