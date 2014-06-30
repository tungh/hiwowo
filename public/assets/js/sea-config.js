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
        'unslider':'sea-modules/jquery.unslider.min.js',
        'pin':'sea-modules/jquery.pin.min.js',
        'bootstrap':'flatui/bootstrap',
        'simpleEditor':'hiwowo/editor/hiwowo.simpleEditor',

        'bootstrap3.2':'bootstrap/3.2.0/bootstrap.min',
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