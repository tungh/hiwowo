define(function (require, exports) {
    var $ = jQuery = require("jquery");
    var SWFUpload = require("swfupload")
    /*
     * Copyright 2011-2012, hiwowo.com
     * @contain: 富文本编辑器
     * @depends: jquery.js  swfupload
     * zuosanshao@qq.com
     */


//(function ($) {
    var COLOR = [
        {
            key:"333333",
            val:"灰色-80%"
        },
        {
            key:"666666",
            val:"灰色-60%"
        },
        {
            key:"999999",
            val:"灰色-40%"
        },
        {
            key:"cccccc",
            val:"灰色-20%"
        },
        {
            key:"bb0000",
            val:"深红"
        },
        {
            key:"dd0000",
            val:"红色"
        },
        {
            key:"ee4488",
            val:"粉红"
        },
        {
            key:"ff66dd",
            val:"淡紫"
        },
        {
            key:"333399",
            val:"深蓝"
        },
        {
            key:"0066cc",
            val:"蓝色"
        },
        {
            key:"0099cc",
            val:"天蓝"
        },
        {
            key:"66cccc",
            val:"淡蓝"
        },
        {
            key:"336600",
            val:"深绿"
        },
        {
            key:"999900",
            val:"深黄"
        },
        {
            key:"cccc33",
            val:"淡黄"
        },
        {
            key:"77cc33",
            val:"淡绿"
        },
        {
            key:"663300",
            val:"咖啡"
        },
        {
            key:"cc6633",
            val:"褐色"
        },
        {
            key:"ff9900",
            val:"橙黄"
        },
        {
            key:"ffcc33",
            val:"黄色"
        }
    ];
    var FONTSIZE = [1, 2, 3, 4];
    //RGB(0,0,0)转#000000
    var RGB2HEX = {
        "_515151":"333333",
        "_102102102":"666666",
        "_153153153":"999999",
        "_204204204":"cccccc",
        "_18700":"bb0000",
        "_22100":"dd0000",
        "_23868136":"ee4488",
        "_255102221":"ff66dd",
        "_5151153":"333399",
        "_0102204":"0066cc",
        "_0153204":"0099cc",
        "_102204204":"66cccc",
        "_511020":"336600",
        "_1531530":"999900",
        "_20420451":"cccc33",
        "_11920451":"77cc33",
        "_102510":"663300",
        "_20410251":"cc6633",
        "_2551530":"ff9900",
        "_25520451":"ffcc33"
    };

    //魔法开始
    var Editor = function (config) {
        return new Editor.fn.init(config);
    };
    Editor.prototype = Editor.fn = {
        config:{
            //将编辑器组装好后插入到textarea后面，textarea的ID
            textareaID:"J_ForumPostCon",
            formId:"J_ForumPostEditForm",
            toolbarId:"J_GuangEditorToolbar",

            //按钮参数配置
            btnFontSize:{
                cssName:"font-size",
                visible:true,
                exec:function (self) {
                    if (!self.FontSizeWrapDom || self.FontSizeWrapDom.length === 0) {
                        var html = '<div class="fontSizeWrap"><a btntype="btnFontSizeAction" size="1" style="font-size:12px;" href="javascript:;" title="小号" unselectable="on">小号</a><a btntype="btnFontSizeAction" size="2" style="font-size:14px;" href="javascript:;" title="标准" unselectable="on">标准</a><a btntype="btnFontSizeAction" size="3" style="font-size:16px;" href="javascript:;" title="大号" unselectable="on">大号</a><a btntype="btnFontSizeAction" size="4" style="font-size:18px;" href="javascript:;" title="特大" unselectable="on">特大</a></div>';
                        var config = self.config;
                        $('#' + config.toolbarId).append(html);
                        self.FontSizeWrapDom = $('#' + config.toolbarId + ' .fontSizeWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.FontSizeWrapDom;
                    } else {
                        if (self.curVisiableDom === self.FontSizeWrapDom) {
                            self.FontSizeWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom) {
                                self.curVisiableDom.hide();
                            }
                            self.FontSizeWrapDom.show();
                            self.curVisiableDom = self.FontSizeWrapDom;
                        }
                    }
                },
                selectionStyleFun:function (self, curElm, $parents) {
                    var tagName = curElm.nodeName.toLowerCase();
                    var val = null;
                    var reg_css = /size/i;
                    val = $(curElm).attr("size") || null;
                    if (!val && $parents) {
                        var length = $parents.length;
                        for (var i = 0; i < length; i++) {
                            val = $parents.eq(i).attr("size") || null;
                            if (val) {
                                break;
                            }
                        }
                    }
                    if (!self.curFontSize) {
                        self.curFontSize = null;
                    }
                    if (val != self.curFontSize) {
                        if (val == null) {
                            $('#' + self.config.toolbarId + ' .font-size a').text("标准");
                            self.curFontSize = null;
                        } else {
                            $('#' + self.config.toolbarId + ' .font-size a').text(self.config.btnFontSize.data["f" + val]);
                            self.curFontSize = val;
                        }
                    }
                },
                data:{
                    "f1":"小号",
                    "f2":"标准",
                    "f3":"大号",
                    "f4":"特大"
                },
                html:"<div class='font-btns font-size'><a href='javascript:;' btntype='btnFontSize' title='字号' unselectable='on'>标准</a></div>"
            },
            btnFontSizeAction:{
                exec:function (self, $srcElement) {
                    var size = $srcElement.attr("size");
                    self.execCommand("fontsize", size);
                    $('#' + self.config.toolbarId + ' .font-size a').text($srcElement.attr("title"));
                    self.curFontSize = size;
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            },
            btnFontBold:{
                cssName:"font-bold",
                visible:true,
                exec:function (self, $srcElement) {
                    if ($srcElement.parent().hasClass("font-bold-active")) {
                        $('#' + self.config.toolbarId + ' .font-bold').removeClass("font-bold-active");
                    } else {
                        $('#' + self.config.toolbarId + ' .font-bold').addClass("font-bold-active");
                    }
                    self.execCommand("bold", "");
                },
                selectionStyleFun:function (self, curElm, $parents) {
                    var tagName = curElm.nodeName.toLowerCase();
                    var reg_tagName = {
                        "b":true,
                        "strong":true
                    };
                    var reg_css = /bold/i;
                    var outerHTML = curElm.outerHTML.match(/\<[^\>]+\>/)[0];
                    var val = false;

                    if (reg_tagName[tagName] || reg_css.test(outerHTML)) {
                        val = true;
                    }
                    if (!val && $parents) {
                        var length = $parents.length
                        for (var i = 0; i < length; i++) {
                            var curDom = $parents[i];
                            var tagName = curDom.nodeName.toLowerCase();
                            var outerHTML = curDom.outerHTML.match(/\<[^\>]+\>/)[0];
                            if (reg_tagName[tagName] || reg_css.test(outerHTML)) {
                                val = true;
                                break;
                            }
                        }
                    }

                    var btnDom = $('#' + self.config.toolbarId + ' .font-bold');
                    var hasBold = btnDom.hasClass("font-bold-active");
                    if (val && !hasBold) {
                        $('#' + self.config.toolbarId + ' .font-bold').addClass("font-bold-active");
                    }
                    if (!val && hasBold) {
                        $('#' + self.config.toolbarId + ' .font-bold').removeClass("font-bold-active");
                    }
                },
                html:"<div class='font-btns font-bold'><a href='javascript:;' btntype='btnFontBold' title='粗体' unselectable='on'>粗体</a></div>"
            },
            btnFontColo:{
                cssName:"font-color",
                visible:true,
                exec:function (self) {
                    if (!self.FontColoWrapDom || self.FontColoWrapDom.length == 0) {
                        var html = '<div class="fontColoWrap">';
                        var length = COLOR.length;
                        for (var i = 0; i < length; i++) {
                            html += '<a btntype="btnFontColoAction" coloval="#' + COLOR[i].key + '" style="background-color:#' + COLOR[i].key + ';" href="javascript:;" title="' + COLOR[i].val + '" unselectable="on">#' + COLOR[i].key + '</a>'
                        }
                        html += '</div>';
                        $('#' + self.config.toolbarId).append(html);
                        self.FontColoWrapDom = $('#' + self.config.toolbarId + ' .fontColoWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.FontColoWrapDom;
                    } else {
                        if (self.curVisiableDom == self.FontColoWrapDom) {
                            self.FontColoWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.FontColoWrapDom.show();
                            self.curVisiableDom = self.FontColoWrapDom;
                        }
                    }
                },
                selectionStyleFun:function (self, curElm, $parents) {
                    var tagName = curElm.nodeName.toLowerCase();
                    var val = null;
                    var reg_css = /color\:/i;
                    var reg_rgb = /rgb\(\s?(\d{1,3})\,\s?(\d{1,3})\,\s?(\d{1,3})\)/i;
                    var outerHTML = curElm.outerHTML.match(/\<[^\>]+\>/)[0];
                    var attrColor = $(curElm).attr("color");
                    if (attrColor) {
                        val = attrColor;
                    } else if (reg_css.test(outerHTML)) {
                        var rgbArr = outerHTML.match(reg_rgb);
                        if (rgbArr) {
                            var hex = RGB2HEX["_" + rgbArr[1] + rgbArr[2] + rgbArr[3]];
                            val = "#" + hex;
                        }
                        //to do reg hex
                    }
                    if (!val && $parents) {
                        var length = $parents.length
                        for (var i = 0; i < length; i++) {
                            var curDom = $parents[i];
                            var tagName = curDom.nodeName.toLowerCase();
                            var outerHTML = curDom.outerHTML.match(/\<[^\>]+\>/)[0];
                            var attrColor = $(curDom).attr("color");
                            if (attrColor) {
                                val = attrColor;
                            } else if (reg_css.test(outerHTML)) {
                                var rgbArr = outerHTML.match(reg_rgb);
                                if (rgbArr) {
                                    var hex = RGB2HEX["_" + rgbArr[1] + rgbArr[2] + rgbArr[3]];
                                    val = "#" + hex;
                                }
                                //to do reg style hex
                            }
                            if (val) {
                                break;
                            }
                        }
                    }
                    if (!self.curFontColor) {
                        self.curFontColor = null;
                    }
                    if (val != self.curFontColor) {
                        if (val == null) {
                            $('#' + self.config.toolbarId + ' .font-colo i').css("background-color", "#333333");
                            self.curFontColor = null;
                        } else {
                            $('#' + self.config.toolbarId + ' .font-colo i').css("background-color", val);
                            self.curFontColor = val;
                        }
                    }
                },
                html:"<div class='font-btns font-colo'><a href='javascript:;' btntype='btnFontColo' title='前景色' unselectable='on'><span btntype='btnFontColo' unselectable='on'><i btntype='btnFontColo' unselectable='on'></i></span></a></div>"
            },
            btnFontColoAction:{
                exec:function (self, $srcElement) {
                    var color = $srcElement.attr("coloval");
                    self.execCommand("forecolor", color);
                    $('#' + self.config.toolbarId + ' .font-colo i').css("background-color", $srcElement.attr("coloval"));
                    self.curFontColor = color;
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            },
            btnImg:{
                visible:true,
                exec:function (self) {
                    if (!self.ImgWrapDom || self.ImgWrapDom.length == 0) {
                        var html = '<div class="imgWrap"><form method="post" enctype="multipart/form-data" class="uploadImg clearfix" action="/uploadDiagramPic" target="photo-frame" id="J_LocalImgForm"><input type="button" value="上传本地图片" class="bbl-btn upload-cover" id="J_swfUpload"><input type="file" class="upload-btn" id="J_LocalImgFormSubmit" name="fileData"><span class="fl gc pt5 pl5">支持GIF、JPG、PNG,大小不超过2M</span></form><div class="netImg clearfix"><p>插入网络图片：</p><input class="base-input" id="J_InsertNetImgInput" value="" placeholder="http://" autocomplete="off"/><input type="button" id="J_InsertNetImgSubmit" class="bbl-btn" value="确定"></div><div class="tipbox-up"><em>◆</em><span>◆</span></div><iframe style="width:0px;height:0px;padding:0px;" src="" frameborder="0" name="photo-frame"></iframe></div>';
                        $('#' + self.config.toolbarId).append(html);
                        swfu = new SWFUpload({//全局变量
                            upload_url:"/uploadDiagramPic",
                            file_size_limit: "2 MB",
                            file_types: "*.jpg;*.gif;*.png;*.bmp;*.jpeg",
                            file_post_name: "fileData",
                            file_upload_limit : 10,
                            file_queue_limit : 10,
                            // Event Handler Settings
                            file_queue_error_handler: fileQueueError,
                            file_dialog_complete_handler: fileDialogComplete,
                            upload_progress_handler: uploadProgress,
                            upload_error_handler: uploadError,
                            upload_success_handler: uploadSuccess,
                            upload_complete_handler: uploadComplete,
                            // Button settings
                         //   button_image_url: www_domain + "images/bbs/swfupload_btn.gif?v=0418.css",
                            button_placeholder_id: "J_swfUpload",
                            button_image_url : "http://www.swfupload.org/button_sprite.png",
                            button_width : 61,
                            button_height : 22,
                            button_text : "上传本地图片",
                            button_text_style : ".redText { color: #FF0000; }",
                        //    button_text_style : "bbl",
                      //      button_width: 48,
                         //   button_height: 38,
                            // Flash Settings
                            flash_url: "/assets/js/sea-modules/swfupload.swf",
                            debug: false
                        });

                        //-----------------------------------------------------------------------------------------------------
//-------------------------------------swfupload 相关函数-------------------------------------------------
                        function fileDialogComplete(numFilesSelected, numFilesQueued) { //选取完图片后开始上传
                            if (numFilesQueued > 0) {
                                this.startUpload();
                                document.getElementById("upload_bg").style.display = "inline-block";
                            }
                        }
                        function uploadSuccess(file, serverData) { //上传成功后插入图片
                          var data = jQuery.parseJSON(serverData)
                            if(data.code =="100"){

                                if (self.isIE678) {
                                    self.insertHTML("<img unselectable='on'class='img-upload'  src='" +data.src + "'/>");
                                } else {
                                    var imgDom = self.iframeDocument.createElement("img");
                                    imgDom.src = data.src;
                                    imgDom.setAttribute("class","img-upload")
                                    imgDom.setAttribute("unselectable", "on")
                                    //    imgDom.setAttribute("title", $srcElement.attr("title"))
                                    //     imgDom.setAttribute("alt", $srcElement.attr("title"))
                                    self.insertHTML(imgDom);
                                }
                            }else{
                                alert("fail")
                            }
                         //   self.curVisiableDom.hide();
                         //   self.curVisiableDom = null
                        }
                        function uploadComplete(file) {		//一张图片上传完成
                            if (this.getStats().files_queued > 0) {
                                this.startUpload();
                                document.getElementById("upload_bg").style.display = "inline-block";
                            }
                        }
                        function fileQueueError(file, errorCode, message) {	//加入上传队列出错
                            switch (errorCode) {
                                case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                                    alert('图片大小为零！');
                                    break;
                                case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                                    alert('图片大小限制为2M！');
                                    break;
                                default:
                                    alert(message);
                                    break;
                            }
                        }
                        function uploadProgress(file, bytesLoaded) {	//显示上传进度,进度条总宽度为200px
                            var percent = Math.ceil((bytesLoaded / file.size) * 100);
                            document.getElementById("upload_color").style.width = percent / 100 * 200 + "px";
                            document.getElementById("upload_percent_num").innerHTML = percent;
                        }
                        function uploadError(file, errorCode, message) {  //上传过程出错
                            if (errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
                                alert('上传已终止，请重试！');
                            } else {
                                alert(message);
                            }
                        }
                        /* 网络图片 */
                        self.insertNetImgInputDom = $('#J_InsertNetImgInput');
                        self.insertNetImgSubmitDom = $('#J_InsertNetImgSubmit');
                        self.insertNetImgSubmitDom.click(function () {
                            var url = $.trim(self.insertNetImgInputDom.val());
                            var reg_url = /^https?\:\/\//i;
                            if (url.length > 200) {
                                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.hiwowo.tip.show(self.insertNetImgSubmitDom, "请输入一个正确的图片网址");
                                return "image src is too long!";
                            }
                            if (reg_url.test(url)) {
                                var media = {};
                                media.id = url;
                                media.pic = url;
                                media.name = "网络图片";
                             //   self.insertMedia(media, "img");
                                if (self.isIE678) {
                                    self.insertHTML("<img unselectable='on'class='img-upload'  src='" + url + "'/>");
                                } else {
                                    var imgDom = self.iframeDocument.createElement("img");
                                    imgDom.src = url;
                                    imgDom.setAttribute("class","img-upload")
                                    imgDom.setAttribute("unselectable", "on")
                                    //    imgDom.setAttribute("title", $srcElement.attr("title"))
                                    //     imgDom.setAttribute("alt", $srcElement.attr("title"))
                                    self.insertHTML(imgDom);
                                }
                                self.insertNetImgInputDom.val("");
                                self.curVisiableDom.hide();
                                self.curVisiableDom = null
                            } else {
                                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.hiwowo.tip.show(self.insertNetImgSubmitDom, "请输入一个正确的图片网址(带http://)");
                            }
                        })
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.ImgWrapDom = $('#' + self.config.toolbarId + ' .imgWrap');
                        self.curVisiableDom = self.ImgWrapDom;
                    } else {
                        if (self.curVisiableDom == self.ImgWrapDom) {
                            self.ImgWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.ImgWrapDom.show();
                            self.curVisiableDom = self.ImgWrapDom;
                        }
                    }

                },
                html:"<div class='media-btns img'><a href='javascript:;' btntype='btnImg' title='图片' unselectable='on'>图片</a></div>"
            },
            btnVideo:{
                visible:true,
                exec:function (self) {
                    if (!self.VideoWrapDom || self.VideoWrapDom.length == 0) {
                        var html = '<div class="videoWrap sg-dialog"><div class="content"><p class="title">输入视频播放页网址：</p><form class="sg-form" name="shareGoods" action=""><div class="clearfix"><input class="base-input sg-input" id="J_InsertVideoInput" name="url" value="" placeholder="http://" autocomplete="off"><input type="button" id="J_InsertVideo" class="bbl-btn" value="确定"></div></form><div class="sg-source"><p>已支持网站：</p><div class="source-list clearfix"><a class="icon-youku" href="http://www.youku.com/" target="_blank">优酷网</a><a class="icon-tudou" href="http://www.tudou.com/" target="_blank">土豆网</a><a class="icon-sinavideo" href="http://video.sina.com.cn/" target="_blank">新浪视频</a></div></div><div class="tipbox-up"><em>◆</em><span>◆</span></div></div></div>';
                        $('#' + self.config.toolbarId).append(html);
                        self.insertVideoSubmitDom = $('#J_InsertVideo');
                        self.VideoInputDom = $('#J_InsertVideoInput');
                        self.insertVideoSubmitDom.bind("click", function () {
                            var videoUrl = $.trim(self.VideoInputDom.val());
                            var reg_url = /^https?\:\/\//i;
                            var reg_youku = /^https?\:\/\/v\.youku\.com\//i;
                            var reg_sinavideo = /^https?\:\/\/[^\/]+sina\.com\.cn\//i;
                            var reg_tudou = /^https?\:\/\/[^\/]+tudou\.com\//i;
                            if (reg_url.test(videoUrl)) {
                                if (reg_youku.test(videoUrl) || reg_sinavideo.test(videoUrl) || reg_tudou.test(videoUrl)) {
                                    self.VideoInputDom.val("");
                                    $.ajax({
                                        url:"/editor/getVideo",
                                        type:"post",
                                        dataType:"json",
                                        data:{
                                            url:videoUrl
                                        },
                                        success:function (json) {
                                            switch (json.code) {
                                                case 100:
                                                {
                                                    self.curVisiableDom.hide();
                                                    self.curVisiableDom = null;
                                                    json.id = json.swf;
                                                    json.name = json.title;
                                                 //   self.insertMedia(json, "video");
                                                    alert("提示输入video")
                                                }
                                                    break;
                                                case 101:
                                                {
                                                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                                                    $.hiwowo.tip.show(self.btnBaobei, json.msg);
                                                    self.curVisiableDom.hide();
                                                    self.curVisiableDom = null;
                                                }
                                                    break;
                                            }
                                        }
                                    });
                                } else {
                                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                                    $.hiwowo.tip.show(self.insertVideoSubmitDom, "不支持该站视频");
                                }
                            } else {
                                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                                $.hiwowo.tip.show(self.insertVideoSubmitDom, "请输入一个正确的视频网页地址(带http://)");
                            }
                        });
                        self.VideoWrapDom = $('#' + self.config.toolbarId + ' .videoWrap');
                        if (self.curVisiableDom) {
                            self.curVisiableDom.hide();
                        }
                        self.curVisiableDom = self.VideoWrapDom;
                    } else {
                        if (self.curVisiableDom == self.VideoWrapDom) {
                            self.VideoWrapDom.hide();
                            self.curVisiableDom = null;
                        } else {
                            if (self.curVisiableDom)
                                self.curVisiableDom.hide();
                            self.VideoWrapDom.show();
                            self.curVisiableDom = self.VideoWrapDom;
                        }
                    }
                },
                html:"<div class='media-btns video'><a href='javascript:;' btntype='btnVideo' title='视频' unselectable='on'>视频</a></div>"
            },
            btnSplit:{
                visible:true,
                html:"<span class='split'></span>"
            },
            //按钮按顺序加载
            btnsLoadOrder:['btnFontSize', 'btnFontBold', 'btnFontColo', 'btnSplit','btnImg','btnVideo']
        },
        isIE678:!+"\v1",
        iframe:null,
        iframeDocument:null,
        setConfig:function (conf) {
            return $.extend(true, this.config, conf || {});
        },
        init:function (conf) {
            var self = this;
            //更新配置
            this.setConfig(conf)
            //加载Editor
            this.insertEditor();
            this.setEditor();

            this.id = 0;
        },
        insertEditor:function () {
            var self = this;
            var html = "<div class='guang-editor-wrap'><div class='guang-editor'><div class='edit-btns' id='" + self.config.toolbarId + "'>";
            $.each(self.config.btnsLoadOrder, function () {
                var btn = self.config[this];
                if (btn.visible) {
                    html += btn.html;
                    if (btn.cssName) {
                        if (!self.selectionStyleFuns) {
                            self.selectionStyleFuns = {};
                        }
                        self.selectionStyleFuns[btn.cssName] = btn.selectionStyleFun;
                    }
                }
            }) ;
            html += "</div>"  ;
            html += "<div class='iframeWrap'><iframe frameborder='0' id='J_GuangEditorIframe'></iframe></div>"  ;
            html += "</div></div>" ;
            $("#" + self.config.textareaID).after(html).hide();
            this.iframe = $("#J_GuangEditorIframe")[0];
            this.iframeDocument = this.iframe.contentDocument || this.iframe.contentWindow.document;
        },
        setToolbar:function () {
            var self = this;
            $("body").bind("click", function () {
                if (self.curVisiableDom) {
                    self.curVisiableDom.hide();
                    self.curVisiableDom = null;
                }
            })
            $('#' + self.config.toolbarId).bind("click", function () {
                var e = arguments[0] || window.event,
                    target = e.srcElement ? $(e.srcElement) : $(e.target),
                    btnType = target.attr("btntype");
                if (e.stopPropagation) {
                    e.stopPropagation();
                } else {
                    e.cancelBubble = true;
                }
                if (btnType) {
                    self.config[btnType].exec(self, target);
                } else {
                    //self.execCommand("","");
                }
            });
        },
        setEditor:function () {
            var self = this;
            //给按钮添加功能
            self.setToolbar();
            //填充iframe内容，主要功能是使用户在多行输入的时候，iframe自动增高
            self.iframeDocument.designMode = "on";
            self.iframeDocument.open();
            if (self.isIE678) {
                self.iframeDocument.write('<html><head><style type="text/css">html,body{height:100%;width:100%;margin:0;padding:0;border:0;overflow:auto;background:#fff;cursor:text;font-size:14px;word-wrap:break-word;}p{padding:0;margin:0;}*{line-height:160%;}body{font-family:Arial,Helvetica,Sans-Serif;font-size:14px;text-align:left;} p{margin:10px 0;} em{font-style:italic;} img{border:0;max-width:100%;cursor:default;}.img-goods,.img-upload { display: block;max-width: 200px; max-height: 250px; _width: 200px;background:  url(/assets/css/global/images/editor-img.gif) no-repeat bottom right transparent;}</style></head></html>');
            } else {
                self.iframeDocument.write('<html><head><style type="text/css">html,body{height:100%;width:100%;margin:0;padding:0;border:0;overflow:auto;background:#fff;cursor:text;font-size:14px;word-wrap:break-word;}p{padding:0;margin:0;}*{line-height:160%;}html{height:1px;overflow:visible;} body{overflow:hidden;font-family:Arial,Helvetica,Sans-Serif;font-size:14px;text-align:left;} p{margin:10px 0;} em{font-style:italic;} img{border:0;max-width:100%;}.img-goods,.img-upload { display: block;max-width: 200px; max-height: 250px; _width: 200px;background:  url(/assets/css/global/images/editor-img.gif) no-repeat bottom right transparent;}</style></head></html>');
            }
            self.iframeDocument.close();
            var textareaVal = $("#" + self.config.textareaID).val();
            if (textareaVal != "") {
              self.iframeDocument.body.innerHTML =textareaVal;
                self.iframe.contentWindow.focus();
                $(self.iframe).height($(self.iframeDocument).height());
            }
            //当用户使用鼠标在文本上操作的时候，获得该文本区域的样式，使工具栏样式联动
            $(self.iframeDocument).bind("mouseup click", function () {
                var e = arguments[0] || window.event,
                    curElm,
                    nodeName;
                $.hiwowo.loginDialog.isLogin();
                //时间涉及选中和点击，选中有可能只在某个节点内，那么会同时触发点击
                //判断是否选中文本
                if (e.type == "mouseup") {
                    var range = self.getRange();
                    if (self.isIE678) {
                        if (range.text.length != 0) {
                            curElm = self.selectionTextContainer = range.parentElement();
                        } else {
                            self.selectionTextContainer = null;
                        }
                    } else {
                        if (range.endContainer != range.startContainer) {
                            if (range.commonAncestorContainer.nodeType == 3) {
                                curElm = self.selectionTextContainer = range.commonAncestorContainer.parentNode;
                            } else {
                                curElm = self.selectionTextContainer = range.commonAncestorContainer;
                            }
                        } else {
                            self.selectionTextContainer = null;
                        }
                    }
                    if (self.selectionTextContainer) {
                        for (var i in self.selectionStyleFuns) {
                            var parents = $(self.selectionTextContainer).parents("font,b,span,p,div");
                            if (parents.length == 0) {
                                parents = null;
                            }
                            self.selectionStyleFuns[i](self, self.selectionTextContainer, parents);
                        }
                    }
                    //未选中文本
                } else if (self.selectionTextContainer == null) {
                    curElm = e.srcElement ? e.srcElement : e.target;
                    if (self.curVisiableDom) {
                        self.curVisiableDom.hide();
                        self.curVisiableDom = null;
                    }
                    for (var i in self.selectionStyleFuns) {
                        var parents = $(curElm).parents();
                        if (parents.length == 0) {
                            parents = null;
                        }
                        self.selectionStyleFuns[i](self, curElm, parents);
                    }
                }
            })
            $(self.iframeDocument).bind("keyup", function (event) {
                try {
                    var range = self.getRange();
                    var funs = self.selectionStyleFuns;
                    var length = self.selectionStyleFuns.length;
                    for (var i = 0; i < length; i++) {
                        funs[i](self, self.isIE678 ? range.parentElement() : range.endContainer.parentNode);
                    }
                } catch (e) {
                    alert(e)
                }
                $(self.iframe).height($(self.iframeDocument).height());
                //当工具栏被滚动到看不见的时候...
                if (!self.toolbarBindScrollEvent) {
                    $(window).bind("scroll", function () {
                        self.toolbarBindScrollEvent = true;
                        var docScrollTop = $(document).scrollTop();
                        if (!self.toolbarOffsetTop) {
                            self.toolbarOffsetTop = $('#' + self.config.toolbarId).offset().top;
                        }
                        if (self.toolbarOffsetTop <= docScrollTop) {
                                if (!self.toolbarPositionFixed) {
                                    self.toolbarPositionFixed = true;
                                    $('#' + self.config.toolbarId).css({
                                        position:"fixed",
                                        top:"38px",
                                        width:$('#' + self.config.toolbarId).width() + "px"
                                    })
                                }

                        } else {
                            if (self.toolbarPositionFixed) {
                                self.toolbarPositionFixed = false;
                                $('#' + self.config.toolbarId).css({
                                    position:"relative",
                                    top:"0"
                                })
                            }
                        }
                    });
                }
            });
            //IE下光标会丢失
            if (/msie/.test(navigator.userAgent.toLowerCase())) {
                var addEvent = function (el, type, fn) {
                    el['e' + type + fn] = fn;
                    el.attachEvent('on' + type, function () {
                        el['e' + type + fn]();
                    });
                }
                var bookmark;
                //记录IE的编辑光标
                addEvent(self.iframe, "beforedeactivate", function () { //在文档失去焦点之前
                    var range = self.iframeDocument.selection.createRange();
                    bookmark = range.getBookmark();
                });
                //恢复IE的编辑光标
                addEvent(self.iframe, "activate", function () {
                    if (bookmark) {
                        var range = self.iframeDocument.body.createTextRange();
                        range.moveToBookmark(bookmark);
                        range.select();
                        bookmark = null;
                    }
                });
            }
        },
        getRange:function () {
            var contentWindow = this.iframe.contentWindow;
            var selection = null;
            var range = null;
            if (this.isIE678) { // ie6,7,8 not ie9
                selection = contentWindow.document.selection;
                range = selection.createRange();
            } else { // 标准
                selection = contentWindow.getSelection();
                range = selection.getRangeAt(0);
            }
            return range;
        },
        //insertHTML 向编辑器插入html代码
        //@param html (String||Node @@如果是ie678则传字符串，如果是标准浏览器，则传node)
        insertHTML:function (html) {
            var contentWindow = this.iframe.contentWindow;
            contentWindow.focus();
            var range = this.getRange(0);
            var selection = null;
            if (this.isIE678) {
                range.pasteHTML(html);
            } else {
                range.insertNode(html);
                range.setEndAfter(html);
                range.setStartAfter(html);
                selection = contentWindow.getSelection();
                selection.removeAllRanges();
                selection.addRange(range);
            }
        },
        execCommand:function (cmd, val) {
            try {
                this.iframeDocument.execCommand(cmd, false, val);
                this.iframe.contentWindow.focus();
            } catch (e) {
            }
        }



    }
    Editor.fn.init.prototype = Editor.fn;
    $.hiwowo.Editor = Editor;



});