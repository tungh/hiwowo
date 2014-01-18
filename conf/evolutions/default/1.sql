# --- First database schema
# --- First database schema

# --- !Ups

/************************************************************
 * 用户是个大的结构  主要有以下几个表
 * user表                User常用的基本信息，包括常用的统计信息
 * user_profile 表        User不常用的信息、例如生日、性别、所在位置等等
 * user_stats             用户统计信息
 * user_tag 表            用户的标签
 * user_follow 表         用户关注的人
 * user_love_site 表     用户喜欢的小站
 * user_love_blog 表     用户喜欢的博客
 * user_love_pic 表      用户喜欢的图片
 * user_love_video 表    用户喜欢的视频
 * user_love_shop 表     用户喜欢的店铺
 * user_invite_prize 表    用户
 * user_record     表    用户动态，类似于用户的操作记录，记录用户的动作

 **************************************************************/
 /*数据表  User  用户的基本信息和常用信息
     id         用户ID
     name       用户昵称
     password   用户密码
     email      用户邮箱
     credit    用户积分
     intro      简介
     pic       用户头像
     status      用户状态 例如正常状态、拉黑状态、活跃用户等    0:邮箱未验证 1：正常用户 2 拉黑 3 活跃
     title      头衔
     come_from   用户的来源：0 食美特 1 qq 2 新浪微博 3 淘宝 4 微信
     open_id     第三方账号ID
     province    所在省份
     tags        标签，以,隔开


  */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `name`                varchar(64) NOT NULL ,
  `passwd`              varchar(64) NOT NULL default '0',
  `email`               varchar(128),
  `credits`              smallint(10) not null default '0',
  `pic`                 varchar(255) NOT NULL default '/images/user/default.jpg',
  `daren`               tinyint not null default '0',
  `status`                tinyint    not null default '0',
  `come_from`              tinyint    not null default '0',
  `open_id`               varchar(64),
  `province`            varchar(20),
  `tags`                    varchar(250) ,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


  /*数据表  User  用户的基本信息和常用信息
     id               表D
     uid             用户ID
     invite_uid      邀请者ID
     regist_time     注册时间
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
  `invite_name`         varchar(50),
  `gender`              tinyint(4) not null DEFAULT '2',
  `intro`               varchar(200),
  `birth`                varchar(16) ,
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

  -- 表的结构 `user_stats`
     id                               表的ID
     uid                              用户ID
--
*/

DROP TABLE IF EXISTS `user_stats`;
CREATE TABLE `user_stats` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) NOT NULL ,
  `fans_num`                  smallint(11) DEFAULT '0',
  `follow_num`                smallint(11) DEFAULT '0',
  `record_num`                 smallint(10) DEFAULT '0',
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
  `action_url`         varchar(128) NOT NULL DEFAULT 'http://smeite.com',
  `action_content`     varchar(128) NOT NULL DEFAULT 'smeite',
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
  `credits`             smallint (10) ,
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
     site_id 小镇的Id
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user_love_site`;
CREATE TABLE `user_love_site` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL DEFAULT '0',
  `site_id` int(20) NOT NULL DEFAULT '0',
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_baobei`
     id 表的ID
     uid 用户的id
     blog_id 商品的Id
     add_time  添加时间
--
*/

-- --------------------------------------------------------
 DROP TABLE IF EXISTS `user_love_blog`;
CREATE TABLE `user_love_blog` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL ,
  `blog_id` int(10) NOT NULL ,
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_pic`
     id 表的ID
     uid 用户的id
     pic_id 图片的Id
     add_time  添加时间
--
*/

-- --------------------------------------------------------

 DROP TABLE IF EXISTS `user_love_pic`;
CREATE TABLE `user_love_pic` (
  `id`   int(10) NOT NULL AUTO_INCREMENT,
  `uid`   int(10) not null,
  `pic_id` int(10) NOT NULL,
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_video`
     id 表的ID
     uid 用户的id
     video_id 的Id
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `user_love_video`;
CREATE TABLE `user_love_video` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL DEFAULT '0',
  `video_id` int(20) NOT NULL DEFAULT '0',
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_topic`
     id 表的ID
     uid 用户的id
     topic_id 话题的Id
     topic_name 话题的名称
     add_time  添加时间
--
*/

-- --------------------------------------------------------
 DROP TABLE IF EXISTS `user_love_topic`;
CREATE TABLE `user_love_topic` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `uid` int(10) NOT NULL ,
  `topic_id` int(10) NOT NULL ,
  `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------
