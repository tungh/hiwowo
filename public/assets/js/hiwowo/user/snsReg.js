/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
 * @contain:
 * @depends:
 * Since:13-1-2下午7:07
 * ModifyTime :
 * ModifyContent:
 * http://www.hiwowo.com/
 *
 */


define(function(require, exports){
    var $ = jQuery = require("jquery");
    require("hiwowo/common/validator")($);
    jQuery(function() {
        $("#setForm").validator();
    });
});