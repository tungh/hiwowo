/*
 * by zdy 2013.08.22
 */
define(function(require){
   var $ = require("jquery")

//(function ($) {
    $.fn.scrollLazyLoad = function (options) {
        var $container = $(this),
            elemList = [],

            // 默认参数
            defaults = {
                scroller: $(window),
                attr: 'data-url',
                fn: function () {}
            },
            settings = $.extend({}, defaults, options),
            scroller = settings['scroller'];

        // 缓存待加载的元素
        $.each($container.find('[' + settings['attr'] + ']'), function (i, v) {
            elemList.push($(v));
        });

        // 判断滚动的最外层是window/others
        var isWinScroller = function () {
            if (scroller[0] === window) {
                return function (elem) {
                    return elem.offset().top <=
                     $(window).height() + $(window).scrollTop();
                };
            } else {
                return function (elem) {
                    return elem.offset().top <=
                     scroller.innerHeight() + scroller.offset().top;
                };
            }
        },
        isInView = isWinScroller(),

        loading = function () {
            $.each(elemList, function (i, v) {
                var $this = $(v),
                    url = $this.attr(settings['attr']),
                    tagName = $this[0].nodeName.toLowerCase();
                // 在视窗范围内 && 没有被加载过
                if (isInView($this) && url) {
                    if (tagName === 'img') {
                        $this.attr('src', url);
                        // callback
                        settings.fn.call($this[0]);
                    } else {
                        $this.load(url, function () {
                            // 加载子区域中的img
                            $.each($this.find('[' + settings['attr'] + ']'),
                             function (i, v) {
                                elemList.push($(v));
                            });
                            // callback
                            settings.fn.call($this[0]);
                        });
                    }
                    $this.removeAttr(settings['attr']);
                }

            });
        },

        // 函数节流
        throttle = function (method, context, time) {
            clearTimeout(method.tid);
            method.tid = setTimeout(function () {
                method.call(context);
            }, time);
        };

        loading();

        scroller.scroll(function () {
            throttle(loading, $container, 100);
        });
    };
//}(jQuery));
})