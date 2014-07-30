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
define(function(require, exports){
    require("hiwowo")
    /* diagram favor */
    $.hiwowo.userDiagram={
        /* thumbs Up */
        thumbsUp:function(id,$this){
            /* 不存在，则保存 */

            var params={
                diagramId:id
            }
            $.ajax({
                url:"/diagram/favor",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code=="100"){
                       var $likeCount = $this.find(".J_FavorNum");
                        var  loveNum = parseInt($likeCount.data("val")) + 1;
                        $likeCount.text(loveNum);
                        $likeCount.data("val", loveNum);

                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show($this,"已经喜欢了");
                    }
                }
            });


        },
        /* thumbs down */
        thumbsDown:function(id,$this){

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
                        var $likeCount = $this.find(".J_hateNum");
                        var  loveNum = parseInt($likeCount.data("val")) + 1;
                        $likeCount.text(loveNum);
                        $likeCount.data("val", loveNum);
                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show($this,"已经不喜欢了");
                    }
                }
            });


        }
    }
    /* diagram discuss favor */
    $.hiwowo.userDiagramDiscuss={
        /* thumbs up */
        thumbsUp:function(id,$this){
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
                        var num = $this.data("val")+1;
                        $this.text(num)
                        $this.data("val",num)
                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show($this,"已经赞了");
                    }
                }
            });


        },
        /* thumbs down */
        thumbsDown:function(id,$this){
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
                        var num = $this.data("val")+1;
                        $this.text(num)
                        $this.data("val",num)

                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show($this,"已经鄙视过了");
                    }
                }
            });


        }
    }

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
                        $this.find(".J_collectNum").text(num)
                        $this.data("val",num)

                    }else if(data.code =="102"){
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

    $.hiwowo.userTopic={
        favor:function(id,$this){
            /* 不存在，则保存 */
            var params={
                topicId:id,
                typeId:0
            }
            $.ajax({
                url:"/topic/favor",
                type : "POST",
                contentType:"application/json; charset=utf-8",
                dataType: "json",
                data:JSON.stringify(params),

                success: function(data){
                    if(data.code=="100"){
                        var $likeCount = $this.find(".J_favorNum");
                        var  loveNum = parseInt($likeCount.data("val")) + 1;
                        $likeCount.text(loveNum);
                        $likeCount.data("val", loveNum);

                    }else if(data.code =="104"){
                        $.hiwowo.tip.conf.tipClass = "tipmodal-s tipmodal-ok";
                        $.hiwowo.tip.show($this,"已经喜欢了");
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
        /* diagram favor */
        $(document).on("click","a[rel=thumbsUp]",function(){
            var id =$(this).data("id")
            $.hiwowo.userDiagram.thumbsUp(id,$(this))
        })
        $(document).on("click","a[rel=thumbsDown]",function(){
            var id =$(this).data("id")
            $.hiwowo.userDiagram.thumbsDown(id,$(this))
        })
        /* discuss favor */
        $(document).on("click","a[rel=upDiscuss]",function(){
            var id =$(this).data("id")
            $.hiwowo.userDiagramDiscuss.thumbsUp(id,$(this))
        })
        $(document).on("click","a[rel=downDiscuss]",function(){
            var id =$(this).data("id")
            $.hiwowo.userDiagramDiscuss.thumbsDown(id,$(this))
        })

       /* topic favor */
        $(document).on("click","a[rel=addTopicFavor]",function(){
            var id =$(this).data("id")
            $.hiwowo.userTopic.favor(id,$(this))
        })
    })
});