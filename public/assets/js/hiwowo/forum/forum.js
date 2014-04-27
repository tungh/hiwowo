/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-1
 * Time: 下午10:49
 *
 */

define(function(require, exports) {
    var $ = jQuery = require("jquery");
    require("simpleEditor")
    isCommited = false;
    //讨论组组件
    $.hiwowo.forum = {
        //评论与回复提交前校验
        discussSubmit : function($this){
            $this.attr('disabled',true);

                  var content =$("#J_discussContent").val()
                if(content==""){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不能为空哦！");
                    $this.attr('disabled',false);
                    return;
                }
                if(content >= 500){
                    $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                    $.hiwowo.tip.show($this,"内容不得超过500字");
                    $this.attr('disabled',false);
                    return;
                }

            var discuss = {
                "topicId":parseInt($("#J_topicId").val()),
                "quoteContent": $("#J_discussQuote").html(),
                "content": $("#J_discussContent").val()
            };
            var $topicDiscussForm = $("#J_topicDiscussForm");

            $("#J_quoteContent").val($("#J_discussQuote").html())
            if($.hiwowo.loginDialog.isLogin()){

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
        },
        //通用讨论组初始化
        init : function(){
            /* format emotion */
            $(".J_discussCon").each(function(){
                var $this = $(this);
                var html = $this.html();
                $this.data("content",html)
                html = $.hiwowo.simpleEditor.decodeFace(html);
                $this.html(html);
            })

            //评论与回复
            var $discussQuote = $("#J_discussQuote");
            var $topicDiscussForm = $("#J_topicDiscussForm");
            //回复
            $(document).on("click",".J_discussReply",function(){

                var $li = $(this).closest("li");
                var userInfo = $li.find(".user-info").html();
                var replyCon = $li.find(".J_discussCon").html();
                alert(userInfo)
                var segmentHtml = "";
                segmentHtml += '<blockquote class="clearfix">';
                segmentHtml += userInfo ;
                segmentHtml += '<p class="fl ml10">' + $.trim(replyCon) + '</p>';
                segmentHtml +='<a class="close">X</a>';
                segmentHtml += '</blockquote>';


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