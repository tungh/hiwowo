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
define(function (require,exports) {
    require("hiwowo")
    require("hiwowo/common/validator")($);
    jQuery(function() {
        $("#J_regLoginForm").validator();
    });
});