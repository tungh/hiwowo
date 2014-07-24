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

define(function(require, exports) {
    require("hiwowo")
    require("simpleEditor")
    isCommited = false;
    //讨论组组件
    $.hiwowo.forum = {
        //帖子创建与编辑
        editSubmit : function($this){

            if(!isCommited){
                isCommited = true;
                var topic={
                    id:$("#J_id").val(),
                    typeId:$("#J_typeId").val(),
                    title:'',
                    content:''
                }
                var frameBody = $("#J_GuangEditorIframe").contents().find("body")
                if($.trim(frameBody.html())!="偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^"){
                    $("#J_editorContent").val(frameBody.html())
                    topic.content=frameBody.html()
                }

                $("#J_typeId").val(parseInt($(".editor-select").find(".selected").data("typeid")))
                topic.title=$("#J_title").val()
                if(topic.title == ""){
                    isCommited=false;
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"标题不能为空哦！");
                }else if($.trim(topic.content) == ""){
                    isCommited=false;
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不能为空哦！");
                }else if($.hiwowo.util.getStrLength(topic.title) > 50 || $.hiwowo.util.getStrLength(topic.content) >= 10000){
                    isCommited=false;
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"亲，标题<50字，内容<10000字");
                }else{
                    isCommited=false;
                    $("#J_editForm").submit();
                }
            } else{
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"正在提交，请耐心等待 ^_^");
            }
        },
        //通用讨论组初始化
        init : function(){
            /* editor */
            /* 触发焦点 判断 是否登录 */
            $("#J_title").focus(function(){
                $.hiwowo.loginDialog.isLogin();
            });
            $("#J_editorContent").focus(function(){
                $.hiwowo.loginDialog.isLogin();
            });
            //标题输入框
            $('#J_title').focus(function() {
                if ($(this).val() == '请输入标题') {
                    $(this).val('').css({'color': '#323232'});
                }
            })
            $('#J_title').blur(function() {
                if ($(this).val() == '') {
                    $(this).val('请输入标题').css({'color': '#a0a0a0'});
                }
            })
            /* text area 内容输入 */
            $('#J_editorContent').focus(function() {
                if ($(this).val() == '内容要健康哦') {
                    $(this).val('').css({'color': '#323232'});
                }
            })
            $('#J_editorContent').blur(function() {
                if ($(this).val() == '') {
                    $(this).val('内容要健康哦').css({'color': '#a0a0a0'});
                }
            })
            /* editor 内容输入 */
            //    if($.hiwowo.editor){
            var frameBody = $("#J_GuangEditorIframe").contents().find("body")
            frameBody.focus(function(){
                if($.trim(frameBody.html())=="内容要健康哦"){
                    frameBody.html("");
                }
            })
            frameBody.blur(function(){
                if ($.trim(frameBody.html())== '') {
                    frameBody.html("内容要健康哦")

                }
            })
            //  }
            //点击主要板块
            $('.main-select a').click(function() {
                $('.main-select a, .other-list a').removeClass('selected');
                $('#J_otherBtn').removeClass('selected').addClass('other-btn').text('');
                $(this).addClass('selected');
            })

            /* edit 提交 */
            $("#J_submit").click(function(event){
                event.preventDefault();
                if($.hiwowo.loginDialog.isLogin()){
                    $.hiwowo.forum.editSubmit($(this));
                }
            })


        }
    }

    $(function(){
        $.hiwowo.forum.init();
    })










});