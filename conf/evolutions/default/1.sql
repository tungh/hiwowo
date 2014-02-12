# --- First database schema
# --- First database schema

# --- !Ups

/************************************************************
 * 用户是个大的结构  主要有以下几个表
 * user表                User常用的基本信息，包括常用的统计信息
 * user_profile 表        User不常用的信息、例如生日、性别、所在位置等等
 * user_static             用户统计信息
 * user_tag 表            用户的标签
 * user_follow 表         用户关注的人
 * user_love 表           用户喜欢的（收藏的）


 * user_record     表    用户动态，类似于用户的操作记录，记录用户的动作

 **************************************************************/
 /*数据表  User  用户的基本信息和常用信息
     id         用户ID
     name       用户昵称
     password   用户密码
     email      用户邮箱
     credits    用户积分
     intro      简介
     pic       用户头像
     status      用户状态 例如正常状态、拉黑状态、活跃用户等    0:邮箱未验证 1：正常用户 2 拉黑 3 活跃
     title      头衔
     come_from   用户的来源：0 hiwowo 1 qq 2 新浪微博 3 淘宝 4 微信
     open_id     第三方账号ID
     province    所在省份
     tags        标签，以,隔开


  */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `name`                varchar(64) NOT NULL ,
  `password`              varchar(64) NOT NULL default '0',
  `email`               varchar(128),
  `credits`              smallint(10) not null default '0',
  `pic`                 varchar(255) NOT NULL default '/images/user/default.jpg',
  `title`                varchar(64),
  `intro`                varchar(250),
  `status`                tinyint    not null default '0',
  `come_from`              tinyint    not null default '0',
  `open_id`               varchar(64),
  `province`            varchar(20),
  `tags`                    varchar(250) ,
  `modify_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


  /*数据表  User  用户的基本信息和常用信息
     id               表D
     uid             用户ID
     invite_id      邀请者ID
     register_time     注册时间
     login_time     上次登录时间
     login_ip        用户登录的IP
     gender          用户性别  0 女  1 男 2 保密
     province         所在省份
     city         所在城市
     town         所在镇、县
     street       所在具体的街道
     post_code    邮编
     phone        联系手机
     blog        个人博客、微博、qq空间
  */
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE IF NOT EXISTS `user_profile` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `invite_id`           int(10) ,
  `gender`              tinyint(4) not null DEFAULT '2',
  `birth`                varchar(16) ,
  `qq`                varchar(64) ,
  `weixin`                varchar(64) ,
  `receiver`            varchar(20) ,
  `province`            varchar(20),
  `city`               varchar(20) ,
  `town`                varchar(20),
  `street`              varchar(50) ,
  `post_code`           varchar(16) ,
  `phone`               char(32) ,
  `blog`                varchar(200) ,
  `login_time`          timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `login_num`           smallint(10) NOT NULL default '1',
  `login_ip`            varchar(32) DEFAULT '0',
  `regist_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------
/*

  -- 表的结构 `user_static`
     id                               表的ID
     uid                              用户ID
--
*/

