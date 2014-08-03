/**
 * Created by zuosanshao.
 * User: hiwowo.com
 * Email:zuosanshao@qq.com
 * @contain: sea.js config
 * @depends:
 * Includes:
 * Since: 2014-5-3  上午11:35
 * ModifyTime :
 * ModifyContent:
 * http://hiwowo.com/
 *
 */
seajs.config({
    // Sea.js 的基础路径
    base: '/assets/js/',
    // 别名配置
    alias: {
        'jquery': 'sea-modules/jquery/1.11.1/jquery.js',
         'json': 'sea-modules/json/1.0.3/json.js',
        'detector':'sea-modules/detector/2.0.1/detector.js',

         'imgAreaSelect':'libs/imageselect/jquery.imgAreaSelect.js',
         'unslider':'libs/slider/jquery.unslider.min.js',
        'swfupload':'libs/swfupload/swfupload.js',
        'vticker':'libs/vticker/jquery.vticker.min.js',
        'headroom':'libs/headroom/headroom.min.js',
        'bootstrap':'libs/bootstrap/3.2.0/bootstrap.min',
        'webuploader':'libs/webuploader/0.1.5/webuploader.min',
        'cookie': 'libs/cookies/1.4.1/cookie.js',

        'lazyload': 'libs/lazyload/jquery.lazyload.js',


        'simpleEditor':'hiwowo/editor/hiwowo.simpleEditor',
        'hiwowo':'hiwowo/1.0.0/hiwowo'
    },


    // 预加载项
    preload: [
        this.JSON ? '' : 'json'
    ],
    // 调试模式
    debug: false,

    // 文件编码
    charset: 'utf-8'
});