/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-1
 * Time: 下午10:49
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
  var UM =  require("umeditor");
    require("umeditor-lang")
    require("umeditor-emotion")
    require("umeditor-image")
    require("umeditor-link")
    require("umeditor-map")
    require("umeditor-video")
    var discussEditor = UM.getEditor('J_discussContent');
    var topicEditor = UM.getEditor('J_topicContent');
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

                                var html ='<li>';
                                html +='<a class="img" href="/user/'+SMEITER.userId+'" target="_blank"><img src="'+SMEITER.userPhoto+'" width="80" height="80"></a>';
                                html +='<span class="info"><a class="J_UserNick" href="/user/'+SMEITER.userId+'" target="_blank">'+SMEITER.nick +'</a>/ <span class="time">刚刚</span> </span>';
                                html +=' <div class="post">';
                                if($.trim(comment.quoteContent)!=''){
                                    html +='<div class="post-reply">'+comment.quoteContent+'</div>';
                                }

                                html +=' <div class="J_PostCon wordbreak">'+comment.content+'</div>';
                                html +='</div>';
                                html +='<p class="oper"><a class="J_postReply">回复</a></p>';
                                html +='</li>';

                                $("#J_forumPost").append(html);

                            }
                        }
                    });
                    return false

                }
            }
        },

        //帖子创建与编辑
        editSubmit : function($this){
            var title = $.trim($("#J_postTitle").val());

                if(topicEditor.getContent()==""){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"标题和内容不能为空哦！");
                    $this.attr('disabled',false);
                    return;
                }
                if(topicEditor.getPlainTxt() >= 10000){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不得超过10000字");
                    $this.attr('disabled',false);
                    return;
                }

                $("#J_topicContent").val(topicEditor.getContent());

            var content = $("#J_topicContent").val();
            if(title == "" || $.trim(content) == ""){
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"标题和内容不能为空哦！");
            }else if($.hiwowo.util.getStrLength(title) > 50 || $.hiwowo.util.getStrLength(content) >= 10000){

                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"亲，标题<50字，内容<10000字");
            }else{
                $("#J_ForumTopicEditForm").submit();

            }
        },

        //通用讨论组初始化
        init : function(){
            //评论与回复
            var $postCmtSegment = $("#J_postQuote");
            var $postCommentForm = $("#J_postCommentForm");

            //评论格式化
            if($(".J_PostCon")[0]){
                $(".J_PostCon").each(function(){
                    $(this).html($.hiwowo.util.trim($(this).html()).replace(/\n/g,"<br/>"));
                });
            }

            //回复
            $(document).on("click",".J_postReply",function(){

                var $li = $(this).closest("li");
                var userNick = $li.find(".J_UserNick:first").html();
                var replyCon = $li.find(".J_PostCon").html();
                var time = $li.find(".time").html();
                var segmentHtml = "";
                segmentHtml += '<blockquote>';
                segmentHtml += '<span class="info g-daren">回复 ' + userNick + ' <span class="time">' + time + '</span></span>';
                segmentHtml += '<p>' + $.trim(replyCon) + '</p>';
                segmentHtml += '</blockquote>';
                //console.log(segmentHtml);
                $postCmtSegment.html(segmentHtml);

                $("html, body").scrollTop($postCommentForm.offset().top -50);

                //删除回应
                $postCmtSegment.find(".close:first").unbind("click").click(function(){
                    $postCmtSegment.html("");
                });
            });

            $("#J_discussPublishBtn").click(function(event){
                event.preventDefault();
                $.hiwowo.forum.discussSubmit($(this));
            });

            $postCommentForm.find("textarea").focus(function(){
                $.hiwowo.loginDialog.isLogin();
            });

            //回车键提交评论
            $postCommentForm.find("textarea").on("keyup",function(e){
                var $this = $(this);
                $.hiwowo.util.submitByEnter(e, function(){
                    $.hiwowo.forum.discussSubmit($("#J_postCommentSubmit"));
                });
            });
            //话题创建与编辑
            if($("#J_postEditBtn")[0]){
                $("#J_postTitle").focus(function(){
                    $.hiwowo.loginDialog.isLogin();
                });
                $("#J_commentContent").focus(function(){
                    $.hiwowo.loginDialog.isLogin();
                });
                $("#J_postEditBtn").click(function(event){
                    event.preventDefault();
                    if($.hiwowo.loginDialog.isLogin()){
                        $.hiwowo.forum.editSubmit($(this));
                    }
                });
            }
        }
    }
    $.hiwowo.forum.init();









});