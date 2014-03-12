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
     $(function(){
         $("textarea").jqte({
             format: false,
             fsize:false,
             color:false
         })
     })

 })