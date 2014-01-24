/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户账号 选择地址
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.hiwowo.com/
 *
 */

define(function(require, exports){
	var $  = require("$");
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