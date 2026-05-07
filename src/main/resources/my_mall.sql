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

 Date: 07/05/2026 08:37:21
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
) ENGINE = InnoDB AUTO_INCREMENT = 10912 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (10003, '迪奥小姐花漾淡香水', '40ml', 105, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', '商品介绍加载中...', 1030, 1000, 978, 0, 0, '2019-09-18 13:18:47', 1, '2026-05-06 23:11:11');
INSERT INTO `goods` VALUES (10005, '419美的（Midea）电煮锅', '多功能锅 1.7L', 20, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/cc49632b-5fb5-4b0b-bb36-3c1599a48543..jpg', '美的（Midea）电煮锅 电热锅 小电锅 宿舍小锅 学生寝室一体泡面小火锅多功能锅 1.7L 电煮锅小型1-2人 XZE1612', 59, 47, 998, 0, 0, '2019-09-18 13:18:47', 1, '2026-05-06 12:20:01');
INSERT INTO `goods` VALUES (10907, '双飞燕键盘', '键盘', 82, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/7edf4b07-2b99-4c7e-86d6-7c79a35ff5a9..png', '双飞燕（A4TECH）X7-G800V 128键QQ炫舞游戏专业键盘有线USB劲舞团打P吃鸡宏编程', 168, 149, 200, 0, 1, '2026-05-06 11:46:35', 1, '2026-05-06 11:46:35');
INSERT INTO `goods` VALUES (10908, '志高（CHIGO）加厚电热锅', '多功能炒煮一体锅家用电炒锅电蒸锅电火锅', 21, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/6b891a64-2dd7-4c84-8e0f-2869df73d386..jpg', '志高（CHIGO）加厚电热锅电锅多功能炒煮一体锅家用电炒锅电蒸锅电火锅电煮锅多用途 34CM一蒸笼', 155, 152, 300, 0, 1, '2026-05-06 12:21:18', 1, '2026-05-06 12:21:18');
INSERT INTO `goods` VALUES (10909, '米家【新品来袭】 扫地机器人6 ', '水箱版', 22, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/308677b8-659d-4f2a-bb21-d9ee8ff0287f..jpg', '米家【新品来袭】 扫地机器人6 水箱版 滚筒活水清洁去渍洗地机 扫拖一体全自动清洗基站 扫地机拖地', 2400, 2232, 1000, 0, 1, '2026-05-06 12:23:32', 1, '2026-05-06 12:23:32');
INSERT INTO `goods` VALUES (10910, '小米（MI）米家有线吸尘器', '家用有线手持大吸力吸尘机', 23, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/f5f2a957-fe72-40ce-977e-d05fb577e53e..jpg', '小米（MI）米家有线吸尘器 家用有线手持大吸力吸尘机吸猫狗毛清洁机 米家有线吸尘器', 185, 155, 7000, 0, 1, '2026-05-06 12:24:58', 1, '2026-05-06 12:25:11');
INSERT INTO `goods` VALUES (10911, '奥克斯（AUX）取暖器', '塔式石墨烯暖风机', 24, 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/1909f25c-79a4-4f23-93ed-584d3899ca62..jpg', '奥克斯（AUX）取暖器/电暖器/电暖气家用/取暖电器/电暖气暖风机电暖风热风机电热扇塔式石墨烯暖风机NSBE-200GS', 89, 80, 700, 0, 1, '2026-05-06 12:26:51', 1, '2026-05-06 12:26:51');

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
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '1-搜索框热搜 2-搜索下拉框热搜 3-(首页)热销商品 4-(首页)新品上线 5-(首页)为你推荐',
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
INSERT INTO `index_config` VALUES (11, 'MAC 磨砂系列', 5, 10237, '##', 103, '2019-09-18 17:47:44', 0, '2026-05-02 22:00:10', 1);
INSERT INTO `index_config` VALUES (14, '小米 Redmi AirDots', 5, 10160, '##', 100, '2019-09-18 17:49:28', 0, '2019-09-18 17:49:28', 0);
INSERT INTO `index_config` VALUES (15, '2019 MacBookAir 13', 5, 10254, '##', 100, '2019-09-18 17:50:18', 0, '2019-09-18 17:50:18', 0);
INSERT INTO `index_config` VALUES (16, '女式粗棉线条纹长袖T恤', 5, 10158, '##', 99, '2019-09-18 17:52:03', 0, '2019-09-18 17:52:03', 0);
INSERT INTO `index_config` VALUES (17, '塑料浴室座椅', 5, 10154, '##', 100, '2019-09-18 17:52:19', 0, '2019-09-18 17:52:19', 0);
INSERT INTO `index_config` VALUES (19, '小型超声波香薰机', 5, 10113, '##', 100, '2019-09-18 17:54:07', 0, '2019-09-18 17:54:07', 0);
INSERT INTO `index_config` VALUES (24, '华为 Mate 30 Pro', 5, 10894, '##', 101, '2019-09-19 23:27:00', 0, '2019-09-19 23:27:00', 0);
INSERT INTO `index_config` VALUES (486, '213213', 5, 10172, '##', 3, '2026-05-02 22:00:21', 1, '2026-05-02 22:00:21', 1);
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
  `order_status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.完成订单关闭 -2.超时关闭 -3.取消订单关闭',
  `extra_info` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '订单body',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最新修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (3, '20260502120417096379', 7, 8598, 1, 1, '2026-05-03 11:30:24', 4, '', '2026-05-02 12:04:17', '2026-05-03 14:09:01');
INSERT INTO `order` VALUES (4, '20260502170938521287', 7, 9258, 0, 0, NULL, -1, '', '2026-05-02 17:09:39', '2026-05-03 13:30:00');
INSERT INTO `order` VALUES (6, '20260506195652207166', 7, 99, 0, 0, NULL, -1, '', '2026-05-06 19:56:52', '2026-05-06 22:30:00');
INSERT INTO `order` VALUES (7, '20260506195940754767', 7, 99, 0, 0, NULL, -1, '', '2026-05-06 19:59:41', '2026-05-06 22:30:00');

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '订单收货地址关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_address
-- ----------------------------
INSERT INTO `order_address` VALUES (1, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '3');
INSERT INTO `order_address` VALUES (2, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '4');
INSERT INTO `order_address` VALUES (3, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '6');
INSERT INTO `order_address` VALUES (4, '11', '13864971051', '上海市', '上海市', '徐汇区', '我的家在这里', '7');

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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (7, 6, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 99, 1, '2026-05-06 19:56:52');
INSERT INTO `order_item` VALUES (8, 7, 10003, '迪奥小姐花漾淡香水', 'https://liuyijia-jiava.oss-cn-beijing.aliyuncs.com/a28f956b-3df8-4f46-8701-ca7d93a98786..jpg', 99, 1, '2026-05-06 19:59:41');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_goods
-- ----------------------------
INSERT INTO `seckill_goods` VALUES (6, 10003, 99.00, 0, '2026-05-04 10:00:00', '2026-05-12 22:00:00', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of seckill_order
-- ----------------------------
INSERT INTO `seckill_order` VALUES (2, 7, 10003, 6, 1, '2026-05-06 19:56:52', '2026-05-06 19:56:52');
INSERT INTO `seckill_order` VALUES (3, 7, 10003, 7, 1, '2026-05-06 19:59:41', '2026-05-06 19:59:41');
INSERT INTO `seckill_order` VALUES (4, 1, 10003, 1, 1, '2026-05-06 23:11:10', '2026-05-06 23:11:10');
INSERT INTO `seckill_order` VALUES (5, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (6, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (7, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (8, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (9, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (10, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (11, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (12, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (13, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (14, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (15, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (16, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (17, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (18, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (19, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (20, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (21, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (22, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');
INSERT INTO `seckill_order` VALUES (23, 1, 10003, 1, 1, '2026-05-06 23:11:11', '2026-05-06 23:11:11');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '收货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (1, 7, '11', '13864971051', 1, '上海市', '上海市', '徐汇区', '我的家在这里', '2026-05-01 10:32:07', '2026-05-02 11:57:55');

SET FOREIGN_KEY_CHECKS = 1;
