//----------------------派代编辑器相关-----------------
define(function(require){
    var $ = jQuery = require("jquery")
    var SWFUpload = require("swfupload")
    var Cookie = require("cookie")

    var COLOR = [
        {
            key:"333333",
            val:"灰色-80%"
        },
        {
            key:"666666",
            val:"灰色-60%"
        },
        {
            key:"999999",
            val:"灰色-40%"
        },
        {
            key:"cccccc",
            val:"灰色-20%"
        },
        {
            key:"bb0000",
            val:"深红"
        },
        {
            key:"dd0000",
            val:"红色"
        },
        {
            key:"ee4488",
            val:"粉红"
        },
        {
            key:"ff66dd",
            val:"淡紫"
        },
        {
            key:"333399",
            val:"深蓝"
        },
        {
            key:"0066cc",
            val:"蓝色"
        },
        {
            key:"0099cc",
            val:"天蓝"
        },
        {
            key:"66cccc",
            val:"淡蓝"
        },
        {
            key:"336600",
            val:"深绿"
        },
        {
            key:"999900",
            val:"深黄"
        },
        {
            key:"cccc33",
            val:"淡黄"
        },
        {
            key:"77cc33",
            val:"淡绿"
        },
        {
            key:"663300",
            val:"咖啡"
        },
        {
            key:"cc6633",
            val:"褐色"
        },
        {
            key:"ff9900",
            val:"橙黄"
        },
        {
            key:"ffcc33",
            val:"黄色"
        }
    ];
    var FONTSIZE = [1, 2, 3, 4];
    //RGB(0,0,0)转#000000
    var RGB2HEX = {
        "_515151":"333333",
        "_102102102":"666666",
        "_153153153":"999999",
        "_204204204":"cccccc",
        "_18700":"bb0000",
        "_22100":"dd0000",
        "_23868136":"ee4488",
        "_255102221":"ff66dd",
        "_5151153":"333399",
        "_0102204":"0066cc",
        "_0153204":"0099cc",
        "_102204204":"66cccc",
        "_511020":"336600",
        "_1531530":"999900",
        "_20420451":"cccc33",
        "_11920451":"77cc33",
        "_102510":"663300",
        "_20410251":"cc6633",
        "_2551530":"ff9900",
        "_25520451":"ffcc33"
    };

//$(document).ready(function() {
    //编辑器初始化
    var frame = window.frames['pdeditor'],
            doc = window.frames['pdeditor'].document,
            pdtitle=document.getElementById('pdtitle'),
            pdbd = window.frames['pdeditor'].document.body,
            browser = !!window.ActiveXObject ? 'ie' : 'other',
            pdeditor = document.getElementById('pdeditor');
    var bbs_domain = window['_domain_bbs'] || 'http://bbs.paidai.com/',
        www_domain = window['_domain_default'] || 'http://www.paidai.com/',
        my_domain  = window['_domain_my'] || 'http://my.paidai.com/';

    if (browser == 'ie') {
        pdbd.contentEditable = true;
        pdbd.attachEvent('onpaste', function(e) {
            return pasteClipboardData('pdeditor', e);
        });
        pdbd.attachEvent('onkeyup', function() {
            setTimeout(countNum, 1)
        });
        pdbd.attachEvent('onkeydown', function() {
            setTimeout(countNum, 1)
        });
        pdtitle.attachEvent('onkeyup', function() {
            setTimeout(countNum, 1)
        });
        pdtitle.attachEvent('onkeydown', function() {
            setTimeout(countNum, 1)
        });
        pdbd.attachEvent('onclick', checkStatus);
//		pdbd.attachEvent('onkeyup',preventBr);
    } else {
        doc.designMode = 'on';
        pdbd.addEventListener('paste', function(e) {
            return pasteClipboardData('pdeditor', e);
        }, false);
        frame.addEventListener('keyup', countNum, false);
        frame.addEventListener('keydown', countNum, false);
        pdtitle.addEventListener('keyup', countNum, false);
        pdtitle.addEventListener('keydown', countNum, false);
        frame.addEventListener('click', checkStatus, false);
//		pdbd.addEventListener('keyup',preventBr,false);
//		pdbd.addEventListener('keyup',preventBr,false);
    }
    registerAutoSaveDraft();
	//标题输入框的颜色
	if( pdtitle.value=="请输入文章标题"){
		pdtitle.style.color = '#A0A0A0';
	}else{
		pdtitle.style.color = '#323232';
	}

    // 输入框高度自动
    function IFrameReSize(iframename) {
        var pTar = document.getElementById(iframename);
        if (pTar) {  //ff
            if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight) {
                pTar.height = pTar.contentDocument.body.offsetHeight;
            } //ie
            else if (pTar.Document && pTar.Document.body.scrollHeight) {
                pTar.height = pTar.Document.body.scrollHeight;
            }
        }
    }

	//2013-08-19 预览和保存草稿按钮禁用的情况
		function btnAble (){
			 var pdtext = $.trim($(pdbd).text()),
					pdimg = /img/gi.test(pdbd.innerHTML),
					pdembd = /embed/gi.test(pdbd.innerHTML),
					title_value = true;
					if(pdtitle.value=='请输入文章标题' || pdtitle.value==''){
							title_value = false;
					}
			if(title_value || pdimg || pdembd || pdtext.length > 0){
                $('#save_draft').removeAttr('disabled');
                $('#save_draft').removeClass('func_button_disabled');
			}else{
				$('#save_draft').addClass('func_button_disabled')
			}
			if(pdtext == '' && !pdimg && !pdembd){
				$('#preview').addClass('func_button_disabled');
			}else{
				$('#preview').removeClass('func_button_disabled');
			}
		}
		btnAble();

    function pdAutoHeight() {
        //2012.01.07高度对内容自适应
        var _height = 0;
        var pre_height = 0;
        var cur_height = 0;
        var span;
        var tmpNode;
        var pos;
        var b = browser == 'ie' ? pdbd.innerText.length : pdbd.textContent.length;;
        if (b == 0 || b == 1)
        {
            pdeditor.style.height = '300px';
        } else {
            clearTimeout(timer);
            timer = setTimeout(function() {
                if (!span) {
                    span = doc.createElement('span');
                    span.style.cssText = 'display:block; height:0; margin:0;padding:0; border:0; clear:both';
                    span.innerHTML = '.';
                    //span.className = "CheckHeightSpan";
                }
                tmpNode = span.cloneNode(true);
                pdbd.appendChild(tmpNode);
                cur_height = getY(tmpNode) + tmpNode.offsetHeight;
                //$(pdbd).find('span.CheckHeightSpan').remove();
                if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                    if (cur_height <= 300) {
                        cur_height = 300;
                    }
                }
                if (pre_height != cur_height) {
                    $('#pdeditor').css('height', cur_height + 'px');
                    pre_height = cur_height;
                }
                pdbd.removeChild(tmpNode);
                // $(pdbd).find('span.CheckHeightSpan').remove();
				//2014-2-11   bbs发表文章 悬浮工具栏
				toolmovescroll();

            }, 50);
        }
    }
    //添加iframe内部样式
    pdbd.style.cssText = 'width:630px;background-color:#ffffff;padding:0;margin:0;font-size:14px;font-family:Verdana, tahoma, arial, \5b8b\4f53, sans-serif;line-height:30px;word-break:break-all;border-radius:4px;padding:10px 0 0px 20px;cursor:text;overflow:hidden';
    //text-indent:2em;
    var i_style = doc.createElement('link');
    i_style.rel = 'stylesheet';
    i_style.href = www_domain + 'styles/bbs/iframe.css?v=20120330.css';
    doc.getElementsByTagName('head')[0].appendChild(i_style);
    //swf初始化
 //   var formauthcode = encodeURIComponent(getCookie('XForum_AuthCode'));
  //  var iid = encodeURIComponent(getCookie('XForum__pdiid'));
      var formauthcode = encodeURIComponent(Cookie.get('XForum_AuthCode'));
    var iid = encodeURIComponent(Cookie.get('XForum__pdiid'));
    swfu = new SWFUpload({//全局变量
        upload_url: www_domain + "new_uploadfile.php?act=uploadfile&authcode="+formauthcode,
        post_params: {"uid" :HIWOWO.userId ,"uname" :HIWOWO.nick,'authcode':formauthcode,'iid':iid},  //
        file_size_limit: "2 MB",
        file_types: "*.jpg;*.gif;*.png;*.bmp;*.jpeg",
        file_post_name: "filedata",
        // Event Handler Settings
        file_queue_error_handler: fileQueueError,
        file_dialog_complete_handler: fileDialogComplete,
        upload_progress_handler: uploadProgress,
        upload_error_handler: uploadError,
        upload_success_handler: uploadSuccess,
        upload_complete_handler: uploadComplete,
        // Button settings
        button_image_url: www_domain + "images/bbs/swfupload_btn.gif?v=0418.css",
        button_placeholder_id: "swfuploadbtn",
        button_width: 48,
        button_height: 38,
        // Flash Settings
        flash_url: www_domain + "images/bbs/swfupload.swf",
        debug: false
    });
    //标题输入框
    $('#pdtitle').focus(function() {
        if ($(this).val() == '请输入文章标题') {
            $(this).val('').css({'color': '#323232'});
        }
    })
    $('#pdtitle').blur(function() {
        if ($(this).val() == '') {
            $(this).val('请输入文章标题').css({'color': '#a0a0a0'});
        }
        countNum();
    })
