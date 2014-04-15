/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-15
 * Time: 下午11:34
 *
 */
define(function(require, exports){
    var $  = require("jquery");

    $.hiwowo.userCollect={
        /* 收藏图说 */
        diagram:function(id,o){
            /* 不存在，则保存 */
            var params={
                collectId:id,
                typeId:0
            }
            $.ajax({
                url:"/user/collect",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show(o,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show(o,"你被关进小黑屋，禁止收藏哦");
                    }
                   else if(data.code=="100"){
                        var num = o.data("val")+1;
                        o.find(".collectNum").text(num)
                        o.data("val",num)
                        //  $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        //  $.hiwowo.tip.show(o,"喜欢了");
                    }else if(data.code =="102"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show(o,"已经收藏了");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show(o,"哎呀，我抽疯了，伤心离开中……");
                    }
                }
            });
        }
    }

    $(function(){

        $(document).on("click","li[rel=collectDiagram]",function(){
            if(!$.hiwowo.loginDialog.isLogin()){
                return false;
            }
            var id =$(this).data("id")
            $.hiwowo.userCollect.diagram(id,$(this))
        })
    })
});