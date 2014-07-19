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
    require("vticker")
    $(function(){


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
        })
        $(".pics-too-high-close").click(function(){
            $(this).hide()
            $(this).prev(".pics").addClass("pics-too-high")
        })


        $('#J_notices').vTicker({
            speed: 500,        //滚动速度，单位毫秒。
            pause: 3000,       //暂停时间，就是滚动一条之后停留的时间，单位毫秒。
            showItems: 1,     //显示内容的条数。
            animation: 'fade', //动画效果，默认是fade，淡出。
            mousePause: true,  //鼠标移动到内容上是否暂停滚动，默认为true。
            height: 100,       //滚动内容的高度。
            direction: 'up'        //滚动的方向，默认为up向上，down则为向下滚动。
        });
})
})