//鼠标感应-----
    //点击主要板块
    $('.main_section a').click(function() {
        $('.main_section a, #other_list a').removeClass('selected_s');
        $('#other_btn').removeClass('selected_s').addClass('other_btn').text('');
        $(this).addClass('selected_s');
    })
    //点击其他版块
    $('#other_btn').click(function() {
        $('#other_list').slideToggle('slow');
    })
    //点击其他板块栏目
    $('#other_list a').click(function() {
        $('.main_section a, #other_list a').removeClass('selected_s');
        var secVal = $(this).text(),
            secNum = $(this).attr('section');
        $('#other_btn').removeClass('other_btn').addClass('selected_s').html('<em>' + secVal + '</em>').attr('section', secNum);
        $('#other_list').slideUp('slow');
        $(this).addClass('selected_s');
    })

    // 预览
    $('#preview').bind('click', function(){
        var form = $('#preview_form');
        var title = $.trim($('#pdtitle').val()).replace(/</gi, '').replace(/>/gi, '').replace(/script/gi, ''),
            boardid = $('.selected_s').attr('section'),
            contents = (pdbd.innerHTML).replace(/(<span[^>]*?style="font-weight: bold;">)([\s\S]*?)(<\/span>)/gi, "<b>$2</b>").replace(/!(<embed)\s(width|height)\s*=\s*"[\s\S]*?"/gi, '').replace(/<\s*(h\d|div|pre)[^>]*?>/gi, '<p>').replace(/<\s*\/\s*(h\d|div|pre)[^>]*?>/gi, '</p>');
        if (title == '请输入文章标题') {
            title = '';
        }
        //检查内容为空
        var pdtext = $.trim($(pdbd).text()),
                pdimg = /img/gi.test(pdbd.innerHTML),
                pdembd = /embed/gi.test(pdbd.innerHTML);
        if (pdtext == '' && !pdimg && !pdembd) {
            return;
            if(!confirm('没有填写文字，继续预览？')){
                return;
            }
        }
        form.find('input[name="title"]').val(title);
        form.find('input[name="contents"]').val(contents);
        form.find('input[name="boardid"]').val(boardid);
        form.submit();
    });



    // 草稿列表处理
    var draft_ul = $('.draft_ul');
    if(draft_ul)
    {

        var draft_a_bind_click = function(){

            // hover出现删除按钮
            var draft_ul_li = $('.draft_ul li');
            var txt = "";
            if(draft_ul_li.length > 0) {
                txt = '('+ draft_ul_li.length +')';
                draft_ul_li.each(function(){
                    $(this).hover(function(){
                      $(this).find('.del_icon').addClass('del_icon_hover');
                    },function(){
                      $(this).find('.del_icon').removeClass('del_icon_hover');
                    });
                });
            }
            $('#draft_count').html(txt);


            // 给删除按钮加事件
            $('.draft_ul .del_icon').click(function(e){
                var del = confirm('确认删除此草稿？');
                if(!del) return;
                var li = $(this).parent('li');
                var uuid = li.attr('uuid');
                var _this = $(this);
                $.post('draft.php',{act:'del', 'uuid':uuid}, function(data){
                  if(data['error'] == 0){
                    li.remove();
                    var draft_ul_li = $('.draft_ul li');
                    var txt = "";
                    if(draft_ul_li.length > 0) {
                        txt = '('+ $('.draft_ul li').length +')';
                    } else {
                        $('.draft_ul').append('<li style="color:#B8B4B4;">您目前没有草稿哦~</li>');
                    }
                    $('#draft_count').html(txt);

                  } else {
                    alert(data['message']);
                  }

                }, 'json');
                // e.stopPropagation();
              });

            // 草稿列表点击
          //   $('.draft_ul li a').unbind('click').bind('click', function(){
          //   var uuid = $(this).attr('uuid');
          //   if(!uuid){
          //     return;
          //   }
          //   $.get('draft.php',{act:'draft',uuid:uuid},function(data){
          //     var pdbd = window.frames['pdeditor'].document.body;
          //     var pdtext = $.trim($(pdbd).text()),
          //               pdimg = /img/gi.test(pdbd.innerHTML),
          //               pdembd = /embed/gi.test(pdbd.innerHTML);
          //     var use_draft = false;
          //     if (pdtext == '' && !pdimg && !pdembd) {
          //       use_draft = true;
          //     } else if(confirm('编辑框内已有内容，是否用草稿覆盖？')){
          //       use_draft = true;
          //     };
          //     if(use_draft){
          //       $('#draft_hash').val(data['uuid']);
          //       $('#pdtitle').val(data['title']);
          //       pdbd.innerHTML = data['contents'];
          //       setDraftBoard(data['boardid']);
          //       setTimeout(countNum, 200);
          //     }
          //   },'json');

          // });
        }


        var draft_li = $('.draft_ul li');
        if(draft_li.length == 0){
            $('.draft_ul').append('<li style="color:#B8B4B4;">您目前没有草稿哦~</li>');
        } else {
            draft_a_bind_click();
        }

    }

    // 点击草稿设置版块
    function setDraftBoard(boardid)
    {
        if(!boardid) return;
        var boardExsits = false;
        var main_a, other_a, board_a, is_other;
        main_a = $('.main_section a');
        main_a.each(function(i, item){
          if($(item).attr('section') == boardid){
            boardExsits = true;
            board_a = $(item);
          }
        });
        if(boardExsits == false){
          other_a = $('#other_list a');
          other_a.each(function(i, item){
            if($(item).attr('section') == boardid){
              boardExsits = true;
              board_a = $(item);
              is_other = true;
            }
          });
        }

        if (boardExsits == true) {
          $('.main_section a, #other_list a').removeClass('selected_s');
          board_a.addClass('selected_s');
          if(is_other){
            $('#other_btn').removeClass('other_btn').addClass('selected_s').html(board_a.html()).attr('section', board_a.attr('section'));
          }
        };
    }
	    //窗体的大小改变时，保存草稿的提示浮层的位置
		var save_draft_bg = null ;
		var save_draft_tip = null;
		window.onresize = window.onscroll = docSize;
		function docSize(){
			var oDocSize = {};
			oDocSize.width = $(window).width();
			oDocSize.height = $(window).height()+$(window).scrollTop();
			oDocSize.center = $(window).height()/2+$(window).scrollTop()-100;
			if(save_draft_bg && save_draft_tip){
				save_draft_bg.height(oDocSize.height);
				save_draft_bg.width(oDocSize.width);
				save_draft_tip.css('top',oDocSize.center);
			}
			return oDocSize;
		}
		//保存草稿的提示浮层
		function createSaveTip(text,draft_class){
			if(!save_draft_bg){
				  save_draft_bg = $('<div></div>');
					save_draft_bg.attr('id','save_draft_bg');
					save_draft_bg.height(docSize().height);
					save_draft_bg.width(docSize().width);
					save_draft_tip = $('<div>'+text+'</div>');
					save_draft_tip.attr('id','save_draft_tip');
					save_draft_tip.addClass(draft_class)
					save_draft_tip.css('top',docSize().center);
					$('body').append(save_draft_bg);
					$('body').append(save_draft_tip);
				}else{
					save_draft_tip.html(text);
					save_draft_tip.removeClass('draft_fail').removeClass('draft_sucess').addClass(draft_class);
					save_draft_bg.fadeIn('fast');
					save_draft_tip.fadeIn('fast');
				}
		}

    function registerAutoSaveDraft() {
        setTimeout(autoSaveAraft,60000);
    };
    function autoSaveAraft() {
        var title = $.trim($('#pdtitle').val()).replace(/</gi, '').replace(/>/gi, '').replace(/script/gi, '');
        if(title != '请输入文章标题' && title != '') {
            document.getElementById('save_draft').click();
        }
        setTimeout(autoSaveAraft, 300000);
    };
    // 保存到草稿箱
    $('#save_draft').bind('click', function(){
        var title = $.trim($('#pdtitle').val()).replace(/</gi, '').replace(/>/gi, '').replace(/script/gi, ''),
            boardid = $('.selected_s').attr('section'),
            topicid = $('#cur_topicid').val(),
            hash = $('#draft_hash').val(),
            contents = (pdbd.innerHTML).replace(/(<span[^>]*?style="font-weight: bold;">)([\s\S]*?)(<\/span>)/gi, "<b>$2</b>").replace(/!(<embed)\s(width|height)\s*=\s*"[\s\S]*?"/gi, '').replace(/<\s*(h\d|div|pre)[^>]*?>/gi, '<p>').replace(/<\s*\/\s*(h\d|div|pre)[^>]*?>/gi, '</p>');
        if (title == '请输入文章标题') {
            title = '';
        }
        //检查内容为空
        var pdtext = $.trim($(pdbd).text()),
                pdimg = /img/gi.test(pdbd.innerHTML),
                pdembd = /embed/gi.test(pdbd.innerHTML);
        if ((pdtext == '' && !pdimg && !pdembd) && title=="") {
         //   alert('没有填写文字，不能保存为草稿');
         return;
		 	createSaveTip('没有填写文字，不能保存为草稿！','draft_fail');
			draft_timer = setTimeout(function(){
				 save_draft_bg.fadeOut('slow')
				 save_draft_tip.fadeOut('slow');
				 clearTimeout(draft_timer);
			},1300);
            return;
         //   if(!confirm('没有填写文字，不能保存为草稿？')){
        //        return;
        //    }
        }
        $.post('draft.php', {act:"save", title:title, contents:contents, boardid:boardid, 'hash':hash, topicid:topicid}, function(data){
            if(data['error'] == 2) {
                if (confirm('已有相同内容草稿，是否载入？')) {
                    var uuid = data['uuid'];
                    $.get('draft.php',{act:'draft',uuid:uuid},function(d){
                        // var pdbd = window.frames['pdeditor'].document.body;
                        $('#draft_hash').val(d['uuid']);
                        if (d['title']) {
                            $('#pdtitle').val(d['title']);
                        };
                        if(d['contents']) {
                            pdbd.innerHTML = d['contents'];
                        }
                        if (d['boardid']) {
                            setDraftBoard(d['boardid']);
                        };
                    },'json');
                    return;
                }
                return;
            }else if(data['error'] !== 0) {
                alert(data['message']);
                return;
            }
            $('#draft_hash').val(data['uuid']);
            refrushDraftBox();
           // alert('保存成功');
            $('#save_status').text('保存成功');
//		    createSaveTip('保存成功！','draft_sucess');
			draft_timer = setTimeout(function(){
//				 save_draft_bg.fadeOut('slow')
//				 save_draft_tip.fadeOut('slow');
                 $('#save_status').text(' ')
				 clearTimeout(draft_timer);
			},1000);
        }, 'json')
        .error(function(dd){
            createSaveTip('保存失败，请稍后再试！','draft_fail');
            draft_timer = setTimeout(function(){
                 save_draft_bg.fadeOut('slow')
                 save_draft_tip.fadeOut('slow');
                 clearTimeout(draft_timer);
            },1000);
        });
    });
    function refrushDraftBox()
    {
        $.get('draft.php', {act:"all"}, function(data){
            var draft_ul = $('.draft_ul');
            if(data['error'] == 0 && draft_ul.length>0) {
                var html = '';
                $.each(data['draft'], function(i, item){
                    html += '<li class="clearfix zoom"';
                    html +=' uuid="'+item['uuid']+'"><p class="draft_title"><a href="post.php?';
                    if(item['topicid'] > 0) {
                        html += 'act=edit&topicid=' + item['topicid'] + "&";
                    }
                    html += 'draft=' + item['uuid'];
                    html += '" title="'+item['posttime']+'" target="_black">';
                    if(item['title']){
                        html += item['title'];
                    } else {
                        html += '未命名';
                    }
                    html += '</a><span class="draft_time" title="'+item['posttime']+'">'+item['stime']+'</span></p>';
                    html += '<a href="javascript:;" title="删除这篇草稿" class="del_icon"></a></li>';
                });
                draft_ul.html(html);
                draft_a_bind_click();
            }
        }, 'json');
    }
      // 字体大小
     $('#pdfontsize').click(function(){
             $(".fontSizeWrap").show()
     })
    /* 字体颜色 */
    $("#pdfontcolor").click(function(){
        $(".fontColorWrap").show()
    })
    //字体加粗
    $('#pdbold').click(function() {
        frame.focus();
        doc.execCommand('bold', false, null);
        checkStatus();
        frame.focus();
    })
    //检查加粗状态
    function checkStatus() {
        var bStatus = doc.queryCommandState('bold') ? '0 -240px' : '0 -200px';
        $('#pdbold').css('background-position', bStatus);
    }
    //插入图片
    insertImg = function(src) {		//全局变量，供外部uploadSuccess函数调用
        if (browser == 'ie') {
            frame.focus();
            doc.selection.createRange().pasteHTML(src);
        } else {
            doc.execCommand('insertHTML', false, src);
        }
    }

    $(pdeditor).one('focus', function() {
        //pdbd.innerHTML = '';   	//为兼容ie，必须使得iframe中的内容是空
    });

    //字数统计
    var timer;
    function countNum() {
        // $(pdbd).find('p').css('margin', '0px');
        $('#count_num_tips').show();
        document.getElementById('count_num').innerHTML = browser == 'ie' ? pdbd.innerText.length : pdbd.textContent.length;
        var a = $.trim($('#pdtitle').val()),
            b = $.trim($('#count_num').html());

        btnAble();   //发表按钮和预览按钮是否可以用
				var pdimg = /img/gi.test(pdbd.innerHTML);
				var pdembd = /embed/gi.test(pdbd.innerHTML);
        if (b > 6) {
            $('#pdsubmit,#editsubmit').removeClass('func_button_disabled').addClass('func_publish');
        } else {
            $('#pdsubmit,#editsubmit').removeClass('func_publish').addClass('func_button_disabled');
        }

        pdAutoHeight();

    }
    function getY(ele) {
        var y = 0;
        while (ele.offsetParent) {
            y += ele.offsetTop;
            ele = ele.offsetParent;
        }
        return y;
    }


    //回车键插入段前空格
    function preventBr(e) {
        var keyNum = e.keyCode || event.keyCode;
        if (keyNum == 13) {
            if (browser == 'ie') {
                setTimeout(function() {
                    doc.selection.createRange().pasteHTML('&nbsp;&nbsp;&nbsp;&nbsp;');
                }, 1)
            } else {
                setTimeout(function() {
                    doc.execCommand('insertHTML', false, '&nbsp;&nbsp;&nbsp;&nbsp;');
                }, 1)
            }
        }
    }


    //2012.01.10 复制粘贴中中用到的函数

    function getSel(w)
    {
        return w.getSelection ? w.getSelection() : w.document.selection;   //ff : ie
    }
    function setRange(sel, r)
    {
        sel.removeAllRanges();
        sel.addRange(r);
    }
    function block(e)
    {
        e.preventDefault();
    }
    //粘贴过滤
    function filterPasteData(originalText)
    {
        return originalText.replace(/(<span[^>]*?style="font-weight: bold;">)([\s\S]*?)(<\/span>)/gi, "<b>$2</b>").replace(/<xml>[\s\S]*?<\/xml>/gi, '').replace
                (/<\s*style[^>]*?>[^<]*?<\s*\/\s*style\s*>/gi, '').replace(/<\s*script[^>]*?>[\s\S]*?<\s*\/\s*script\s*>/gi, '').replace(/<\s*(h\d|div|pre)[^>]*?>/gi, '<p>').replace(/<\s*\/\s*(h\d|div|pre)[^>]*?>/gi, '</p>').replace
                (/<(?!img|br|\/?p|\/?b\s?>|\/?strong)[^>]*?>/gi, '').replace(/\s(style|class|id|align|border)\s*=\s*(["'])[\s\S]*?\2/gi, '').replace(/<!--.*?-->/gi, '').replace
                (/(class=\w*\w*)|(align=\w*\w*)/gi, '').replace(/<\s*p\s*><\s*\/\s*p\s*>/gi, '').replace(/<\s*b\s*><\s*\/\s*b\s*>/gi, '');
    }

    //粘贴函数
    var or, div_temp, originText;
    var newData;
    function pasteClipboardData(editorId, e)
    {
        var objEditor = document.getElementById(editorId);
        var ed_win = objEditor.contentWindow;
        var edDoc = ed_win.document;
        var ed_body = edDoc.body;
        // $(ed_body).find('p').css('margin', '0px');
        if (browser == 'ie')
        {
            ed_win.focus();
            var orRange = edDoc.selection.createRange();  //保存光标位置和选中信息
            var ifmTemp = document.getElementById("ifmTemp");
            if (!ifmTemp)
            {
                ifmTemp = document.createElement("iframe");
                ifmTemp.id = "ifmTemp";
                ifmTemp.style.width = "1px";
                ifmTemp.style.height = "1px";
                ifmTemp.style.position = "absolute";
                ifmTemp.style.border = "none";
                ifmTemp.style.left = "-10000px";
                document.body.appendChild(ifmTemp);
                ifmTemp.contentWindow.document.open();           //因为ifmTemp没有src属性，所以必须要有一下三句,否则出现"没有权限"的错误
                ifmTemp.contentWindow.document.write("<body></body>");
                ifmTemp.contentWindow.document.close();
                ifmTemp.contentWindow.document.body.contentEditable = true;
            } else
            {
                ifmTemp.contentWindow.document.body.innerHTML = "";
            }
            originText = ed_body.innerText;
            ifmTemp.contentWindow.focus();
            ifmTemp.contentWindow.document.execCommand("Paste", false, null);
            newData = ifmTemp.contentWindow.document.body.innerHTML;
            //filter the pasted data
            newData = filterPasteData(newData);
            ifmTemp.contentWindow.document.body.innerHTML = newData;
            document.body.removeChild(ifmTemp);
            //paste the data into the editor
            ed_win.focus();
            orRange.pasteHTML(newData);
            //block default paste
            if (e)
            {
                e.returnValue = false;
            }
            setTimeout(remotePic, 200);
            return false;
        }
        else
        {
            //create the temporary html editor
            if (!div_temp) {
                div_temp = edDoc.createElement("div");
                div_temp.id = 'tempdiv';
                div_temp.innerHTML = '.';
            }
            var divTemp = div_temp.cloneNode(true);
            edDoc.body.appendChild(divTemp);
            //disable keyup,keypress, mousedown and keydown
            edDoc.addEventListener("mousedown", block, false);
            edDoc.addEventListener("keydown", block, false);
            //get current selection;
            ed_win.focus();

            or = getSel(ed_win).getRangeAt(0);   //保存光标和焦点的位置

            //move the cursor to into the div
            var docBody = divTemp.firstChild;
            rng = edDoc.createRange(); //创建Range 对象，两个边界点都被设置为文档的开头,用来表示文档的一个区域或与该文档相关的 DocumentFragment 对象。
            rng.setStart(docBody, 0);   //设置范围的开始点。
            rng.setEnd(docBody, 1);
            setRange(getSel(ed_win), rng);

            originText = ed_body.textContent;  //textContent如果返回文本，则该属性返回元素节点内所有文本节点的值。
            if (originText === '.')
            {
                originText = "";
            }

            window.setTimeout(function()
            {
                //get and filter the data after onpaste is done
                if (divTemp.innerHTML === '.')
                {
                    newData = "";
                    ed_body.removeChild(divTemp);
                    return;
                }
                newData = divTemp.innerHTML;
                // Restore the old selection
                if (or)
                {
                    setRange(getSel(ed_win), or);
                }
                newData = filterPasteData(newData);
                ed_body.removeChild(divTemp);
                //paste the new data to the editor
                ed_win.focus();
                edDoc.execCommand('insertHTML', false, newData);

            }, 1);
            //enable keydown,keyup,keypress, mousedown;
            edDoc.removeEventListener("mousedown", block, false);
            edDoc.removeEventListener("keydown", block, false);
            setTimeout(remotePic, 200);
            return true;
        }
    }

    // 远程图片保存导本地
    function remotePic(){
        var separater = 'pd_separate_pd';
        var remoteImages = [];
        var imgs = $(pdbd).find('img');
        imgs.each(function(i, item){
            var src = item.src;
            $(item).removeAttr('original');
            if (/^(https?|ftp):/i.test(src) && src.indexOf('paidai.com') == -1 && src.indexOf('29n.org') == -1) {
                remoteImages.push(src);
            }
        });
        if (remoteImages.length <= 0) {
            return;
        };
        var submitStr = remoteImages.join(separater)
        $.post(bbs_domain + 'ueditor/getRemoteImage.php', {upfile:submitStr}, function(info){
            var srcUrls = info.srcUrl.split(separater),
                urls = info.url.split(separater);
                imgs.each(function(i, ci){
                    var src = ci.src || "";
                    for (var j = 0, cj; cj = srcUrls[j++];) {
                        var url = urls[j - 1];
                        if (src == cj && url != "error") {  //抓取失败时不做替换处理
                            //地址修正
                            var newSrc = www_domain + url;
                            ci.src = newSrc;
                            break;
                        }
                    }
                });
        }, 'json');
        // .error(function(a){
        //     console.log(a);
        //     alert(a['tip']);
        // });
    }

    //插入视频
    $('#insertvideo').click(function() {
        $('#videopanel').slideToggle('slow');
        $('#videoaddr').focus();
    })
    //点击确定按钮
    $('#addvideo').click(function() {
        var vURL = $('#videoaddr').val();

        var urlReg = /v\.qq\.com|tudou\.com|youku\.com|sohu\.com|video\.sina\.com\.cn|ku6\.com|56\.com|letv\.com|cntv.cn/gi,
                resultUrl = vURL.match(urlReg);

        // 插入视频执行函数
        function insertVideoHandler(vURL)
        {
            frame.focus();
            if (browser == 'ie') {
                doc.selection.createRange().pasteHTML(vURL);
                $('#videoaddr').val('');
                $('#videopanel').slideUp('slow');
            } else {
                doc.execCommand('insertHTML', false, vURL);
                $('#videoaddr').val('');
                $('#videopanel').slideUp('slow');
            }
        }

        if (/swf/gi.test(vURL)) {
            vURL = /embed/gi.test(vURL) ? vURL : ('<embed src="' + vURL + '" quality="high" width="480" height="400" allowScriptAccess="always" allowFullScreen="false" mode="transparent" type="application/x-shockwave-flash"></embed>');
            insertVideoHandler(vURL);
            return false;
        }

        if (vURL == "") {
            alert("请输入视频地址！");
            return false;
        } else if (resultUrl == null) {
            alert("抱歉，暂不支持该视频地址！");
            return false;
        }

        switch (resultUrl.toString())
        {
            case "v.qq.com": // 腾讯视频
                var vidReg = /vid=.{5,11}$/gi;
                if (vURL.match(vidReg) != null)
                {
                    vURL = '<embed src="http://static.video.qq.com/TPout.swf?' + vURL.match(vidReg) + '&auto=0" allowFullScreen="true" quality="high" width="480" height="400" align="middle" allowScriptAccess="always" type="application/x-shockwave-flash"></embed>'
                    insertVideoHandler(vURL);
                }
                else
                {
                    alert("抱歉，暂不支持该视频地址！");
                }
                break;

            case "tudou.com": // 土豆视频
                alert("抱歉，暂不支持土豆网视频播放地址！");
                break;

            case "youku.com": // 优酷视频
                var fid = /f=\d{0,8}$/gi,
                        id = /id_.{0,13}\.html/gi;
                if (vURL.match(id) != null && vURL.match(fid) != null)
                {
                    vURL = '<embed src="http://player.youku.com/player.php/Type/Folder/Fid/' + vURL.match(fid).toString().substring(2) + '/Ob/1/sid/' + vURL.match(id).toString().substring(3, 16) + '/v.swf" quality="high" width="480" height="400" align="middle" allowScriptAccess="always" allowFullScreen="true" mode="transparent" type="application/x-shockwave-flash"></embed>';
                    insertVideoHandler(vURL);
                }
                else if (vURL.match(id) != null)
                {
                    vURL = '<embed src="http://player.youku.com/player.php/sid/' + vURL.match(id).toString().substring(3, 16) + '/v.swf" allowFullScreen="true" quality="high" width="480" height="400" align="middle" allowScriptAccess="always" type="application/x-shockwave-flash"></embed>';
                    insertVideoHandler(vURL);
                }
                else
                {
                    alert("抱歉，暂不支持该视频地址！");
                }

                break;

            case "sohu.com": // 搜狐视频

                //http://my.tv.sohu.com/u/vw/33261184
                var _id = /\d{8,}$/gi;
                //alert(str.match(_id));
                //alert("抱歉，暂不支持搜狐视频播放地址！");
                break;

            case "video.sina.com.cn": // 新浪视频
                alert("抱歉，暂不支持新浪视频播放地址！");
                break;

            case "ku6.com": // 酷六视频
                if (vURL.match(/show.{0,}\.html/gi) == null)
                {
                    alert("抱歉，暂不支持该视频地址！");
                    return false;
                }
                var url = /show.{0,}\.html/gi,
                        v1 = vURL.match(url).toString();
                url2 = /\/.*/gi,
                        result = v1.match(url2).toString().substring(1),
                        parameter = result.replace(/\.html/gi, "");

                vURL = '<embed src="http://player.ku6.com/refer/' + parameter + '/v.swf" width="480" height="400" allowscriptaccess="always" allowfullscreen="true" type="application/x-shockwave-flash" flashvars="from=ku6"></embed>';
                insertVideoHandler(vURL);
                break;
            case "56.com": // 56视频
                alert("抱歉，暂不支持56视频播放地址！");
                break;

            case "letv.com": // 乐视视频
                alert("抱歉，暂不支持乐视网视频播放地址！");
                break;

            case "cntv.cn": // CNTV视频
                alert("抱歉，暂不支持CNTV视频播放地址！");
                break;
        }

        /*if(/swf/gi.test(vURL)){
         vURL = /embed/gi.test(vURL) ? vURL:('<embed src="' + vURL + '" quality="high" width="480" height="400"allowScriptAccess="always" allowFullScreen="false" mode="transparent" type="application/x-shockwave-flash"></embed>')
         alert(/embed/gi.test(vURL));
         frame.focus();
         if(browser == 'ie'){
         doc.selection.createRange().pasteHTML(vURL);
         $('#videoaddr').val('');
         $('#videopanel').slideUp('slow');
         }else{
         doc.execCommand('insertHTML',false,vURL);
         $('#videoaddr').val('');
         $('#videopanel').slideUp('slow');
         }
         }else{
         alert('视频URL不正确，请重新输入！');
         $('#videoaddr').val('');
         return;
         }*/
    })
    //点击关闭按钮
    $('#cancelvideo').click(function() {
        $('#videoaddr').val('');
        $('#videopanel').slideUp('slow');
    })


    if ($('#pdeditor_container #old_cont').length > 0) {
        pdbd.innerHTML = $('#pdeditor_container #old_cont').html() || '';
        var pdsavekey = 'pdsavededit';
        setTimeout(countNum, 200);
    }

    //本地存储
    if (!!window.localStorage) {
        //判断是否为编辑状态
        if ($('#pdeditor_container #old_cont').length > 0) {
            pdbd.innerHTML = $('#pdeditor_container #old_cont').html() || '';
            var boardid = $('input[name="data_boardid"]').val();
            if(boardid) {
                setDraftBoard(boardid);
            }
            var pdsavekey = 'pdsavededit';
        } else {
            var pdsavekey = 'pdsaved';
            pdbd.innerHTML = localStorage.getItem(pdsavekey) || '';
        }
        setTimeout(countNum, 200);
        setInterval(function() {
            var pddata = pdbd.innerHTML || '';
            localStorage.setItem(pdsavekey, pddata);
        }, 5000);
    } else {
        var udObj = document.getElementById('pd_userdata');
        udObj.load('pdsavedfile');
        //判断是否为编辑状态
        if ($('#pdeditor_container #old_cont').length > 0) {
            pdbd.innerHTML = $('#pdeditor_container #old_cont').html() || '';
            var pdsavekey = 'pdsavededit';
        } else {
            var pdsavekey = 'pdsaved';
            pdbd.innerHTML = udObj.getAttribute(pdsavekey) || '';
        }
        setTimeout(countNum, 200);
        setInterval(function() {
            var pddata = pdbd.innerHTML || '';
            udObj.setAttribute(pdsavekey, pddata);
            udObj.save('pdsavedfile');
        }, 5000);
    }

    //弹出层位置调整
    /*;(function(){
     var width = window.screen.availWidth;
     var left = width/2 - 335;
     $('#add_tag_container').css('left',left);
     })();*/


//提交文章.2012--1-18
    var pdsubmit_defalut_status = 0;
    $('#pdsubmit').click(function() {
		 //检查内容为空
        var pdtext = $.trim($(pdbd).text()),
                pdimg = /img/gi.test(pdbd.innerHTML),
                pdembd = /embed/gi.test(pdbd.innerHTML);
		if(pdtext.length <= 6){
			return;
		}
     /*   if (pdtext == '' && !pdimg && !pdembd) {
            alert('请填写内容!');
            return;
        }
	*/
        var data_title = $.trim($('#pdtitle').val()).replace(/</gi, '').replace(/>/gi, '').replace(/script/gi, '');
        if (data_title == '' || data_title == '请输入文章标题') {
            alert('请填写文章标题!');
            return;
        }
        var data_boardid = $('.selected_s').attr('section'),
            pdcontent = (pdbd.innerHTML).replace(/(<span[^>]*?style="font-weight: bold;">)([\s\S]*?)(<\/span>)/gi, "<b>$2</b>").replace(/!(<embed)\s(width|height)\s*=\s*"[\s\S]*?"/gi, '').replace(/<\s*(h\d|div|pre)[^>]*?>/gi, '<p>').replace(/<\s*\/\s*(h\d|div|pre)[^>]*?>/gi, '</p>');
        if(!data_boardid) {
            alert('请选择版块');
            return;
        }

        $(this).css('background-position','-102px -95px');
        // 防止重复提交
        if (pdsubmit_defalut_status == 1) {
            return false;
        }
        pdsubmit_defalut_status = 1;
        var draft_hash = $('#draft_hash').val();
        var post_data = {data_title: data_title, data_boardid: data_boardid, data_content: pdcontent, 'draft': draft_hash};
        // 如果存在验证码
        if ($('#chk_code_container').length > 0) {
            post_data['code'] = $('#chk_code').val();
        }
        function topicSubmitDisabled(txt)
        {
            $('#pdsubmit').html(txt).css({'background': '#c8c8c8', 'color': '#FFFFFF'}).attr('disabled', 'disabled');
        }
        function topicSubmitNormal()
        {
            $('#pdsubmit').html('发表').css({'background': 'url(http://static.epaidai.com/images/bbs/pdeditor_ico.gif?v=2012091202) no-repeat left -681px', 'color': '#fff'}).removeAttr('disabled');
        }

        topicSubmitDisabled('正在提交...')
        $.post(bbs_domain + 'post.php?act=save', post_data, function(data) {
            //初始化标签变量
            var tagpos = '';
            if (data['status'] == 106) {
                topicSubmitDisabled('最后一步')
                //显示添加标签面板
                var doc_height = $(document).height();
                $('#grey_bg').fadeTo('fast', 0.5).css({'height': doc_height, 'width': $(document).width()});
                $('#add_tag_container').fadeIn('fast');
                //判断是否存在系统推荐标签
                if (data['data']!=null && data['data'] != '') {
                    $.each(data['data'], function(i, item) {
                        tagpos += '<a href="javascript:;" rid=' + item['id'] + ' oid=' + item['obj_id'] + ' >' + item['name'] + '</a>';
                    })
                    $('#sys_tags').html(tagpos);
                } else {
                    $('#sys_tags').html('<h2 style="color:#646464;">抱歉哦，系统未能推荐相关标签，请手动添加</h2>');
                }
                //关闭标签面板
                $('#close_tag_panel').click(function() {
                    if ($('#my_tags a').length > 0) {
                        if ($('#sys_tags a').length > 0) {
                            var systag = [];
                            $('#sys_tags a').each(function(index) {
                                systag.push($(this).attr('rid'));
                            })
                            //删除系统推荐的标签
                            $.get(bbs_domain + 'post.php?act=ajaxDelTags', {systag: systag}, function(data) {
                            }, 'json');
                        }
                        var tags_data = [];
                        $('#my_tags a').each(function(index) {
                            tags_data.push($(this).text());
                        })
                        $.post(bbs_domain + 'post.php?act=ajaxAddTags', {topic_id: data['topicid'], author_id: data['uid'], tag_data: tags_data}, function(data) {
                            if (data['status'] != 107) {
                                alert(data['tips']);
                                return false;
                            } else {
                                if (!!window.localStorage) {	//清空本地存储
                                    localStorage.setItem('pdsaved', '');
                                } else {
                                    document.getElementById('pd_userdata').setAttribute('pdsaved', '');
                                    document.getElementById('pd_userdata').save('pdsavedfile');
                                }
                                $('#pdtitle').val('请输入文章标题');
                                pdsubmit_defalut_status = 0;
                                window.location.href = bbs_domain + 'topic/' + data['topicid'];
                            }
                        }, 'json');
                    } else {
                        if (!!window.localStorage) {	//清空本地存储
                            localStorage.setItem('pdsaved', '');
                        } else {
                            document.getElementById('pd_userdata').setAttribute('pdsaved', '');
                            document.getElementById('pd_userdata').save('pdsavedfile');
                        }
                        $('#pdtitle').val('请输入文章标题');
                        //urls_tag_mng+'?act=ajax03   =>ajaxAddRelationTag
                        pdsubmit_defalut_status = 0;
                        window.location.href = bbs_domain + 'topic/' + data['topicid'];
                    }
                })
                var add_tag_complated = 0;
                $('#add_tag_complate').click(function() {

                    if (add_tag_complated == 1) {
                        return;
                    };
                    $(this).html('正在提交').css({'background': '#ddd', 'color': '#999'}).attr('disabled', 'disabled');

                    add_tag_complated = 1;

                    if ($('#my_tags a').length > 0) {
                        if ($('#sys_tags a').length > 0) {
                            var systag = [];
                            $('#sys_tags a').each(function(index) {
                                systag.push($(this).attr('rid'));
                            })
                            //删除系统推荐的标签
                            $.get(bbs_domain + 'post.php?act=ajaxDelTags', {systag: systag}, function(data) {
                            }, 'json');
                        }
                        var tags_data = [];
                        $('#my_tags a').each(function(index) {
                            tags_data.push($(this).text());
                        })
                        $.post(bbs_domain + 'post.php?act=ajaxAddTags', {topic_id: data['topicid'], author_id: data['uid'], tag_data: tags_data}, function(data) {
                            if (data['status'] != 107) {
                                alert(data['tips']);
                                $(this).html('完成').css({background:'#698361', color:'#fff'}).removeAttr('disabled');
                                return false;
                            } else {
                                if (!!window.localStorage) {	//清空本地存储
                                    localStorage.setItem('pdsaved', '');
                                } else {
                                    document.getElementById('pd_userdata').setAttribute('pdsaved', '');
                                    document.getElementById('pd_userdata').save('pdsavedfile');
                                }
                                $('#pdtitle').val('请输入文章标题');
                                pdsubmit_defalut_status = 0;
                                $(this).html('完成');
                                window.location.href = bbs_domain + 'topic/' + data['topicid'];
                            }

                        }, 'json');

                    } else {
                        if (!!window.localStorage) {	//清空本地存储
                            localStorage.setItem('pdsaved', '');
                        } else {
                            document.getElementById('pd_userdata').setAttribute('pdsaved', '');
                            document.getElementById('pd_userdata').save('pdsavedfile');
                        }
                        $('#pdtitle').val('请输入文章标题');
                        //urls_tag_mng+'?act=ajax03   =>ajaxAddRelationTag
                        pdsubmit_defalut_status = 0;
                        window.location.href = bbs_domain + 'topic/' + data['topicid'];
                    }
                });
            } else {
                alert(data['tips']);
                topicSubmitNormal();
                pdsubmit_defalut_status = 0;
                return false;
            }
        }, 'json')
        .error(function(data){
            alert('发表文章出现错误，请再次尝试，或联系管理人员');
            topicSubmitNormal();
            pdsubmit_defalut_status = 0;
        });
    });


    //编辑文章
    var editsubmit_defalut_status = 0;
    $('#editsubmit').click(function() {
        var data_title = $.trim($('#pdtitle').val()).replace(/</gi, '').replace(/>/gi, '').replace(/script/gi, '');
        if (data_title == '' || data_title == '请输入文章标题') {
            alert('请填写标题!');
            return;
        }
        var data_topicid = $('#cur_topicid').val(),
                data_postid = $('#cur_postid').val(),
                data_isbest =$('#isbest').val(),
                data_boardid = $('.selected_s').attr('section'),
                pdcontent = (pdbd.innerHTML).replace(/(<span[^>]*?style="font-weight: bold;">)([\s\S]*?)(<\/span>)/gi, "<b>$2</b>").replace(/!(<embed)\s(width|height)\s*=\s*"[\s\S]*?"/gi, '').replace(/<\s*(h\d|div|pre)[^>]*?>/gi, '<p>').replace(/<\s*\/\s*(h\d|div|pre)[^>]*?>/gi, '</p>');

        var pdtext = $.trim($(pdbd).text()),
                pdimg = /img/gi.test(pdbd.innerHTML),
                pdembd = /embed/gi.test(pdbd.innerHTML);
        if (pdtext == '' && !pdimg && !pdembd) {
            alert('请填写内容!');
            return;
        }
        //防止重复提交
        if (editsubmit_defalut_status == 1) {
            return false;
        }
        editsubmit_defalut_status = 1;
        var draft_hash = $('#draft_hash').val();
        $.post(bbs_domain + 'post.php?act=ajaxUpdate', {data_topicid: data_topicid, data_postid: data_postid, data_title: data_title, data_boardid: data_boardid, data_isbest:data_isbest,data_content: pdcontent, 'draft':draft_hash}, function(data) {
            //判断提交状态
            if (data['status'] == 103) {
                if (!!window.localStorage) {	//清空本地存储
                    localStorage.setItem('pdsaved', '');
                } else {
                    document.getElementById('pd_userdata').setAttribute('pdsaved', '');
                    document.getElementById('pd_userdata').save('pdsavedfile');
                }
                editsubmit_defalut_status = 0;
                window.location.href = bbs_domain + 'topic/' + data['topicid'];
            } else {
                alert(data['tips']);
                editsubmit_defalut_status = 0;
                return false;
            }
        }, 'json');
    });

    //编辑主题回复
    var editreply_submit_defalut_status = 0;
    $('#editreply_submit').click(function() {
        //检查内容为空
        var pdcontent = filterPasteData(pdbd.innerHTML),
                pdtext = $.trim($(pdbd).text()),
                data_boardid = $('#rep_boardid').val(),
                data_topicid = $('#rep_topicid').val(),
                data_postid = $('#rep_postid').val(),
                pdimg = /img/gi.test(pdbd.innerHTML),
                pdembd = /embed/gi.test(pdbd.innerHTML);
        if (pdtext == '' && !pdimg && !pdembd) {
            alert('请填写内容!');
            return;
        }
        if (pdtext.length < 3) {
            alert('回复内容不能少于3个字符!');
            return;
        }
        //防止重复提交
        if (editreply_submit_defalut_status == 1) {
            return false;
        }
        editreply_submit_defalut_status = 1;
        $.post(bbs_domain + 'post.php?act=ajaxUpdateEditReply', {bid: data_boardid, tid: data_topicid, pid: data_postid, content: pdcontent}, function(data) {
            editreply_submit_defalut_status = 0;
            if (data['status'] == 103) {
                window.location.href = bbs_domain + 'topic/' + data_topicid;
            } else {
                alert(data['tips']);
                return false;
            }
        }, 'json');
    });

    //-----------------------------添加标签部分--------------------------

    function tag_tips(hide)
    {
        if (hide == 'hide') {
            $('#add_tag_tips').html('&nbsp;');
        }
        if(hide == 'show') {
            $('#add_tag_tips').html('添加精准的标签，让文章更容易获得自然流量，提高曝光率');
        }
    }
    $('#tags_container').click(function() {
        if ($('#my_tags a').length < 5) {
            $('#search_tag_input').focus();
            $('#search_tag_input').click(function(e) {
                e.stopPropagation();
            });
        } else {
            $('#search_tag_input').hide();
            if ($('#tip_input_size').text() == '') {
                $('#tip_input_size').text('(已有5个标签，不可再添加)');
            }
        }
    });

    //点击添加系统标签
    $('#sys_tags').click(function(e) {
        var clicked = $(e.target);
        var kk = false;
        if (clicked.is('a')) {
            if ($('#my_tags a').length < 5) {
                var a = clicked.text();
                var tagid = clicked.attr('rid');
                var autid = clicked.attr('oid');
                var b = '<a href="javascript:;" cla="s" rid=' + tagid + ' oid=' + autid + '>' + a + '</a>';
                //去重标签添加
                $('#my_tags a').each(function() {
                    if ($(this).text() == a) {
                        alert('已添加过此标签');
                        kk = true;
                        return false;
                    }
                })
                if (kk == true)
                    return false;
                $.get(bbs_domain + 'post.php?act=ajaxDelTag', {id: tagid, oid: autid, type: 1}, function(data) {
                }, 'json');
                $('#my_tags').append(b);
                tag_tips('hide');
                clicked.remove();
                if($('#sys_tags a').length == 0){
                    $('.sys_tags dt.fl').hide();
                }
                if ($('#my_tags a').length == 5) {
                    $('#search_tag_input').hide();
                }
            } else {
                if ($('#tip_input_size').text() == '') {
                    $('#tip_input_size').text('(已有5个标签，不可再添加)');
                }
                return false;
            }
        }
    });
    //点击删除我的标签
    $('#my_tags').click(function(e) {
        var _this = $(this);
        var clicked = $(e.target);
        if (clicked.is('a')) {
            if (clicked.attr('cla') == 's') {
                $('#sys_tags').append(clicked);
                $('.sys_tags dt.fl').show();
            } else {
                clicked.remove();
            }
        }
        if (_this.find('a').length < 5) {
            $('#search_tag_input').show().focus();
            $('#tip_input_size').text('');
        }
        if(_this.find('a').length == 0) {
            tag_tips('show');
        }
        e.stopPropagation();
    });

    //搜索相关标签函数
    //输入框获得焦点
    $('#search_tag_input').focus(function(e) {
        if ($.trim($(this).val())) {
            $('#tag_searched').slideDown();
        }
        e.stopPropagation();
    });
    //输入框keyup事件
    var oldKeyWord;
    $('#search_tag_input').keyup(function(e) {
        var relatTags = $("#tag_searched a"),
                currentTag = $("#current_tag"),
                newKeyWord = $.trim($(this).val()).replace(/[^\u4E00-\u9FA50-9a-zA-Z]/g, '');
        if (!newKeyWord) {							//删除键
            $('#tag_searched').empty().hide();
            oldKeyWord = '';
            return;
        }
        //限定英文最多10个字符，中文最多6个字
        for (var i = 0; i < newKeyWord.length; i++) {
            if (newKeyWord.charCodeAt(i) > 0 && newKeyWord.charCodeAt(i) < 255) {
                $(this).attr('maxlength', 10);
                if (navigator.userAgent.indexOf('WebKit') != -1) {
                    newKeyWord = newKeyWord.slice(0, 10);
                }
            } else {
                $(this).attr('maxlength', 6);
                if (navigator.userAgent.indexOf('WebKit') != -1) {
                    newKeyWord = newKeyWord.slice(0, 6);
                }
            }
        }

        if (newKeyWord != oldKeyWord) {
            $('#tag_searched').show().empty().append('<a href="javascript:;">添加 <b>' + newKeyWord + '</b> 标签</a>');
            $('#tag_searched a').first().attr("id", "current_tag");
            function get_ser_tags() {
                $.getJSON(www_domain + 'tag/mng.php?act=get_RelationTag&msg=ok&format=json&tagKeyWord=' + newKeyWord + '&jsoncallback=?', function(data) {
                    if (data.error == 0) {
                        $.each(data.content, function(i, item) {
                            $('#tag_searched').append('<a href="javascript:;" autid=' + item['uid'] + '><span oid=' + item['id'] + '>' + item['name'] + '</span></a>');
                        })
                    }
                })
            }
            clearTimeout(limit);
            var limit = setTimeout(get_ser_tags, 300);
            oldKeyWord = newKeyWord;
        } else {							//判断按键
            var keyCode = e.keyCode || event.keyCode;
            switch (keyCode) {
                case 40:                //按了向下的键
                    if (currentTag.index() == relatTags.last().index()) {
                        currentTag.removeAttr("id");
                        relatTags.first().attr("id", "current_tag");
                    } else {
                        currentTag.removeAttr("id").next().attr("id", "current_tag");
                    }
                    ;
                    break;
                case 38:                //按了向上的键
                    if (currentTag.index() == relatTags.first().index()) {
                        currentTag.removeAttr("id");
                        relatTags.last().attr("id", "current_tag");
                    } else {
                        currentTag.removeAttr("id").prev().attr("id", "current_tag");
                    }
                    ;
                    break;
                case 13:                //回车键
                    var a = currentTag.find("span").text() || currentTag.find("b").text();
                    if (!a) {
                        $("#tag_searched").slideUp();
                        break;
                    } else {
                        var kk = false;
                        if (currentTag.find("span").length > 0) {
                            var tagid = currentTag.find('span').attr('oid');
                            var autid = currentTag.attr('autid');
                            var b = '<a href="javascript:;" cla ="ser" tid=' + tagid + ' autid=' + autid + '>' + a + '</a>';
                        } else {
                            var b = '<a href="javascript:;" cla ="def" >' + a + '</a>';
                        }
                        //去重标签添加
                        $('#my_tags a').each(function() {
                            if ($(this).text() == a) {
                                alert('已添加过此标签');
                                kk = true;
                                return false;
                            }
                        })
                        if (kk == true)
                            return false;
                        $('#my_tags').append(b);
                        $('#search_tag_input').val('');
                        $('#tag_searched').empty().hide();
                        tag_tips('hide');
                        oldKeyWord = '';
                        if ($('#my_tags a').length == 5) {
                            $('#search_tag_input').val('').hide();
                        }
                    }
                    break;
            }
        }
    })
    //标签鼠标感应
    $(document).on('hover','#tag_searched a',function(){
        $("#current_tag").removeAttr('id');
        $(this).attr('id', 'current_tag');
    })

    //鼠标点击添加标签
    $(document).on("click","#tag_searched a",function(){
        if ($('#my_tags a').length < 5) {
            var kk = false;
            var a = $(this).find('b').length ? $(this).find('b').text() : $(this).text();
            if ($(this).find('span').length > 0) {
                var tagid = $(this).find('span').attr('oid');
                var autid = $(this).attr('autid');
                var b = '<a href="javascript:;" cla ="ser" tid=' + tagid + ' autid=' + autid + '>' + a + '</a>';
            } else {
                var b = '<a href="javascript:;" cla ="def" >' + a + '</a>';
            }
            //去重标签添加
            $('#my_tags a').each(function() {
                if ($(this).text() == a) {
                    alert('已添加过此标签');
                    kk = true;
                    return false;
                }
            })
            if (kk == true)
                return false;
            $('#my_tags').append(b);
            $('#search_tag_input').val('');
            $('#tag_searched').empty().hide();
            tag_tips('hide');
            oldKeyWord = '';
            if ($('#my_tags a').length == 5) {
                $('#search_tag_input').val('').hide();
            }
        } else {
            if ($('#tip_input_size').text() == '') {
                $('#tip_input_size').text('(已有5个标签，不可再添加)');
            }
            return false;
        }
    })


    //输入框失去焦点
    $("#search_tag_input").blur(function() {
        setTimeout(searchedHide, 100);
    })
    //师说精华
    $('#shishuo').click(function(){
        if($("#jh").is(":visible")) {
                   $('#jh').hide();
               } else {
                   $('#jh').show();
                   $("#jh").html('数据加载中。。。');
                   $.post(bbs_domain + 'post.php?act=ajaxGetsh','',function(data){
                       if(data['status']==103) {
                           sh=data['content'];
                           //---------
                           var news='';
                           var len=0;
                           if(sh){
                               len=sh.length;
                           }else{
                               len=0;
                           }                           
                           var str='<ul>';
                           if(len>0){
                                   for (i=0;i<len ;i++ )
                                   {                                       
                                           var strs=sh[i].split("|");                                       
                                           if(strs[0]==0){
                                               s=strs[1].split("-");
                                               str+="<li><input type='checkbox' name='shjh' value='"+sh[i]+"'><a href='http://my.paidai.com/"+s[0]+"' target='_black'>@"+s[1]+"</a>:"+s[2]+"</li>";
                                           }else{
                                               s=strs[0].split("-");
                                               str+="<li><input type='checkbox' name='shjh' value='"+sh[i]+"'><a href='http://my.paidai.com/"+s[0]+"' target='_black'>@"+s[1]+"</a>:"+s[2]+"<br>";
                                               s=strs[1].split("-");
                                               str+="<a href='http://my.paidai.com/"+s[0]+"' target='_black'>@"+s[1]+"</a>:"+s[2]+"</li>";
                                           }                                      
                                   }
                           }
                           news+="<br>------师说精华------<br>";
                           news+=str;
                             //---------------
                           news+="<input type='button' value='生成'    name='subSh'/>";                           
                           $("#jh").html(news);
                           $("input[name=subSh]").unbind('click').bind('click', function(){
                               var chk_value='导语在此插入<br>图片在此插入<br>';
                               chk_value+='------师说精华------<br>';
                               i=1;
                               $('input[name="shjh"]:checked').each(function(){
                                   strs=$(this).val().split("|");                                   
                                   if(strs[0]==0){
                                       s=strs[1].split("-");
                                       chk_value+=i+".<a href='http://my.paidai.com/"+s[0]+"' target='_black'>@"+s[1]+"</a>：&nbsp;";
                                       chk_value+=s[2]+"<br>";                                       
                                   }else{
                                       s=strs[0].split("-");
                                       chk_value+=i+".<a href='http://my.paidai.com/"+s[0]+"' target='_black'>@"+s[1]+"</a>：&nbsp;";
                                       chk_value+=s[2]+"<br>";                                      
                                       s=strs[1].split("-");            
                                       chk_value+="<a href='http://my.pbaidai.com/"+s[0]+"' target='_black'>@"+s[1]+"</a>：&nbsp;";
                                       chk_value+=s[2]+"<br>";
                                   }                                   
                                   i++;
                               });                               
                               pdbd.innerHTML=chk_value;
                               countNum();
                               $('#jh').hide();
                           });
                           //-----------
                       }
                   },'json');
               }
    });
    //派精华
   	$('#jinghua').click(function(){
               if($("#jh").is(":visible")) {
                   $('#jh').hide();
               } else {
                   $('#jh').show();

                   $("#jh").html('数据加载中。。。');

                   $.post(bbs_domain + 'post.php?act=ajaxGetjh','',function(data){

                       if(data['status']==103) {

                           gh=data['content']['gh'];
                           bw=data['content']['bw'];
                           sy=data['content']['sy'];
                           //---------
                           var news='';

                           var len=0;
                           if(gh){
                               len=gh.length;
                           }else{
                               len=0;
                           }
                           var str='<ul>';
                           if(len>0){
                                   for (i=0;i<len ;i++ )
                                   {
                                       var strs=gh[i].split("|");
                                       str+="<li><input type='checkbox' name='jhbw' value='"+gh[i]+"'><a href='http://my.paidai.com/"+strs[2]+"' target='_black'>@"+strs[3]+"</a>&nbsp;<a href='http://bbs.paidai.com/topic/"+strs[0]+"' target='_black'>"+strs[1]+"</a></li>";

                                   }
                           }


                           news+="<br>------干货------<br>";

                           //news+="<input type='checkbox' name='jhbw' value='1'>1<br>";
                           news+=str;
                           //------------
                           if(bw){
                               len=bw.length;
                           }else{
                               len=0;
                           }
                           str="";
                           if(len>0){
                                   for (i=0;i<len ;i++ )
                                   {
                                       var strs=bw[i].split("|");
                                       str+="<li><input type='checkbox' name='jhbw' value='"+bw[i]+"'><a href='http://my.paidai.com/"+strs[2]+"' target='_black'>@"+strs[3]+"</a>&nbsp;<a href='http://bbs.paidai.com/topic/"+strs[0]+"' target='_black'>"+strs[1]+"</a></li>";

                                   }
                           }
                           str+="</ul>";
                           news+="<br>------豹纹------<br>";
                           news+=str;
                           //----------
                           if(sy){
                               len=sy.length;
                           }else{
                               len=0;
                           }
                           str="<ul>";
                           if(len>0){
                                   for (i=0;i<len ;i++ )
                                   {
                                       var strs=sy[i].split("|");
                                       str+="<li><input type='checkbox' name='jhsy' value='"+sy[i]+"'><a href='http://my.paidai.com/"+strs[2]+"' target='_black'>@"+strs[3]+"</a>&nbsp;<a href='http://bbs.paidai.com/topic/"+strs[0]+"' target='_black'>"+strs[1]+"</a></li>";

                                   }
                           }
                           str+="</ul>";
                           news+="<br>------首页精选------<br>";
                           news+=str;
                           //---------------

                           news+="<input type='button' value='生成'    name='subSh'/>";



                           $("#jh").html(news);
                           //0:id,1:title,2:uid,3:uname,4:point

                           $("input[name=subSh]").unbind('click').bind('click', function(){
                               var chk_value='导语在此插入<br>图片在此插入<br>';
                               chk_value+='------干货&豹纹篇------<br>';
                               i=1;
                               $('input[name="jhbw"]:checked').each(function(){
                                   strs=$(this).val().split("|");
                                   chk_value+=i+".<a href='http://my.paidai.com/"+strs[2]+"' target='_black'>@"+strs[3]+"</a>：&nbsp;";
                                   chk_value+=strs[1]+"<br>";
                                   chk_value+=bbs_domain + "topic/"+strs[0]+"<br>";
                                   i++;
                               });
                               chk_value+='------首页精选篇------<br>';
                               i=1
                               $('input[name="jhsy"]:checked').each(function(){
                                  strs=$(this).val().split("|");
                                  chk_value+=i+".<a href='http://my.paidai.com/"+strs[2]+"' target='_black'>@"+strs[3]+"</a>：&nbsp;";
                                  chk_value+=strs[1]+"<br>";
                                  chk_value+=bbs_domain + "topic/"+strs[0]+"<br>";                                    i++;
                               });


                               pdbd.innerHTML=chk_value;
                               countNum();
                               $('#jh').hide();
                           });
                           //-----------
                       }
                   },'json');




               }
   	})
    function searchedHide() {
        $("#tag_searched").slideUp('fast');
    }



//-----------------------------------------------------------------------------------------------------
//-------------------------------------swfupload 相关函数-------------------------------------------------
    function fileDialogComplete(numFilesSelected, numFilesQueued) { //选取完图片后开始上传
        if (numFilesQueued > 0) {
            this.startUpload();
            document.getElementById("upload_bg").style.display = "inline-block";
        }
    }
    function uploadSuccess(file, serverData) { //上传成功后插入图片
        var obj = jQuery.parseJSON(serverData);
        if (!obj.error) {
            var srcval = obj.imgUrl,
                    idval = obj.imgid,
                    imagetag = '<img src=' + srcval + ' img_id=' + idval + '  />';
            insertImg(imagetag);
            var a = document.getElementById("upload_color"),
                    b = document.getElementById("upload_percent_num");
            a.style.width = "180px";
            b.innerHTML = 100;
            setTimeout(function() {
                a.style.width = "0px";
                b.innerHTML = 0;
                $('#upload_bg').fadeOut('slow', function() {
                    countNum();
                }); //document.getElementById("upload_bg").style.display = "none";

            }, 1000);

        } else {
            alert(obj.error);
            return false;
        }
    }
    function uploadComplete(file) {		//一张图片上传完成
        if (this.getStats().files_queued > 0) {
            this.startUpload();
            document.getElementById("upload_bg").style.display = "inline-block";
        }
    }
    function fileQueueError(file, errorCode, message) {	//加入上传队列出错
        switch (errorCode) {
            case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                alert('图片大小为零！');
                break;
            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                alert('图片大小限制为2M！');
                break;
            default:
                alert(message);
                break;
        }
    }
    function uploadProgress(file, bytesLoaded) {	//显示上传进度,进度条总宽度为200px
        var percent = Math.ceil((bytesLoaded / file.size) * 100);
        document.getElementById("upload_color").style.width = percent / 100 * 200 + "px";
        document.getElementById("upload_percent_num").innerHTML = percent;
    }
    function uploadError(file, errorCode, message) {  //上传过程出错
        if (errorCode == SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
            alert('上传已终止，请重试！');
        } else {
            alert(message);
        }
    }

//})

})