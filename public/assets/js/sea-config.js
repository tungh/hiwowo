/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-8
 * Time: 下午9:28
 */
seajs.config({
    // Sea.js 的基础路径
    base: '/assets/js/',
    // 别名配置
    alias: {
        'json': 'sea-modules/json',
         'jquery': 'sea-modules/jquery',
        'cookie': 'sea-modules/cookie',
        'zeroclipboard': 'sea-modules/zeroclipboard',
        'easing':'sea-modules/jquery.easing',
        'imgAreaSelect':'sea-modules/jquery.imgAreaSelect',
        'detector':'sea-modules/detector',
        'swfobject':'sea-modules/swfobject',
        'swfupload':'sea-modules/swfupload',
        'uploadify':'sea-modules/jquery.uploadify.js',
        'bootstrap':'flatui/bootstrap',
       'umeditor':'umeditor/umeditor.min.js',
        'umeditor-lang':'umeditor/lang/zh-cn/zh-cn.js',
        'umeditor-emotion':'umeditor/dialogs/emotion/emotion.js',
        'umeditor-image':'umeditor/dialogs/image/image.js',
        'umeditor-link':'umeditor/dialogs/link/link.js',
        'umeditor-map':'umeditor/dialogs/map/map.js',
        'umeditor-video':'umeditor/dialogs/video/video.js',
        'jqueryte':'jqueryte/jquery-te-1.4.0.min.js',
         'hiwowo':'hiwowo/hiwowo'
    },

    // 预加载项
    preload: [
        this.JSON ? '' : 'json','hiwowo'
    ],

    // 调试模式
    debug: false,

    // 文件编码
    charset: 'utf-8'
});