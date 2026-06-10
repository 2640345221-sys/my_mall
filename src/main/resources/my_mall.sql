/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : my_mall

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 31/05/2026 13:33:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '管理员登陆名称',
  `password` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '管理员登陆密码',
  `nick_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '管理员显示昵称',
  `locked` tinyint NULL DEFAULT 0 COMMENT '是否锁定 0未锁定 1已锁定无法登陆',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '十三', 0);
INSERT INTO `admin` VALUES (2, 'newbee-admin1', 'e10adc3949ba59abbe56e057f20f883e', '新蜂01', 0);
INSERT INTO `admin` VALUES (3, 'newbee-admin2', 'e10adc3949ba59abbe56e057f20f883e', '新蜂02', 0);

-- ----------------------------
-- Table structure for carousel
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '首页轮播图主键id',
  `url` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '轮播图',
  `redirect_url` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '\'##\'' COMMENT '点击后的跳转地址(默认不跳转)',
  `rank` int NOT NULL DEFAULT 0 COMMENT '排序值(字段越大越靠前)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` int NOT NULL DEFAULT 0 COMMENT '创建者id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user` int NOT NULL DEFAULT 0 COMMENT '修改者id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of carousel
-- ----------------------------
INSERT INTO `carousel` VALUES (1, 'https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner2.jpg', '##', 200, '2019-08-23 17:50:45', 0, '2019-11-10 00:23:01', 0);
INSERT INTO `carousel` VALUES (2, 'https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner1.png', 'https://juejin.im/book/5da2f9d4f265da5b81794d48/section/5da2f9d6f265da5b794f2189', 13, '2019-11-29 00:00:00', 0, '2019-11-29 00:00:00', 0);
INSERT INTO `carousel` VALUES (3, 'https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner3.jpg', '##', 0, '2019-09-18 18:26:38', 0, '2019-11-10 00:23:01', 0);
INSERT INTO `carousel` VALUES (5, 'https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner2.png', 'https://juejin.im/book/5da2f9d4f265da5b81794d48/section/5da2f9d6f265da5b794f2189', 0, '2019-11-29 00:00:00', 0, '2019-11-29 00:00:00', 0);
INSERT INTO `carousel` VALUES (6, 'https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner1.png', '##', 101, '2019-09-19 23:37:40', 0, '2019-11-07 00:15:52', 0);
INSERT INTO `carousel` VALUES (7, 'https://newbee-mall.oss-cn-beijing.aliyuncs.com/images/banner2.png', '##', 99, '2019-09-19 23:37:58', 0, '2019-10-22 00:15:01', 0);

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品表主键id',
  `name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '商品名',
  `intro` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '商品简介',
  `category_id` bigint NOT NULL DEFAULT 0 COMMENT '关联分类id',
  `cover_img` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '/admin/dist/img/no-img.png' COMMENT '商品主图',
  `detail_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商品详情',
  `original_price` int NOT NULL DEFAULT 1 COMMENT '商品价格',
  `selling_price` int NOT NULL DEFAULT 1 COMMENT '商品实际售价',
  `stock_num` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '商品库存数量',
  `sell_status` tinyint NOT NULL DEFAULT 0 COMMENT '商品上架状态 1-下架 0-上架',
  `create_user` int NOT NULL DEFAULT 0 COMMENT '添加者主键id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '商品添加时间',
  `update_user` int NOT NULL DEFAULT 0 COMMENT '修改者主键id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '商品修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10963 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (10003, '迪奥小姐花漾淡香水', '40ml', 105, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', '商品介绍加载中...', 103000, 100000, 389, 0, 0, '2019-09-18 13:18:47', 1, '2026-05-07 09:37:53');
INSERT INTO `goods` VALUES (10005, '419美的（Midea）电煮锅', '多功能锅 1.7L', 20, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/cc49632b-5fb5-4b0b-bb36-3c1599a48543..jpg', '美的（Midea）电煮锅 电热锅 小电锅 宿舍小锅 学生寝室一体泡面小火锅多功能锅 1.7L 电煮锅小型1-2人 XZE1612', 5900, 4700, 998, 0, 0, '2019-09-18 13:18:47', 1, '2026-05-06 12:20:01');
INSERT INTO `goods` VALUES (10907, '双飞燕键盘', '键盘', 82, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/7edf4b07-2b99-4c7e-86d6-7c79a35ff5a9..png', '双飞燕（A4TECH）X7-G800V 128键QQ炫舞游戏专业键盘有线USB劲舞团打P吃鸡宏编程', 16800, 14900, 200, 0, 1, '2026-05-06 11:46:35', 1, '2026-05-06 11:46:35');
INSERT INTO `goods` VALUES (10908, '志高（CHIGO）加厚电热锅', '多功能炒煮一体锅家用电炒锅电蒸锅电火锅', 21, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/6b891a64-2dd7-4c84-8e0f-2869df73d386..jpg', '志高（CHIGO）加厚电热锅电锅多功能炒煮一体锅家用电炒锅电蒸锅电火锅电煮锅多用途 34CM一蒸笼', 15500, 15200, 300, 0, 1, '2026-05-06 12:21:18', 1, '2026-05-06 12:21:18');
INSERT INTO `goods` VALUES (10909, '米家【新品来袭】 扫地机器人6 ', '水箱版', 22, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/308677b8-659d-4f2a-bb21-d9ee8ff0287f..jpg', '米家【新品来袭】 扫地机器人6 水箱版 滚筒活水清洁去渍洗地机 扫拖一体全自动清洗基站 扫地机拖地', 240000, 223200, 1000, 0, 1, '2026-05-06 12:23:32', 1, '2026-05-06 12:23:32');
INSERT INTO `goods` VALUES (10910, '小米（MI）米家有线吸尘器', '家用有线手持大吸力吸尘机', 23, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/f5f2a957-fe72-40ce-977e-d05fb577e53e..jpg', '小米（MI）米家有线吸尘器 家用有线手持大吸力吸尘机吸猫狗毛清洁机 米家有线吸尘器', 18500, 15500, 7000, 0, 1, '2026-05-06 12:24:58', 1, '2026-05-06 12:25:11');
INSERT INTO `goods` VALUES (10911, '奥克斯（AUX）取暖器', '塔式石墨烯暖风机', 24, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/1909f25c-79a4-4f23-93ed-584d3899ca62..jpg', '奥克斯（AUX）取暖器/电暖器/电暖气家用/取暖电器/电暖气暖风机电暖风热风机电热扇塔式石墨烯暖风机NSBE-200GS', 8900, 8000, 700, 0, 1, '2026-05-06 12:26:51', 1, '2026-05-06 12:26:51');
INSERT INTO `goods` VALUES (10912, '华为 Mate 70 Pro 旗舰手机', '搭载麒麟9100芯片，卫星通信，商务旗舰首选', 46, '/admin/dist/img/no-img.png', '华为2025年度旗舰机型，搭载自研麒麟9100处理器，性能强劲功耗低。6.8英寸OLED曲面屏，支持卫星通信和北斗卫星消息。后置5000万像素可变光圈主摄，拍照效果出色。适合商务人士和追求旗舰体验的用户，送长辈既有面子又实用。', 799900, 699900, 43, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10913, '华为 nova 14 青春版', '前置6000万自拍镜头，轻薄高颜值，学生党首选', 46, '/admin/dist/img/no-img.png', '专为年轻用户打造的高颜值手机，机身仅重168g厚度6.8mm，单手操作无压力。前置6000万像素自拍镜头，AI美颜算法，拍照自然不假面。256GB大存储，满足日常拍照和追剧需求。适合学生党和年轻白领。', 329900, 279900, 80, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10914, '华为畅享 80 老年智能机', '大字大屏大电池，简易模式一键SOS，送爸妈首选', 46, '/admin/dist/img/no-img.png', '专为老年人设计的智能手机，6.75英寸大屏搭配全局大字模式，看消息不费力。6000mAh超大电池，待机可达7天。侧面实体SOS按键一键求助，远程协助功能让子女随时帮忙。送父母的贴心之选。', 159900, 129900, 100, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10915, 'iPhone 17 Pro Max 沙漠金', 'A19 Pro芯片，钛金属机身，影像巅峰之作', 47, '/admin/dist/img/no-img.png', '苹果2025年度机皇，A19 Pro芯片性能领先全行业。钛金属机身轻巧坚固，沙漠金配色低调奢华。后置三摄支持8K视频录制，ProRAW格式适合摄影爱好者。适合追求极致体验的用户，送给女朋友彰显品味。', 1099900, 999900, 30, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10916, 'iPhone SE 4 午夜色', 'A17芯片小钢炮，小屏旗舰性价比之选', 47, '/admin/dist/img/no-img.png', '苹果最具性价比的机型，搭载A17芯片性能不妥协。5.4英寸小屏设计单手操控自如，Touch ID指纹解锁安全便捷。适合喜欢小屏的用户和预算有限的学生党，入门苹果生态的最佳选择。', 399900, 349900, 60, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10917, '小米 16 Ultra 徕卡影像', '徕卡光学镜头，骁龙8Gen5，摄影师的随身相机', 51, '/admin/dist/img/no-img.png', '小米与徕卡联合打造的影像旗舰，一英寸大底传感器配合徕卡光学镜头，夜景和街拍效果惊艳。骁龙8Gen5处理器性能拉满，120W快充19分钟充满。适合喜欢手机摄影的年轻用户。', 599900, 549900, 44, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10918, 'Redmi Note 14 Pro 学生入门机', '5100mAh大电池，1亿像素，学生党性价比神机', 52, '/admin/dist/img/no-img.png', 'Redmi年度走量神机，1亿像素主摄拍照清晰，5100mAh超大电池续航两天无压力。天玑8300处理器日常使用流畅不卡顿。价格亲民配置良心，学生党入门首选，性价比之王。', 179900, 149900, 120, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10919, 'OPPO Reno 14 自拍神机', '前置柔光双摄，AI美颜，拍出自然好气色', 56, '/admin/dist/img/no-img.png', 'OPPO专为自拍打造的Reno系列，前置柔光环配合AI美颜算法，暗光自拍也自然透亮。轻薄机身搭配渐变配色，颜值在线。适合爱自拍的女性用户，送给女朋友记录美好生活。', 299900, 259900, 70, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10920, 'vivo X210 游戏电竞手机', '骁龙8Gen5+独显芯片，144Hz电竞屏，游戏党利器', 57, '/admin/dist/img/no-img.png', 'vivo与高通联合调校的电竞手机，骁龙8Gen5搭配独立显示芯片，120帧高画质运行游戏无压力。144Hz电竞屏触控采样率高达720Hz，操作跟手零延迟。液冷散热系统持久游戏不降频。适合重度手游玩家。', 449900, 399900, 55, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10921, '荣耀 Magic 7 均衡旗舰', '骁龙8Gen5，5000mAh青海湖电池，全面均衡无短板', 45, '/admin/dist/img/no-img.png', '荣耀Magic系列主打全面均衡体验，骁龙8Gen5性能充足，5000mAh青海湖电池续航出色。后置5000万三摄系统覆盖全焦段，信号增强芯片保障网络稳定。适合追求稳定可靠体验的上班族。', 499900, 449900, 40, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10922, '一加 14 极客性能旗舰', '2K东方屏，24GB运存，原生安卓体验流畅不将就', 54, '/admin/dist/img/no-img.png', '一加年度旗舰，2K分辨率东方屏显示效果行业顶尖。24GB超大运存加1TB存储，多任务切换丝滑流畅。原生安卓风格系统无广告，极客和程序员的最爱。适合追求纯粹安卓体验的技术爱好者。', 549900, 499900, 35, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10923, 'Redmi 13C 百元老年智能机', '大音量扬声器，极简桌面，远程协助，送长辈超值', 52, '/admin/dist/img/no-img.png', '百元价位最值得入手的老年智能机。极简桌面图标大字醒目，来电语音播报防止漏接。大音量扬声器通话清晰，支持远程协助功能子女随时帮操作。5000mAh电池续航一周。送父母送长辈的经济实惠之选。', 69900, 59900, 150, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10924, '迪奥魅惑唇膏 999 正红色', '经典正红，显白不挑皮，气场女王必备色号', 86, '/admin/dist/img/no-img.png', '迪奥经典999号正红色，被称为女王红。丝绒质地顺滑不拔干，薄涂日常元气厚涂气场全开。不挑肤色黄皮白皮都显白。礼盒包装精美适合送给女朋友闺蜜或者犒劳自己。约会通勤聚会百搭色号。', 39900, 35900, 80, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10925, 'MAC子弹头唇膏 316 蜜桃豆沙', '温柔豆沙色，日常通勤裸妆必备，素颜也能驾驭', 86, '/admin/dist/img/no-img.png', 'MAC经典子弹头系列316号蜜桃豆沙色，日常通勤万用色号。哑光质地但不拔干，持久度高喝水不沾杯。颜色温柔不张扬，素颜也能涂出门。适合学生党和上班族日常使用，百搭任何妆容。', 22900, 18900, 100, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10926, '完美日记小细跟唇膏 L04 橘调番茄', '奶油橘调元气满满，学生党平价好物，显白一绝', 86, '/admin/dist/img/no-img.png', '完美日记爆款小细跟系列，L04橘调番茄红元气十足。奶油质地顺滑好涂抹，薄涂提气色厚涂显白。细管设计方便补妆携带。价格亲民国货之光，学生党入手无压力，日常使用不心疼。', 9900, 7900, 150, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10927, '祖玛珑蓝风铃淡香水 30ml', '清新铃兰花香，少女感十足，送女友首选礼物', 105, '/admin/dist/img/no-img.png', '祖玛珑最经典的蓝风铃香水，前调蓝风铃清新透亮，中调柿子带来微甜果香，后调白麝香温柔收尾。整体轻盈少女感十足，不浓烈不招摇，适合日常通勤和约会使用。包装精致送礼佳品，送女友送闺蜜几乎零差评。', 69900, 59900, 58, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10928, '香奈儿五号浓香水 50ml', '经典馥郁花香，成熟优雅气质女神专属', 105, '/admin/dist/img/no-img.png', '香奈儿五号百年经典香水，茉莉玫瑰依兰依兰的馥郁花香调，满满的成熟优雅女人味。留香持久一整天，适合重要场合和晚宴使用。送给妈妈或成熟女性长辈，体面又有心意。', 129900, 119900, 29, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10929, '春雨蜂蜜补水面膜 10片装', '天然蜂蜜精华，深层补水保湿，干皮救星', 106, '/admin/dist/img/no-img.png', '韩国春雨经典蜂蜜面膜，天然蜂蜜精华搭配玻尿酸，深度补水锁水效果显著。蚕丝面膜纸轻薄服帖，敷完皮肤水润透亮。适合干皮和换季敏感肌，学生党平价护肤必备。一周用两三次，皮肤水嫩一整天。', 8900, 6900, 200, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10930, 'SK-II 前男友面膜 6片装', 'PITERA精华密集修护，重要场合前一晚急救神器', 106, '/admin/dist/img/no-img.png', 'SK-II明星产品前男友面膜，富含PITERA酵母精华，一片含有一瓶神仙水的精华量。敷15分钟皮肤立刻通透水润毛孔细腻肤色均匀。重要场合前一晚敷一片隔天皮肤状态满分。适合有重要约会或面试时急救使用。', 89900, 79900, 40, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10931, '雅诗兰黛DW持妆粉底液 油皮亲妈', '控油遮瑕持久不脱妆，油皮混油皮挚爱粉底', 102, '/admin/dist/img/no-img.png', '雅诗兰黛DW粉底液被誉为油皮亲妈，控油持妆效果行业顶尖。遮瑕力中上能遮盖痘印和毛孔，妆效哑光自然不假面。持妆12小时不暗沉不斑驳。适合油皮和混油皮用户，夏天出汗出油也不怕。', 45900, 39900, 55, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10932, '兰蔻持妆粉底液 干皮救星', '轻薄水润光泽肌，干皮混干皮挚爱底妆', 102, '/admin/dist/img/no-img.png', '兰蔻持妆粉底液主打轻薄水润妆效，含透明质酸成分上妆同时补水保湿。妆感自然光泽肌，仿佛天生好皮肤。SPF25防晒值日常通勤够用。适合干皮和追求自然裸妆感的用户，秋冬干燥季节也不卡粉。', 49900, 43900, 50, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10933, '雪花秀致美气垫BB霜 21号', '韩方草本养肤气垫，上妆即护肤，清透伪素颜', 99, '/admin/dist/img/no-img.png', '雪花秀经典气垫BB霜，蕴含人参和草本精华，上妆同时滋养肌肤。21号自然色适合大多数亚洲肤色。妆感清透伪素颜效果，补妆方便随身携带。适合上班族快速上妆出门，送妈妈送长辈也很合适。', 55900, 48900, 45, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10934, '兰芝雪纱隔离霜 紫色款', '修饰暗黄提亮肤色，素颜霜打底神器', 101, '/admin/dist/img/no-img.png', '兰芝明星隔离霜紫色款，专为亚洲黄皮设计。淡紫色乳霜能中和暗黄提亮肤色，单独使用即可当素颜霜。质地水润好推开，后续上妆更服帖。适合经常熬夜肤色暗沉的上班族和学生党。', 29900, 24900, 75, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10935, 'Olay小白瓶美白精华 30ml', '烟酰胺5%科学美白，淡化痘印提亮肤色', 100, '/admin/dist/img/no-img.png', 'Olay明星小白瓶美白精华，含5%高浓度烟酰胺，科学美白淡化黑色素。坚持使用四周可见肤色提亮痘印变淡。清爽不油腻适合所有肤质。性价比极高的美白精华，学生党入手美白第一步的首选。', 32900, 26900, 90, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10936, 'SK-II 小灯泡美白精华 50ml', 'PITERA加烟酰胺，贵妇级美白淡斑神器', 100, '/admin/dist/img/no-img.png', 'SK-II小灯泡美白精华，PITERA酵母搭配高浓度烟酰胺，美白淡斑效果业界公认。乳白色精华液带微闪粒子上脸立刻提亮。坚持使用一个月肤色白一个度。价格虽贵但效果确实好，适合有预算的熟龄肌。', 169900, 154900, 25, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10937, 'NARS 高潮腮红 4.8g', '经典蜜桃金闪，自然红润好气色，一块用到天荒地老', 103, '/admin/dist/img/no-img.png', 'NARS全球销量第一的腮红色号，蜜桃粉色带细腻金闪。上脸雾面微光泽，仿佛皮肤透出的自然红润。显色度适中新手友好不猴屁股。一块腮红用到天荒地老，适合所有肤色的百搭腮红。', 31900, 27900, 65, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10938, '美宝莲摩天翘睫毛膏 防水款', '纤长卷翘防水不晕，平价睫毛膏之王', 104, '/admin/dist/img/no-img.png', '美宝莲经典摩天翘睫毛膏防水版，刷头弧度贴合眼型，一刷睫毛立刻纤长卷翘。防水配方游泳出汗都不晕染，温水可卸不伤睫毛。价格亲民效果不输大牌，学生党和化妆新手入门必备。', 12900, 9900, 120, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10939, '米家扫地机器人 3C 增强版', '激光导航5000Pa飓风吸力，扫拖一体解放双手', 22, '/admin/dist/img/no-img.png', '米家扫地机器人3C增强版，LDS激光导航精准建图不漏扫。5000Pa飓风级吸力宠物毛发灰尘一吸即净。拖地功能电控水箱均匀出水扫拖一体解放双手。米家APP远程控制定时清扫。适合养宠物的家庭和忙碌的上班族。', 229900, 179900, 60, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10940, '戴森 V16 无线吸尘器', '激光探测微尘，240AW强劲吸力，除螨除尘全能', 23, '/admin/dist/img/no-img.png', '戴森V16旗舰无线吸尘器，激光探测技术让肉眼看不见的微尘无处遁形。240AW超强吸力深层清洁地毯床垫沙发。配备除螨吸头除螨率99%。最长续航60分钟全屋清洁无压力。适合有洁癖和养宠物的精致生活家。', 549900, 499900, 25, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10941, '米家加湿器 Pro 增强版', '5L大容量UV杀菌，静音加湿不扰眠，空调房必备', 27, '/admin/dist/img/no-img.png', '米家加湿器Pro增强版，5L大水箱持续加湿16小时一整晚不用加水。UV-C紫外线杀菌先杀菌再加湿，出雾干净不滋生细菌。噪音低至28分贝不影响睡眠。适合空调房和暖气房使用，缓解皮肤干燥喉咙不适。', 29900, 24900, 85, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10942, 'JBL 蓝牙音箱 Flip 8 便携防水', '12小时续航IPX7防水，户外聚会音乐伴侣', 28, '/admin/dist/img/no-img.png', 'JBL Flip 8便携蓝牙音箱，12小时超长续航从早嗨到晚。IPX7级防水泳池边淋雨都不怕。低音澎湃中高音清晰，户外聚会氛围利器。轻巧便携放背包即可，露营野餐海滩出游的音乐伴侣。', 59900, 49900, 70, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10943, '猫王收音机 蓝牙音箱 复古绿', '复古收音机造型，FM调频加蓝牙，书房办公桌格调好物', 28, '/admin/dist/img/no-img.png', '猫王收音机经典复古造型蓝牙音箱，胡桃木外壳搭配金属旋钮质感满满。FM调频收音加蓝牙双模播放。音质温暖适合播放爵士和民谣。放在书房或办公桌上既是音箱也是摆件。适合文艺青年和办公桌美学爱好者。', 39900, 34900, 50, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10944, '米家智能空气炸锅 5.5L', '360度热风循环无油烹饪，低脂健康美食轻松做', 21, '/admin/dist/img/no-img.png', '米家智能空气炸锅5.5L大容量，360度热风循环不用翻面均匀酥脆。无油烹饪比传统油炸减少80%油脂。预设8大菜单一键操作厨房小白也能做美食。适合减脂健身人群和喜欢在家做饭的年轻人。', 45900, 39900, 65, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10945, '德龙全自动咖啡机 ECAM 23.460', '意式浓缩美式一键出品，办公室咖啡角升级之选', 20, '/admin/dist/img/no-img.png', '德龙全自动咖啡机ECAM23.460，15Bar泵压萃取醇正意式浓缩。内置研磨器现磨现冲保留咖啡豆新鲜风味。一键制作拿铁卡布奇诺美式，办公室咖啡角品质升级之选。适合咖啡爱好者和追求品质生活的白领。', 459900, 399900, 15, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10946, '小熊暖风机 家用静音速热', '3秒速热倾倒断电，南方冬天取暖神器', 26, '/admin/dist/img/no-img.png', '小熊速热暖风机，PTC陶瓷发热3秒速热无需等待。两档功率可调节摇头送暖覆盖面积更大。倾倒自动断电安全放心，体积小巧桌下床边都能放。适合南方没有暖气的冬天取暖使用。', 15900, 12900, 100, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10947, '美的破壁豆浆机 1.75L', '免泡豆免过滤一键出浆，营养早餐好帮手', 25, '/admin/dist/img/no-img.png', '美的破壁豆浆机1.75L大容量，免泡豆免过滤一键出浆操作简单。破壁技术豆渣细腻无需过滤，营养充分保留不浪费。还能做米糊果汁浓汤一机多用。适合注重健康饮食的家庭，给爸妈买一台改善早餐。', 59900, 49900, 40, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10948, '米家空气净化器 Pro H', '除甲醛除霾除菌，新房装修后必备', 31, '/admin/dist/img/no-img.png', '米家空气净化器Pro H，高效滤芯可过滤PM2.5甲醛细菌病毒。适用面积72平米覆盖客厅卧室。OLED触摸屏实时显示PM2.5数值。新房装修后除甲醛利器，过敏季防雾霾防花粉。', 179900, 149900, 35, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10949, '松下蒸烤箱 NU-SC360B', '蒸烤炸一体30L大容量，厨房全能选手', 29, '/admin/dist/img/no-img.png', '松下蒸烤箱NU-SC360B，蒸烤炸一体兼顾多种烹饪方式。30L大容量一次搞定一家人的饭菜。直喷蒸汽蒸鱼蒸菜鲜嫩多汁，热风烘烤面包蛋糕不翻车。适合喜欢在家研究美食的烹饪爱好者。', 259900, 229900, 20, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10950, '戴森卷发棒 Airwrap 多功能', '康达效应气流自动卷发不伤发，送礼天花板', 30, '/admin/dist/img/no-img.png', '戴森Airwrap多功能美发器，利用康达效应气流自动吸附头发卷发无需技巧。低温气流定型不伤发质，吹干卷发顺发三合一。多种风嘴配件满足不同造型需求。价格虽贵但体验惊艳，送给女朋友或老婆的终极礼物。', 429900, 399900, 15, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10951, '优衣库轻薄羽绒服 男款', '轻便保暖可收纳，秋冬通勤百搭外套', 76, '/admin/dist/img/no-img.png', '优衣库经典轻薄羽绒服，选用90%白鹅绒填充蓬松保暖。自重仅200g可收纳成小包携带方便。简约立领设计百搭日常通勤，内搭衬衫或毛衣都好看。适合秋冬季节穿着轻便不臃肿活动自如。性价比极高人手一件的基础款。', 49900, 39900, 90, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10952, '波司登极寒羽绒服 男款 加厚', '零下30度御寒鹅绒填充，北方过冬必备战袍', 76, '/admin/dist/img/no-img.png', '波司登极寒系列加厚羽绒服，95%白鹅绒填充蓬松度800以上。防风防水面料雨雪天气无压力。蓄热内里锁住体温，零下30度也暖和。多口袋设计实用方便。适合北方严寒地区过冬，送给爸爸保暖御寒。', 159900, 129900, 40, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10953, '太平鸟羊毛呢大衣 男款 商务休闲', '澳洲初剪羊毛双面呢，商务通勤气质之选', 76, '/admin/dist/img/no-img.png', '太平鸟羊毛呢大衣选用澳洲初剪羊毛双面呢面料，手感软糯挺括有型。经典翻领版型修饰身形，商务休闲两穿。中长款长度刚好盖住臀部保暖不压个子。适合上班族通勤穿，搭配衬衫或高领毛衣都显气质。', 89900, 69900, 35, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10954, 'MLB棒球服 男款 拼色潮流', '纽约洋基队联名款，街头潮流百搭单品', 76, '/admin/dist/img/no-img.png', 'MLB棒球服纽约洋基队联名款，拼色设计街头感十足。纯棉面料亲肤透气春秋穿着刚刚好。宽松版型搭配牛仔裤或运动裤都好看。适合追求潮流的学生党和年轻人，情侣款女生穿也很有范。', 59900, 47900, 55, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10955, '骆驼冲锋衣 男款 三合一可拆卸', '防风防水可拆卸内胆，户外徒步登山必备', 76, '/admin/dist/img/no-img.png', '骆驼三合一冲锋衣，外层防风防水可应对小雨天气，内层抓绒保暖可拆卸单穿。一衣三穿春夏秋都能用。立体剪裁活动自如不束缚。适合喜欢户外运动徒步爬山的用户，出差旅行应对多变天气。', 69900, 54900, 60, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10956, '北面羽绒服 男款 1996经典复刻', '700蓬鹅绒经典款，雪山图案街拍出片神器', 76, '/admin/dist/img/no-img.png', '北面1996经典复刻款羽绒服，700蓬松度鹅绒填充轻盈保暖。经典雪山图案辨识度极高街拍出片有面子。防风袖口和下摆锁住热量减少流失。适合追求潮流又怕冷的年轻人，冬天出街又暖又帅。', 199900, 169900, 25, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10957, 'Nike Air Zoom Pegasus 41 男跑鞋', 'ZoomX泡棉加Air气垫，回弹缓震专业慢跑鞋', 78, '/admin/dist/img/no-img.png', 'Nike飞马41代专业慢跑鞋，ZoomX泡棉搭配前后掌Air Zoom气垫，回弹出色缓震到位。Flywire飞线包裹脚感贴合，工程网面透气不闷脚。适合日常五到十公里慢跑和健身房训练使用。', 99900, 79900, 70, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10958, '阿迪达斯 Ultraboost 23 男跑鞋', 'Boost中底踩屎感十足，日常通勤慢跑两不误', 78, '/admin/dist/img/no-img.png', '阿迪达斯UB23跑鞋，Boost中底被跑友称为踩屎感，缓震回弹业界标杆。Primeknit编织鞋面如袜子般贴合长时间穿着不磨脚。适合日常通勤走路和轻度慢跑，久站上班族穿了就不想换鞋。', 89900, 69900, 55, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10959, '亚瑟士 GEL-KAYANO 31 男稳定跑鞋', 'GEL缓震胶支撑稳定，扁平足外翻跑者福音', 78, '/admin/dist/img/no-img.png', '亚瑟士KAYANO31旗舰稳定跑鞋，后跟GEL缓震胶吸收跑动冲击力保护膝盖。内侧DUOMAX支撑柱矫正外翻步态，扁平足和低足弓跑者的保护神。工程网面透气包裹到位。适合大体重和扁平足跑者日常训练。', 129900, 109900, 27, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-30 11:06:14');
INSERT INTO `goods` VALUES (10960, 'new balance 574 灰色 经典复古鞋', '百搭灰色复古慢跑鞋，日系穿搭必备单品', 98, '/admin/dist/img/no-img.png', '新百伦574经典灰配色，复古慢跑鞋鼻祖，日系穿搭街拍出镜率最高鞋款。皮革和网面拼接质感好，ENCAP中底日常走路舒适不累。百搭配色牛仔裤工装裤运动裤都能搭。适合学生党和日常通勤穿着的经典款。', 79900, 64900, 65, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10961, '匡威 Chuck 70 高帮帆布鞋 黑色', '70s复古鞋型加厚鞋底，一年四季百搭王', 98, '/admin/dist/img/no-img.png', '匡威Chuck70高帮帆布鞋，70s复古鞋型橡胶底加厚舒适度提升。黑色经典百搭所有衣服，高帮设计修饰腿型。帆布材质透气夏天穿也不闷。从学生到明星都穿的基础款，一年四季搭配万能选手。', 59900, 49900, 100, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');
INSERT INTO `goods` VALUES (10962, '斯凯奇老爹鞋 女款 厚底增高', '复古厚底增高4cm，显腿长百搭舒适', 98, '/admin/dist/img/no-img.png', '斯凯奇复古老爹鞋女款，厚底设计悄悄增高4cm修饰腿长。记忆海绵鞋垫踩上去柔软回弹逛街一天不累脚。白色拼灰配色百搭所有风格。适合日常通勤逛街和旅游暴走，舒适度拉满的日常鞋款。', 69900, 55900, 75, 0, 1, '2026-05-18 09:35:54', 1, '2026-05-18 09:35:54');

-- ----------------------------
-- Table structure for goods_category
-- ----------------------------
DROP TABLE IF EXISTS `goods_category`;
CREATE TABLE `goods_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `level` tinyint NOT NULL DEFAULT 0 COMMENT '分类级别(1-一级分类 2-二级分类 3-三级分类)',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父分类id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `rank` int NOT NULL DEFAULT 0 COMMENT '排序值(字段越大越靠前)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` int NOT NULL DEFAULT 0 COMMENT '创建者id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_user` int NULL DEFAULT 0 COMMENT '修改者id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 121 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods_category
-- ----------------------------
INSERT INTO `goods_category` VALUES (15, 1, 0, '家电 数码 手机', 101, '2019-09-11 18:45:40', 0, '2026-05-02 09:43:29', 7);
INSERT INTO `goods_category` VALUES (16, 1, 0, '女装 男装 穿搭', 100, '2019-09-11 18:46:07', 0, '2026-05-02 09:43:38', 7);
INSERT INTO `goods_category` VALUES (17, 2, 15, '家电', 10, '2019-09-11 18:46:32', 0, '2019-09-11 18:46:32', 0);
INSERT INTO `goods_category` VALUES (18, 2, 15, '数码', 9, '2019-09-11 18:46:43', 0, '2019-09-11 18:46:43', 0);
INSERT INTO `goods_category` VALUES (19, 2, 15, '手机', 8, '2019-09-11 18:46:52', 0, '2019-09-11 18:46:52', 0);
INSERT INTO `goods_category` VALUES (20, 3, 17, '生活电器', 0, '2019-09-11 18:47:38', 0, '2019-09-11 18:47:38', 0);
INSERT INTO `goods_category` VALUES (21, 3, 17, '厨房电器', 0, '2019-09-11 18:47:49', 0, '2019-09-11 18:47:49', 0);
INSERT INTO `goods_category` VALUES (22, 3, 17, '扫地机器人', 0, '2019-09-11 18:47:58', 0, '2019-09-11 18:47:58', 0);
INSERT INTO `goods_category` VALUES (23, 3, 17, '吸尘器', 0, '2019-09-11 18:48:06', 0, '2019-09-11 18:48:06', 0);
INSERT INTO `goods_category` VALUES (24, 3, 17, '取暖器', 0, '2019-09-11 18:48:12', 0, '2019-09-11 18:48:12', 0);
INSERT INTO `goods_category` VALUES (25, 3, 17, '豆浆机', 0, '2019-09-11 18:48:26', 0, '2019-09-11 18:48:26', 0);
INSERT INTO `goods_category` VALUES (26, 3, 17, '暖风机', 0, '2019-09-11 18:48:40', 0, '2019-09-11 18:48:40', 0);
INSERT INTO `goods_category` VALUES (27, 3, 17, '加湿器', 0, '2019-09-11 18:48:50', 0, '2019-09-11 18:48:50', 0);
INSERT INTO `goods_category` VALUES (28, 3, 17, '蓝牙音箱', 0, '2019-09-11 18:48:57', 0, '2019-09-11 18:48:57', 0);
INSERT INTO `goods_category` VALUES (29, 3, 17, '烤箱', 0, '2019-09-11 18:49:09', 0, '2019-09-11 18:49:09', 0);
INSERT INTO `goods_category` VALUES (30, 3, 17, '卷发器', 0, '2019-09-11 18:49:19', 0, '2019-09-11 18:49:19', 0);
INSERT INTO `goods_category` VALUES (31, 3, 17, '空气净化器', 0, '2019-09-11 18:49:30', 0, '2019-09-11 18:49:30', 0);
INSERT INTO `goods_category` VALUES (32, 3, 18, '游戏主机', 0, '2019-09-11 18:49:50', 0, '2019-09-11 18:49:50', 0);
INSERT INTO `goods_category` VALUES (33, 3, 18, '数码精选', 0, '2019-09-11 18:49:55', 0, '2019-09-11 18:49:55', 0);
INSERT INTO `goods_category` VALUES (34, 3, 18, '平板电脑', 0, '2019-09-11 18:50:08', 0, '2019-09-11 18:50:08', 0);
INSERT INTO `goods_category` VALUES (35, 2, 18, '苹果 Apple', 1, '2019-09-11 18:50:24', 0, '2026-04-26 20:34:56', 1);
INSERT INTO `goods_category` VALUES (36, 3, 18, '电脑主机', 0, '2019-09-11 18:50:36', 0, '2019-09-11 18:50:36', 0);
INSERT INTO `goods_category` VALUES (37, 3, 18, '数码相机', 0, '2019-09-11 18:50:57', 0, '2019-09-11 18:50:57', 0);
INSERT INTO `goods_category` VALUES (38, 3, 18, '电玩动漫', 0, '2019-09-11 18:52:15', 0, '2019-09-11 18:52:15', 0);
INSERT INTO `goods_category` VALUES (39, 3, 18, '单反相机', 0, '2019-09-11 18:52:26', 0, '2019-09-11 18:52:26', 0);
INSERT INTO `goods_category` VALUES (40, 3, 18, '键盘鼠标', 0, '2019-09-11 18:52:46', 0, '2019-09-11 18:52:46', 0);
INSERT INTO `goods_category` VALUES (41, 3, 18, '无人机', 0, '2019-09-11 18:53:01', 0, '2019-09-11 18:53:01', 0);
INSERT INTO `goods_category` VALUES (42, 3, 18, '二手电脑', 0, '2019-09-11 18:53:08', 0, '2019-09-11 18:53:08', 0);
INSERT INTO `goods_category` VALUES (43, 3, 18, '二手手机', 0, '2019-09-11 18:53:14', 0, '2019-09-11 18:53:14', 0);
INSERT INTO `goods_category` VALUES (45, 3, 19, '荣耀手机', 99, '2019-09-11 18:53:59', 0, '2019-09-18 13:40:59', 0);
INSERT INTO `goods_category` VALUES (46, 3, 19, '华为手机', 98, '2019-09-11 18:54:20', 0, '2019-09-18 13:40:51', 0);
INSERT INTO `goods_category` VALUES (47, 3, 19, '苹果 iPhone', 88, '2019-09-11 18:54:49', 0, '2019-11-15 18:31:22', 0);
INSERT INTO `goods_category` VALUES (48, 3, 19, '华为 Mate 20', 79, '2019-09-11 18:55:03', 0, '2019-09-11 18:55:13', 0);
INSERT INTO `goods_category` VALUES (49, 3, 19, '华为 P30', 97, '2019-09-11 18:55:22', 0, '2019-09-11 18:55:22', 0);
INSERT INTO `goods_category` VALUES (50, 3, 19, '华为 P30 Pro', 0, '2019-09-11 18:55:32', 0, '2019-09-11 18:55:32', 0);
INSERT INTO `goods_category` VALUES (51, 3, 19, '小米手机', 0, '2019-09-11 18:55:52', 0, '2019-09-11 18:55:52', 0);
INSERT INTO `goods_category` VALUES (52, 3, 19, '红米', 0, '2019-09-11 18:55:58', 0, '2019-09-11 18:55:58', 0);
INSERT INTO `goods_category` VALUES (53, 3, 19, 'OPPO', 0, '2019-09-11 18:56:06', 0, '2019-09-11 18:56:06', 0);
INSERT INTO `goods_category` VALUES (54, 3, 19, '一加', 0, '2019-09-11 18:56:12', 0, '2019-09-11 18:56:12', 0);
INSERT INTO `goods_category` VALUES (55, 3, 19, '小米 MIX', 0, '2019-09-11 18:56:37', 0, '2019-09-11 18:56:37', 0);
INSERT INTO `goods_category` VALUES (56, 3, 19, 'Reno', 0, '2019-09-11 18:56:49', 0, '2019-09-11 18:56:49', 0);
INSERT INTO `goods_category` VALUES (57, 3, 19, 'vivo', 0, '2019-09-11 18:57:01', 0, '2019-09-11 18:57:01', 0);
INSERT INTO `goods_category` VALUES (58, 3, 19, '手机以旧换新', 0, '2019-09-11 18:57:09', 0, '2019-09-11 18:57:09', 0);
INSERT INTO `goods_category` VALUES (59, 1, 0, '运动 户外 乐器', 97, '2019-09-12 00:08:46', 0, '2019-09-12 00:08:46', 0);
INSERT INTO `goods_category` VALUES (60, 1, 0, '游戏 动漫 影视', 96, '2019-09-12 00:09:00', 0, '2019-09-12 00:09:00', 0);
INSERT INTO `goods_category` VALUES (61, 1, 0, '家具 家饰 家纺', 98, '2019-09-12 00:09:27', 0, '2019-09-12 00:09:27', 0);
INSERT INTO `goods_category` VALUES (62, 1, 0, '美妆 清洁 宠物', 94, '2019-09-12 00:09:51', 0, '2019-09-17 18:22:34', 0);
INSERT INTO `goods_category` VALUES (63, 1, 0, '工具 装修 建材', 93, '2019-09-12 00:10:07', 0, '2019-09-12 00:10:07', 0);
INSERT INTO `goods_category` VALUES (65, 1, 0, '玩具 孕产 用品', 0, '2019-09-12 00:11:17', 0, '2019-09-12 00:11:17', 0);
INSERT INTO `goods_category` VALUES (66, 1, 0, '鞋靴 箱包 配件', 91, '2019-09-12 00:11:30', 0, '2019-09-12 00:11:30', 0);
INSERT INTO `goods_category` VALUES (67, 2, 16, '女装', 10, '2019-09-12 00:15:19', 0, '2019-09-12 00:15:19', 0);
INSERT INTO `goods_category` VALUES (68, 2, 16, '男装', 9, '2019-09-12 00:15:28', 0, '2019-09-12 00:15:28', 0);
INSERT INTO `goods_category` VALUES (69, 2, 16, '穿搭', 8, '2019-09-12 00:15:35', 0, '2019-09-12 00:15:35', 0);
INSERT INTO `goods_category` VALUES (70, 2, 61, '家具', 10, '2019-09-12 00:20:22', 0, '2019-09-12 00:20:22', 0);
INSERT INTO `goods_category` VALUES (71, 2, 61, '家饰', 9, '2019-09-12 00:20:29', 0, '2019-09-12 00:20:29', 0);
INSERT INTO `goods_category` VALUES (72, 2, 61, '家纺', 8, '2019-09-12 00:20:35', 0, '2019-09-12 00:20:35', 0);
INSERT INTO `goods_category` VALUES (73, 2, 59, '运动', 10, '2019-09-12 00:20:49', 0, '2019-09-12 00:20:49', 0);
INSERT INTO `goods_category` VALUES (74, 2, 59, '户外', 9, '2019-09-12 00:20:58', 0, '2019-09-12 00:20:58', 0);
INSERT INTO `goods_category` VALUES (75, 2, 59, '乐器', 8, '2019-09-12 00:21:05', 0, '2019-09-12 00:21:05', 0);
INSERT INTO `goods_category` VALUES (76, 3, 67, '外套', 10, '2019-09-12 00:21:55', 0, '2019-09-12 00:21:55', 0);
INSERT INTO `goods_category` VALUES (77, 3, 70, '沙发', 10, '2019-09-12 00:22:21', 0, '2019-09-12 00:22:21', 0);
INSERT INTO `goods_category` VALUES (78, 3, 73, '跑鞋', 10, '2019-09-12 00:22:42', 0, '2019-09-12 00:22:42', 0);
INSERT INTO `goods_category` VALUES (79, 2, 60, '游戏', 10, '2019-09-12 00:23:13', 0, '2019-09-12 00:23:13', 0);
INSERT INTO `goods_category` VALUES (80, 2, 60, '动漫', 9, '2019-09-12 00:23:21', 0, '2019-09-12 00:23:21', 0);
INSERT INTO `goods_category` VALUES (81, 2, 60, '影视', 8, '2019-09-12 00:23:27', 0, '2019-09-12 00:23:27', 0);
INSERT INTO `goods_category` VALUES (82, 3, 79, 'LOL', 10, '2019-09-12 00:23:44', 0, '2019-09-12 00:23:44', 0);
INSERT INTO `goods_category` VALUES (83, 2, 62, '美妆', 10, '2019-09-12 00:23:58', 0, '2019-09-17 18:22:44', 0);
INSERT INTO `goods_category` VALUES (84, 2, 62, '宠物', 9, '2019-09-12 00:24:07', 0, '2019-09-12 00:24:07', 0);
INSERT INTO `goods_category` VALUES (85, 2, 62, '清洁', 8, '2019-09-12 00:24:15', 0, '2019-09-17 18:22:51', 0);
INSERT INTO `goods_category` VALUES (86, 3, 83, '口红', 10, '2019-09-12 00:24:38', 0, '2019-09-17 18:23:08', 0);
INSERT INTO `goods_category` VALUES (87, 2, 63, '工具', 10, '2019-09-12 00:24:56', 0, '2019-09-12 00:24:56', 0);
INSERT INTO `goods_category` VALUES (88, 2, 63, '装修', 9, '2019-09-12 00:25:05', 0, '2019-09-12 00:25:05', 0);
INSERT INTO `goods_category` VALUES (89, 2, 63, '建材', 8, '2019-09-12 00:25:12', 0, '2019-09-12 00:25:12', 0);
INSERT INTO `goods_category` VALUES (90, 3, 87, '转换器', 10, '2019-09-12 00:25:45', 0, '2019-09-12 00:25:45', 0);
INSERT INTO `goods_category` VALUES (91, 2, 64, '珠宝', 10, '2019-09-12 00:26:10', 0, '2019-09-12 00:26:10', 0);
INSERT INTO `goods_category` VALUES (92, 2, 64, '金饰', 9, '2019-09-12 00:26:18', 0, '2019-09-12 00:26:18', 0);
INSERT INTO `goods_category` VALUES (93, 2, 64, '眼镜', 8, '2019-09-12 00:26:25', 0, '2019-09-12 00:26:25', 0);
INSERT INTO `goods_category` VALUES (94, 3, 91, '钻石', 10, '2019-09-12 00:26:40', 0, '2019-09-12 00:26:40', 0);
INSERT INTO `goods_category` VALUES (95, 2, 66, '鞋靴', 10, '2019-09-12 00:27:09', 0, '2019-09-12 00:27:09', 0);
INSERT INTO `goods_category` VALUES (96, 2, 66, '箱包', 9, '2019-09-12 00:27:17', 0, '2019-09-12 00:27:17', 0);
INSERT INTO `goods_category` VALUES (97, 2, 66, '配件', 8, '2019-09-12 00:27:23', 0, '2019-09-12 00:27:23', 0);
INSERT INTO `goods_category` VALUES (98, 3, 95, '休闲鞋', 10, '2019-09-12 00:27:48', 0, '2019-09-12 00:27:48', 0);
INSERT INTO `goods_category` VALUES (99, 3, 83, '气垫', 0, '2019-09-17 18:24:23', 0, '2019-09-17 18:24:23', 0);
INSERT INTO `goods_category` VALUES (100, 3, 83, '美白', 0, '2019-09-17 18:24:36', 0, '2019-09-17 18:24:36', 0);
INSERT INTO `goods_category` VALUES (101, 3, 83, '隔离霜', 0, '2019-09-17 18:27:04', 0, '2019-09-17 18:27:04', 0);
INSERT INTO `goods_category` VALUES (102, 3, 83, '粉底', 0, '2019-09-17 18:27:19', 0, '2019-09-17 18:27:19', 0);
INSERT INTO `goods_category` VALUES (103, 3, 83, '腮红', 0, '2019-09-17 18:27:24', 0, '2019-09-17 18:27:24', 0);
INSERT INTO `goods_category` VALUES (104, 3, 83, '睫毛膏', 0, '2019-09-17 18:27:47', 0, '2019-09-17 18:27:47', 0);
INSERT INTO `goods_category` VALUES (105, 3, 83, '香水', 0, '2019-09-17 18:28:16', 0, '2019-09-17 18:28:16', 0);
INSERT INTO `goods_category` VALUES (106, 3, 83, '面膜', 0, '2019-09-17 18:28:21', 0, '2019-09-17 18:28:21', 0);
INSERT INTO `goods_category` VALUES (115, 2, 65, '玩具', 0, '2019-11-28 20:24:58', 0, '2019-11-28 20:24:58', 0);
INSERT INTO `goods_category` VALUES (116, 3, 115, '机器人', 0, '2019-11-28 20:25:16', 0, '2019-11-28 20:25:16', 0);

-- ----------------------------
-- Table structure for index_config
-- ----------------------------
DROP TABLE IF EXISTS `index_config`;
CREATE TABLE `index_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '首页配置项主键id',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '显示字符(配置搜索时不可为空，其他可为空)',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT ' 3-(首页)热销商品 4-(首页)新品上线 5-(首页)为你推荐',
  `goods_id` bigint NOT NULL DEFAULT 0 COMMENT '商品id 默认为0',
  `redirect_url` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '##' COMMENT '点击后的跳转地址(默认不跳转)',
  `rank` int NOT NULL DEFAULT 0 COMMENT '排序值(字段越大越靠前)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` int NOT NULL DEFAULT 0 COMMENT '创建者id',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新修改时间',
  `update_user` int NULL DEFAULT 0 COMMENT '修改者id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 553 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of index_config
-- ----------------------------
INSERT INTO `index_config` VALUES (547, '小米（MI）米家有线吸尘器', 4, 10910, '##', 0, '2026-05-06 12:26:53', 0, '2026-05-06 12:26:53', 0);
INSERT INTO `index_config` VALUES (548, '米家【新品来袭】 扫地机器人6 ', 4, 10909, '##', 0, '2026-05-06 12:26:53', 0, '2026-05-06 12:26:53', 0);
INSERT INTO `index_config` VALUES (549, '志高（CHIGO）加厚电热锅', 4, 10908, '##', 0, '2026-05-06 12:26:53', 0, '2026-05-06 12:26:53', 0);
INSERT INTO `index_config` VALUES (550, '双飞燕键盘', 4, 10907, '##', 0, '2026-05-06 12:26:53', 0, '2026-05-06 12:26:53', 0);
INSERT INTO `index_config` VALUES (551, '迪奥小姐花漾淡香水', 4, 10003, '##', 0, '2026-05-06 12:26:53', 0, '2026-05-06 12:26:53', 0);
INSERT INTO `index_config` VALUES (552, '419美的（Midea）电煮锅', 4, 10005, '##', 0, '2026-05-06 12:26:53', 0, '2026-05-06 12:26:53', 0);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单表主键id',
  `order_no` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `user_id` bigint NOT NULL DEFAULT 0 COMMENT '用户主键id',
  `total_price` int NOT NULL DEFAULT 1 COMMENT '订单总价',
  `pay_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态:0.未支付,1.支付成功,-1:支付失败',
  `pay_type` tinyint NOT NULL DEFAULT 0 COMMENT '0.无 1.支付宝支付 2.微信支付',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `order_status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1关闭订单',
  `extra_info` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '订单body',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64299 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (3, '20260502120417096379', 7, 8598, 1, 1, '2026-05-03 11:30:24', 4, '', '2026-05-02 12:04:17', '2026-05-03 14:09:01');
INSERT INTO `order` VALUES (4, '20260502170938521287', 7, 9258, 0, 0, NULL, -1, '', '2026-05-02 17:09:39', '2026-05-03 13:30:00');
INSERT INTO `order` VALUES (6, '20260506195652207166', 7, 99, 0, 0, NULL, -1, '', '2026-05-06 19:56:52', '2026-05-06 22:30:00');
INSERT INTO `order` VALUES (7, '20260506195940754767', 7, 99, 0, 0, NULL, -1, '', '2026-05-06 19:59:41', '2026-05-06 22:30:00');
INSERT INTO `order` VALUES (8, '20260508170538794777', 7, 99, 0, 0, NULL, -1, '', '2026-05-08 17:05:39', '2026-05-08 17:30:00');
INSERT INTO `order` VALUES (62349, '20260508182728203760', 7, 99, 0, 0, NULL, -1, '', '2026-05-08 18:27:28', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64202, '20260508182844508811', 2, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64203, '20260508182844526419', 6, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64204, '20260508182844531157', 8, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64205, '20260508182844536567', 9, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64206, '20260508182844540151', 10, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64207, '20260508182844546120', 11, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64208, '20260508182844550430', 12, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64209, '20260508182844555206', 13, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64210, '20260508182844560935', 14, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64211, '20260508182844564844', 15, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64212, '20260508182844568543', 16, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64213, '20260508182844573647', 17, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64214, '20260508182844581940', 18, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64215, '20260508182844588673', 19, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64216, '20260508182844607010', 20, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64217, '20260508182844617890', 21, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64218, '20260508182844625387', 22, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64219, '20260508182844633031', 23, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64220, '20260508182844639562', 24, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64221, '20260508182844645606', 25, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64222, '20260508182844649509', 26, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64223, '20260508182844656517', 27, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64224, '20260508182844661662', 28, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64225, '20260508182844665386', 29, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64226, '20260508182844670851', 30, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64227, '20260508182844676363', 31, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64228, '20260508182844680189', 32, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64229, '20260508182844686883', 33, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64230, '20260508182844690358', 34, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64231, '20260508182844694813', 35, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64232, '20260508182844699868', 36, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64233, '20260508182844703063', 37, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64234, '20260508182844708430', 38, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64235, '20260508182844712111', 39, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64236, '20260508182844716352', 40, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64237, '20260508182844720428', 41, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64238, '20260508182844725089', 42, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64239, '20260508182844729600', 43, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64240, '20260508182844733599', 44, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64241, '20260508182844737069', 45, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64242, '20260508182844740385', 46, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64243, '20260508182844744625', 47, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64244, '20260508182844747014', 48, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64245, '20260508182844751997', 49, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64246, '20260508182844754042', 50, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64247, '20260508182844758363', 51, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64248, '20260508182844761216', 52, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64249, '20260508182844764367', 53, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64250, '20260508182844768056', 54, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64251, '20260508182844771048', 55, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64252, '20260508182844774979', 56, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64253, '20260508182844777901', 57, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64254, '20260508182844781462', 58, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64255, '20260508182844784962', 59, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64256, '20260508182844787353', 60, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64257, '20260508182844792951', 61, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64258, '20260508182844795693', 62, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64259, '20260508182844797470', 63, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64260, '20260508182844801972', 64, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64261, '20260508182844804392', 65, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64262, '20260508182844807711', 66, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64263, '20260508182844812716', 67, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64264, '20260508182844816275', 68, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64265, '20260508182844821233', 69, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64266, '20260508182844827096', 70, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64267, '20260508182844832709', 71, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64268, '20260508182844836856', 72, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64269, '20260508182844841402', 73, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64270, '20260508182844846880', 74, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64271, '20260508182844850900', 75, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64272, '20260508182844853726', 76, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64273, '20260508182844857636', 77, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64274, '20260508182844861426', 78, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64275, '20260508182844865126', 79, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64276, '20260508182844869882', 80, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64277, '20260508182844874374', 81, 99, 0, 0, NULL, -1, '', '2026-05-08 18:28:45', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64278, '20260508183028943886', 5, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64279, '20260508183028961365', 6, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64280, '20260508183028974995', 7, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64281, '20260508183029017180', 8, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64282, '20260508183029066334', 9, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64283, '20260508183029117570', 10, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64284, '20260508183029184114', 11, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64285, '20260508183029222829', 12, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64286, '20260508183029272571', 13, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64287, '20260508183029328462', 14, 99, 0, 0, NULL, -1, '', '2026-05-08 18:30:29', '2026-05-08 19:30:00');
INSERT INTO `order` VALUES (64288, '20260508192803207347', 7, 99, 0, 0, NULL, -1, '', '2026-05-08 19:28:03', '2026-05-20 15:00:00');
INSERT INTO `order` VALUES (64289, '20260520103123929142', 1, 2100700, 0, 0, NULL, -1, '', '2026-05-20 10:31:23', '2026-05-20 15:00:00');
INSERT INTO `order` VALUES (64290, '20260522161327122358', 1, 2169600, 0, 0, NULL, -1, '', '2026-05-22 16:13:27', '2026-05-30 10:00:00');
INSERT INTO `order` VALUES (64291, '20260522162338935846', 1, 699900, 0, 0, NULL, -1, '', '2026-05-22 16:23:38', '2026-05-30 10:00:00');
INSERT INTO `order` VALUES (64292, '20260522163135123503', 1, 59900, 0, 0, NULL, -1, '', '2026-05-22 16:31:35', '2026-05-30 10:00:00');
INSERT INTO `order` VALUES (64293, '20260522165021281265', 1, 59900, 0, 0, NULL, -1, '', '2026-05-22 16:50:21', '2026-05-30 10:00:00');
INSERT INTO `order` VALUES (64294, '20260522172635701425', 1, 699900, 0, 0, NULL, -1, '', '2026-05-22 17:26:35', '2026-05-30 10:00:00');
INSERT INTO `order` VALUES (64295, '20260522173417362612', 1, 129900, 1, 1, NULL, 4, '', '2026-05-22 17:34:17', '2026-05-22 17:34:17');
INSERT INTO `order` VALUES (64296, '20260528081451537224', 1, 7900, 0, 0, NULL, -1, '', '2026-05-28 08:14:51', '2026-05-28 08:14:51');
INSERT INTO `order` VALUES (64297, '2060558375079469056', 1, 329700, 1, 1, '2026-05-30 11:07:53', 4, '', '2026-05-30 11:06:15', '2026-05-30 11:29:07');
INSERT INTO `order` VALUES (64298, '2060571781543055360', 1, 300, 0, 0, NULL, -1, '', '2026-05-30 11:59:31', '2026-05-30 12:31:54');

-- ----------------------------
-- Table structure for order_address
-- ----------------------------
DROP TABLE IF EXISTS `order_address`;
CREATE TABLE `order_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '收货人姓名',
  `user_phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '收货人手机号',
  `province` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '城',
  `region` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '区',
  `detail_address` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '收件详细地址(街道/楼宇/单元)',
  `order_id` mediumtext CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '订单的主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单收货地址关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_address
-- ----------------------------
INSERT INTO `order_address` VALUES (1, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '3');
INSERT INTO `order_address` VALUES (2, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '4');
INSERT INTO `order_address` VALUES (3, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '6');
INSERT INTO `order_address` VALUES (4, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '7');
INSERT INTO `order_address` VALUES (5, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '8');
INSERT INTO `order_address` VALUES (6, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '62349');
INSERT INTO `order_address` VALUES (7, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '64288');
INSERT INTO `order_address` VALUES (8, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '64289');
INSERT INTO `order_address` VALUES (9, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '64290');
INSERT INTO `order_address` VALUES (10, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '64291');
INSERT INTO `order_address` VALUES (11, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '64292');
INSERT INTO `order_address` VALUES (12, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '64293');
INSERT INTO `order_address` VALUES (13, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号', '64294');
INSERT INTO `order_address` VALUES (16, '张三', '149xxxx', '北京市', '北京市', '朝阳区', '望京街道xxx', '64297');
INSERT INTO `order_address` VALUES (17, '张三', '13800138000', '浙江省', '杭州市', '西湖区', '文三路100号', '64298');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单关联购物项主键id',
  `order_id` bigint NOT NULL DEFAULT 0 COMMENT '订单主键id',
  `goods_id` bigint NOT NULL DEFAULT 0 COMMENT '关联商品id',
  `goods_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '下单时商品的名称(订单快照)',
  `cover_img` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '下单时商品的主图(订单快照)',
  `price` int NOT NULL DEFAULT 1 COMMENT '下单时商品的价格(订单快照)',
  `count` int NOT NULL DEFAULT 1 COMMENT '数量(订单快照)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64304 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (7, 6, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 99, 1, '2026-05-06 19:56:52');
INSERT INTO `order_item` VALUES (8, 7, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 99, 1, '2026-05-06 19:59:41');
INSERT INTO `order_item` VALUES (9, 8, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 99, 1, '2026-05-08 17:05:39');
INSERT INTO `order_item` VALUES (64290, 64289, 10912, '华为 Mate 70 Pro 旗舰手机', '/admin/dist/img/no-img.png', 699900, 3, '2026-05-20 10:31:23');
INSERT INTO `order_item` VALUES (64291, 64289, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 1000, 1, '2026-05-20 10:31:23');
INSERT INTO `order_item` VALUES (64292, 64290, 10917, '小米 16 Ultra 徕卡影像', '/admin/dist/img/no-img.png', 549900, 1, '2026-05-22 16:13:27');
INSERT INTO `order_item` VALUES (64293, 64290, 10912, '华为 Mate 70 Pro 旗舰手机', '/admin/dist/img/no-img.png', 699900, 2, '2026-05-22 16:13:27');
INSERT INTO `order_item` VALUES (64294, 64290, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 100000, 1, '2026-05-22 16:13:27');
INSERT INTO `order_item` VALUES (64295, 64290, 10928, '香奈儿五号浓香水 50ml', '/admin/dist/img/no-img.png', 119900, 1, '2026-05-22 16:13:27');
INSERT INTO `order_item` VALUES (64296, 64291, 10912, '华为 Mate 70 Pro 旗舰手机', '/admin/dist/img/no-img.png', 699900, 1, '2026-05-22 16:23:38');
INSERT INTO `order_item` VALUES (64297, 64292, 10927, '祖玛珑蓝风铃淡香水 30ml', '/admin/dist/img/no-img.png', 59900, 1, '2026-05-22 16:31:35');
INSERT INTO `order_item` VALUES (64298, 64293, 10927, '祖玛珑蓝风铃淡香水 30ml', '/admin/dist/img/no-img.png', 59900, 1, '2026-05-22 16:50:21');
INSERT INTO `order_item` VALUES (64299, 64295, 10912, '华为 Mate 70 Pro 旗舰手机', '/admin/dist/img/no-img.png', 699900, 1, '2026-05-22 17:26:35');
INSERT INTO `order_item` VALUES (64302, 64297, 10959, '亚瑟士 GEL-KAYANO 31 男稳定跑鞋', '/admin/dist/img/no-img.png', 109900, 3, '2026-05-30 11:06:15');
INSERT INTO `order_item` VALUES (64303, 64298, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 100, 3, '2026-05-30 11:59:31');

-- ----------------------------
-- Table structure for seckill_goods
-- ----------------------------
DROP TABLE IF EXISTS `seckill_goods`;
CREATE TABLE `seckill_goods`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `goods_id` bigint NOT NULL COMMENT '普通商品ID',
  `seckill_price` decimal(10, 2) NOT NULL COMMENT '秒杀价',
  `stock_count` int NOT NULL COMMENT '秒杀库存',
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '1启用 0禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_goods
-- ----------------------------
INSERT INTO `seckill_goods` VALUES (6, 10003, 99.00, 9, '2026-05-04 10:00:00', '2026-05-12 22:00:00', 0);
INSERT INTO `seckill_goods` VALUES (7, 10003, 100.00, 7, '2026-05-30 10:00:00', '2026-05-31 10:00:00', 1);

-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `goods_id` bigint NOT NULL,
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联的普通订单ID',
  `status` tinyint NULL DEFAULT 0 COMMENT '0处理中 1成功 2失败',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 752 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_order
-- ----------------------------
INSERT INTO `seckill_order` VALUES (2, 7, 10003, 6, 1, '2026-05-06 19:56:52', '2026-05-06 19:56:52');
INSERT INTO `seckill_order` VALUES (3, 7, 10003, 7, 1, '2026-05-06 19:59:41', '2026-05-06 19:59:41');
INSERT INTO `seckill_order` VALUES (740, 5, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (741, 6, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (742, 7, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (743, 8, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (744, 9, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (745, 10, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (746, 11, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (747, 12, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (748, 13, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (749, 14, 10003, 0, 1, '2026-05-08 18:30:29', '2026-05-08 18:30:29');
INSERT INTO `seckill_order` VALUES (750, 7, 10003, 0, 1, '2026-05-08 19:28:03', '2026-05-08 19:28:03');
INSERT INTO `seckill_order` VALUES (751, 1, 10003, 0, 1, '2026-05-30 11:59:31', '2026-05-30 11:59:31');

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物项主键id',
  `user_id` bigint NOT NULL COMMENT '用户主键id',
  `goods_id` bigint NOT NULL DEFAULT 0 COMMENT '关联商品id',
  `goods_count` int NOT NULL DEFAULT 1 COMMENT '数量(最大为5)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户主键id',
  `nick_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `login_name` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '登陆名称(默认为手机号)',
  `password` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT 'MD5加密后的密码',
  `introduce_sign` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '个性签名',
  `locked` tinyint NOT NULL DEFAULT 0 COMMENT '锁定标识字段(0-未锁定 1-已锁定)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '十三', '13700002703', 'e10adc3949ba59abbe56e057f20f883e', '我不怕千万人阻挡，只怕自己投降', 0, '2020-05-22 08:44:57');
INSERT INTO `user` VALUES (6, '陈尼克', '13711113333', 'e10adc3949ba59abbe56e057f20f883e', '测试用户陈尼克', 0, '2020-05-22 08:44:57');
INSERT INTO `user` VALUES (7, '张三1', '13864971605', 'fcea920f7412b5da7be0cf42b8c93759', '这个人很勤快，但还是什么都没有写', 0, '2026-04-14 15:18:26');
INSERT INTO `user` VALUES (8, '并非用户', '13864971606', 'fcea920f7412b5da7be0cf42b8c93759', '214124', 0, '2026-05-03 15:21:45');
INSERT INTO `user` VALUES (9, '测试01', 'test01', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (10, '测试02', 'test02', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (11, '测试03', 'test03', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (12, '测试04', 'test04', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (13, '测试05', 'test05', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (14, '测试06', 'test06', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (15, '测试07', 'test07', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (16, '测试08', 'test08', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (17, '测试09', 'test09', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (18, '测试10', 'test10', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (19, '测试11', 'test11', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (20, '测试12', 'test12', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (21, '测试13', 'test13', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (22, '测试14', 'test14', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (23, '测试15', 'test15', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (24, '测试16', 'test16', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (25, '测试17', 'test17', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (26, '测试18', 'test18', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (27, '测试19', 'test19', '123456', '', 0, '2026-05-06 22:22:59');
INSERT INTO `user` VALUES (28, '测试20', 'test20', '123456', '', 0, '2026-05-06 22:22:59');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL DEFAULT 0 COMMENT '用户主键id',
  `username` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '收货人姓名',
  `user_phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '收货人手机号',
  `is_default` tinyint NOT NULL DEFAULT 0 COMMENT '是否为默认 0-非默认 1-是默认',
  `province` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '省',
  `city` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '城',
  `region` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '区',
  `detail_address` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '收件详细地址(街道/楼宇/单元)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (1, 7, '11', '13864971051', 1, '上海市', '上海市', '徐汇区', '我的家在这里', '2026-05-01 10:32:07', '2026-05-02 11:57:55');
INSERT INTO `user_address` VALUES (2, 1, '张三', '149xxxx', 1, '北京市', '北京市', '朝阳区', '望京街道xxx', '2026-05-20 10:31:22', '2026-05-20 10:31:22');
INSERT INTO `user_address` VALUES (3, 1, '张三', '13800138000', 0, '北京市', '北京市', '朝阳区', '望京街道xxx', '2026-05-22 18:18:36', '2026-05-22 18:18:36');
INSERT INTO `user_address` VALUES (4, 1, '张三', '13800138000', 0, '浙江省', '杭州市', '西湖区', '文三路100号', '2026-05-28 08:21:25', '2026-05-28 08:21:25');

SET FOREIGN_KEY_CHECKS = 1;
