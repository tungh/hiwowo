/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-1-18
 * Time: 下午3:16
 *
 */
define(function (require) {
    var $ = jQuery = require("jquery");
    var pin = require("pin")

    $(function(){
        $(".pin").pin({
            containerSelector: ".diagram"
        })


    })
})