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
            var title="";
            if ($("#J_title").val() != '哎呀，我要个牛逼哄哄的名字') {
              title = $.trim($("#J_title").val())
            }

            if($.hiwowo.editor){
                if($.trim($.hiwowo.editor.iframeDocument.body.innerHTML)==""){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不能为空哦！");
                    $this.attr('disabled',false);
                    return;
                }
                if($.hiwowo.util.getStrLength($.hiwowo.editor.iframeDocument.body.innerHTML) >= 10000){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不得超过10000字");
                    $this.attr('disabled',false);
                    return;
                }

                $("#J_content").val($.hiwowo.editor.iframeDocument.body.innerHTML);
            }
            var content = $("#J_content").val();
            if(title == "" || $.trim(content) == ""){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"标题和内容不能为空哦！");
            }else if($.hiwowo.util.getStrLength(title) > 50 || $.hiwowo.util.getStrLength(content) >= 10000){

                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"亲，标题<50字，内容<10000字");
            }else{
              //  $("#J_ForumPostEditForm").submit();
                alert("hello")

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

    $.hiwowo.diagramEditor.init()

})
