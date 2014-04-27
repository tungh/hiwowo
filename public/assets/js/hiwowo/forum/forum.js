/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-1
 * Time: 下午10:49
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    isCommited = false;
    //讨论组组件
    $.hiwowo.forum = {

        //评论与回复提交前校验
        discussSubmit : function($this){
            $this.attr('disabled',true);


                if(discussEditor.getContent()==""){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不能为空哦！");
                    $this.attr('disabled',false);
                    return;
                }
                if(discussEditor.getPlainTxt() >= 500){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不得超过500字");
                    $this.attr('disabled',false);
                    return;
                }
                $("#J_discussContent").val(discussEditor.getContent());

            var discuss = {
                "topicId":parseInt($("#J_topicId").val()),
                "quoteContent": $("#J_discussQuote").html(),
                "content": $("#J_discussContent").val()
            };
            var $topicDiscussForm = $("#J_topicDiscussForm");
            var $textarea = $topicDiscussForm.find("textarea");
            $("#J_quoteContent").val($("#J_discussQuote").html())
            if($.hiwowo.loginDialog.isLogin()){
                if($.trim($textarea.val()) == ""){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"亲，评论内容不能为空哦！");
                    $this.attr('disabled',false);
                }else if($.hiwowo.util.getStrLength($textarea.val()) >= 500){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容小于10000字！");
                    $this.attr('disabled',false);
                }else{
                    $this.attr('disabled',false);

                    $.ajax({
                        url: $("#J_topicDiscussForm").attr("action"),
                        type : "POST",
                        contentType:"application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify(discuss),
                        beforeSend: function(){
                            $this.disableBtn("bbl-btn");
                        },
                        success: function(data){
                            if(data.code=="100"){
                                $this.enableBtn("bbl-btn");

                                var html ='<li class="clearfix">';
                                html +='<a class="fl" href="/user/'+HIWOWO.userId+'" target="_blank"><img src="'+HIWOWO.userPhoto+'" width="50" height="50"></a>';
                                html +='<div class="info fr"><span class="fl"><a class="J_UserNick" href="/user/'+HIWOWO.userId+'" target="_blank">'+HIWOWO.nick +'</a>/ <em class="time">刚刚</em> </span>';
                                html +=' <div class="topic">';
                                if($.trim(discuss.quoteContent)!=''){
                                    html +='<div class="topic-reply">'+discuss.quoteContent+'</div>';
                                }

                                html +=' <div class="J_discussCon wordbreak">'+discuss.content+'</div>';
                                html +='</div>';
                                html +='<div class="oper"><div class="fr"><a class="J_discussReply">回复</a></div></div>';
                                html +='</div> ';
                                html +='</li>';

                                $("#J_forumTopic").append(html);

                            }
                        }
                    });
                    return false

                }
            }
        },

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
            //评论与回复
            var $discussQuote = $("#J_discussQuote");
            var $topicDiscussForm = $("#J_topicDiscussForm");
            //回复
            $(document).on("click",".J_discussReply",function(){

                var $li = $(this).closest("li");
                var userInfo = $li.find(".user-info").html();
                var replyCon = $li.find(".J_discussCon").html();

                var segmentHtml = "";
                segmentHtml += '<blockquote>';
                segmentHtml += userInfo ;
                segmentHtml += '<p>' + $.trim(replyCon) + '</p>';
                segmentHtml += '</blockquote>';
                //console.log(segmentHtml);

                $discussQuote.html(segmentHtml);

                $("html, body").scrollTop($topicDiscussForm.offset().top -50);

                //删除回应
                $discussQuote.find(".close:first").unbind("click").click(function(){
                    $discussQuote.html("");
                });
            });

            $("#J_discussPublishBtn").click(function(event){
                event.preventDefault();
                $.hiwowo.forum.discussSubmit($(this));
            });

            $topicDiscussForm.find("textarea").focus(function(){
                $.hiwowo.loginDialog.isLogin();
            });

            //回车键提交评论
            $topicDiscussForm.find("textarea").on("keyup",function(e){
                var $this = $(this);
                $.hiwowo.util.submitByEnter(e, function(){
                    $.hiwowo.forum.discussSubmit($("#J_discussPublishBtn"));
                });
            });


        }
    }

    $(function(){
        $.hiwowo.forum.init();
    })










});