/*
  -- 表的结构 `user_love_shop`
     id 表的ID
     uid 用户的id
     shop_id 商品的Id
     add_time  添加时间
--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `user_love_shop`;
 CREATE TABLE `user_love_shop` (
   `id` int(10) NOT NULL AUTO_INCREMENT,
   `uid` int(10) NOT NULL DEFAULT '0',
   `shop_id` int(20) NOT NULL DEFAULT '0',
   `add_time` timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/************************************************************
 * forum  论坛：主要有话题、话题回复     板块》话题》回复，讨论话题与goods的评论、theme的讨论分开设计，这就是一个独立的模块
 * topic表                话题
 * topic_group 表         话题组：即论坛的板块        暂不实现，都是统一的板块之下食美特:0，以后扩展的时候，在添加
 * topic_reply 表         话题的回复，与话题是多对一的关系
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
  `uname`              varchar (32) NOT NULL ,
  `title`              varchar (128) NOT NULL ,
  `content`            text    NOT NULL ,
  `intro`             varchar(200)   NOT NULL ,
  `pics`              text ,
  `type_id`          tinyint NOT NULL DEFAULT '0',
  `is_best`            tinyint(1) NOT NULL DEFAULT '0',
  `is_top`            tinyint(1) NOT NULL DEFAULT '0',
  `hot_index`           smallint(10)  default '0',
  `reply_num`           smallint(10) default '0',
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
   `uname`                   varchar (32) NOT NULL ,
  `topic_id`                 int(10) NOT NULL ,
  `quote_content`             text,
  `content`                 text ,
   `check_state`          tinyint NOT NULL DEFAULT '0',
  `add_time`               timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;










/************************************************************
 * site ，主要有
 * goods 表                   宝贝的常用的、基本信息
 * goods_profile 表           宝贝的详细信息
 * goods_assess 表            宝贝的评价、鉴定等
 * goods_tag 表               宝贝的标签
 *   shop   表                宝贝店铺
  * goods_shop 表              可以购买的宝贝店铺，一个宝贝可以有从多个店铺购买

 **************************************************************/
  -- --------------------------------------------------------
/*
  -- 表的结构 `goods `
     id                 表的ID
	 num_iid             商品ID
	 track_iid            跟踪ID  商品ID 和 跟踪Id 至少有一个存在，如果track_iid 存在，则优先使用。
     name              商品名称
     intro              商品介绍
     desc               商品详情  主要为合作商户使用
     price              价格
     pic                 主图片
	 item_pics            所有图片
	 nick                  卖家昵称
	 food_security         生产许可号|产品标准号|配料表|储藏方法|保质期|食品添加剂|供货商|生产日期
	 detail_url           商品的销售网址
     love_num           喜欢的人数
	 volume            最近30天销售额
     `status`            商品状态、上架 、下架 、审核
     `is_member`            是否是会员专享
     `hot_index`            热门指数
      collect_time        采集时间
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
   `num_iid`               bigint (20) not null,
   `track_iid`             varchar(32),
  `name`                    varchar(128) not null  ,
  `intro`                    varchar(255) not null ,
  `desc`                     text ,
  `price`                    varchar(16) not null ,
  `pic`                      varchar(128) not null ,
  `item_pics`                varchar(500),
  `nick`                     varchar(32),
  `food_security`            varchar(500),
  `detail_url`               varchar(128),
  `love_num`                 smallint(10) not null  DEFAULT '1',
   `volume`                  smallint(10) default '0',
  `status`                   tinyint  not null default '1'   ,
  `is_member`                  tinyint(1) not null default '0',
  `hot_index`                   smallint(10) not null default '0',
  `collect_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  `add_time`                 timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
  -- 表的结构 `shop `
   `id`
   nick	        	卖家昵称
   title	     	店铺标题
   item_score	 String	 否	60	商品描述评分
   service_score	 String	 否	100	服务态度评分
   delivery_score	 String	 否	90	发货速度评分
   created	         Date	 	   开店时间。格式：yyyy-MM-dd HH:mm:ss
   credits            卖家信用
   grade              店铺级别
   note               备注
--
*/
-- ------------------------------------------------------------
 DROP TABLE IF EXISTS `shop`;
