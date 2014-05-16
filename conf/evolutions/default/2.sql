 set character_set_client=utf8;
 set character_set_results=utf8;
 set character_set_connection=utf8;

 insert into topic(uid,title,content,pics,intro,check_state) value(1,"意见反馈","todo"," ","todo",1);

 /* 初始化标签组 */
 insert into `group`(id,name,intro,status) value(1,"萌","关于萌的关键词集合，比如萌宠，萌主 萌等",1);
 insert into `group`(id,name,intro,status) value(2,"笑","关于笑的关键词集合，比如搞笑，爆笑",1);
 insert into `group`(id,name,intro,status) value(3,"主人","关于主人的关键词集合，比如二货主人，有爱主人",1);
 insert into `group`(id,name,intro,status) value(4,"笑","关于笑的关键词集合，比如搞笑，爆笑",1);
 insert into `group`(id,name,intro,status) value(5,"喵星人","关于猫猫的关键词集合，比如喵星人 猫猫",1);
 insert into `group`(id,name,intro,status) value(6,"汪星人","关于狗狗网的关键词集合，比如狗狗，哈士奇",1);
 insert into `group`(id,name,intro,status) value(7,"神吐槽","关于配词的关键词集合，比如神吐槽，神自白",1);
 /* 初始化核心标签 */
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(1,"萌宠",3,"萌宠大集合",1,"m",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(2,"爆笑",3,"爆笑大集合",1,"b",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(3,"神吐槽",3,"神吐槽大集合",1,"s",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(4,"二货主人",3,"二货主人大集合",1,"e",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(5,"喵星人",3,"喵星人大集合",1,"m",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(6,"汪星人",3,"汪星人大集合",1,"w",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(7,"吱星人",3,"吱星人大集合",1,"z",1);
 insert into label(id,name,level,intro,is_hot,spell,check_state) value(8,"鸟星人",3,"鸟星人大集合",1,"n",1);







