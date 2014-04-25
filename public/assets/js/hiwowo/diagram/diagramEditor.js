/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-20
 * Time: 下午11:00
 *
 */
define(function(require){
    var $ = jQuery = require("jquery")

    $.hiwowo.diagramEditor={
        /* 保存草稿 */
        saveDraft:function($this){

        },
        editSubmit:function($this){

            var diagram={
                id:0,
                typeId:0,
                title:'',
                content:'',
                status:0
            }
            var frameBody = $("#J_GuangEditorIframe").contents().find("body")
                if($.trim(frameBody.html())!="偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^"){
                    diagram.content=frameBody.html()
                }
            if ($("#J_title").val() != '哎呀，我要个牛逼哄哄的名字') {
                diagram.title = $.trim($("#J_title").val())
            }
               diagram.typeId= parseInt($(".editor-select").find(".selected").data("typeid"))

               diagram.id =$("#J_id").val()
            if(diagram.title == ""){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"标题不能为空哦！");
            }else if($.trim(diagram.content) == ""){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"内容不能为空哦！");
            }else if(diagram.content.indexOf("img-upload")<0){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"至少需要上传一张图片哦");
            }else if($.hiwowo.util.getStrLength(diagram.title) > 50 || $.hiwowo.util.getStrLength(diagram.content) >= 10000){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"亲，标题<50字，内容<10000字");
            }else{
                $.ajax({
                    url: "/diagram/ajaxSave",
                    type: "post",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data:JSON.stringify(diagram),
                    beforeSend:function(){
                        $this.disableBtn("bbl-btn");
                    },
                    success: function(data) {
                        if(data.code=="100") {
                            $.hiwowo.diagramEditor.selectTags(data.id,data.tags)
                        } else {
                            alert(data.message)
                        }
                    }
                });


            }
        },
        selectTags:function(id,tags){

            if(!$("#J_selectTagsDialog")[0]){
                var html = "";
                html +='<div class="modal fade" id="J_selectTagsDialog">';
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
                html += '<input type="submit" class="bbl-btn login-submit" value="登录" />';
                html += '<a class="ml10 l30" href="/user/resetPassword">忘记密码？</a></div>';
                html += '</form></div>';
                html += '<div class="bd-r">';
                html += '<p class="mb15">你也可以使用这些帐号登录</p>';
                html += '<div class="site-openid clearfix"><ul class="fl mr20 outlogin-b">';
                html += '<li class="qq mr15"><a id="qq_auth" href="/user/snsLogin?snsType=qzone&backType=asyn&i=0"><i></i><p>QQ</p></a></li>';
                html += ' <li class="weibo"><a id="weibo_auth" href="/user/snsLogin?snsType=sina&backType=asyn&i=0"><i></i><p>新浪微博</p></a></li>';
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
                $("#J_loginDialog").modal('show',{
                    backdrop:'static'
                })
            }else{
                $("#J_selectTagsDialog").modal({
                    backdrop:'static'
                })
            }
        },
        init:function(){
            /* 触发焦点 判断 是否登录 */
            $("#J_title").focus(function(){
                $.hiwowo.loginDialog.isLogin();
            });
            $("#J_editorContent").focus(function(){
                $.hiwowo.loginDialog.isLogin();
            });
            //标题输入框
            $('#J_title').focus(function() {
                if ($(this).val() == '哎呀，我要个牛逼哄哄的名字') {
                    $(this).val('').css({'color': '#323232'});
                }
            })
            $('#J_title').blur(function() {
                if ($(this).val() == '') {
                    $(this).val('哎呀，我要个牛逼哄哄的名字').css({'color': '#a0a0a0'});
                }
            })
            /* text area 内容输入 */
            $('#J_editorContent').focus(function() {
                if ($(this).val() == '偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^') {
                    $(this).val('').css({'color': '#323232'});
                }
            })
            $('#J_editorContent').blur(function() {
                if ($(this).val() == '') {
                    $(this).val('偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^').css({'color': '#a0a0a0'});
                }
            })
            /* editor 内容输入 */
        //    if($.hiwowo.editor){
            var frameBody = $("#J_GuangEditorIframe").contents().find("body")
            frameBody.focus(function(){
                if($.trim(frameBody.html())=="偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^"){
                    frameBody.html("");
                }
            })
            frameBody.blur(function(){
                if ($.trim(frameBody.html())== '') {
                    frameBody.html("偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^")

                }
            })
          //  }
            //点击主要板块
            $('.main-select a').click(function() {
                $('.main-select a, .other-list a').removeClass('selected');
                $('#J_otherBtn').removeClass('selected').addClass('other-btn').text('');
                $(this).addClass('selected');
            })
            //点击其他版块
            $('#J_otherBtn').click(function() {
                $('#J_otherList').slideToggle('slow');
            })
            //点击其他板块栏目
            $('#J_otherList a').click(function() {
                $('.main-select a, #J_otherList a').removeClass('selected');
                var secVal = $(this).text(),
                    secNum = $(this).attr('section');
                $('#J_otherBtn').removeClass('other-btn').addClass('selected').html('<em>' + secVal + '</em>').attr('section', secNum);
                $('#J_otherList').slideUp('slow');
                $(this).addClass('selected');
            })
            /* 保存草稿 */
            $("#J_saveDraft").click(function(){

            })
            /* 提交 */
            $("#J_submit").click(function(event){
                event.preventDefault();
                if($.hiwowo.loginDialog.isLogin()){
                    $.hiwowo.diagramEditor.editSubmit($(this));
                }
            })
        }
    }

    $(function(){
        $.hiwowo.diagramEditor.init()
    })


})
