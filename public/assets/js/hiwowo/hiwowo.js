/**
 * Created by zuosanshao.
 * User: Administrator
 * Date: 12-10-22
 * Time: 下午8:58
 * Email:zuosanshao@qq.com
 * @contain: 前端基础功能插件，包含基础库、功能库（定位、浮层、校验、滚动、提示框） 用户登录、分享宝贝
 * @depends:
 * Includes:
 * Since: 2014-01-18
 * ModifyTime : 2014-01-18
 * ModifyContent: 基础功能
 * http://www.hiwow.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    var Cookie = require("cookie");
    $.hiwowo = $.hiwowo || {
        version: "v1.0.0"
    };
    /* 基础库*/
    $.extend($.hiwowo, {
        util: {
            //获取url参数名
            getUrlParam : function(paramName){
                var reg = new RegExp("(^|\\?|&)"+ paramName +"=([^&]*)(\\s|&|$)", "i");
                if (reg.test(location.href)) return unescape(RegExp.$2.replace(/\+/g, " ")); return "";
            },
            getUrlParam : function(url,paramName){
                var reg = new RegExp("(^|\\?|&)"+ paramName +"=([^&]*)(\\s|&|$)", "i");
                if (reg.test(url)) return unescape(RegExp.$2.replace(/\+/g, " ")); return "";
            },
            //判断是否ie6
            isIE6: function() {
                return navigator.userAgent.indexOf("MSIE 6.0") !== -1
            },
            //判断是否是苹果手持设备
            isIOS: function(){
                return /\((iPhone|iPad|iPod)/i.test(navigator.userAgent)
            },
            //去掉首尾空字符
            trim: function(str) {
                return str.replace(/(^\s*)|(\s*$)/g,"");
            },
            //去掉首空字符
            lTrim: function(str){
                return str.replace(/(^\s*)/g, "");
            },
            //去掉尾空字符
            rTrim: function(str){
                return str.replace(/(\s*$)/g, "");
            },
            //获取字符串中文长度
            getStrLength: function(str) {
                str = $.hiwowo.util.trim(str);
                var theLen = 0,
                    strLen = str.replace(/[^\x00-\xff]/g,"**").length;
                theLen = parseInt(strLen/2)==strLen/2 ? strLen/2 : parseInt(strLen/2)+0.5;
                return theLen;
            },
            //截取一定长度的中英文字符串并转全角
            substring4ChAndEn: function(str,maxLength){
                var strTmp = str.substring(0,maxLength*2);
                while($.hiwowo.util.getStrLength(strTmp)>maxLength){
                    strTmp = strTmp.substring(0,strTmp.length-1);
                }
                return strTmp;
            },
            //将<>"'&符号转换成全角
            htmlToTxt: function(str){
                var RexStr = /\<|\>|\"|\'|\&/g;
                str = str.replace(RexStr, function(MatchStr) {
                    switch (MatchStr) {
                        case "<":
                            return "＜";
                            break;
                        case ">":
                            return "＞";
                            break;
                        case "\"":
                            return "＼";
                            break;
                        case "'":
                            return "＇";
                            break;
                        case "&":
                            return "＆";
                            break;
                        default:
                            break;
                    }
                })
                return str;
            },
            //截取一定长度的字符串
            ellipse: function(str, len) {
                var boolLimit = $.hiwowo.util.getStrLength(str)*2 > len;
                if(str && boolLimit){
                    return str.replace(new RegExp("([\\s\\S]{"+len+"})[\\s\\S]*"),"$1…");
                }
                return str;
            },
            //校验是否为空
            isEmpty: function(v) {
                return $.hiwowo.util.trim(v)=="" ? false : true;
            },
            //校验邮箱是否合法
            isEmail: function(v) {
                return /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(v);
            },
            /*判断是否是url*/
            isUrl:function(v){ return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(v));
            },
            //校验字符长度不超过某个长度
            tooShort: function(v,l) {
                return v.length<l ? false : true;
            },
            //校验是否包含网址
            noLink: function(v){
                var matchURL = v.match(/(http[s]?:\/\/)?[a-zA-Z0-9-]+(\.[a-zA-Z0-9]+)+/);
                return matchURL==null ? true : false;
            },
            //获取dom对象的当前位置
            getPosition: function(ele){
                var top = ele.offset().top,
                    left = ele.offset().left,
                    bottom = top + ele.outerHeight(),
                    right = left + ele.outerWidth(),
                    lmid = left + ele.outerWidth()/2,
                    vmid = top + ele.outerHeight()/2;
                var position = {
                    leftTop: function(){
                        return {x: left, y: top}
                    },
                    leftMid: function(){
                        return {x: left, y: vmid}
                    },
                    leftBottom: function(){
                        return {x: left, y: bottom}
                    },
                    topMid: function(){
                        return {x: lmid, y: top}
                    },
                    rightTop: function(){
                        return {x: right, y: top}
                    },
                    rightMid: function(){
                        return {x: right, y: vmid}
                    },
                    rightBottom: function(){
                        return {x: right, y: bottom}
                    },
                    MidBottom: function(){
                        return {x: lmid, y: bottom}
                    },
                    middle: function(){
                        return {x: lmid, y: vmid}
                    }
                };
                return position;
            },
            //获取根域名
            getDomain: function(url){
                var domain = "null";
                var regex = /[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\.?/;
                var match = regex.exec(url);
                if (typeof match != "undefined" && null != match) {
                    domain = match[0];
                }
                return domain;
            },
            //校验合法网站
            validSite: function(url){
                var domain = $.hiwowo.util.getDomain(url);
                if(url.indexOf("hiwowo.com") != -1){
                    return (domain == "hiwowo.com" && url.indexOf("goods/") != -1) ? "hiwowo" : false;
                }else if (url.indexOf("tmall.com") != -1) {
                    var curBool=true, bool1, bool2, bool3, boo4;
                    bool3 = (domain == "detail.tmall.com" || domain == "item.tmall.com") && (url.indexOf("item.htm?") != -1);
                    bool4 = (domain == "detail.tmall.com" || domain == "item.tmall.com") && (url.indexOf("spu_detail.htm?") != -1);
                    switch(curBool){
                        case bool3 : {return "tmall3"} break;
                        case bool4 : {return "tmall4"} break;
                        default : {return false;} break;
                    }
                }else if (url.indexOf("taobao.com") != -1) {
                    var curBool=true, bool1, bool2, bool3;
                    bool1 = (domain == "item.taobao.com" || domain == "item.beta.taobao.com" || domain == "item.lp.taobao.com") && (url.indexOf("item.htm?") != -1);
                    switch(curBool){
                        case bool1 : {return "taobao"} break;
                        default : {return false;} break;
                    }
                } else {
                    return false;
                }
            },
            openWin: function(url){
                var top=190;
                var whichsns = url.substr(url.lastIndexOf("snsType=")+8,1);
                if(whichsns==4 || whichsns==5){
                    var left=document.body.clientWidth>820 ? (document.body.clientWidth-820)/2 : 0;
                    window.open(url, 'connect_window', 'height=700, width=820, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else if(whichsns==8){
                    var left=(document.body.clientWidth-580)/2;
                    window.open(url, 'connect_window', 'height=620, width=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else if(whichsns==9){
                    var left=document.body.clientWidth>900 ? (document.body.clientWidth-900)/2 : 0;
                    window.open(url, 'connect_window', 'height=550, width=900, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }else{
                    var left=(document.body.clientWidth-580)/2;
                    window.open(url, 'connect_window', 'height=420, width=580, toolbar=no, menubar=no, scrollbars=yes, resizable=no,top='+top+',left='+left+', location=no, status=no');
                }

            },
            moveEnd : function(obj){
                obj.focus();
                var len = obj.value.length;
                if (document.selection) {
                    var sel = obj.createTextRange();
                    sel.moveStart('character',len);
                    sel.collapse();
                    sel.select();
                } else if (typeof obj.selectionStart == 'number' && typeof obj.selectionEnd == 'number') {
                    obj.selectionStart = obj.selectionEnd = len;
                }
            },
            submitByEnter : function(e, clk) {
                e = e || window.event;
                var key = e ? (e.charCode || e.keyCode) : 0;
                if(key == 13) {
                    clk();
                }
            }
        }
    });
    /*功能库*/
    $.fn.extend({
        //返回顶部
        returnTop: function(){
            if(!this[0]){
                return;
            }
            var backToTopEle = this.click( function() {
                $("html, body").animate({
                    scrollTop: 0
                }, 500);
                var topH = $(window).height()+80+"px";

                backToTopEle.data("isClick",true);
                backToTopEle.css("bottom",topH);
            });


            var showEle = function(){
                if(backToTopEle.data("isClick")){
                }else{
                    backToTopEle.css({"opacity":1,"bottom":"200px"});
                }
            };
            var timeDelay = null;
            var backToTopFun = function() {
                var docScrollTop = $(document).scrollTop();
                var winowHeight = $(window).height();
                (docScrollTop > 0)? showEle(): backToTopEle.css({"opacity":0,"bottom":"-200px"}).data("isClick",false);
                //IE6下的定位
                if ($.hiwowo.util.isIE6()) {
                    backToTopEle.hide();
                    clearTimeout(timeDelay);
                    timeDelay = setTimeout(function(){
                        backToTopEle.show();
                        clearTimeout(timeDelay);
                    },1000);
                    backToTopEle.css("top", docScrollTop + winowHeight - 125);

                }
            };
            $(window).bind("scroll", backToTopFun);
        },
        //等比例缩放图片
        resizeImage: function(width,height){
            this.each(function(){
                var obj = $(this)[0];
                var w = obj.width;
                var h = obj.height;
                if (w <= width && h <= height) {
                    return;
                } else if (w <= width && h > height) {
                    obj.width = w * height / h;
                    obj.height = height;
                } else if (w > width && h <= height) {
                    obj.width = width;
                    obj.height = h * width / w;
                } else {
                    obj.width = width;
                    obj.height = h * width / w;
                    var temp=h * width / w;
                    if(temp>height) {
                        obj.width = w * height / h;
                        obj.height = height;
                    }
                }
            });
        },
        //文本框高度自适应
        textareaAutoHeight: function(){
            var obj = this;
            var h = obj.outerHeight();
            var func = function(){
                h < 0 && (h = obj.outerHeight());
                if($.browser.mozilla || $.browser.safari){
                    obj.height(h);
                }
                var sh = obj[0].scrollHeight,
                    autoHeight = sh < h ? h: sh,
                    autoHeight = autoHeight < h * 1.5 ? h: sh;
                obj.height(autoHeight);
            }
            obj.bind("keyup input propertychange focus",func);
        },
        //按钮置为灰色
        disableBtn: function(str){
            var $btn = this;
            $btn[0].disabled = "disabled";
            $btn.removeClass(str).addClass("disabled");
        },
        //开启按钮
        enableBtn: function(str){
            var $btn = this;
            $btn[0].disabled = "";
            $btn.removeClass("disabled").addClass(str);
        },
        //下拉菜单
        dropDown: function(options){
            var settings = {
                event: "mouseover",
                classNm: ".dropdown",
                timer: null,
                fadeSpeed: 100,
                duration: 500,
                offsetX: 82,
                offsetY: 8,
                isLocation: false
            };
            if(options) {
                $.extend(settings, options);
            }

            var triggers = this,
                $dropDown = $(settings.classNm);
            triggers.each(function() {
                $this = $(this);
                $this.hover(function(){
                    clearTimeout(settings.timer);
                    $(".dropdown:not("+settings.classNm+")").hide();
                    if(settings.isLocation){
                        var position = $.hiwowo.util.getPosition($(this)).rightBottom();
                        $dropDown.css({
                            left: position.x - settings.offsetX + "px",
                            top: position.y + settings.offsetY + "px"
                        });
                    }
                    $dropDown.fadeIn(settings.fadeSpeed);
                },function(){
                    settings.timer = setTimeout(function(){
                        $dropDown.fadeOut(settings.fadeSpeed);
                    },settings.duration);
                });
                $dropDown.hover(function(){
                    clearTimeout(settings.timer);
                    $dropDown.show();
                },function(){
                    settings.timer = setTimeout(function(){
                        $dropDown.fadeOut(settings.fadeSpeed);
                    },settings.duration);
                });
            });
        },
        addToFav:function(o){
            var url = "http://hiwowo.com";
            var title = "hiwowo，Love for animal";
            if (window.sidebar) { // Mozilla Firefox Bookmark
                window.sidebar.addPanel(title, url,"");
            } else if( window.external&&document.all) { // IE Favorite
                window.external.AddFavorite( url, title);
            } else if(window.opera) { // Opera 7+
                return false; // do nothing
            } else {
                jQuery.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error2";
                jQuery.hiwowo.tip.show($(o),"您的浏览器不支持自动加收藏，请按 ctrl+d 加入收藏。");
            }
        }
    });


    /*
     * 字数统计限制
     * $.hiwowo.wordCount.init($wordCon,$wordNum,10)
     */
    $.hiwowo.wordCount = {
        conf : {
            okClk : function(){},
            errorClk : function(){}
        },
        init : function($wordCon, $wordNum, limitNum, speed) {
            //判断是否存在评论框，不存在则返回
            var thelimit = limitNum;
            var charDelSpeed = speed || 15;
            var toggleCharDel = speed != -1;
            var toggleTrim = true;
            var that = $wordCon[0];
            var getLen = function(){
                var wordConVal = $.trim($wordCon.val());
                return $.hiwowo.util.getStrLength(wordConVal);
            }
            updateCounter();
            function updateCounter(){
                $wordNum.text(thelimit - parseInt(getLen()));
            };

            $wordCon.bind("keypress", function(e) {
                if (getLen() >= thelimit && e.charCode != '0') e.preventDefault()
            });
            $wordCon.bind("keyup", function(e) {
                updateCounter();
                if (getLen() >= thelimit && toggleTrim) {
                    if (toggleCharDel) {
                        that.value = that.value.substr(0, thelimit * 2 + 100);
                        var init = setInterval(function() {
                                if (getLen() <= thelimit) {
                                    init = clearInterval(init);
                                    updateCounter()
                                } else {
                                    that.value = that.value.substring(0, that.value.length - 1);
                                    $.hiwowo.wordCount.conf.errorClk(that.value.length);
                                    //$wordNum.text('trimming...  ' + (thelimit - that.value.length));
                                };
                            },
                            charDelSpeed);
                    } else {
                        $wordCon[0].value = that.value.substr(0, thelimit);
                    }
                }
            }).focus(function() {
                    updateCounter();
                });
        }
    }
    /*
     * 提示框
     */
    $.hiwowo.tip = {
        conf: {
            timer: null,
            timerLength: 3000,
            tipClass : ""
        },
        show: function(o,text){
            clearTimeout($.hiwowo.tip.conf.timer);
            var position = $.hiwowo.util.getPosition(o).topMid();
            if(!$(".tipbox")[0]){
                $("body").append('<div class="tipbox"></div>');
            }
            $(".tipbox").attr("class",'tipbox ' + $.hiwowo.tip.conf.tipClass)
            $(".tipbox").html(text);
            var W = $(".tipbox").outerWidth(),
                H = $(".tipbox").outerHeight();
            $(".tipbox").css({
                left: position.x - W/2 + "px",
                top: position.y - H - 10 + "px"
            }).fadeIn();
            $.hiwowo.tip.conf.timer = setTimeout(function(){
                $(".tipbox").fadeOut();
            },$.hiwowo.tip.conf.timerLength);
        }
    }
    /* 初始化加载中的内容*/
    $(function(){

        //标签输入框自动转换“,”
        $(document).on("keyup","input[rel=tagsInput]",function(){
            //限制每个标签的中文长度
            var MaxSingleTagLength = 14,
                MaxAllTagsLength = 64,
                thisVal = $(this).val();
            if($.hiwowo.util.getStrLength($.hiwowo.util.htmlToTxt(thisVal))<=MaxAllTagsLength){
                var $this = $(this);
                thisVal = thisVal.replace(/\uff0c|\s+/g,",");
                while(thisVal.indexOf(',,')>=0){
                    thisVal = thisVal.replace(',,',',');
                }
                var thisValueArr = thisVal.split(","),
                    thisValueArrIndex = 0,
                    istoolong = false;
                for(;thisValueArrIndex<thisValueArr.length;thisValueArrIndex++){
                    var val = thisValueArr[thisValueArrIndex]
                    if($.hiwowo.util.htmlToTxt(val).length>MaxSingleTagLength){
                        istoolong = true;
                        thisValueArr[thisValueArrIndex] = $.hiwowo.util.substring4ChAndEn(val,MaxSingleTagLength);
                    }
                }
                if(istoolong){
                    thisVal = thisValueArr.join(",");
                }
                if(thisVal != this.value){
                    this.value = thisVal;
                }
            }else{
                this.value = $.hiwowo.util.substring4ChAndEn(thisVal,64);
            }
        })


        /* 下拉框 */
        $(".gohome").dropDown({
            classNm: ".set-dropdown"
        });
        /*分享好东西*/
        $(".btn-sg").dropDown({
            classNm: ".shareit-dropdown"
        });
        $(".btn-checkIn").dropDown({
            classNm: ".checkIn-dropdown"
        });
        /*消息*/
        $(".xiaoxi").dropDown({
            classNm: ".xiaoxi-dropdown"
        });


        /* 搜索框效果 header 搜索框*/
        $(".header-search-button").bind("click",function(){
            var self=$(this);
            var form=self.closest("form");
            var input=$(".search-input-keyword");
            var inputVal=input.val();
            inputVal=$.hiwowo.util.trim(inputVal);
            if(inputVal=="请输入您想要搜索关键词"){
                input.val("");
                input.focus();
                return false;
            }
            if(inputVal==""){
                input.focus();
                return false;
            }
            form.submit(function(){});
        });

        $(".search-input-keyword").bind("click",function(){
            $("#search").addClass("typing")
            var self=$(this);
            var inputVal=$.hiwowo.util.trim(self.val());
            if(inputVal=="请输入您想要搜索关键词"){
                self.val("");
            }
        });

        $(".search-input-keyword").bind("keydown",function(event){
            var keycode=event.which;
            var self=$(this);
            var form=self.closest("form");
            var inputVal=self.val();
            inputVal=$.hiwowo.util.trim(inputVal);
            if(keycode=="13"){
                if(inputVal==""){
                    self.focus();
                    return false;
                }

                form.submit(function(){});
            }
        });

        $(".search-input-keyword").bind("blur",function(){
            $("#search").removeClass("typing")
            var self=$(this);
            var inputVal=$.hiwowo.util.trim(self.val());
            if(inputVal==""){
                self.val("请输入您想要搜索关键词");
            }
        });

        /* 返回顶部 */
        $("#returnTop").returnTop();

        $('.like-common .like').hover(function(){
            $(this).parent().children('.like-num').find('.J_scrollUp').animate({ top:"-24" }, 600)
        },function(){
            $(this).parent().children('.like-num').find('.J_scrollUp').animate({  top:"0" },  600)

        })
        //鼠标enter,leave到item上显示可点击的喜欢,喜欢数
        $('.like-state .ico-likes').hover(function(){
            $(this).closest(".like-state").find(".J_scrollUp").animate({top:'-24'},600)
        },function(){
            $(this).closest(".like-state").find(".J_scrollUp").animate({top:'0'},600)
        })




        //第三方登录优先级
        var refererUrl=document.referrer
        var referer="hiwowo"
        if(refererUrl.indexOf("qq")>0)referer ="qq"
        if(refererUrl.indexOf("taobao")>0 || refererUrl.indexOf("tmall")>0)referer ="taobao"
        if(refererUrl.indexOf("weibo")>0) referer="weibo"
        var loginNavArr = [
            {"referer":"qq", "snsType":"qzong"},//4
            {"referer":"weibo", "snsType":"sina"},//3
            {"referer":"taobao", "snsType":"taobao"}//8
        ]
        var RefererGuide = function(curReferer, snsType){
            if(!$(".guide")[0]){
                var otherLoginArr = $.grep(loginNavArr, function(value, index){
                    return (value.referer != curReferer);
                });
                var otherLoginHtml = "";
                $.map(otherLoginArr, function(value, index){
                    otherLoginHtml += '<li><a target="_blank" class="shortcut-login-' + value.referer + '" href="http://hiwowo.com/user/snsLogin?snsType=' + value.snsType + '">' + value.referer + '</a></li>';
                });
                var HTML = '<div class="guide guide-' + curReferer + '">'
                    +'<div class="guide_boby">'
                    +'<p><i></i>登录 hiwowo.com ，发现你的喜欢！<span><a href="/user/regist">注册帐号 ></a></span></p>'
                    +'<div class="guide_add">'
                    +'<a id="J_RefererLogin" class="referer-login" target="_blank" href="http://hiwowo.com/user/snsLogin?snsType=' + snsType + '">' + curReferer + '</a>'
                    +'<div class="favorites" id="J_Favorites"><a href="javascript:void(0);">加入收藏夹</a></div>'
                    +'<ul class="other-login">' + otherLoginHtml + '</ul>'
                    +'</div>'
                    +'<div class="del" id="J_CloseGuide">'
                    +'<a href="javascript:void(0);">关闭引导</a>'
                    +'</div>'
                    +'</div>'
                    +'</div>';
                $("body").append(HTML);
                $("#J_Favorites").click(function(){
                    $.hiwowo.addToFav(this);
                })
                $("#J_CloseGuide").click(function(){
                    Cookie.set("refererGuide","no")    // 点击取消之后，不在出现
                    $(".guide").hide();
                })
            }
            var posGuide = function(){
                if($.hiwowo.util.isIE6()){
                    var windowtop = $(window).height()-90;
                    $(".guide").css({
                        position:"absolute",
                        top:windowtop
                    }).show();
                    $(window).bind("scroll",function(){
                        var docScrollTop = $(document).scrollTop();
                        if ($.hiwowo.util.isIE6()) {
                            $(".guide").css("top", (docScrollTop+windowtop)+"px")
                        }
                    });
                }else{
                    $(".guide").show().animate({
                        bottom:0
                    },500);
                }
            }
            var href = window.location.href;
            if(href.indexOf("/user/login")==-1 && href.indexOf("/user/doEmailLogin")==-1&& href.indexOf("/user/regist")==-1){
                if("no" != Cookie.get("refererGuide") && referer !="hiwowo"){
                    posGuide();
                }
            }

        }

        //不登陆的用户会出现分享条 并且根据referer 引导登录
        if(HIWOWO.userId == "" && typeof referer != 'undefined'){
            switch(referer){
                case "hiwowo":
                    break;
                case "taobao" : {
                    RefererGuide("taobao", "taobao");
                }break;
                case "weibo" : {
                    RefererGuide("weibo", "sina");
                }break;
                case "qq": {
                    RefererGuide("qq", "qzone");
                }break;
                /*default : {
                 RefererGuide("qq", "qzone");
                 }*/
            }
        }else{
            $(window).bind("scroll",function(){
                var showguide = function(){
                    var win = $(window);
                    var HTML = "";
                    if($(".guide").length!==1){
                        HTML = '<div class="guide guide-share">'
                            +'<div class="guide_boby">'
                            +'<p>如果你喜欢Hiwowo，就把hiwowo.com加入收藏夹吧，或者分享给你的朋友~</p>'
                            +'<div class="guide_add" style="margin-right:174px;">'
                            +'<div class="favorites" id="J_Favorites"><a href="javascript:void(0);">加入收藏夹</a></div>'
                            +'<div class="weibo">'
                            +'<a href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。hiwowo：http://hiwowo.com\');var link=encodeURIComponent(window.location.href);var pic=\'http://hiwowo.com/assets/img/ui/hiwowo.jpg\';window.open(\'http://service.t.sina.com.cn/share/share.php?appkey=2610725727&title=\'+title+\'&pic=\'+pic);})())" alt="分享到新浪微博">分享到新浪微博</a>'
                            +'<a style="margin-left:6px; width:130px" href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。hiwowo：http://hiwowo.com\');var link=encodeURIComponent(window.location.href);var pic=\'http://hiwowo.com/assets/img/ui/hiwowo.jpg\';window.open(\'http://v.t.qq.com/share/share.php?appkey=db0de5e94b314972b3e7efd23fa7ce1e&title=\'+title+\'&pic=\'+pic+\'&site=\'+link);})())" alt="分享到腾讯微博"></a>'
                            +'<a style="margin-left:6px; width:118px" href="javascript:void((function(){var title=encodeURIComponent(\'推荐个不错的网站，能找到好多喜欢的东西。hiwowo：http://hiwowo.com\');var link=encodeURIComponent(window.location.href);window.open(\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=\'+link);})())" alt="分享到QQ空间"></a>'
                            +'</div>'
                            +'</div>'
                            +'<div class="del" id="J_CloseGuide">'
                            +'<a href="javascript:void(0);">关闭引导</a>'
                            +'</div>'
                            +'</div>'
                            +'</div>';
                        $("body").append(HTML);

                        $("#J_Favorites").click(function(){
                            $.hiwowo.addToFav(this);
                        })
                        $("#J_CloseGuide").click(function(){
                            // 用户取消后，cookie 记录，为了保证不在出现，扰民
                            Cookie.set("showGuide","no")
                            $(".guide").hide();
                        })
                    }
                    if($.hiwowo.util.isIE6()){
                        $(".guide").css({
                            position:"absolute",
                            top:win.scrollTop()+win.height()-90
                        })
                        if(win.scrollTop()>800){
                            $(".guide").show();
                        }
                    }else{

                        if(win.scrollTop()>800){

                            $(".guide").show().animate({
                                bottom:0
                            },500);
                        }
                    }
                }
                /*如果用户没有点击取消 则显示 引导*/
                if("no" != Cookie.get("showGuide")){
                    showguide();
                }
            });
        }
//同步授权登录后关注弹出层
        window.followhiwowo = function(code,msg,site,flag,refresh){
            if(code==444){
                alert(msg);
                return false;
            }
            if((site!="sina" && site!="qzone" ) || flag=="true" ){
                if(refresh){
                    window.location.reload();
                }
                return false;
            }
            var bdClass = "",
                frameHtml = "";
            if(site=="sina"){
                bdClass = "sinaBd";
                frameHtml = '<iframe width="63" height="24" frameborder="0" allowtransparency="true" marginwidth="0" marginheight="0" scrolling="no" border="0" src="http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=63&height=24&uid=1283431903&style=1&btn=red&dpc=1"></iframe>';
            }else if(site=="qzone"){
                bdClass = "qzoneBd";
                frameHtml = '<iframe src="http://open.qzone.qq.com/like?url=http%3A%2F%2Fuser.qzone.qq.com%2F1469909930&type=button&width=400&height=30&style=2" allowtransparency="true" scrolling="no" border="0" frameborder="0" style="width:65px;height:30px;border:none;overflow:hidden;"></iframe>';
            }

            if(!$("#followDialog")[0]){
                var html = '<div id="J_followDialog" class="g-dialog">';
                html +=	'<div class="dialog-content">';
                html +=	'<div class="hd"><h3></h3></div>';
                html +=	'<div class="bd clearfix '+bdClass+'">';
                html +=	'<div class="btnFrame">';
                html +=	frameHtml;
                html +=	'</div>';
                html +=	'</div>';
                html +=	'<i></i>';
                html +=	'<label><input type="checkbox" class="check" name="noMore" />不再提示</label>';
                html +=	'<a class="close" href="javascript:;"></a>';
                html +=	'</div>';
                html +=	'</div>';
                $("body").append(html);
                if($("#loginDialog:visible")[0]){
                    $("#loginDialog").empty().remove();
                    $("#exposeMask").empty().remove();
                }
                followOverlay = new Overlay({
                    classPrefix: "g-dialog",
                    template:html,
                    width: 600,
                    zIndex: 9999,
                    align: {
                        selfXY: [ "50%", "50%" ],
                        baseXY: [ "50%", "50%" ]
                    }
                });
                followOverlay.show();
                Mask.show() ;
            }else{
                followOverlay = new Overlay({
                    classPrefix: "g-dialog",
                    template:"#J_followDialog",
                    width: 600,
                    zIndex: 9999,
                    align: {
                        selfXY: [ "50%", "50%" ],
                        baseXY: [ "50%", "50%" ]
                    }
                });
                followOverlay.show();
                Mask.show() ;
            }
            $(document).on("click","#J_followDialog",function(){
                if($("input[name=noMore]")[0].checked){
                    Cookie.set("noMoreTip","n");
                }
                if(refresh){
                    window.location.reload();
                }
            })

        }

        /*异步授权登陆后*/
        window.refresh=function(){
            window.location.reload();
        }


    });



});