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
         'jquery': 'sea-modules/jquery/1.11.1/jquery',
         'bootstrap':'bootstrap/3.2.0/bootstrap.min',

        'cookie': 'sea-modules/cookie',
        'easing':'sea-modules/jquery.easing',
        'imgAreaSelect':'sea-modules/jquery.imgAreaSelect',
        'detector':'sea-modules/detector',
        'unslider':'sea-modules/jquery.unslider.min.js',
        'pin':'sea-modules/jquery.pin.min.js',
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