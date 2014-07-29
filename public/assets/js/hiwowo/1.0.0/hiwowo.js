/**
 * Created by zuosanshao.
 * User: hiwowo.com
 * Date: 12-10-22
 * Time: 下午8:58
 * Email:zuosanshao@qq.com
 * @contain: 前端基础功能插件，包含基础库、功能库（定位、浮层、校验、滚动、提示框） 用户登录、分享宝贝
 * @depends:
 * Includes:
 * Since: 2014-01-18
 * ModifyTime : 2014-01-18
 * ModifyContent: 基础功能
 * http://hiwow.com/
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    require("bootstrap")

    $.hiwowo = $.hiwowo || {
        version: "v1.0.0"
    };
    /* 基础库*/
    $.extend($.hiwowo, {
        util: {
            //判断是否ie6
            isIE6: function() {
                return navigator.userAgent.indexOf("MSIE 6.0") !== -1
            },

            //去掉首尾空字符
            trim: function(str) {
                return str.replace(/(^\s*)|(\s*$)/g,"");
            },

            //获取字符串中文长度
            getStrLength: function(str) {
                str = $.hiwowo.util.trim(str);
                var theLen = 0,
                    strLen = str.replace(/[^\x00-\xff]/g,"**").length;
                theLen = parseInt(strLen/2)==strLen/2 ? strLen/2 : parseInt(strLen/2)+0.5;
                return theLen;
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
        }

    });

    /* 对话框：用户登陆 和 判断是否为新用户 */
    $.hiwowo.loginDialog = {
        /*判断是否登陆*/
        isLogin: function(){
            if(HIWOWO.userId == ""){
                $.hiwowo.loginDialog.login();
                return false;
            }
            return true;
        },
        login: function(){

            if(!$("#J_loginDialog")[0]){
                var html = "";
                html +='<div class="modal fade" id="J_loginDialog">';
                html +='<div class="modal-dialog">';
                html +='<div class="modal-content">';
                html +='<div class="modal-header"> ';
                html +='<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
                html +='<h4 class="modal-title">登录</h4>';
                html +='</div>';
                html +='<div class="modal-body">';
                html += '<div class="bd clearfix"><div class="bd-l">';
                html += '<form id="J_loginDialogForm" action="/user/dialogEmailLogin" method="POST">';
                html += '<div class="error-row"><p class="error"></p></div>';
                html += '<div class="form-row"><label>Email：</label>';
                html += '<input type="text" class="base-input" name="email" id="email" value="" placeholder="" />';
                html += '</div>';
                html += '<div class="form-row"><label>密码：</label>';
                html += '<input type="password" class="base-input" name="password" id="password" value="" />';
                html += '</div>';
                html += '<div class="form-row"><label>&nbsp;</label>';
                html += '<input type="checkbox" class="check" name="remember" value="1" checked="checked" />';
                html += '<span>两周内自动登录</span>';
                html += '</div>';
                html += '<div class="form-row act-row clearfix"><label>&nbsp;</label>';
                html += '<input type="submit" class="btn btn-info left" value="登录" />';
                html += '<a class="ml10 l30" href="/user/resetPassword">忘记密码？</a></div>';
                html += '</form></div>';
                html += '<div class="bd-r">';
                html += '<p class="mb15">你也可以使用这些帐号登录</p>';
                html += '<div class="site-openid clearfix"><ul class="fl mr20 outlogin-b">';
                html += '<li class="qq mr15"><a id="qq_auth" href="/user/snsLogin?snsType=qzone"><i></i><p>QQ</p></a></li>';
                html += ' <li class="weibo"><a id="weibo_auth" href="/user/snsLogin?snsType=sina"><i></i><p>新浪微博</p></a></li>';
                html += '</ul>';
                html += '</div>';
                html += '</div>';
                html += '<div class="clear"></div>';
                html += '</div>';
                html += '</div>';
                html += '<div class="modal-footer">';
                html += '<div class="noaccount">还没有帐号？<a href="/user/regist">免费注册一个</a></div>';
                html += '</div>';
                html += '</div> ';
                html += '</div>';
                html += '</div> ';
                $("body").append(html)
                $("#J_loginDialog").modal('show')
            }else{
                $("#J_loginDialog").modal('show')
            }

            $("#J_loginDialog .close").bind("click",function(){

                $("#J_loginDialogForm")[0].reset();
                $("#J_loginDialog").find(".error-row").hide();
                $("#J_loginDialog").modal('hide')
            });

            $("#J_loginDialogForm").submit(function(){
                var  $this = $(this);
                $.ajax({
                    type: "POST",
                    url:$this.attr("action"),
                    data:$this.serializeArray(),
                    dataType:"json",
                    success:function(data){
                        if(data.code==100){
                            $("#J_loginDialog").modal('show')
                            window.location.reload();
                        }else if(data.code==101){
                            $("#J_loginDialog").find(".error-row").fadeIn();
                            $("#J_loginDialog").find(".error").html(data.message);
                            $("#J_loginDialog input[name=password]").val("");
                        }
                    }
                });
                return false;
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
    /* 初始化加载中的内容*/
    $(function(){



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
        /* 下拉框 */
        $("#J_topbar_user").dropDown({
            classNm: ".topbar-dropdown"
        });


        /* 用户登录弹出框 */
        if($("a[rel=loginD]")[0]){
            $("a[rel=loginD]").click(function(event){
                event.preventDefault();
                $.hiwowo.loginDialog.login();
            });
        }


        window._bd_share_config = {
            common : {
                "bdSnsKey":{"tsina":"2918471608","tqq":"801278954"},
                "bdStyle": "1",
                "bdSize": "24",
                onBeforeClick:function(cmd,config){
                    var share = config.tag
                    config.bdText = $("#"+share).data("title");
                    config.bdDesc = $("#"+share).data("desc");
                    config.bdUrl =  $("#"+share).data("url");
                    config.bdPic = $("#"+share).data("pic");
                    return config;
                }
            },
            share : [{
                "bdSize" : 24
            }],

            image : [{
                viewType : 'list',
                viewPos : 'top',
                viewColor : 'black',
                viewSize : '24',
                viewList : ['qzone','tsina','weixin','sqq']
            }],
            selectShare : [{
                "bdselectMiniList" : ['qzone','tsina','weixin','sqq']
            }]
        }


    });



});