DROP TABLE IF EXISTS `user_static`;
CREATE TABLE `user_static` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `fans_num`                  smallint(11) DEFAULT '0',
  `follow_num`                smallint(11) DEFAULT '0',
  `love_site_num`           smallint(11) DEFAULT '0',
  `love_blog_num`            smallint(11) DEFAULT '0',
  `love_pic_num`            smallint(11) DEFAULT '0',
  `love_video_num`            smallint(11) DEFAULT '0',
  `love_shop_num`            smallint(11) DEFAULT '0',
  `love_topic_num`           smallint(11) DEFAULT '0',
  `own_site_num`           smallint(11) DEFAULT '0',
  `own_blog_num`            smallint(11) DEFAULT '0',
  `own_pic_num`            smallint(11) DEFAULT '0',
  `own_video_num`            smallint(11) DEFAULT '0',
  `own_shop_num`            smallint(11) DEFAULT '0',
  `own_topic_num`           smallint(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_follow`
     id 表的ID
     uid  用户ID
     fans_id 粉丝ID
     add_time  添加时间
--
*/

DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow` (
  `id`                int(10) NOT NULL AUTO_INCREMENT,
  `uid`               int(10) NOT NULL DEFAULT '0',
  `fans_id`           int(10) NOT NULL DEFAULT '0',
  `add_time`          timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*
  -- 表的结构 `user_record`
     id 表的ID
     uid 用户的id
     action_type 行为动作，例如 0、喜欢的商品 1、关注主题 2、发布宝贝 3、创作主题 4、评论主题 5、回复话题 6 认为值得
     action_id 行为的第三方Id 或者 数量
     action_url   链接的url
     action_content 行为的内容
     add_time  添加时间
--
*/

-- --------------------------------------------------------
 DROP TABLE IF EXISTS `user_record`;
CREATE TABLE `user_record` (
  `id`                 int(10) NOT NULL AUTO_INCREMENT,
  `uid`                int(10) NOT NULL DEFAULT '0',
  `action_name`       varchar(32) not null default 'love',
  `action_id`         int(10) NOT NULL,
  `action_url`         varchar(128) NOT NULL DEFAULT 'http://hiwowo.com',
  `action_content`     varchar(128) NOT NULL DEFAULT 'hiwowo',
  `add_time`           timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*
* 用户签到表设计
* check_in_days 连续签到时间
* credits 获得的积分
* check_in_month 201305
* check_in_history day:state:credit  01:1:3,02:0:0,03:1:2 诸如此类
*/


DROP TABLE IF EXISTS `user_check_in`;
CREATE TABLE `user_check_in` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) ,
  `credit`             smallint (10) ,
  `days`      smallint (10) ,
  `month`     int(10) unsigned ,
  `history`   varchar(200),
  `add_time`      timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_site`
     id 表的ID
     uid 用户的id
     love_id  第三方的ID
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user_love`;
CREATE TABLE `user_love` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL ,
  `love_id` int(20) NOT NULL ,
  `type_id` tinyint NOT NULL ,
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/************************************************************
 * forum  论坛：主要有话题、话题回复     板块》话题》回复，讨论话题与goods的评论、theme的讨论分开设计，这就是一个独立的模块
 * topic表                话题
 * topic_group 表         话题组：即论坛的板块        暂不实现，都是统一的板块之下hiwowo:0，以后扩展的时候，在添加
 * topic_discuss 表         话题的回复，与话题是多对一的关系
 * topic_type 表          话题的类型，例如普通、问答、知识、经验、精华、文化等等，目前暂不写在数据库中，直接enum枚举在程序中实现

 **************************************************************/
 -- --------------------------------------------------------
/*
  -- 表的结构 `topic 讨论话题`
     id 表的ID
     uid           用户的id
     uname         用户的名称
     title         讨论的话题名称
     content       讨论的话题内容
     intro         简介
     pics          图片
     topic_type    话题的类型 0 普通 2、问答 3 知识 4、经验、5、文化故事
     is_best      是否精华
     is_top       是否置顶
     hot_index     排序指数
     check_state   审核状态
     add_time     添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `topic`;
CREATE TABLE IF NOT EXISTS `topic`(
   `id`                int(10) NOT NULL AUTO_INCREMENT,
  `uid`                int(10) NOT NULL ,
  `title`              varchar (128) NOT NULL ,
  `content`            text    NOT NULL ,
  `intro`             varchar(200)   NOT NULL ,
  `pics`              text ,
  `type_id`          tinyint NOT NULL DEFAULT '0',
  `is_best`            tinyint(1) NOT NULL DEFAULT '0',
  `is_top`            tinyint(1) NOT NULL DEFAULT '0',
  `discuss_num`           smallint(10) default '0',
  `view_num`           smallint(10)  default '1',
  `love_num`           smallint(10)  default '0',
  `check_state`          tinyint NOT NULL DEFAULT '0',
  `add_time`          timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 -- --------------------------------------------------------
/*
  -- 表的结构 `topic_discuss 话题讨论回复`
     id                  表的ID
     uid                  用户的id
     uname               用户的名称
     topic_id            话题名称
     quote_content          引用的内容
     content             回复的内容
     is_delete           是否删除
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `topic_discuss`;
CREATE TABLE IF NOT EXISTS `topic_discuss`(
    `id`                     int(10) NOT NULL AUTO_INCREMENT,
    `uid`                    int(10) NOT NULL ,
  `topic_id`                 int(10) NOT NULL ,
  `quote_content`             text,
  `content`                 text ,
   `check_state`          tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;










/************************************************************
 * shop ，主要有
  * shop 表                    小店
 * shop_discuss 表            小店讨论
 * shop_style   表          小店样式
 * goods 表                   宝贝
 * goods_discuss 表           宝贝讨论


 -- --------------------------------------------------------
/*
  -- 表的结构 `shop `
     id                 表的ID
     name              专题组名称
     intro             介绍
     is_visible       是否显示
     uid         作者id
     cid          分类  线上店铺 线下店铺
     tags              标签组      2012.11.04 新增，用于简化处理shop_tag,
     love_num        喜欢的人
     pic               主题封面
     seo_title       用户的名称
     seo_keywords     seo 关键词
     seo_desc          seo 描述
     modify_time      修改
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE IF NOT EXISTS `shop`(
  `id`                      int(10) NOT NULL AUTO_INCREMENT,
  `uid`                int(10) not null ,
  `cid`                tinyint default '0' ,
  `name`                    varchar(128) not null default '',
  `intro`                   varchar(255) ,
  `is_visible`              tinyint(1) not null default '0',
  `pic`                varchar(128) not null default '/images/shop/default.jpg',
  `tags`                   varchar(128),
  `love_num`              smallint(10) not null default '1' ,
  `discuss_num`              smallint(10) not null default '0' ,
  `goods_num`              smallint(10) not null default '0' ,
  `province`             varchar(20),
  `city`                 varchar(20) ,
  `town`                 varchar(20),
  `street`               varchar(50) ,
  `modify_time`             timestamp,
  `add_time`                timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `shop_discuss `
     id                 表的ID
     content            评论内容
     shop_id              专题ID
     uid                    评论人ID
     is_delete              是否删除
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `shop_discuss`;
CREATE TABLE IF NOT EXISTS `shop_discuss`(
  `id`                          int(10) NOT NULL AUTO_INCREMENT,
  `shop_id`                      int(10) NOT NULL ,
  `uid`                           int(10) NOT NULL ,
  `quote_content`             text,
  `content`                     text,
  `check_state`          tinyint NOT NULL DEFAULT '0',
  `add_time`                      timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------
/*
  -- 表的结构 `theme_style `
    <style>
body{
	background-color:#F9F9EF;
	 backgroup-image:url();
	background-repeat:no-repeat;
	background-position:center top;
	background-attachment:fixed;
}
.banner {
	height:70px;
	background-color:#fafafa;
	 background-image:url();
	 	background-repeat:repeat;
	background-position:center top;
	max-height:600px;
	min-heihgt:80px;
	overflow:hidden;
}
.banner .banner-title .subtitle {
	color : #666;
}
</style>
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `shop_style`;
CREATE TABLE IF NOT EXISTS `shop_style`(
  `id`                          int(10) NOT NULL AUTO_INCREMENT,
  `shop_id`                      int(10) NOT NULL ,
  `page_bg_color`                      varchar (16) NOT NULL default'#F9F9EF',
  `page_bg_image`                      varchar (128) NOT NULL default '',
  `page_bg_repeat`                      varchar (32) NOT NULL default 'no-repeat',
  `page_bg_position`                      varchar (32) NOT NULL default 'center top',
  `page_bg_attachment`                      varchar (16) NOT NULL  default 'scroll',
  `banner_height`                      varchar (16) NOT NULL  default '70',
  `banner_color`                      varchar (16) NOT NULL  default '#f666',
  `banner_bg_color`                      varchar (16) NOT NULL default '#fafafa',
  `banner_bg_image`                      varchar (128) NOT NULL  default '',
  `banner_bg_repeat`                      varchar (32) NOT NULL default 'no-repeat',
  `banner_bg_position`                      varchar (32) NOT NULL  default 'scroll',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  -- --------------------------------------------------------
/*
  -- 表的结构 `goods `
     id                 表的ID
     shop_id            店铺Id
	  num_iid             商品ID
	 track_iid            跟踪ID  商品ID 和 跟踪Id 至少有一个存在，如果track_iid 存在，则优先使用。
     name              商品名称
     intro              商品介绍
     desc               商品详情  主要为合作商户使用
     price              价格
     pic                 主图片
	 item_pics            所有图片
	 detail_url           商品的销售网址
     love_num           喜欢的人数
     `status`            商品状态、上架 、下架 、审核
     `is_member`            是否是会员专享
      modify_time        采集时间
      add_time            修改时间
--
*/
/*
*  每个用户都有个默认的shop，在发布时即可获取。但是考虑到淘宝同质化的产品很多，即同一个产品在不同店销售，去购买的shop很多，需要人工编辑，通过后台使用goods_shop 编辑。
*  当然了，这个人工处理量很大。
*/
-- ------------------------------------------------------------
   DROP TABLE IF EXISTS `goods`;
CREATE TABLE IF NOT EXISTS `goods`(
   `id`                   int(10) NOT NULL AUTO_INCREMENT,
  `shop_id`               int (10) not null,
   `num_iid`               bigint (20) not null,
  `name`                    varchar(128) not null  ,
  `intro`                    varchar(255) not null ,
  `content`                     text ,
  `price`                    varchar(16) not null ,
  `pic`                      varchar(128) not null ,
  `item_pics`                varchar(500),
  `detail_url`               varchar(128),
  `love_num`                 smallint(10) not null  DEFAULT '1',
  `status`                   tinyint  not null default '1'   ,
  `is_member`                  tinyint(1) not null default '0',
  `modify_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `add_time`                 timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




   -- --------------------------------------------------------
/*
  -- 表的结构 `goods_discuss `
     id                 表的ID
     gid               商品名称
     uid               用户的ID
     uname             用户的名称
     content           评论内容
     is_value          鉴定是否值得
     is_buy           是否购买过
     add_time         添加时间
--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `goods_discuss`;
CREATE TABLE IF NOT EXISTS `goods_discuss`(
   `id` int(10) NOT NULL AUTO_INCREMENT,
  `goods_id`                       int(10) not null,
  `uid`                          int(10) not null,
  `quote_content`             text,
  `content`                    text,
  `check_state`             tinyint  not null default '0',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






 /************************************************************
 * tag ，
 * tag  表                    标签
 * tag_group 表               标签组
 **************************************************************/
 -- --------------------------------------------------------
/*
  -- 表的结构 `hi tag `
     id                 表的ID
     name               标签名称
     intro              简介
     tag_group_id       标签组id
     add_num          添加次数  用户添加标签时，同样的标签记录一次
     is_top             是否置顶
     is_highlight        是否强调
     sort_num            排序
     check_state         审核状态
     seo_title          seo
     seo_keywords       seo 关键词
     seo_desc           seo 描述
     modify_time        修改
     add_time           添加时间


--
*/
-- ------------------------------------------------------------

DROP TABLE IF EXISTS `hi-tag`;
CREATE TABLE IF NOT EXISTS `hi-tag`(
  `id`                   int(10) NOT NULL AUTO_INCREMENT,
  `name`                       varchar(32) not null ,
   `add_num`                 smallint(10) not null default '1',
  `is_top`                 tinyint(1) not null default '0',
  `is_highlight`                 tinyint(1) not null default '0',
  `sort_num`                 smallint(10) not null default '0',
  `check_state`                 tinyint(4) not null default '0',
  `seo_title`                  varchar(128)  ,
  `seo_keywords`               varchar(128)  ,
  `seo_desc`                   varchar(255)  ,
  `modify_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





  /************************************************************
 * 站内信、短消息
 * msg 表               用户发的msg
 * msg_receiver 表      用户发的msg接受者
 **************************************************************/
   -- --------------------------------------------------------
/*
  -- 表的结构 `msg `
     id                 表的ID
     title              标题
     content           内容
     sender_id          发信者名称
     sender_name        发信者名称
     status        未读  已读 删除
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
      DROP TABLE IF EXISTS `at_msg`;
CREATE TABLE IF NOT EXISTS `at_msg`(
       `id` int(10) NOT NULL AUTO_INCREMENT,
         `sender_id`                int(10) not null default '0',
  `sender_name`                varchar(32) not null default 'hiwowo',
  `content`                varchar(255) not null default '',
  `receiver_id`                int(10) not null default '0',
  `receiver_name`             varchar(32) not null default '',
  `status`               tinyint not null default '1',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
   -- --------------------------------------------------------
/*
  -- 表的结构 `system_msg_receiver `
     id                 表的ID
     receiver_id          发信者名称
     receiver_name        发信者名称
     msg_id
     add_time           添加时间
--
*/
-- ------------------------------------------------------------

   DROP TABLE IF EXISTS `system_msg`;
CREATE TABLE IF NOT EXISTS `system_msg`(
       `id`                 int(10) NOT NULL AUTO_INCREMENT,
       `title`                varchar(32) not null default 'hiwowo',
       `content`                varchar(255) not null default '',
       `status`               tinyint not null default '1',
       `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `system_msg_receiver`;
CREATE TABLE IF NOT EXISTS `system_msg_receiver`(
        `id`                       int(10) NOT NULL AUTO_INCREMENT,
       `msg_id`                    int(10) not null default '1',
       `receiver_id`                int(10) not null default '0',
       `status`                tinyint not null default '0',
       `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



  DROP TABLE IF EXISTS `favor_msg`;
CREATE TABLE IF NOT EXISTS `favor_msg`(
       `id`                          int(10)          NOT NULL AUTO_INCREMENT,
        `lover_id`                    int(10) not null ,
       `lover_name`                varchar(32) not null ,
        `love_action`                varchar(32) not null ,
       `favor_type`               tinyint not null default '0',
       `third_id`               int(10) not null ,
        `content`                varchar(200) not null,
       `loved_id`               int(10) not null,
        `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



   /************************************************************
 * 管理员
 * admin  表             管理员
 * admin_role            管理员角色
 **************************************************************/
    /*
  数据表：user  用户（使用的人）*/
    DROP TABLE IF EXISTS `administrator`;
    create table administrator(
      `id`                 smallint (10) not null  auto_increment ,
      `email`             varchar(64) not null,
      `password`             varchar(64) not null,
      `name`          varchar(32) not null default '',
      `department`         varchar(16) not null default '',
      `phone`              varchar(30) not null default '',
      `login_time`         timestamp default '2012-5-12 14:18:00',
      `login_num`           smallint(10) NOT NULL default '1',
      `login_ip`            varchar(32) DEFAULT '0',
      `last_login_time`         timestamp default '2012-5-12 14:18:00',
      `add_time`            timestamp default '2012-5-12 14:18:00',
      `role_id`        smallint  not null default '1',
      `role_name`      varchar(32) not null default '',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    /*
     * 数据表  userRole   用户角色
    */
    DROP TABLE IF EXISTS `administrator_role`;
    create table administrator_role(
      id             int  unsigned primary  key  auto_increment ,
      name           varchar(32) not null,
      note           varchar(128) not null default '',
      modify_time     timestamp  ,
      add_time       timestamp default '2012-5-12 14:18:00'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;






/*广告组*/
DROP TABLE IF EXISTS `advert_position`;
create table advert_position(
  id              smallint (10) not null  auto_increment ,
  position        varchar(16)   not null,
  name            varchar (64)  not null,
  code            varchar(32)   not null,
  advert_type       tinyint default 0,
  add_time          timestamp default '2012-5-12 14:18:00',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*广告
* name  位置名称
* title 广告名称
*/
DROP TABLE IF EXISTS `advert`;
create table advert(
  id              smallint (10) not null  auto_increment ,
  position_code   varchar(32)  not null,
  third_id          int,
  name            varchar (64)  not null,
  title           varchar(128),
  content         text ,
  pic             varchar(128),
  spic           varchar(128),
  width           smallint(10) default '0',
  height          smallint(10) default '0',
  link            varchar(128),
note              varchar(200) default 'note',
click_num       smallint(10) default '1',
add_time          timestamp default '2012-5-12 14:18:00',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






/* ***************************************************** */

/*
 小站 site

 site              小站表 site （分为几类：生活  品牌站 其他）
 site_style        小站样式表
 site_blog          小镇日志
 site_blog_discuss          小镇日志
 site_pic           小站图片
 site_pic_discuss           小站图片
 site_video            小站视频
 site_video_discuss            小站视频


*/

/*
*  site  小镇
   创建者      uid
   名称  name
   站牌  logo
   简介  intro
   标签  tags
   状态  status 0 未审核  1 通过审核 2 优质
   通告 notice
   修改时间   modify_time
   创建时间   add_time
*/
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) ,
  `title`              varchar(64) not null,
  `pic`                 varchar(250) not null ,
  `intro`             varchar(250) ,
  `tags`             varchar(250) ,
  `status`            tinyint  not null default  '0',
  `notice`             varchar(250) ,
  `blog_num`           smallint(10) not null  DEFAULT '0',
  `pic_num`                 smallint(10) not null  DEFAULT '0',
  `video_num`                 smallint(10) not null  DEFAULT '0',
  `modify_time`         timestamp default '2013-07-18 12:00:00',
  `add_time`           timestamp default '2013-07-18 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `blog_discuss 帖子回复`
     id                  表的ID
     uid                  用户的id
     bid                  帖子
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `site_discuss`;
CREATE TABLE IF NOT EXISTS `site_discuss`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `site_id`                    int(10) NOT NULL ,
  `quote_content`             text,
  `content`                  text ,
  `status`            tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*
  site_pic         小站-图片
    sid               小站
    intro             图片说明
    pic
    is_top             置顶
    add_time           加入时间
 */
DROP TABLE IF EXISTS `site_pic`;
CREATE TABLE `site_pic` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `sid`                 int(10) ,
  `intro`               varchar(200) ,
  `pic`               varchar(250) ,
  `is_top`            tinyint  not null default  '0',
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


/*
  -- 表的结构 `site_pic_discuss 帖子回复`
     id                  表的ID
     uid                  用户的id
     bid                  帖子
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `site_pic_discuss`;
CREATE TABLE IF NOT EXISTS `site_pic_discuss`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `pic_id`                    int(10) NOT NULL ,
  `quote_content`             text,
  `content`                  text ,
  `status`            tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  site_video         视频
    sid                   小镇
    intro                 图片说明
    url                 视频网址
    is_top             置顶
    add_time           加入时间
 */
DROP TABLE IF EXISTS `site_video`;
CREATE TABLE `site_video` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `sid`                 int(10) ,
  `intro`               varchar(200) ,
  `url`               varchar(250) ,
  `is_top`            tinyint  not null default  '0',
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/*
  -- 表的结构 `site_pic_discuss 帖子回复`
     id                  表的ID
     uid                  用户的id
     bid                  帖子
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `site_video_discuss`;
CREATE TABLE IF NOT EXISTS `site_video_discuss`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `video_id`                    int(10) NOT NULL ,
  `quote_content`             text,
  `content`                  text ,
  `status`            tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*
*  日志 blog
   创建者      uid
   来源        comefrom
   所属小镇     sid
   分类   0 食谱  1 食材 2 其他
   名称  name
   主图  logo
   内容 content
   标签  tags
   状态  status 0 草稿  1 发布 2 优质
   置顶  isTop
   浏览次数 view_num
   原文链接（参考）   refer
   创建时间   add_time
*/
DROP TABLE IF EXISTS `site_blog`;
CREATE TABLE `site_blog` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) ,
  `sid`                 int(10) ,
  `come_from`          varchar(250) not null ,
  `title`              varchar(128) not null,
  `pic`                 varchar(250) not null ,
  `content`             text ,
  `tags`             varchar(200) ,
  `status`            tinyint  not null default  '1',
  `is_top`            tinyint  not null default  '0',
  `view_num`          int unsigned  not null default  '1',
  `love_num`          int unsigned  not null default  '1',
  `discuss_num`          int unsigned  not null default  '1',
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


-- --------------------------------------------------------
/*
  -- 表的结构 `blog_discuss 帖子回复`
     id                  表的ID
     uid                  用户的id
     bid                  帖子
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `site_blog_discuss`;
CREATE TABLE IF NOT EXISTS `site_blog_discuss`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `blog_id`                    int(10) NOT NULL ,
  `quote_content`             text,
  `content`                  text ,
  `status`            tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;







/************************************************************
* 站内信、短消息
* msg 表               用户发的msg
* msg_receiver 表      用户发的msg接受者
**************************************************************/
-- --------------------------------------------------------
/*
  -- 表的结构 `msg `
     id                 表的ID
     title              标题
     content           内容
     sender_id          发信者名称
     sender_name        发信者名称
     status        未读  已读 删除
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `at_msg`;
CREATE TABLE IF NOT EXISTS `at_msg`(
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `sender_id`                int(10) not null default '0',
  `sender_name`                varchar(32) not null default 'hiwowo',
  `content`                varchar(255) not null default '',
  `receiver_id`                int(10) not null default '0',
  `receiver_name`             varchar(32) not null default '',
  `status`               tinyint not null default '1',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- --------------------------------------------------------
/*
  -- 表的结构 `system_msg_receiver `
     id                 表的ID
     receiver_id          发信者名称
     receiver_name        发信者名称
     msg_id
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
   /*
     status  0 未审核  1 审核
   */
DROP TABLE IF EXISTS `system_msg`;
CREATE TABLE IF NOT EXISTS `system_msg`(
  `id`                 int(10) NOT NULL AUTO_INCREMENT,
  `title`                varchar(32) not null default 'hiwowo',
  `content`                varchar(255) not null default '',
  `status`               tinyint not null default '1',
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*status 0 未读  1已读 2 删除 */
DROP TABLE IF EXISTS `system_msg_receiver`;
CREATE TABLE IF NOT EXISTS `system_msg_receiver`(
  `id`                       int(10) NOT NULL AUTO_INCREMENT,
  `msg_id`                    int(10) not null default '1',
  `receiver_id`                int(10) not null default '0',
  `receiver_name`             varchar(32) not null default '',
  `status`                tinyint not null default '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `favor_msg`;
CREATE TABLE IF NOT EXISTS `favor_msg`(
  `id`                          int(10)          NOT NULL AUTO_INCREMENT,
  `lover_id`                    int(10) not null ,
  `lover_name`                varchar(32) not null ,
  `love_action`                varchar(32) not null ,
  `favor_type`               tinyint not null default '0',
  `third_id`               int(10) not null ,
  `content`                varchar(200) not null,
  `loved_id`               int(10) not null,
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
discuss type 0 帖子回复 1 宝贝回复 2 主题回复
*/
DROP TABLE IF EXISTS `discuss_msg`;
CREATE TABLE IF NOT EXISTS `discuss_msg`(
  `id`                          int(10)          NOT NULL AUTO_INCREMENT,
  `discusser_id`                    int(10) not null ,
  `discusser_name`                varchar(32) not null ,
  `discuss_action`                varchar(32) not null ,
  `discuss_type`               tinyint not null default '0',
  `third_id`               int(10) not null ,
  `content`                varchar(200) not null,
  `owner_id`               int(10) not null,
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*  pet  */
/* ***************************************************** */

/*
 小站 site

pet              小站表 site （分为几类：生活  品牌站 其他）
pet_pic           小站图片
pet_pic_discuss           小站图片
pet_video            小站视频
pet_video_discuss            小站视频


*/

/*
*  pet  小镇
   创建者      uid
   名称  name
   站牌  logo
   简介  intro
   标签  tags
   状态  status 0 未审核  1 通过审核 2 优质
   通告 notice
   修改时间   modify_time
   创建时间   add_time
*/
DROP TABLE IF EXISTS `pet`;
CREATE TABLE `pet` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) ,
  `title`              varchar(64) not null,
  `pic`                 varchar(250) not null ,
  `intro`             varchar(250) ,
  `tags`             varchar(250) ,
  `status`            tinyint  not null default  '0',
  `notice`             varchar(250) ,
  `modify_time`         timestamp default '2013-07-18 12:00:00',
  `add_time`           timestamp default '2013-07-18 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;



/*
  pet_pic        pet-图片
    pet id               小站
    intro             图片说明
    pic
    is_top             置顶
    add_time           加入时间
 */
DROP TABLE IF EXISTS `pet_pic`;
CREATE TABLE `pet_pic` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `pet_id`                 int(10) ,
  `intro`               varchar(200) ,
  `pic`               varchar(250) ,
  `is_top`            tinyint  not null default  '0',
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


/*
  -- 表的结构 `pet_pic_discuss 帖子回复`
     id                  表的ID
     uid                  用户的id
     bid                  帖子
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pet_pic_discuss`;
CREATE TABLE IF NOT EXISTS `pet_pic_discuss`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `pic_id`                    int(10) NOT NULL ,
  `quote_content`             text,
  `content`                  text ,
  `status`            tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  pet_video         视频
    sid                   小镇
    intro                 图片说明
    url                 视频网址
    is_top             置顶
    add_time           加入时间
 */
DROP TABLE IF EXISTS `pet_video`;
CREATE TABLE `pet_video` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `pet_id`                 int(10) ,
  `intro`               varchar(200) ,
  `url`               varchar(250) ,
  `is_top`            tinyint  not null default  '0',
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/*
  -- 表的结构 pet_video_discuss 帖子回复`
     id                  表的ID
     uid                  用户的id
     bid                  帖子
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `pet_video_discuss`;
CREATE TABLE IF NOT EXISTS `pet_video_discuss`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `video_id`                    int(10) NOT NULL ,
  `quote_content`             text,
  `content`                  text ,
  `status`            tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*
  -- 表的结构 cms 内容管理系统
     id                  表的ID
     aid                  发布者（跟administrator 一致）
     title                 标题
     content             回复的内容
     seo
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cms`;
CREATE TABLE IF NOT EXISTS `cms`(
  `id`                         int(10) NOT NULL AUTO_INCREMENT,
  `aid`                        int(10) NOT NULL ,
  `title`                      varchar(128)  ,
  `content`                    text ,
  `view_num`                   int(10) NOT NULL ,
  `seo_title`                  varchar(128)  ,
  `seo_keywords`               varchar(128)  ,
  `seo_desc`                   varchar(255)  ,
  `modify_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `status`            tinyint NOT NULL DEFAULT '1',
  `add_time`               timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


