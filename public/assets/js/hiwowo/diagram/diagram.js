/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午11:42
 *
 */
 define(function(require){
     var $ = jQuery = require("jquery")
     require("simpleEditor")
     var DiagramReply = {
         //评论与回复提交前校验
         submit : function($this){
             $this.attr('disabled',true);
             var $postCommentForm = $("#J_postCommentForm");
             var $textarea = $postCommentForm.find("textarea");
             $("#J_quoteContent").val($("#J_postQuote").html())

             var comment = {
                 "diagramId":parseInt($("#J_diagramId").val()),
                 "quoteContent": $("#J_postQuote").html(),
                 "content": $("#J_discussContent").val()
             };

             if($.hiwowo.loginDialog.isLogin()){
                 if($.trim($textarea.val()) == ""){
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"亲，评论内容不能为空哦！");
                     $this.attr('disabled',false);
                 }else if($.hiwowo.util.getStrLength($textarea.val()) >= 10){
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"内容小于10字！");
                     $this.attr('disabled',false);
                 }else{
                     $this.attr('disabled',false);

                     $.ajax({
                         url: $("#J_discussForm").attr("action"),
                         type : "POST",
                         contentType:"application/json; charset=utf-8",
                         dataType: "json",
                         data: JSON.stringify(comment),
                         beforeSend: function(){
                             $this.disableBtn("bbl-btn");
                         },
                         success: function(data){
                             if(data.code=="100"){
                                 $this.enableBtn("bbl-btn");

                                 var html ='<li>';
                                 html += '<div class="share-avt">';
                                 html +='<a class="fl" href="/user/'+HIWOWO.userId+'"target="_blank">';
                                 html +='<img class="avt32 fl" src="'+HIWOWO.userPhoto+'" width="38" height="38">';
                                 html +='</a>';
                                 html +="</div>";
                                 html +=' <span class="arrow"></span>';
                                 html +=' <div class="share-user">';
                                 html +='<h3> <a class="J_userNick" href="/user/'+HIWOWO.userId+'" target="_blank">'+HIWOWO.nick+'</a> <p class="user-title"></p></h3>';
                                 html +=' <p class="quote-content">'+comment.quoteContent+'</p>';
                                 html +=' <p class="content J_commentCon">'+comment.content +'</p>';
                                 html +='<div class="item-doing"> <a class="reply J_postReply"  href="javascript:;">回复</a><span class="time">刚刚</span> </div>';
                                 html +='</div>';
                                 html +='</li>';

                                 $("#J_items").append(html);

                             }
                         }
                     });
                     return false
                 }
             }
         },
         //通用讨论组初始化
         init : function(){
             //评论与回复
             var $postQuote = $("#J_postQuote");
             var $postCommentForm = $("#J_postCommentForm");
             //点击回复
             $(document).on("click",".J_postReply",function(){
                 var $li = $(this).closest("li");
                 var userNick = $li.find(".J_userNick:first").html();
                 var commentCon = $li.find(".J_commentCon").html();
                 var time = $li.find(".time").html();
                 var quoteHtml = "";
                 quoteHtml += '<blockquote>';
                 quoteHtml += '<span class="info">回复 ' + userNick + ' <span class="time">' + time + '</span></span>';
                 quoteHtml += '<p>' + $.trim(commentCon) + '</p>';
                 // quoteHtml +='<a class="close">X</a>';
                 quoteHtml += '</blockquote>';

                 $postQuote.html(quoteHtml);
                 $("html, body").scrollTop($postCommentForm.offset().top -50);
                 //删除引用回复
                 $postQuote.find(".close:first").unbind("click").click(function(){
                     $postQuote.html("");
                 });
             });

             $("#J_postCommentSubmit").submit(function(event){
                 event.preventDefault();
                 DiagramReply.submit($(this));
             });

             $postCommentForm.find("textarea").focus(function(){
                 $.hiwowo.loginDialog.isLogin()
             });

             //回车键提交评论
             $postCommentForm.find("textarea").on("keyup",function(e){
                 var $this = $(this);
                 $.hiwowo.util.submitByEnter(e, function(){
                     DiagramReply.submit($("#J_postCommentSubmit"));
                 });
             });

         }


     }

     var diagramDiscuss={
         submit:function($this){
             $this.attr('disabled',true);
             var discuss = {
                 "diagramId":parseInt($("#J_diagramId").val()),
                 "quoteContent": "",
                 "content": $("#J_discussContent").val()
             };

             if($.hiwowo.loginDialog.isLogin()){
                 if($.trim(discuss.content) == ""){
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"亲，评论内容不能为空哦");
                     $this.attr('disabled',false);
                 }else if($.hiwowo.util.getStrLength(discuss.content) >= 140){
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"内容不能大于140字哦");
                     $this.attr('disabled',false);
                 }else{

                     $.ajax({
                         url: $("#J_discussForm").attr("action"),
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
                                 $("#J_discussContent").val("")
                                 var html ='<div class="item clearfix">';
                                     html +='<div class="item-left">';
                                     html += '<a href="/user/'+HIWOWO.userId+'"target="_blank"><img src="'+HIWOWO.userPhoto+'" width="40" height="40"></a>';
                                     html +='</div>';
                                     html +='<div class="item-right">';
                                     html +='<div class="c-head"></div>';
                                     html +='<div class="c-body">'+discuss.content +'</div>';
                                     html +='<div class="c-meta"></div>';
                                     html +='</div>';
                                     html +='</div>';
                                 $("#J_discussForm").append(html);

                             }
                         }
                     });
                     return false
                 }
             }
         },
         formatDiscuss:function(data){
             $("#J_discusses").html(data);
             $(".item .c-body").each(function(){
                 var $this = $(this);
                 var html = $this.html();
                 $this.data("content",html)
                 html = $.hiwowo.simpleEditor.decodeFace(html);
                 $this.html(html);
             })

         },
         init:function(){
             var $discussForm = $("#J_discussForm");
             $discussForm.find("textarea").focus(function(){
                 $.hiwowo.loginDialog.isLogin()
             });
             $("#J_discussSubmit").click(function(event){
                 event.preventDefault();
                 diagramDiscuss.submit($(this));
             });
             //回车键提交评论
             $discussForm.find("textarea").on("keyup",function(e){
                 var $this = $(this);
                 $.hiwowo.util.submitByEnter(e, function(){
                    diagramDiscuss.submit($("#J_discussSubmit"));
                 });
             });
         }
         }


    $(function(){
        diagramDiscuss.init()


        /* ajax 加载 discuss*/
        $.ajax({
            url: "/diagram/getDiscusses",
            type : "GET",
            dataType:"html",
            data:{diagramId:parseInt($("#J_diagramId").val())},
            success: function(data){
                diagramDiscuss.formatDiscuss(data)
            }
        });

        /* 翻页 */
        $(document).on("click","a.pager",function(){
            $.ajax({
                url:"/diagram/getDiscusses",
                type:"get",
                data:{
                    diagramId:parseInt($(this).data("diagramid")),
                    sortBy:$(".pagination span.active").data("order"),
                    p:parseInt($(this).data("page"))
                },
                dataType:"html",
                success:function (data) {
                    diagramDiscuss.formatDiscuss(data)
                }})
        })
        /* 排序查看 */
          $(document).on("click","span.refresh",function(){
              $.ajax({
                  url:"/diagram/getDiscusses",
                  type:"get",
                  data:{
                      diagramId:parseInt($(this).data("diagramid")),
                      sortBy:$(this).data("order"),
                      p:parseInt($(this).data("page"))
                  },
                  dataType:"html",
                  success:function (data) {
                      diagramDiscuss.formatDiscuss(data)
                  }})
          })


    })

 })