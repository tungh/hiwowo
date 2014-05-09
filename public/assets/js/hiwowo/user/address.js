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

define(function(require, exports){
	var $ = jQuery = require("jquery");
	var Setup = require("hiwowo/common/area");
    require("hiwowo/common/validator")($);
	$("#J_Address").validator({fun:function(vali,inputs){
		inputs.trigger("blur");
		if($("#J_City").val().length<2){
			vali.effects($("#J_City"), "请选择省份和城市", "error");
			$("#J_province_city").data("vali", 0);
		}else{
			vali.effects($("#J_City"), "", "empty");
			$("#J_province_city").data("vali", 1);
		}
	}})
	var pro = "请选择";
	var city = "请选择";
	if(address != null){
	  var adds = address.split("|");
	  pro = adds[0];
	  city = adds[1];
	}
	var setup = new Setup(pro,city);
	//setup(pro,city);
});