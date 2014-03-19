/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-2-23
 * Time: 下午11:42
 *
 */
 define(function(require){
     var $ = jQuery = require("jquery")
      require("jqueryte")
     require("uploadify")
     function isVisiable(node) {
         return $(node).is(":visible");
     }
     var attatchedFunc;
     // 是否有新的编辑动作
     var newaction = false;
     function notChanged() {
         newaction = false;
         blackButton($("#id-btn-draft"));
     }

     function setChanged() {
         newaction = true;
         $("#id-btn-draft").removeClass("btn-draft");
         $("#id-btn-draft").addClass("btn-post");
     }

     function hasChanged() {
         return newaction;
     }



     $(function(){

         $("#J_intro").jqte({
             format: false,
             fsize:false,
             color:false
         })
         $("#J_psLabel").click(function(){
             $("#J_diagramPs").jqte({
                 format: false,
                 fsize:false,
                 color:false
             })
         })
         $("#J_tagsLabel").click(function(){
             $("#J_diagramTags").toggleClass("hidden")
         })
         /* 上传图片*/
         $('#J_uploadify').uploadify({
             'fileObjName' : 'fileData',
             'swf'      : '/assets/js/sea-modules/uploadify.swf',
             'uploader' : '/uploadDiagramPic',
             'buttonText' : '添加图片，支持JPG, GIF, PNG，最多15张',
             'fileSizeLimit' : '5MB',
             'fileTypeExts' : '*.gif; *.jpg; *.png',
             'uploadLimit' : 15,
             'width'    : 580,
             'height':60,
             'itemTemplate' : ' <div class="post-photo post-item-edit" id="${fileID}">\
                 <div class="move-bar"><a class="move-up" href="javascript:;" style="">上移</a><a class="move-down" href="javascript:;" style="display: none;">下移</a></div>\
                 <div  class="uploadify-queue-item"><div class="cancel"> <a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a></div><span class="fileName">${fileName} (${fileSize})</span><span class="data"></span></div>\
                 <div class="edit-box clearfix"><div class="upload-img"><div class="img"></div></div>\
                 <div class="photo-des"><textarea id="image_desc" name="image_desc" cols="" rows="" class="textarea-normal" label="图片描述(可不填)" style="color: rgb(188, 188, 188);"></textarea></div></div>\
                 <div class="site-doing-all clearfix"><span class="site-doing"><a class="img_save_href" href="javascript:;"><ins class="ico-site-save"></ins>保存</a><a id="img_del_href" href="javascript:;"><ins class="ico-site-del"></ins>删除</a></span></div>\
                </div>',
             'removeCompleted' : false,
             'onUploadSuccess' : function(file, data, response) {

                  $("#"+file.id).find("div.img").html("<img src='"+JSON.parse(data).src+"'with='580'> ")
                 $("#"+file.id).find(".uploadify-queue-item").hide()
             }
             // Put your options here
         });

         $("a").bind("click", function(e) {
             var href = $(this).attr("href");
             if($(this).attr("target")) {
                 return;
             }

             if(!hasChanged()) {
                 return;
             }

             if(!href) {
                 return;
             }

             if(href == "#") {
                 return;
             }

             if(href.indexOf("javascript") == -1) {
                 confirmOperate(function() {
                     window.location.href=href;
                 }, "编辑贴子中，确定要跳转到其它页面？");
                 e.preventDefault();
             }
         });
         // 上移的事件
         $(document).on("click",".move-up",function () {
                 var parentNode = $(this).closest(".topic_node");
                 var preNode = getPreClassNode($(parentNode), "topic_node");
                 var moveBarNode = $(parentNode).prev(".add-post-panel");
                 if(preNode) {
                     $(parentNode).insertBefore($(preNode));
                     $(moveBarNode).insertBefore($(preNode));
                 }
                 $(".topic_node").each(function() {
                     checkAndSetMoveBar($(this));
                 });

         });

         // 下移的事件
         $(document).on("click",".move-down",function () {
                 var parentNode = $(this).closest(".topic_node");
                 var nextNode = getNextClassNode($(parentNode), "topic_node");
                 if(nextNode) {
                     var moveBarNode = $(nextNode).prev(".add-post-panel");
                     $(parentNode).insertAfter($(nextNode));
                     $(moveBarNode).insertBefore($(parentNode));
                 }
                 $(".topic_node").each(function() {
                     checkAndSetMoveBar($(this));
                 });

         });
         function btnVisiable(node) {
             $(node).each(function() {
                 if(isVisiable($(this))) {
                     return true;
                 }
             });
             return false;
         }

         function callbackFunc(func) {
             if(func) {
                 func();
             }
         }

// 点击可见的保存按钮
         function clickBtn(node) {
             var visiable = false;
             $(node).each(function() {
                 if(isVisiable($(this))) {
                     $(this).click();
                     visiable = true;
                 }
             });
             return visiable;
         }

         function buttonVisiable(node) {
             var visiable = false;
             $(node).each(function() {
                 if(isVisiable($(this))) {
                     visiable = true;
                 }
             });
             return visiable;
         }
         // 保存草稿
         $("#id-btn-draft").click(function(){
             saveDraft(true)
         })
         $("#id-btn-diagram").click(function(){
             postTopic()
         })
         function saveDraft(draft) {
             var notSavedTopic = false;
             if(attatchedFunc) {
                 if(buttonVisiable($(".img_save_href"))) {
                     return;
                 }
             }
             if(clickBtn($(".img_save_href"))) {
                 notSavedTopic = true;
             }
             if(notSavedTopic) {
                 attatchedFunc = function() {
                     saveDraft(draft);
                 };
                 return;
             }

             var notdefined;
             attatchedFunc = notdefined;

             var diagramId =$("#J_diagramId").val()
             var diagramTitle =$("#J_title").val()
             var diagramPic = $(".topic_node").first().find("img").attr("src");
             var diagramIntro =$("#J_intro").val()
             var diagramContent=getContent()
             var diagramPs =$("#J_ps").val()
             var diagramTags =$("#J_tags").val()
             var picIds = getPicIDs()
             alert(picIds + " : " +diagramContent)
             $.ajax({
                 url: "/diagram/saveDraft",
                 type: "POST",
                 contentType:"application/json; charset=utf-8",
                 dataType: "json",
                 data: JSON.stringify({
                     diagramId:diagramId,
                     diagramTitle: diagramTitle,
                     diagramPic:diagramPic,
                     diagramIntro:diagramIntro,
                     diagramContent:diagramContent,
                     diagramPs:diagramPs,
                     diagramTags:diagramTags,
                     picIds:picIds
                 }),
                 success: function(data) {
                     if(data.code=="100"){
                        alert("success")
                     }
                 }
             });

         }

         function blackButton(node) {
             $(node).addClass("btn-post");
             $(node).removeClass("btn-draft");
         }

         function grayButton(node) {
             $(node).removeClass("btn-post");
             $(node).addClass("btn-draft");
         }
         function postTopic() {
             if(!checkTopicTitleNode($("#title"), 100)) {
                 $("#title").focus();
                 return false;
             }

             $("#id-btn-post").html("正在发布中");
             grayButton($("#id-btn-post"));
             saveDraft(false);
         }
         // 获取匹配到className的前一个元素
         function getPreClassNode(node, className) {
             if($(node).prev().length > 0) {
                 var preNode = $(node).prev();
                 if($(preNode).hasClass(className)) {
                     return preNode;
                 } else {
                     return getPreClassNode($(preNode), className);
                 }
             }
             return null;
         }

         // 设置move bar的class
         function checkAndSetMoveBar(node) {
             var preNode = getPreClassNode($(node), "topic_node");
             var nextNode = getNextClassNode($(node), "topic_node");

             if(!preNode) {
                 $(node).find(".move-up").hide();
             } else {
                 $(node).find(".move-up").show();
             }

             if(!nextNode) {
                 $(node).find(".move-down").hide();
             } else {
                 $(node).find(".move-down").show();
             }
         }

// 获取匹配到className的next元素
         function getNextClassNode(node, className) {
             if($(node).next().length > 0) {
                 var nextNode = $(node).next();
                 if($(nextNode).hasClass(className)) {
                     return nextNode;
                 } else {
                     return getNextClassNode($(nextNode), className);
                 }
             }
             return null;
         }

// 获取贴子的id列表组装而成的字符串
         function getPicIDs() {
             var idstrings = "";
             var id = 0;
             $(".topic_node").each(function() {
                 var cid = $(this).data("picid");
                 if(id > 0) {
                     idstrings += "-";
                 }
                 idstrings += cid;
                 id ++;
             });

             return idstrings;
         }
   /* 获得 所有图片及说明 */
         function getContent(){
             var picContents="";
             $(".topic_node").each(function(){
              picContents += $(this).find(".edit-box").html()
             })
             return picContents;
         }
         function checkTopicTitleNode(titleNode, length) {
             msgNode = $(titleNode).closest("dl").find(".msg");
             var txtlen = $(titleNode).val().length;
             if(txtlen == 0) {
                 $(msgNode).html('<div class="msg-error">：（ 贴子标题不能为空哦');
                 $(msgNode).show();
                 return false;
             } else if(txtlen > length) {
                 var n = txtlen - length;
                 $(msgNode).html('<div class="msg-error">：（ 超出' + n + '个字了</div>' + txtlen + '/' + length);
                 $(msgNode).show();
                 return false;
             }
             $(msgNode).html(txtlen + '/' + length);
             return true;
         }


         // 图片保存事件
         $(document).on("click",".img_save_href",function(){
             var parentDiv = $(this).closest(".post-photo");
             var picId = $(parentDiv).data("picid");
             var picUrl= $(parentDiv).find(".img img").attr("src");
             var picIntro =$(parentDiv).find("textarea").val();

             $.ajax({
                 url: "/diagram/savePic",
                 type: "POST",
                 contentType:"application/json; charset=utf-8",
                 dataType: "json",
                 data: JSON.stringify({
                     pidId:picId,
                     picUrl: picUrl,
                     picIntro: picIntro
                 }),
                 success: function(data) {
                     var status = data.status;
                     var message = data.message;
                     if(data.code=="100") {
                         insertImageNode($(parentDiv), data.picId, picUrl, picIntro);
                         $(parentDiv).hide();
                         callbackFunc(attatchedFunc);
                     } else {
                        alert(data.message)
                     }
                 }
             });
         });

         // 节点删除点击
         $(document).on("click",".sitedel-href",function () {
             var parentNode = $(this).closest(".topic_node");
             var cid = $(parentNode).attr("cid");
             var type = $(parentNode).attr("type");
             //todo
         });

         // 图片上传节点删除
         $(document).on("click","#img_del_href",function () {
             var parentNode = $(this).closest(".post-photo");
             // todo
         });

         // 在某一节点后面插入图片节点
         function insertImageNode(afterNode, image_id, image_url, image_intro) {
             setChanged();
             var clone = $("#topic_image_example").clone();
             clone.attr("id", "image_" + image_id)
             afterNode.after(clone)
             clone.show();
             clone.attr("type", "image");
             clone.data("picid", image_id);
             clone.attr("picid",image_id)
             clone.addClass("topic_node");
             checkAndSetMoveBar(clone);
            clone.find(".img").html("<img src=\"" + image_url + "\">");
            clone.find(".photo-des").html(image_intro);

         }




     })

 })