CREATE TABLE IF NOT EXISTS `shop`(
      `id`                  int(10) NOT NULL AUTO_INCREMENT,
      `nick`                 varchar(32) not null ,
      `title`               varchar(128) ,
     `detail_url`               varchar(128) ,
      `item_score`           varchar(10)  not null default '0',
      `service_score`        varchar(10)  not null default '0',
      `delivery_score`       varchar(10)  not null default '0',
      `created`             varchar(32) ,
      `credits`             smallint(10),
     `grade`              varchar(10),
      `status`              tinyint default '0',
      `note`               varchar (250),
  `collect_time`             timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


   -- --------------------------------------------------------
/*
  -- 表的结构 `goods_assess `
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
 DROP TABLE IF EXISTS `goods_assess`;
CREATE TABLE IF NOT EXISTS `goods_assess`(
   `id` int(10) NOT NULL AUTO_INCREMENT,
  `goods_id`                       int(10) not null,
  `uid`                          int(10) not null,
  `uname`                       varchar (32) not null,
  `content`                       varchar (255) not null,
  `is_worth`                      tinyint(1) not null default '0',
  `is_bought`                      tinyint(1) not null default '0',
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
  -- 表的结构 `tag `
     id                 表的ID
     name               标签名称
     intro              简介
     tag_group_id       标签组id
     tag_group_name     标签组名称
     hot_index          推荐指数
     goods_num          产品的数量
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

DROP TABLE IF EXISTS `tag`;
CREATE TABLE IF NOT EXISTS `tag`(
  `id`                   int(10) NOT NULL AUTO_INCREMENT,
  `name`                       varchar(32) not null ,
  `cid`                   tinyint (4) ,
  `group_id`                   int(10) ,
  `group_name`                 varchar(32),
   `hot_index`                 smallint(10) not null default '1',
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


 -- --------------------------------------------------------

/*  -- 表的结构 `tag_group `
     id                 表的ID
     name               标签名称
     category_id         theme 分类 id
     category_name         theme 分类名称
     hot_index         推荐指数 与排序有关
     intro
     is_visible
      seo_title          用户的名称
     seo_keywords       seo 关键词
     seo_desc           seo 描述
     modify_time        修改
     add_time           添加时间
     11月3日
     tag_group 需要跟theme_group进行关联,一个theme_group 对应多个tag_group
--*/
-- ------------------------------------------------------------
  DROP TABLE IF EXISTS `tag_group`;
CREATE TABLE IF NOT EXISTS `tag_group`(
       `id`                     int(10) NOT NULL AUTO_INCREMENT,
      `name`                    varchar(32) not null default '',
      `pic`                     varchar(128) not null default '/assets/img/ui/tag.jpg',
      `intro`                   varchar(128) not null default '',
      `cid`                     smallint(10) NOT NULL,
      `hot_index`               smallint(10) NOT NULL default '0',
     `is_visible`               tinyint(1) not null default '1',
  `sort_num`                 smallint(10) not null default '0',
    `seo_title`                 varchar(128) not null default '' ,
    `seo_keywords`              varchar(128) not null default '' ,
    `seo_desc`                  varchar(255) not null default '' ,
    `modify_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
    `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 -- --------------------------------------------------------
/*
  -- 表的结构 `tag_goods `
     id                 表的ID
     tag_name           标签名称
     check_state        审核状态  未审核、审核通过 审核未通过
     add_time           添加时间
--
*/
-- ------------------------------------------------------------
    DROP TABLE IF EXISTS `tag_goods`;
CREATE TABLE IF NOT EXISTS `tag_goods`(
       `id`                 int(10) NOT NULL AUTO_INCREMENT,
  `tag_name`                varchar(32) not null ,
  `goods_id`                int (10)  not null ,
  `add_num`                 int(10) not null default '1',
  `check_state`             tinyint  not null default '0',
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
  `sender_name`                varchar(32) not null default 'smeite',
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
       `title`                varchar(32) not null default 'smeite',
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

   /*
   reply type 0 帖子回复 1 宝贝回复 2 主题回复
   */
 DROP TABLE IF EXISTS `reply_msg`;
CREATE TABLE IF NOT EXISTS `reply_msg`(
       `id`                          int(10)          NOT NULL AUTO_INCREMENT,
        `replier_id`                    int(10) not null ,
       `replier_name`                varchar(32) not null ,
        `reply_action`                varchar(32) not null ,
       `reply_type`               tinyint not null default '0',
       `third_id`               int(10) not null ,
        `content`                varchar(200) not null,
       `owner_id`               int(10) not null,
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
    DROP TABLE IF EXISTS `manager`;
    create table manager(
      `id`                 smallint (10) not null  auto_increment ,
      `email`             varchar(64) not null,
      `passwd`             varchar(64) not null,
      `name`          varchar(32) not null default '',
      `department`         varchar(16) not null default '',
      `phone`              varchar(30) not null default '',
      `login_time`         timestamp default '2012-5-12 14:18:00',
      `login_num`           smallint(10) NOT NULL default '1',
      `login_ip`            varchar(32) DEFAULT '0',
      `add_time`            timestamp default '2012-5-12 14:18:00',
      `role_id`        smallint  not null default '1',
      `role_name`      varchar(32) not null default '',
        PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    /*
     * 数据表  userRole   用户角色
    */
    DROP TABLE IF EXISTS `manager_role`;
    create table manager_role(
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
  `title`                  varchar(64) not null,
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
  site_pic         相册-图片
    sid               小镇
    intro             图片说明
    pic
    is_top             置顶
    add_time           加入时间
 */
DROP TABLE IF EXISTS `site_album_pic`;
CREATE TABLE `site_album_pic` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `sid`                 int(10) ,
  `intro`               varchar(200) ,
  `pic`               varchar(250) ,
  `is_top`            tinyint  not null default  '0',
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
*  日志 blog
   创建者      uid
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
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id`                  int(10) NOT NULL  AUTO_INCREMENT ,
  `uid`                 int(10) ,
  `sid`                 int(10) ,
  `cid`                tinyint  not null default  '0',
  `title`              varchar(64) not null,
  `pic`                 varchar(250) not null ,
  `content`             text ,
  `tags`             varchar(200) ,
  `status`            tinyint  not null default  '1',
  `is_top`            tinyint  not null default  '0',
  `view_num`          int unsigned  not null default  '1',
  `love_num`          int unsigned  not null default  '1',
  `reply_num`          int unsigned  not null default  '1',
  `extra_attr1`             varchar(200) ,
  `extra_attr2`             varchar(200) ,
  `extra_attr3`             varchar(200) ,
  `extra_attr4`             varchar(200) ,
  `extra_attr5`             varchar(200) ,
  `extra_attr6`             varchar(200) ,
  `add_time`           timestamp,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


-- --------------------------------------------------------
/*
  -- 表的结构 `post_reply 帖子回复`
     id                  表的ID
     uid                  用户的id
     pid                  帖子
     reply_type          0 随意吐槽 1 提问求解  2 上传成果
     quote_content          引用的内容
     content             回复的内容
     check_state        审核状态
     add_time           添加时间

--
*/
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `post_reply`;
CREATE TABLE IF NOT EXISTS `post_reply`(
  `id`                     int(10) NOT NULL AUTO_INCREMENT,
  `uid`                    int(10) NOT NULL ,
  `pid`                    int(10) NOT NULL ,
  `cid`             tinyint NOT NULL DEFAULT '0',
  `quote_content`             text,
  `content`                  text ,
  `status`          tinyint NOT NULL DEFAULT '0',
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
  `sender_name`                varchar(32) not null default 'smeite',
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
  `title`                varchar(32) not null default 'smeite',
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
reply type 0 帖子回复 1 宝贝回复 2 主题回复
*/
DROP TABLE IF EXISTS `reply_msg`;
CREATE TABLE IF NOT EXISTS `reply_msg`(
  `id`                          int(10)          NOT NULL AUTO_INCREMENT,
  `replier_id`                    int(10) not null ,
  `replier_name`                varchar(32) not null ,
  `reply_action`                varchar(32) not null ,
  `reply_type`               tinyint not null default '0',
  `third_id`               int(10) not null ,
  `content`                varchar(200) not null,
  `owner_id`               int(10) not null,
  `add_time`                timestamp NOT NULL DEFAULT '2012-10-1 12:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




