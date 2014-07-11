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
    $(function() {

        $('.main-select a').click(function() {
            $('.main-select a').removeClass('selected');
            $(this).addClass('selected');
            $("#J_typeId").val($(this).data("typeid"))
        })

       $("#uploader li").on( 'mouseenter', function() {
          $(this).find(".file-panel").stop().animate({height: 30});
        });

        $("#uploader li").on( 'mouseleave', function() {
           $(this).find(".file-panel").stop().animate({height: 0});
        });

        $("#uploader li .file-panel .cancel").on("click",function(){
            $(this).parent().parent().remove()
        })

    });
})
