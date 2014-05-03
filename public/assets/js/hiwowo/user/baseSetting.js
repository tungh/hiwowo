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
define(function(require) {
	var $ = jQuery = require("jquery");
    require("hiwowo/common/validator")($);
    require("imgAreaSelect");
    require("bootstrap")
	var DateSelector = require("hiwowo/common/dateSelect");
    var Detector = require("detector")
$.hiwowo.photoarea = null;
$.hiwowo.rotate = null;
$.hiwowo.setting = {
	setAvatar: {
		preview: function(img,selection) {
			var w = $('#photo').width(),h = $('#photo').height();
			if (!selection.width || !selection.height){
				return false;
			}
			
			$("#area-x1").val(selection.x1);
			$("#area-y1").val(selection.y1);
			$("#area-x2").val(selection.x1 + selection.width);
			$("#area-y2").val(selection.y1 + selection.height);
		},
		setPhoto: function(path){
			var w,h,x1,y1,x2,y2;
			$('#photo').attr("src","").attr("src",path);
			$("#thumb-path").val(path);
			var image = $('#photo')[0],imageTime;
            if(Detector.browser.ie){
                if(Detector.browser.version < 7){
					image.onreadystatechange = function(){ 
						if(image.readyState=="complete" || image.readyState=="loaded"){ 
							dosth();
						}
					}
				}else{
					imageTime = setInterval(function(){
						if(image.readyState=="complete"){
							clearInterval(imageTime);
							dosth();
						}
					},200);
				}
			}else{
				image.onload = function(){
					if (image.complete == true){
						dosth();
					}
				}
			}
			var dosth = function(){
				w = $('#photo').width();
				h = $('#photo').height();
				if (w < 180 || h < 180){
					if(w<h){
						x1 = 0;
						y1 = h/2-w/2;
						x2 = x1+w;
						y2 = y1+w;
					}else{
						y1 = 0;
						x1 = w/2-h/2;
						y2 = y1+h;
						x2 = x1+h;	
					}
				}else{
					x1 = w/2-90;
					y1 = h/2-90;
					x2 = x1+180;
					y2 = y1+180;
				}
				//图片裁切
                $("#faceUpload2 .face-submit").show();
                $.hiwowo.photoarea = $('#photo').imgAreaSelect({
                    aspectRatio: '1:1',
                    handles: true,
                    fadeSpeed: 200,
                    onSelectChange: $.hiwowo.setting.setAvatar.preview,
                    instance: true,
                    persistent: true,
                    minWidth:200, minHeight: 200
                });
                $.hiwowo.photoarea.setSelection(x1, y1, x2, y2, true);
                $.hiwowo.photoarea.setOptions({ show: true });
                $.hiwowo.photoarea.update();
                var selection = $.hiwowo.photoarea.getSelection(true);
                $.hiwowo.setting.setAvatar.preview($('#photo')[0], selection);
				
			};
		}

	}
}	


$(function(){
	$("#J_SetBasic").validator();
	var setInputs = $("#J_SetBasic").find(":input").not(":button, :image, :reset, :submit");
	setInputs.each(function(){
		$(this).data("vali",1);
	});
	
	$(".radio-txt").click(function(){
		$(this).prev("input").trigger("click");
	});
	

	var year = 1985;
	var month = 1;
	var day = 1;
	if(brithday != null){
	  var bths = brithday.split("|");
	  year = bths[0];
	  month = bths[1];
	  day = bths[2];
	}
	var ds = new DateSelector("J_Year", "J_Month", "J_Day", {Year: year, Month: month, Day: day, MinYear: new Date().getFullYear() -100, MaxYear: new Date().getFullYear() });
	
	$("#J_ModifyPhoto, #thePhoto").click(function(){
			if(!$("#J_photoDialog")[0]){
				var html = "";
                html +='<div class="modal fade" id="J_photoDialog">';
                html +='<div class="modal-dialog">';
                html +='<div class="modal-content">';
                html +='<div class="modal-header"> ';
                html +='<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
                html +='<h4 class="modal-title">登录</h4>';
                html +='</div>';
                html +='<div class="modal-body">';
               /* body content */
                html += '<div class="bd clearfix">';
                html += '<form id="faceUpload" name="faceUpload" enctype="multipart/form-data" method="post" target="photo-frame" action="/uploadPic/select">';
                html += '<div class="photo-row clearfix">';
                html += '<input type="button" value="上传新头像" class="bbl-btn upload-cover" />';
                html += '<input type="file" class="upload-btn" name="filedata" id="J_FilePath" />';
                html += '</div>';
                html += '<div class="photo-row pt10 pb15">';
                html += '<span class="gc6">支持JPG、GIF、PNG格式，且文件小于2M</span>';
                html += '</div>';
                html += '<div class="photo-row">';
                html += '<div class="photo-box">';
                html += '<span><img src="/assets/images/blank.gif" id="photo" alt=""  /> </span>';
                html += '</div>';
                html += '</div>';
                html += '</form>';
                html += '<form class="mt20" id="faceUpload2" name="faceUpload2" enctype="multipart/form-data" method="post" action="/user/account/doUploadPic">';
                html += '<input type="hidden" value="" name="thumb-path" id="thumb-path" />';
                html += '<input type="hidden" value="" name="area-x1" id="area-x1" />';
                html += '<input type="hidden" value="" name="area-y1" id="area-y1" />';
                html += '<input type="hidden" value="" name="area-x2" id="area-x2" />';
                html += '<input type="hidden" value="" name="area-y2" id="area-y2" />';
                html += '<div class="photo-row face-submit dn">';
                html += '<input type="submit" class="bbl-btn submit" value="保存头像"/>';
                html += '<span id="J_Waiting" class="ml10 gc6 hidden"><img src="/assets/images/loading16.gif">请耐心等待…</span>';
                html += '</div>';
                html += '</form>';
                html += '<iframe style="width:0px;height:0px;padding:0px;" src="" frameborder="0" name="photo-frame"></iframe>';
                html += '</div>';

                /* body content */
                html += '</div>';
                html += '<div class="modal-footer">';
                html += '</div>';
                html += '</div> ';
                html += '</div>';
                html += '</div> ';

				$("body").append(html);

                   $("#J_photoDialog").modal('show')

            }else{
                $("#J_photoDialog").modal('show')
            }
        $("#J_FilePath").change(function(){
            $("#faceUpload").submit();
            $('#photo').attr("src","/assets/images/loading.gif");
        });
        $("#faceUpload2").submit(function(){
            $this = $(this);
            $("#faceUpload2 input[type=submit]")[0].disabled = "disabled";
            $("#faceUpload2 input[type=submit]").removeClass("bbl-btn").addClass("disabled");
            $("#J_Waiting").show();
            $.post($this.attr("action"),$this.serializeArray(),function(data){
                $("#J_Waiting").hide();
                $("#faceUpload2 input[type=submit]")[0].disabled = "";
                $("#faceUpload2 input[type=submit]").removeClass("disabled").addClass("bbl-btn");
                if(data.code=="100"){
                    $("#J_photoDialog").modal('hide')

                    $("#thePhoto").attr("src",data.src)
                  //  $(".site-logo").show()
                    $("#faceUpload")[0].reset();
                    $("#faceUpload2")[0].reset();
                   // $("#J_uploadImg").val(data.src)
                    $("#faceUpload2 .face-submit").hide();
                    $.hiwowo.photoarea.cancelSelection();

                }
            });
            return false;
        });
        $("#photoDialog .close").unbind("click").click(function(){
            $("#faceUpload")[0].reset();
            $("#faceUpload2")[0].reset();
            $('#photo').attr("src","");
            $("#faceUpload2 .face-submit").hide();
            if($.hiwowo.photoarea!=null){
                $.hiwowo.photoarea.cancelSelection();
            }
            $("#J_photoDialog").modal('hide')
        });

	});
});


});