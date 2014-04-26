/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-13
 * Time: 下午11:53
 *   任何人都可以表达 like or Hate，但是，对于同一个diagram,有时间限制，表示重复的喜欢。对于已登录的用户，需要记录他喜欢的东西
 */
define(function(require){
    var $ = jQuery = require("jquery")
    $.hiwowo.diagramFavor={
        /* like */
        like:function(id,o){
            /* 不存在，则保存 */

              var params={
                  diagramId:id
              }
              $.ajax({
                  url:"/diagram/up",
                  type : "POST",
                  contentType:"application/json; charset=utf-8",
                  dataType: "json",
                  data:JSON.stringify(params),

                  success: function(data){
                      if(data.code=="100"){
                          var $likeCount = o.next(".like-num").find(".J_FavorNum");
                          var  loveNum = parseInt($likeCount.data("val")) + 1;
                          $likeCount.text(loveNum);
                          $likeCount.data("val", loveNum);
                        //  $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        //  $.hiwowo.tip.show(o,"喜欢了");
                      }else if(data.code =="104"){
                          $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                          $.hiwowo.tip.show(o,"已经喜欢了");
                      }
                  }
              });


        },
        /* hate */
        hate:function(id,o){

                var params={
                    diagramId:id
                }
                $.ajax({
                    url:"/diagram/disfavor",
                    type : "POST",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data:JSON.stringify(params),

                    success: function(data){
                        if(data.code=="100"){
                            var $likeCount = o.next(".like-num").find(".J_FavorNum");
                            var  loveNum = parseInt($likeCount.data("val")) + 1;
                            $likeCount.text(loveNum);
                            $likeCount.data("val", loveNum);
                         //   $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                         //   $.hiwowo.tip.show(o,"不喜欢了");

                        }else if(data.code =="104"){
                            $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                            $.hiwowo.tip.show(o,"已经不喜欢了");
                        }
                    }
                });


        }
    }
    $.hiwowo.discussFavor={
        /* like */
        like:function(id,o){
            /* 不存在，则保存 */

            var params={
                discussId:id
            }
            $.ajax({
                url:"/discuss/favor",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code=="100"){
                          var num = o.data("val")+1;
                          o.text(num)
                         o.data("val",num)
                         // $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                       //   $.hiwowo.tip.show(o,"支持了");
                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show(o,"已经支持了");
                    }
                }
            });


        },
        /* hate */
        hate:function(id,o){

            var params={
                discussId:id
            }
            $.ajax({
                url:"/discuss/down",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code=="100"){
                        var num = o.data("val")+1;
                        o.text(num)
                        o.data("val",num)
                         //  $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                       //   $.hiwowo.tip.show(o,"反对");

                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show(o,"已经不反对了");
                    }
                }
            });


        }
    }
    $(function(){
        /* diagram favor */
        $(document).on("click","a[rel=upDiagram]",function(){
            var id =$(this).data("id")
            $.hiwowo.diagramFavor.like(id,$(this))
        })
        $(document).on("click","a[rel=downDiagram]",function(){
            var id =$(this).data("id")
            $.hiwowo.diagramFavor.hate(id,$(this))
        })
        /* discuss favor */
        $(document).on("click","a[rel=upDiscuss]",function(){
            var id =$(this).data("id")
            $.hiwowo.discussFavor.like(id,$(this))
        })
        $(document).on("click","a[rel=downDiscuss]",function(){
            var id =$(this).data("id")
            $.hiwowo.discussFavor.hate(id,$(this))
        })
    })
})
