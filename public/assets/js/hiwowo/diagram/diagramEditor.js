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
            var content="";
            var frameBody = $("#J_GuangEditorIframe").contents().find("body")
                if($.trim(frameBody.html())!="偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^"){
                    $("#J_content").val(frameBody.html());
                    content=frameBody.html()
                }
            if ($("#J_title").val() != '哎呀，我要个牛逼哄哄的名字') {
                title = $.trim($("#J_title").val())
            }

            $("#J_typeId").val($('.main-select a:has(".selected")').data("typeid"))

            if(title == ""){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"标题不能为空哦！");
            }else if($.trim(content) == ""){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"内容不能为空哦！");
            }else if($.hiwowo.util.getStrLength(title) > 50 || $.hiwowo.util.getStrLength(content) >= 10000){

                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"亲，标题<50字，内容<10000字");
            }else{
               $("#J_editForm").submit();


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
