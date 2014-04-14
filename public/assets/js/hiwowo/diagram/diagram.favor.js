/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-13
 * Time: 下午11:53
 *   任何人都可以表达 like or Hate，但是，对于同一个diagram,有时间限制，表示重复的喜欢。对于已登录的用户，需要记录他喜欢的东西
 */
define(function(require){
    var $ = jQuery = require("jquery")
    var Cookie = require("cookie");
    $.hiwowo.diagramFavor={
        /* like */
        like:function(id){
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
                      if(data.message =="loved") { alert("loved")}
                      if(data.message =="success"){alert("success")}
                      }
                  }
              });


        },
        /* hate */
        hate:function(id){

                var params={
                    diagramId:id
                }
                $.ajax({
                    url:"/diagram/down",
                    type : "POST",
                    contentType:"application/json; charset=utf-8",
                    dataType: "json",
                    data:JSON.stringify(params),

                    success: function(data){
                        if(data.code=="100"){
                            if(data.message =="loved") { alert("loved")}
                            if(data.message =="success"){alert("success")}
                        }
                    }
                });


        },
        /* collect */
        collect:function(){

        }
    }
    $(function(){
        $(document).on("click","a[rel=upDiagram]",function(){
            var id =$(this).data("id")
            $.hiwowo.diagramFavor.like(id)
        })
        $(document).on("click","a[rel=downDiagram]",function(){
            var id =$(this).data("id")
            $.hiwowo.diagramFavor.like(id)
        })
    })
})
