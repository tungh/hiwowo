/**
 * Created with IntelliJ IDEA.
 * User: zuosanshao
 * Date: 14-4-15
 * Time: 下午11:34
 *
 */
define(function(require, exports){
    var $  = require("jquery");

    /* 用户收藏 diagram */
    $.hiwowo.userCollect={
        /* 收藏图说 */
        addCollect:function(id,$this){
            /* 不存在，则保存 */
            var params={
                collectId:id,
                typeId:0
            }
            $.ajax({
                url:"/user/addCollect",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"你被关进小黑屋，禁止收藏哦");
                    }
                   else if(data.code=="100"){
                        var num = $this.data("val")+1;
                        $this.find(".collectNum").text(num)
                        $this.data("val",num)
                        //  $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        //  $.hiwowo.tip.show($this,"收藏了");
                    }else if(data.code =="101"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show($this,"已经收藏了");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，网站抽疯了，伤心离开中……");
                    }
                }
            });
        },
        removeCollect:function(id,$this){
            var params={
                collectId:id,
                typeId:0
            }
            $.ajax({
                url:"/user/removeCollect",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"你被关进小黑屋，禁止操作哦");
                    }
                    else if(data.code=="100"){
                       alert("取消收藏成功")
                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"id 为空啊");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，网站抽疯了，伤心离开中……");
                    }
                }
            });
        }
    }

    /* 用户订阅 标签 */
    $.hiwowo.userSubscribe={
        addSubscribe:function(id,$this){
            $.ajax({
                url:"/user/addSubscribe",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify({labelId:id}),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"你被关进小黑屋，禁止操作哦");
                    }
                    else if(data.code=="100"){
                        alert("订阅成功")
                    }else if(data.code =="101"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"已经订阅了");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，网站抽疯了，伤心离开中……");
                    }
                }
            });
        },
        removeSubscribe:function(id,$this){
            $.ajax({
                url:"/user/removeSubscribe",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify({labelId:id}),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"你被关进小黑屋，禁止操作哦");
                    }
                    else if(data.code=="100"){
                        alert("取消订阅成功")
                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"id 为空啊");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，网站抽疯了，伤心离开中……");
                    }
                }
            });
        }
    }
    /* 用户关注 */
    $.hiwowo.userFollow={
        addFollow:function(id,$this){
            $.ajax({
                url:"/user/addFollow",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify({followId:id}),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"你被关进小黑屋，禁止操作哦");
                    }
                    else if(data.code=="100"){
                        alert("关注成功")
                    }else if(data.code =="101"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"已经关注了");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，网站抽疯了，伤心离开中……");
                    }
                }
            });
        },
        removeFollow:function(id,$this){
            $.ajax({
                url:"/user/removeFollow",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify({followId:id}),

                success: function(data){
                    if(data.code =="200"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，你还没有登录");
                    }else if(data.code =="444"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"你被关进小黑屋，禁止操作哦");
                    }
                    else if(data.code=="100"){
                        alert("取消关注成功")
                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"id 为空啊");
                    }else{
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-error";
                        $.hiwowo.tip.show($this,"哎呀，网站抽疯了，伤心离开中……");
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
            $.hiwowo.userCollect.addCollect(id,$(this))
        })
        $(document).on("click","li[rel=removeCollectDiagram]",function(){
            if(!$.hiwowo.loginDialog.isLogin()){
                return false;
            }
            var id =$(this).data("id")
            $.hiwowo.userCollect.removeCollect(id,$(this))
        })
        $(document).on("click","li[rel=subscribeLabel]",function(){
            if(!$.hiwowo.loginDialog.isLogin()){
                return false;
            }
            var id =$(this).data("id")
            $.hiwowo.userSubscribe.addSubscribe(id,$(this))
        })
        $(document).on("click","li[rel=removeSubscribeLabel]",function(){
            if(!$.hiwowo.loginDialog.isLogin()){
                return false;
            }
            var id =$(this).data("id")
            $.hiwowo.userSubscribe.removeSubscribe(id,$(this))
        })
        $(document).on("click","li[rel=addFollow]",function(){
            if(!$.hiwowo.loginDialog.isLogin()){
                return false;
            }
            var id =$(this).data("id")
            $.hiwowo.userFollow.addFollow(id,$(this))
        })
        $(document).on("click","li[rel=removeFollow]",function(){
            if(!$.hiwowo.loginDialog.isLogin()){
                return false;
            }
            var id =$(this).data("id")
            $.hiwowo.userFollow.removeFollow(id,$(this))
        })
    })
});