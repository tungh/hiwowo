/**
 * Created by zuosanshao.
 * User: hiwowo.com
 * Email:zuosanshao@qq.com
 * @contain: 用于 联系我们、关于我们等底部信息页
 * @depends:
 * Includes:
 * Since: 12-12-24  上午11:35
 * ModifyTime :
 * ModifyContent:
 * http://hiwowo.com/
 *
 */
define(function(require, exports) {
    require("hiwowo")

    $("#J_help").find("dt").click(function(){
        $(this).next("dd").toggle()
    })
})