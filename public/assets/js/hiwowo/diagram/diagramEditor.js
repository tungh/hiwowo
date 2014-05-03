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
define(function(require){
    var $ = jQuery = require("jquery")
    isCommited = false;
    $.hiwowo.diagramEditor={

        /* 保存草稿 */
        saveDraft:function($this){

        },
        createSubmit:function($this){
            if(!isCommited){
            isCommited = true;
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

            diagram.id =parseInt($("#J_id").val())
          if(diagram.title == ""){
               isCommited=false;
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"标题不能为空哦！");
            }else if($.trim(diagram.content) == ""){
              isCommited=false;
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"内容不能为空哦！");
            }else if(diagram.content.indexOf("img-upload")<0){
              isCommited=false;
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"至少需要上传一张图片哦");
            }else if($.hiwowo.util.getStrLength(diagram.title) > 50 || $.hiwowo.util.getStrLength(diagram.content) >= 10000){
              isCommited=false;
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
                        $this.addClass("func_button_disabled").val("提交中……")
                    },
                    success: function(data) {
                      isCommited=false;
                        if(data.code=="100") {
                            $this.removeClass("func_button_disabled").val("发表")
                            if(diagram.id ==0){
                                $.hiwowo.diagramEditor.systemTags(data.diagramId,data.tags)
                            }else{
                                window.location="/diagram/"+data.diagramId
                            }

                        } else {
                            alert(data.message)
                        }
                    }
                });
           }
            } else{
                $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                $.hiwowo.tip.show($this,"正在提交，请耐心等待 ^_^");
            }
        },
         systemTags:function(id,tags){

            if(!$("#J_systemTagsDialog")[0]){
                var html = "";
                html +='<div class="modal fade" data-backdrop="static" id="J_systemTagsDialog">';
                html +='<div class="modal-dialog">';
                html +='<div class="modal-content">';
                html +='<div class="modal-header"> ';
                html +='<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>';
                html +='<h4 class="modal-title">最后一步</h4>';
                html +='</div>';
                html +='<div class="modal-body">';
                html += '<div class="bd clearfix">';
                html +='<span class="tags-intro">亲，我也是有身份的人，给我贴几个标签O(∩_∩)O </span>';
               html +='<div class="tags-container" id="J_tagsContainer" class="clearfix"><span class="my-tags" id="J_myTags"></span><input class="fl" type="text" id="tag-input" autocomplete="off" maxlength="6" size="10"></div> ';
                html +='<div id="tag-searched" style="display: none;"></div>';
               html +='<p class="tips"><span class="c96">输入标签，按"回车键"完成一个标签</span><em id="tip-input-size"></em></p>';
                html +='<dl class="sys-tags clearfix zoom" id="J_sysTags"> <dt class="fl">推荐标签：</dt><dd class="fl"></dd> </dl>';
                html += '<div class="clear"></div>';
                html += '</div>';
                html += '</div>';
                html += '<div class="modal-footer">';
                html += '<input type="button" class="bbl-btn" id="J_submitLabels" value="确认">';
                html += '</div>';
                html += '</div> ';
                html += '</div>';
                html += '</div> ';
                $("body").append(html)
                $("#J_systemTagsDialog").modal('show',{
                    backdrop:'static'
                })
            }else{
                $("#J_systemTagsDialog").modal('show',{
                    backdrop:'static'
                })
            }
             /* 分配 tags to J_sysTags */
             var list =""
             if (tags!=null && tags !="" ) {
                 $.each(tags, function(i, item) {
                     list += '<a href="javascript:;" >' +item + '</a>';
                 })

                 $('#J_sysTags dd').html(list);
             } else {
                 $('#J_sysTags dd').html('<h2 style="color:#646464;">抱歉哦，系统未能推荐相关标签，请手动添加</h2>');
             }

             /* 选择system tags 进入 my tags 中 */
             $(document).on("click","#J_sysTags a",function(){
                 $(this).appendTo("#J_myTags")
             })
            /* remove my tags 中的标签*/
             $(document).on("click","#J_myTags a",function(){
                 if ($("#J_myTags").find('a').length < 5) {
                     $('#tag-input').show().focus();
                     $('#tip-input-size').text('');
                 }
                 $(this).remove()
             })

             //输入框获得焦点
             $('#tag-input').focus(function(e) {
                 if ($.trim($(this).val())) {
                     $('#tag-searched').slideDown();
                 }
                 e.stopPropagation();
             });
              /* 处理输入的标签 */
             var oldKeyWord;
             $('#tag-input').keyup(function(e) {
                 var  currentTag = $("#current-tag"),
                     newKeyWord = $.trim($(this).val()).replace(/[^\u4E00-\u9FA50-9a-zA-Z]/g, '');
                 if (!newKeyWord) {
                     $('#tag-searched').empty().hide();
                     oldKeyWord = '';
                     return;
                 }
                 if (newKeyWord != oldKeyWord) {
                     $('#tag-searched').show().empty().append('<a href="javascript:;">添加 <b>' + newKeyWord + '</b> 标签</a>');
                     $('#tag-searched a').first().attr("id", "current-tag");
                     oldKeyWord = newKeyWord;
                 } else {							//判断按键
                     var keyCode = e.keyCode || event.keyCode;
                     switch (keyCode) {
                         case 13:                //回车键
                             var a =  currentTag.find("b").text();
                             if (!a) {
                                 $("#tag-searched").slideUp();
                                 break;
                             } else {
                                     var kk = false;
                                     var b = '<a href="javascript:;"  >' + a + '</a>';
                                 //去重标签添加
                                 $('#J_myTags a').each(function() {
                                     if ($(this).text() == a) {
                                         alert('已添加过此标签');
                                         kk = true;
                                         return false;
                                     }
                                 })
                                 if (kk == true) return false;
                                 $('#J_myTags').append(b);
                                 $('#tag-input').val('');
                                 $('#tag-searched').empty().hide();

                                 oldKeyWord = '';
                                 if ($('#J_myTags a').length == 5) {
                                     $('#tag-input').val('').hide();
                                 }
                             }
                             break;
                     }
                 }
             })

             //输入框失去焦点
             $("#tag-input").blur(function() {
                 setTimeout(searchedHide, 100);
             })

             function searchedHide() {
                 $("#tag-searched").slideUp('fast');
             }
             /* 处理当close modal*/
             $(document).on("click","#J_systemTagsDialog button.close",function(){
                     if ($('#J_myTags a').length > 0) {
                         var mytags ="";
                         $('#J_myTags a').each(function() {
                             mytags +=$(this).text()+ ",";
                         })
                         $.hiwowo.diagramEditor.addTags(id,mytags)
                     }else{
                         window.location="/diagram/"+id
                     }
             })
             /* 处理 确认 */
             $(document).on("click","#J_submitLabels",function(){
                 if ($('#J_myTags a').length > 0) {
                     var mytags ="";
                     $('#J_myTags a').each(function() {
                         mytags +=$(this).text()+ ",";
                     })
                     $.hiwowo.diagramEditor.addTags(id,mytags)
                 }else{
                     window.location="/diagram/"+id
                 }
             })

        },
        addTags:function(id,tags){
            $.ajax({
                url: "/diagram/addLabels",
                type: "post",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify({id:id,labels:tags}),
                success: function(data) {
                    if(data.code=="100") {
                        window.location="/diagram/"+id
                    } else {
                        alert(data.message)
                    }
                }
            });

        },
         editSubmit:function($this){
             if(!isCommited){
                 isCommited = true;
                 var diagram={
                     id:$("#J_id").val(),
                     typeId:$("#J_typeId").val(),
                     title:'',
                     content:'',
                     status:$("#J_status").val()
                 }
                 var frameBody = $("#J_GuangEditorIframe").contents().find("body")
                 if($.trim(frameBody.html())!="偶的神啊，先介绍英明神武的我，再上传我的“艳照”吧^_^"){
                     $("#J_editorContent").val(frameBody.html())
                     diagram.content=frameBody.html()
                 }

                  $("#J_typeId").val(parseInt($(".editor-select").find(".selected").data("typeid")))
                 diagram.title=$("#J_title").val()
                 if(diagram.title == ""){
                     isCommited=false;
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"标题不能为空哦！");
                 }else if($.trim(diagram.content) == ""){
                     isCommited=false;
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"内容不能为空哦！");
                 }else if(diagram.content.indexOf("img-upload")<0){
                     isCommited=false;
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"至少需要上传一张图片哦");
                 }else if($.hiwowo.util.getStrLength(diagram.title) > 50 || $.hiwowo.util.getStrLength(diagram.content) >= 10000){
                     isCommited=false;
                     $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                     $.hiwowo.tip.show($this,"亲，标题<50字，内容<10000字");
                 }else{
                     $("#J_editForm").submit();
                 }
             } else{
                 $.hiwowo.tip.conf.tipClass = "tipmodal tipmodal-error";
                 $.hiwowo.tip.show($this,"正在提交，请耐心等待 ^_^");
             }
         },
        init:function(){/* 触发焦点 判断 是否登录 */
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
            $("#J_createSubmit").click(function(event){
                event.preventDefault();
                if($.hiwowo.loginDialog.isLogin()){
                  $.hiwowo.diagramEditor.createSubmit($(this));
                }
            })
            /* 提交 */
            $("#J_editSubmit").click(function(event){
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
