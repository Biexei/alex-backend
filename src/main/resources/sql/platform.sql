/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2021-04-09 19:18:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_data_factory
-- ----------------------------
DROP TABLE IF EXISTS `t_data_factory`;
CREATE TABLE `t_data_factory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '数据工厂名称',
  `type` tinyint DEFAULT NULL COMMENT '类型 0SQL 1接口 2ui',
  `times` int DEFAULT NULL COMMENT '执行次数',
  `failed_stop` tinyint DEFAULT NULL COMMENT '遇到错误停止运行 0是1否',
  `sql_run_dev` tinyint DEFAULT NULL COMMENT '0dev1test2stg3prod4debug',
  `sql_db_id` int DEFAULT NULL COMMENT '数据源编号',
  `sql_str` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'sql语句',
  `interface_suite_id` int DEFAULT NULL COMMENT '接口用例编号',
  `ui_suite_id` int DEFAULT NULL COMMENT 'ui用例编号',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `execute_type` tinyint DEFAULT NULL COMMENT '执行方式 0并行1串行',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_data_factory
-- ----------------------------
INSERT INTO `t_data_factory` VALUES ('1', '执行平台测试套件', '1', '100', '1', null, null, null, '35', null, '2020-11-30 16:34:30', '2020-12-16 13:37:03', '0');

-- ----------------------------
-- Table structure for t_db
-- ----------------------------
DROP TABLE IF EXISTS `t_db`;
CREATE TABLE `t_db` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `type` tinyint DEFAULT NULL COMMENT '0mysql 1oracle 2SqlServer',
  `desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '连接url',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint DEFAULT NULL COMMENT '0启动 1禁用',
  `test_url` varchar(100) DEFAULT NULL COMMENT '测试环境url',
  `test_username` varchar(100) DEFAULT NULL COMMENT '测试环境username',
  `test_password` varchar(100) DEFAULT NULL COMMENT '测试环境password',
  `dev_url` varchar(100) DEFAULT NULL COMMENT '开发环境url',
  `dev_username` varchar(100) DEFAULT NULL COMMENT '开发环境username',
  `dev_password` varchar(100) DEFAULT NULL COMMENT '开发环境password',
  `stg_url` varchar(100) DEFAULT NULL COMMENT '预上线环境url',
  `stg_username` varchar(100) DEFAULT NULL COMMENT '预上线环境username',
  `stg_password` varchar(100) DEFAULT NULL COMMENT '预上线环境password',
  `prod_url` varchar(100) DEFAULT NULL COMMENT '生产环境url',
  `prod_username` varchar(100) DEFAULT NULL COMMENT '生产环境username',
  `prod_password` varchar(100) DEFAULT NULL COMMENT '生产环境password',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_db
-- ----------------------------
INSERT INTO `t_db` VALUES ('1', 'platform', '0', '自动化平台库', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8', 'root', 'root', '2020-09-02 15:40:50', '2020-10-21 11:31:11', '0', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8', 'root', 'root', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8', 'root', 'root', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8', 'root', 'root', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8', 'root', 'root');

-- ----------------------------
-- Table structure for t_http_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_http_setting`;
CREATE TABLE `t_http_setting` (
  `setting_id` int NOT NULL AUTO_INCREMENT COMMENT '配置编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '配置项名称',
  `value` varchar(50) DEFAULT NULL,
  `desc` varchar(50) DEFAULT NULL COMMENT '配置项描述',
  `status` tinyint DEFAULT NULL COMMENT '状态 0启用1禁用',
  `type` tinyint DEFAULT NULL COMMENT '类型 0代理1公共头2邮箱',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_http_setting
-- ----------------------------
INSERT INTO `t_http_setting` VALUES ('7', '本地代理', 'localhost:8888', '', '1', '0', '2020-10-02 16:14:57', '2021-04-09 13:04:53');
INSERT INTO `t_http_setting` VALUES ('11', 'qq', '1014759718@qq.com', '', '0', '2', '2020-10-02 18:57:14', '2020-12-14 16:22:22');
INSERT INTO `t_http_setting` VALUES ('12', '163', 'biexei@163.com', '', '0', '2', '2020-10-04 19:21:30', '2020-12-14 16:22:21');
INSERT INTO `t_http_setting` VALUES ('13', '126', 'biexei@126.com', '', '0', '2', '2020-10-04 21:10:46', '2020-12-14 16:22:22');

-- ----------------------------
-- Table structure for t_interface_assert
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_assert`;
CREATE TABLE `t_interface_assert` (
  `assert_id` int NOT NULL AUTO_INCREMENT COMMENT '断言编号',
  `assert_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '断言名称',
  `case_id` int DEFAULT NULL COMMENT '用例编号',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   0json/1html/2header/3responsecode/4runtime',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `operator` tinyint DEFAULT NULL COMMENT '操作符0/=、1/< 、2/>、3/<=、4/>=、5/in、6/!=、7/re、8/isNull、9/notNull',
  `excepted_result` varchar(1000) DEFAULT NULL COMMENT '预期结果',
  `order` int DEFAULT NULL COMMENT '排序 执行断言时按照该字段排序',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`assert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=573 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert
-- ----------------------------
INSERT INTO `t_interface_assert` VALUES ('265', 'HTTP响应状态码', '277', '3', null, '0', '200', '0', '2021-03-08 15:25:56', '2021-03-08 15:25:56');
INSERT INTO `t_interface_assert` VALUES ('266', '接口状态码', '277', '0', '$..code', '0', '200', '1', '2021-03-08 15:25:56', '2021-03-08 15:25:56');
INSERT INTO `t_interface_assert` VALUES ('268', '接口状态码', '279', '0', '$..code', '0', '200', '0', '2021-03-08 15:33:36', '2021-03-08 15:33:36');
INSERT INTO `t_interface_assert` VALUES ('269', '接口状态码', '280', '0', '$..code', '0', '200', '0', '2021-03-08 15:34:20', '2021-03-13 15:48:55');
INSERT INTO `t_interface_assert` VALUES ('270', 'url', '280', '0', '$..data.url', '0', '${InterfaceCaseTable(\"$..url\",\"#{id}\")}', '1', '2021-03-08 15:36:25', '2021-03-13 15:48:55');
INSERT INTO `t_interface_assert` VALUES ('272', 'level', '280', '0', '$..data.level', '0', '${InterfaceCaseTable(\"$..level\",\"#{id}\")}', '2', '2021-03-08 16:12:12', '2021-03-13 15:48:55');
INSERT INTO `t_interface_assert` VALUES ('281', '执行耗时', '280', '4', null, '1', '5', '3', '2021-03-09 09:22:25', '2021-03-13 15:48:55');
INSERT INTO `t_interface_assert` VALUES ('282', 'content-type', '280', '2', 'Content-Type', '0', 'application/json', '4', '2021-03-13 15:48:55', '2021-03-13 15:48:55');
INSERT INTO `t_interface_assert` VALUES ('283', '接口状态码', '283', '0', '$..code', '0', '200', '0', '2021-03-30 14:03:41', '2021-04-05 14:06:58');
INSERT INTO `t_interface_assert` VALUES ('284', '接口状态码', '284', '0', '$..code', '0', '200', '0', '2021-04-05 15:12:13', '2021-04-06 16:08:54');
INSERT INTO `t_interface_assert` VALUES ('285', 'http状态码需等于200', '285', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('286', '接口状态码不等于200', '285', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('287', 'http状态码需等于200', '286', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('288', '接口状态码不等于200', '286', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('289', 'http状态码需等于200', '287', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('290', '接口状态码不等于200', '287', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('291', 'http状态码需等于200', '288', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('292', '接口状态码不等于200', '288', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('293', 'http状态码需等于200', '289', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('294', '接口状态码不等于200', '289', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('295', 'http状态码需等于200', '290', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('296', '接口状态码不等于200', '290', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('297', 'http状态码需等于200', '291', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('298', '接口状态码不等于200', '291', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('299', 'http状态码需等于200', '292', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('300', '接口状态码不等于200', '292', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('301', 'http状态码需等于200', '293', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('302', '接口状态码不等于200', '293', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('303', 'http状态码需等于200', '294', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('304', '接口状态码不等于200', '294', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('305', 'http状态码需等于200', '295', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('306', '接口状态码需等于200', '295', '0', '$..code', '0', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('307', 'http状态码需等于200', '296', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('308', '接口状态码不等于200', '296', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('309', 'http状态码需等于200', '297', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('310', '接口状态码不等于200', '297', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('311', 'http状态码需等于200', '298', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('312', '接口状态码不等于200', '298', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('313', 'http状态码需等于200', '299', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('314', '接口状态码不等于200', '299', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('315', 'http状态码需等于200', '300', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('316', '接口状态码不等于200', '300', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('317', 'http状态码需等于200', '301', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('318', '接口状态码不等于200', '301', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('319', 'http状态码需等于200', '302', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('320', '接口状态码不等于200', '302', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('321', 'http状态码需等于200', '303', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('322', '接口状态码不等于200', '303', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('323', 'http状态码需等于200', '304', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('324', '接口状态码不等于200', '304', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('325', 'http状态码需等于200', '305', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('326', '接口状态码不等于200', '305', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('327', 'http状态码需等于200', '306', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('328', '接口状态码不等于200', '306', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('329', 'http状态码需等于200', '307', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('330', '接口状态码不等于200', '307', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('331', 'http状态码需等于200', '308', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('332', '接口状态码不等于200', '308', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('333', 'http状态码需等于200', '309', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('334', '接口状态码不等于200', '309', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('335', 'http状态码需等于200', '310', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('336', '接口状态码不等于200', '310', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('337', 'http状态码需等于200', '311', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('338', '接口状态码不等于200', '311', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('339', 'http状态码需等于200', '312', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('340', '接口状态码不等于200', '312', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('341', 'http状态码需等于200', '313', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('342', '接口状态码不等于200', '313', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('343', 'http状态码需等于200', '314', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('344', '接口状态码需等于200', '314', '0', '$..code', '0', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('345', 'http状态码需等于200', '315', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('346', '接口状态码不等于200', '315', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('347', 'http状态码需等于200', '316', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('348', '接口状态码不等于200', '316', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('349', 'http状态码需等于200', '317', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('350', '接口状态码不等于200', '317', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('351', 'http状态码需等于200', '318', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('352', '接口状态码不等于200', '318', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('353', 'http状态码需等于200', '319', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('354', '接口状态码不等于200', '319', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('355', 'http状态码需等于200', '320', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('356', '接口状态码不等于200', '320', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('357', 'http状态码需等于200', '321', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('358', '接口状态码不等于200', '321', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('359', 'http状态码需等于200', '322', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('360', '接口状态码不等于200', '322', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('361', 'http状态码需等于200', '323', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('362', '接口状态码不等于200', '323', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('363', 'http状态码需等于200', '324', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('364', '接口状态码不等于200', '324', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('365', 'http状态码需等于200', '325', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('366', '接口状态码不等于200', '325', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('367', 'http状态码需等于200', '326', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('368', '接口状态码不等于200', '326', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('369', 'http状态码需等于200', '327', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('370', '接口状态码不等于200', '327', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('371', 'http状态码需等于200', '328', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('372', '接口状态码不等于200', '328', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('373', 'http状态码需等于200', '329', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('374', '接口状态码不等于200', '329', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('375', 'http状态码需等于200', '330', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('376', '接口状态码不等于200', '330', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('377', 'http状态码需等于200', '331', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('378', '接口状态码不等于200', '331', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('379', 'http状态码需等于200', '332', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('380', '接口状态码不等于200', '332', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('381', 'http状态码需等于200', '333', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('382', '接口状态码不等于200', '333', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('383', 'http状态码需等于200', '334', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('384', '接口状态码不等于200', '334', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('385', 'http状态码需等于200', '335', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('386', '接口状态码不等于200', '335', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('387', 'http状态码需等于200', '336', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('388', '接口状态码不等于200', '336', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('389', 'http状态码需等于200', '337', '3', null, '0', '200', '1', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('390', '接口状态码不等于200', '337', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:28', '2021-04-09 14:12:28');
INSERT INTO `t_interface_assert` VALUES ('391', 'http状态码需等于200', '338', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('392', '接口状态码不等于200', '338', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('393', 'http状态码需等于200', '339', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('394', '接口状态码不等于200', '339', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('395', 'http状态码需等于200', '340', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('396', '接口状态码不等于200', '340', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('397', 'http状态码需等于200', '341', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('398', '接口状态码不等于200', '341', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('399', 'http状态码需等于200', '342', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('400', '接口状态码不等于200', '342', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('401', 'http状态码需等于200', '343', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('402', '接口状态码不等于200', '343', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('403', 'http状态码需等于200', '344', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('404', '接口状态码不等于200', '344', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('405', 'http状态码需等于200', '345', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('406', '接口状态码不等于200', '345', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('407', 'http状态码需等于200', '346', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('408', '接口状态码不等于200', '346', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('409', 'http状态码需等于200', '347', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('410', '接口状态码不等于200', '347', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('411', 'http状态码需等于200', '348', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('412', '接口状态码不等于200', '348', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('413', 'http状态码需等于200', '349', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('414', '接口状态码不等于200', '349', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('415', 'http状态码需等于200', '350', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('416', '接口状态码不等于200', '350', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('417', 'http状态码需等于200', '351', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('418', '接口状态码不等于200', '351', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('419', 'http状态码需等于200', '352', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('420', '接口状态码不等于200', '352', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('421', 'http状态码需等于200', '353', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('422', '接口状态码不等于200', '353', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('423', 'http状态码需等于200', '354', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('424', '接口状态码不等于200', '354', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('425', 'http状态码需等于200', '355', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('426', '接口状态码不等于200', '355', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('427', 'http状态码需等于200', '356', '3', null, '0', '200', '1', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('428', '接口状态码不等于200', '356', '0', '$..code', '6', '200', '2', '2021-04-09 14:12:29', '2021-04-09 14:12:29');
INSERT INTO `t_interface_assert` VALUES ('429', 'http状态码需等于200', '357', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('430', '接口状态码不等于200', '357', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('431', 'http状态码需等于200', '358', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('432', '接口状态码不等于200', '358', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('433', 'http状态码需等于200', '359', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('434', '接口状态码不等于200', '359', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('435', 'http状态码需等于200', '360', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('436', '接口状态码不等于200', '360', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('437', 'http状态码需等于200', '361', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('438', '接口状态码不等于200', '361', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('439', 'http状态码需等于200', '362', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('440', '接口状态码不等于200', '362', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('441', 'http状态码需等于200', '363', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('442', '接口状态码不等于200', '363', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('443', 'http状态码需等于200', '364', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('444', '接口状态码不等于200', '364', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('445', 'http状态码需等于200', '365', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('446', '接口状态码不等于200', '365', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('447', 'http状态码需等于200', '366', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('448', '接口状态码不等于200', '366', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('449', 'http状态码需等于200', '367', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('450', '接口状态码需等于200', '367', '0', '$..code', '0', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('451', 'http状态码需等于200', '368', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('452', '接口状态码不等于200', '368', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('453', 'http状态码需等于200', '369', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('454', '接口状态码不等于200', '369', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('455', 'http状态码需等于200', '370', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('456', '接口状态码不等于200', '370', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('457', 'http状态码需等于200', '371', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('458', '接口状态码不等于200', '371', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('459', 'http状态码需等于200', '372', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('460', '接口状态码不等于200', '372', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('461', 'http状态码需等于200', '373', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('462', '接口状态码不等于200', '373', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('463', 'http状态码需等于200', '374', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('464', '接口状态码不等于200', '374', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('465', 'http状态码需等于200', '375', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('466', '接口状态码不等于200', '375', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('467', 'http状态码需等于200', '376', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('468', '接口状态码不等于200', '376', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('469', 'http状态码需等于200', '377', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('470', '接口状态码不等于200', '377', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('471', 'http状态码需等于200', '378', '3', null, '0', '200', '1', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('472', '接口状态码不等于200', '378', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:21', '2021-04-09 14:48:21');
INSERT INTO `t_interface_assert` VALUES ('473', 'http状态码需等于200', '379', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('474', '接口状态码不等于200', '379', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('475', 'http状态码需等于200', '380', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('476', '接口状态码不等于200', '380', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('477', 'http状态码需等于200', '381', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('478', '接口状态码不等于200', '381', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('479', 'http状态码需等于200', '382', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('480', '接口状态码不等于200', '382', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('481', 'http状态码需等于200', '383', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('482', '接口状态码不等于200', '383', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('483', 'http状态码需等于200', '384', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('484', '接口状态码不等于200', '384', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('485', 'http状态码需等于200', '385', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('486', '接口状态码不等于200', '385', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('487', 'http状态码需等于200', '386', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('488', '接口状态码需等于200', '386', '0', '$..code', '0', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('489', 'http状态码需等于200', '387', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('490', '接口状态码不等于200', '387', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('491', 'http状态码需等于200', '388', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('492', '接口状态码不等于200', '388', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('493', 'http状态码需等于200', '389', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('494', '接口状态码不等于200', '389', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('495', 'http状态码需等于200', '390', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('496', '接口状态码不等于200', '390', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('497', 'http状态码需等于200', '391', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('498', '接口状态码不等于200', '391', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('499', 'http状态码需等于200', '392', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('500', '接口状态码不等于200', '392', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('501', 'http状态码需等于200', '393', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('502', '接口状态码不等于200', '393', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('503', 'http状态码需等于200', '394', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('504', '接口状态码不等于200', '394', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('505', 'http状态码需等于200', '395', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('506', '接口状态码不等于200', '395', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('507', 'http状态码需等于200', '396', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('508', '接口状态码不等于200', '396', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('509', 'http状态码需等于200', '397', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('510', '接口状态码不等于200', '397', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('511', 'http状态码需等于200', '398', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('512', '接口状态码不等于200', '398', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('513', 'http状态码需等于200', '399', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('514', '接口状态码不等于200', '399', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('515', 'http状态码需等于200', '400', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('516', '接口状态码不等于200', '400', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('517', 'http状态码需等于200', '401', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('518', '接口状态码不等于200', '401', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('519', 'http状态码需等于200', '402', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('520', '接口状态码不等于200', '402', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('521', 'http状态码需等于200', '403', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('522', '接口状态码不等于200', '403', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('523', 'http状态码需等于200', '404', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('524', '接口状态码不等于200', '404', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('525', 'http状态码需等于200', '405', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('526', '接口状态码不等于200', '405', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('527', 'http状态码需等于200', '406', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('528', '接口状态码不等于200', '406', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('529', 'http状态码需等于200', '407', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('530', '接口状态码不等于200', '407', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('531', 'http状态码需等于200', '408', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('532', '接口状态码不等于200', '408', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('533', 'http状态码需等于200', '409', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('534', '接口状态码不等于200', '409', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('535', 'http状态码需等于200', '410', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('536', '接口状态码不等于200', '410', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('537', 'http状态码需等于200', '411', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('538', '接口状态码不等于200', '411', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('539', 'http状态码需等于200', '412', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('540', '接口状态码不等于200', '412', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('541', 'http状态码需等于200', '413', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('542', '接口状态码不等于200', '413', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('543', 'http状态码需等于200', '414', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('544', '接口状态码不等于200', '414', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('545', 'http状态码需等于200', '415', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('546', '接口状态码不等于200', '415', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('547', 'http状态码需等于200', '416', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('548', '接口状态码不等于200', '416', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('549', 'http状态码需等于200', '417', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('550', '接口状态码不等于200', '417', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('551', 'http状态码需等于200', '418', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('552', '接口状态码不等于200', '418', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('553', 'http状态码需等于200', '419', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('554', '接口状态码不等于200', '419', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('555', 'http状态码需等于200', '420', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('556', '接口状态码不等于200', '420', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('557', 'http状态码需等于200', '421', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('558', '接口状态码不等于200', '421', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('559', 'http状态码需等于200', '422', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('560', '接口状态码不等于200', '422', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('561', 'http状态码需等于200', '423', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('562', '接口状态码不等于200', '423', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('563', 'http状态码需等于200', '424', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('564', '接口状态码不等于200', '424', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('565', 'http状态码需等于200', '425', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('566', '接口状态码不等于200', '425', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('567', 'http状态码需等于200', '426', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('568', '接口状态码不等于200', '426', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('569', 'http状态码需等于200', '427', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('570', '接口状态码不等于200', '427', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('571', 'http状态码需等于200', '428', '3', null, '0', '200', '1', '2021-04-09 14:48:22', '2021-04-09 14:48:22');
INSERT INTO `t_interface_assert` VALUES ('572', '接口状态码不等于200', '428', '0', '$..code', '6', '200', '2', '2021-04-09 14:48:22', '2021-04-09 14:48:22');

-- ----------------------------
-- Table structure for t_interface_assert_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_assert_log`;
CREATE TABLE `t_interface_assert_log` (
  `assert_log_id` int NOT NULL AUTO_INCREMENT COMMENT '断言日志id',
  `execute_log_id` int DEFAULT NULL COMMENT '执行日志id',
  `assert_id` int DEFAULT NULL COMMENT '断言id',
  `assert_name` varchar(100) DEFAULT NULL COMMENT '断言名称',
  `case_id` int DEFAULT NULL COMMENT '测试用例编号id',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   0json/1html/2header/3responsecode/4runtime',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `operator` tinyint DEFAULT NULL COMMENT '操作符0/=、1/< 、2/>、3/<=、4/>=、5/in、6/!=、7/re、8/isNull、9/notNull',
  `excepted_result` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '预期结果',
  `raw_excepted_result` mediumtext COMMENT '未清洗前的预期结果',
  `order` int DEFAULT NULL COMMENT '排序 执行断言时按照该字段排序',
  `actual_result` mediumtext COMMENT '实际运行结果',
  `status` tinyint DEFAULT NULL COMMENT '是否通过 0通过 1失败 2错误',
  `error_message` mediumtext COMMENT '断言出错异常信息',
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`assert_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface_case
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case`;
CREATE TABLE `t_interface_case` (
  `project_id` int DEFAULT NULL COMMENT '项目名称',
  `module_id` int DEFAULT NULL COMMENT '模块编号',
  `case_id` int NOT NULL AUTO_INCREMENT COMMENT '用例编号',
  `url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `method` tinyint(1) DEFAULT NULL COMMENT '请求方式  0get,1post,2patch,3put,4delete',
  `desc` varchar(1000) DEFAULT NULL COMMENT '用例描述',
  `level` tinyint DEFAULT NULL COMMENT '用例级别0高，1中，2低',
  `doc` varchar(200) DEFAULT NULL COMMENT '接口文档地址',
  `headers` varchar(1000) DEFAULT NULL COMMENT '请求头',
  `params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `data` varchar(1000) DEFAULT NULL COMMENT '请求formdata',
  `json` varchar(1000) DEFAULT NULL COMMENT '请求json',
  `creater` varchar(20) DEFAULT NULL COMMENT '用例创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  `source` tinyint DEFAULT NULL COMMENT '来源0新增1excel导入2csv导入3json导入4yaml导入',
  `import_no` varchar(100) DEFAULT NULL COMMENT '导入编号',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=429 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case
-- ----------------------------
INSERT INTO `t_interface_case` VALUES ('24', '14', '277', '/user/login', '1', '用户登录', '0', null, '', '', '{\"username\":\"123\",\"password\":\"123\"}', null, '系统管理员', '2021-03-08 15:25:56', '2021-03-08 15:25:56', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '279', '/interface/case/list', '0', '查看用例列表', '0', null, '{\"Token\":\"${Token}\"}', '', null, null, '超级管理员', '2021-03-08 15:33:36', '2021-03-08 15:33:36', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '280', '/interface/case/info/{id}', '0', '查看用例详情', '0', null, '{\"Token\":\"${Token}\"}', '{\"id\":\"277\"}', null, null, '超级管理员', '2021-03-08 15:34:20', '2021-03-13 15:48:55', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '283', '/user/info/{userId}', '0', '查看新增用户详情', '0', null, '{\"Token\":\"${Token}\"}', '{\"userId\": \"${select(\"1\", \"select username from t_user\", \"string\")}\"}', null, null, '123', '2021-03-30 14:03:41', '2021-04-05 14:06:58', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '284', '/user/list?pageNum=1&pageSize=10', '0', '查看用户列表', '0', null, '{\"Token\":\"${Token}\"}', '{\"username\": \"${md5(\"123456\")}\"}', null, null, '123', '2021-04-05 15:12:13', '2021-04-06 16:08:54', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '357', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为null 真实姓名|realName|为null *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':null,\'sex\':null,\'username\':null}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '358', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 *密码|password|为空字符串 真实姓名|realName|为空字符串 *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'\',\'sex\':null,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '359', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 密码|password|长度在区间[3,20]内 真实姓名|realName|长度在区间[1,10]内 *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'fo9yx37n\',\'sex\':null,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '360', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 密码|password|长度恰好为最大长度|20| 真实姓名|realName|长度恰好为最大长度|10| *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':null,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '361', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 密码|password|长度恰好为最小长度|3| 真实姓名|realName|长度恰好为最小长度|1| 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'uyw\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '362', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 *密码|password|长度为最大长度|20|+1 *真实姓名|realName|长度为最大长度|10|+1 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '363', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 *密码|password|长度为最小长度|3|-1 *真实姓名|realName|长度为最小长度|1|-1 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'bf\',\'sex\':1,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '364', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|非法字符长度在区间[3,20]内 *真实姓名|realName|非法字符长度在区间[1,10]内 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '365', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最小长度|3|-1 真实姓名|realName|长度在区间[1,10]内-重复 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'bf\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '366', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 *密码|password|长度为最大长度|20|+1 真实姓名|realName|长度在区间[1,10]内-重复 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':1,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '367', '/user/register', '1', '有效等价类 用户注册接口 用户名|username|值不存在于表内 密码|password|长度恰好为最小长度|3| 真实姓名|realName|长度恰好为最大长度|10| 性别|sex|值存在于数组内-重复', '0', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'uyw\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '368', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 密码|password|非法字符长度在区间[3,20]内 真实姓名|realName|为null 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '369', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 *密码|password|为null 真实姓名|realName|为空字符串 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':null,\'sex\':397618,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '370', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 *密码|password|为空字符串 *真实姓名|realName|长度为最小长度|1|-1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'\',\'sex\':397618,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '371', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 密码|password|长度恰好为最大长度|20| *真实姓名|realName|非法字符长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':397618,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '372', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度在区间[3,20]内 真实姓名|realName|长度恰好为最小长度|1| 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'fo9yx37n\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '373', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最大长度|20| *真实姓名|realName|长度为最大长度|10|+1 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '374', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 密码|password|长度恰好为最小长度|3| 真实姓名|realName|长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'uyw\',\'sex\':397618,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '375', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 *密码|password|为null 真实姓名|realName|长度恰好为最小长度|1| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':null,\'sex\':397618,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '376', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 *密码|password|长度为最小长度|3|-1 *真实姓名|realName|非法字符长度在区间[1,10]内 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'bf\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '377', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 密码|password|长度在区间[3,20]内 *真实姓名|realName|长度为最大长度|10|+1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'fo9yx37n\',\'sex\':397618,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '378', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 密码|password|非法字符长度在区间[3,20]内 真实姓名|realName|长度在区间[1,10]内-重复 *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':null,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:21', '2021-04-09 14:48:21', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '379', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 *密码|password|为null 真实姓名|realName|长度恰好为最大长度|10| 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':null,\'sex\':1,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '380', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为空字符串 真实姓名|realName|长度在区间[1,10]内 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '381', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最大长度|20|+1 *真实姓名|realName|长度为最小长度|1|-1 *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':null,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '382', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 密码|password|非法字符长度在区间[3,20]内 真实姓名|realName|长度恰好为最小长度|1| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':397618,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '383', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 *密码|password|长度为最小长度|3|-1 真实姓名|realName|为空字符串 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'bf\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '384', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 *密码|password|长度为最大长度|20|+1 真实姓名|realName|长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':397618,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '385', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 *密码|password|为空字符串 真实姓名|realName|长度在区间[1,10]内-重复 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '386', '/user/register', '1', '有效等价类 用户注册接口 用户名|username|值不存在于表内 密码|password|长度恰好为最大长度|20| 真实姓名|realName|为null 性别|sex|值存在于数组内', '0', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'mqore90vq56r3xltsufc\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '387', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 密码|password|长度在区间[3,20]内 真实姓名|realName|为null *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'fo9yx37n\',\'sex\':397618,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '388', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最小长度|3| 真实姓名|realName|为空字符串 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'uyw\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '389', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最小长度|3| *真实姓名|realName|非法字符长度在区间[1,10]内 *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'uyw\',\'sex\':null,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '390', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度在区间[3,20]内 真实姓名|realName|长度恰好为最大长度|10| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'fo9yx37n\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '391', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内 *密码|password|长度为最小长度|3|-1 *真实姓名|realName|长度为最大长度|10|+1 *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'bf\',\'sex\':null,\'username\':\'1617600148708\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '392', '/user/register', '1', '无效等价类 用户注册接口 用户名|username|值不存在于表内 *密码|password|为null *真实姓名|realName|非法字符长度在区间[1,10]内 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':null,\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '393', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|值存在于表内-重复 密码|password|长度在区间[3,20]内 *真实姓名|realName|长度为最小长度|1|-1 性别|sex|值存在于数组内-重复', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'fo9yx37n\',\'sex\':1,\'username\':\'h3pdzkeimwflm\'}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '394', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最大长度|20| 真实姓名|realName|长度在区间[1,10]内 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '395', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为空字符串 真实姓名|realName|长度恰好为最小长度|1| *性别|sex|为null', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'\',\'sex\':null,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '396', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最大长度|20|+1 真实姓名|realName|长度恰好为最小长度|1| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '397', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|非法字符长度在区间[3,20]内 真实姓名|realName|长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '398', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度在区间[3,20]内 *真实姓名|realName|非法字符长度在区间[1,10]内 性别|sex|值存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'fo9yx37n\',\'sex\':1,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '399', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|非法字符长度在区间[3,20]内 真实姓名|realName|为空字符串 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '400', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最大长度|20|+1 *真实姓名|realName|非法字符长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '401', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为空字符串 *真实姓名|realName|非法字符长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'$<!<!.-#\',\'password\':\'\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '402', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最大长度|20| 真实姓名|realName|长度恰好为最小长度|1| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '403', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为null 真实姓名|realName|长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':null,\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '404', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最小长度|3|-1 真实姓名|realName|长度在区间[1,10]内 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'bf\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '405', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最小长度|3| 真实姓名|realName|为null *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'uyw\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '406', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最小长度|3| *真实姓名|realName|长度为最小长度|1|-1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'uyw\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '407', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最小长度|3|-1 真实姓名|realName|长度恰好为最小长度|1| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'2\',\'password\':\'bf\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '408', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为null *真实姓名|realName|长度为最小长度|1|-1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':null,\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '409', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最大长度|20| *真实姓名|realName|长度为最小长度|1|-1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '410', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为空字符串 真实姓名|realName|为null *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '411', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最大长度|20|+1 真实姓名|realName|为null *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '412', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|非法字符长度在区间[3,20]内 *真实姓名|realName|长度为最小长度|1|-1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '413', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|非法字符长度在区间[3,20]内 真实姓名|realName|长度恰好为最大长度|10| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '414', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最大长度|20|+1 真实姓名|realName|长度恰好为最大长度|10| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '415', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为空字符串 真实姓名|realName|长度恰好为最大长度|10| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '416', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最大长度|20| 真实姓名|realName|为空字符串 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '417', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为null 真实姓名|realName|长度在区间[1,10]内-重复 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':null,\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '418', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最小长度|3|-1 真实姓名|realName|长度恰好为最大长度|10| *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'cb7onnk5rm\',\'password\':\'bf\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '419', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最小长度|3| 真实姓名|realName|长度在区间[1,10]内-重复 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'uyw\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '420', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度在区间[3,20]内 真实姓名|realName|长度在区间[1,10]内-重复 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'fo9yx37n\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '421', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度在区间[3,20]内 真实姓名|realName|为空字符串 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'fo9yx37n\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '422', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最小长度|3| *真实姓名|realName|长度为最大长度|10|+1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'uyw\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '423', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最小长度|3|-1 真实姓名|realName|为null *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':null,\'password\':\'bf\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '424', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为null *真实姓名|realName|长度为最大长度|10|+1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':null,\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '425', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|长度恰好为最大长度|20| 真实姓名|realName|长度在区间[1,10]内-重复 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'yirgphsyap\',\'password\':\'mqore90vq56r3xltsufc\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '426', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|为空字符串 *真实姓名|realName|长度为最大长度|10|+1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '427', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null *密码|password|长度为最大长度|20|+1 真实姓名|realName|为空字符串 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'\',\'password\':\'43pwvkc2zx320kmtyqoj6\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');
INSERT INTO `t_interface_case` VALUES ('24', '14', '428', '/user/register', '1', '无效等价类 用户注册接口 *用户名|username|为null 密码|password|非法字符长度在区间[3,20]内 *真实姓名|realName|长度为最大长度|10|+1 *性别|sex|值不存在于数组内', '1', '', '{\'Token\':\'${token}\'}', null, null, '{\'realName\':\'f3u343xu20i\',\'password\':\'#>-<-!@%--!^<></.\\\\-,\',\'sex\':397618,\'username\':null}', '123', '2021-04-09 14:48:22', '2021-04-09 14:48:22', '3', 'IFIc04f466e-75f7-4305-89df-8f355fa85446');

-- ----------------------------
-- Table structure for t_interface_case_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_execute_log`;
CREATE TABLE `t_interface_case_execute_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用例执行日志id',
  `case_id` int DEFAULT NULL COMMENT '用例id',
  `case_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例描述',
  `case_url` varchar(300) DEFAULT NULL COMMENT '请求地址',
  `case_method` tinyint DEFAULT NULL COMMENT '0get,1post,2patch,3put,4delete',
  `request_headers` varchar(1000) DEFAULT NULL COMMENT '请求头',
  `request_params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `request_data` varchar(1000) DEFAULT NULL COMMENT '请求formdata',
  `request_json` varchar(1000) DEFAULT NULL,
  `raw_request_headers` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '未清洗依赖数据前的原始header',
  `raw_request_params` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '未清洗依赖数据前的原始params',
  `raw_request_data` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '未清洗依赖数据前的原始data',
  `raw_request_json` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '未清洗依赖数据前的原始json',
  `response_code` int DEFAULT NULL COMMENT '响应状态码',
  `response_headers` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '响应头',
  `response_body` mediumtext COMMENT '响应正文',
  `run_time` int DEFAULT NULL COMMENT '运行时长ms',
  `executer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行人',
  `status` tinyint DEFAULT NULL COMMENT '运行结果 0成功 1失败 2错误',
  `created_time` datetime DEFAULT NULL COMMENT '执行时间',
  `error_message` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '执行失败异常错误信息',
  `suite_log_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '测试套件执行日志编号',
  `suite_log_detail_no` varchar(51) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '测试套件执行日志编号(可记录接口依赖的case)',
  `chain` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '日志执行调用链list',
  `is_failed_retry` tinyint DEFAULT NULL COMMENT '是否为失败重跑用例0是1否',
  `source` tinyint DEFAULT NULL COMMENT '来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_execute_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface_case_rely_data
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_rely_data`;
CREATE TABLE `t_interface_case_rely_data` (
  `rely_id` int NOT NULL AUTO_INCREMENT COMMENT ' 自增主键',
  `rely_case_id` int DEFAULT NULL COMMENT '依赖用例编号',
  `rely_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '依赖名称,通过${name}引用',
  `rely_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '依赖描述',
  `content_type` tinyint DEFAULT NULL COMMENT '提取数据类型   0json/1html/2header/',
  `extract_expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`rely_id`),
  UNIQUE KEY `rely_name` (`rely_name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_rely_data
-- ----------------------------
INSERT INTO `t_interface_case_rely_data` VALUES ('14', '277', 'Token', '获取登录token', '0', '$..data.token', '2021-03-08 15:27:29', '2021-03-08 15:27:37');

-- ----------------------------
-- Table structure for t_interface_case_suite
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_suite`;
CREATE TABLE `t_interface_case_suite` (
  `suite_id` int NOT NULL AUTO_INCREMENT COMMENT '接口测试套件id',
  `suite_name` varchar(20) DEFAULT NULL COMMENT '测试套件名称',
  `desc` varchar(100) DEFAULT NULL COMMENT '测试套件描述',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `creator` varchar(20) DEFAULT NULL COMMENT '创建人',
  `execute_type` tinyint DEFAULT NULL COMMENT '执行方式 0并行1串行',
  `run_dev` tinyint DEFAULT NULL COMMENT '0dev1test2stg3prod4debug',
  `is_retry` tinyint DEFAULT NULL COMMENT '是否失败重新 0是1否',
  PRIMARY KEY (`suite_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_suite
-- ----------------------------
INSERT INTO `t_interface_case_suite` VALUES ('35', '测试', null, null, '2021-04-09 19:13:35', '超级管理员', '1', '4', '1');

-- ----------------------------
-- Table structure for t_interface_pre_case
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_pre_case`;
CREATE TABLE `t_interface_pre_case` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_case_id` int DEFAULT NULL COMMENT '用例id',
  `pre_case_id` int DEFAULT NULL COMMENT '前置用例id',
  `order` int DEFAULT NULL COMMENT '排序(越小越先执行)',
  `status` tinyint DEFAULT NULL COMMENT '状态0启用1禁用',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_pre_case
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface_processor
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_processor`;
CREATE TABLE `t_interface_processor` (
  `processor_id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `case_id` int DEFAULT NULL COMMENT '接口测试用例编号',
  `name` varchar(50) DEFAULT NULL COMMENT '后置处理器取值名称',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   提取数据类型   0response-json/1response-html/2response-header/3request-header/4request-params/5request-data/6request-json',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `default_value` varchar(100) DEFAULT NULL COMMENT '缺省值',
  `have_default_value` tinyint DEFAULT NULL COMMENT '是否存在缺省值0是1否',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`processor_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_processor
-- ----------------------------
INSERT INTO `t_interface_processor` VALUES ('56', '280', 'id', '4', '$..id', null, '1', '2021-03-08 16:11:19', '2021-03-13 15:48:55');

-- ----------------------------
-- Table structure for t_interface_processor_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_processor_log`;
CREATE TABLE `t_interface_processor_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '后置处理器使用日志编号',
  `processor_id` int DEFAULT NULL COMMENT '自增主键',
  `case_id` int DEFAULT NULL COMMENT '接口测试用例编号',
  `case_execute_log_id` int DEFAULT NULL COMMENT '用例执行编号',
  `name` varchar(50) DEFAULT NULL COMMENT '后置处理器取值名称',
  `is_default_value` tinyint DEFAULT NULL COMMENT '是否为缺省值0是1否',
  `value` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '后值处理器获取的值',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   提取数据类型   0response-json/1response-html/2response-header/3request-header/4request-params/5request-data/6request-json',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `status` tinyint DEFAULT NULL COMMENT '状态0通过1失败',
  `error_msg` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '失败时错误日志',
  `wr` tinyint DEFAULT NULL COMMENT '读/写 0读1写',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_processor_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface_suite_case_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_suite_case_ref`;
CREATE TABLE `t_interface_suite_case_ref` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `case_id` int NOT NULL COMMENT '接口用例编号',
  `suite_id` int NOT NULL COMMENT '测试套件编号',
  `case_status` tinyint DEFAULT NULL COMMENT '用例状态0启用 1禁用',
  `order` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=338 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_case_ref
-- ----------------------------
INSERT INTO `t_interface_suite_case_ref` VALUES ('189', '277', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('190', '284', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('191', '283', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('192', '280', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('193', '279', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('194', '285', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('195', '286', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('196', '287', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('197', '288', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('198', '289', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('199', '290', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('200', '291', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('201', '292', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('202', '293', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('203', '294', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('204', '295', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('205', '296', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('206', '297', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('207', '298', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('208', '299', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('209', '300', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('210', '301', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('211', '302', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('212', '303', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('213', '304', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('214', '305', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('215', '306', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('216', '307', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('217', '308', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('218', '309', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('219', '310', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('220', '311', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('221', '312', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('222', '313', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('223', '314', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('224', '315', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('225', '316', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('226', '317', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('227', '318', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('228', '319', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('229', '320', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('230', '321', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('231', '322', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('232', '323', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('233', '324', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('234', '325', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('235', '326', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('236', '327', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('237', '328', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('238', '329', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('239', '330', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('240', '331', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('241', '332', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('242', '333', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('243', '334', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('244', '335', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('245', '336', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('246', '337', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('247', '338', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('248', '339', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('249', '340', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('250', '341', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('251', '342', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('252', '343', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('253', '344', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('254', '345', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('255', '346', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('256', '347', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('257', '348', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('258', '349', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('259', '350', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('260', '351', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('261', '352', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('262', '353', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('263', '354', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('264', '355', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('265', '356', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('266', '357', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('267', '358', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('268', '359', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('269', '360', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('270', '361', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('271', '362', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('272', '363', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('273', '364', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('274', '365', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('275', '366', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('276', '367', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('277', '368', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('278', '369', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('279', '370', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('280', '371', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('281', '372', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('282', '373', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('283', '374', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('284', '375', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('285', '376', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('286', '377', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('287', '378', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('288', '379', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('289', '380', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('290', '381', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('291', '382', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('292', '383', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('293', '384', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('294', '385', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('295', '386', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('296', '387', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('297', '388', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('298', '389', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('299', '390', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('300', '391', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('301', '392', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('302', '393', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('303', '394', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('304', '395', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('305', '396', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('306', '397', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('307', '398', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('308', '399', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('309', '400', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('310', '401', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('311', '402', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('312', '403', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('313', '404', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('314', '405', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('315', '406', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('316', '407', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('317', '408', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('318', '409', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('319', '410', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('320', '411', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('321', '412', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('322', '413', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('323', '414', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('324', '415', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('325', '416', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('326', '417', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('327', '418', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('328', '419', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('329', '420', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('330', '421', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('331', '422', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('332', '423', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('333', '424', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('334', '425', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('335', '426', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('336', '427', '35', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('337', '428', '35', '0', '1');

-- ----------------------------
-- Table structure for t_interface_suite_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_suite_log`;
CREATE TABLE `t_interface_suite_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `suite_id` int DEFAULT NULL,
  `suite_log_no` varchar(50) DEFAULT NULL COMMENT '测试套件日志编号',
  `run_time` int DEFAULT NULL COMMENT '运行时长ms',
  `total_case` int DEFAULT NULL COMMENT '测试套件总用例',
  `total_run_case` int DEFAULT NULL COMMENT '运行总用例数',
  `total_skip` int DEFAULT NULL COMMENT '跳过数（未启用）',
  `total_success` int DEFAULT NULL COMMENT '运行成功数',
  `total_failed` int DEFAULT NULL COMMENT '运行失败数',
  `total_error` int DEFAULT NULL COMMENT '运行错误数',
  `total_retry` int DEFAULT NULL COMMENT '总重试用例数',
  `start_time` datetime DEFAULT NULL COMMENT '开始运行时间',
  `end_time` datetime DEFAULT NULL COMMENT '运行完成时间',
  `execute_type` tinyint DEFAULT NULL COMMENT '执行方式 0并行1串行',
  `run_dev` tinyint DEFAULT NULL COMMENT '0dev1test2stg3prod4debug',
  `executor` varchar(20) DEFAULT NULL COMMENT '执行人',
  `is_retry` tinyint DEFAULT NULL COMMENT '是否失败重新 0是1否',
  `progress` tinyint DEFAULT NULL COMMENT '0进行中1执行完成2执行失败',
  `percentage` int DEFAULT NULL COMMENT '执行进度百分比',
  PRIMARY KEY (`id`),
  UNIQUE KEY `suite_log_no` (`suite_log_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface_suite_processor
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_suite_processor`;
CREATE TABLE `t_interface_suite_processor` (
  `id` int NOT NULL AUTO_INCREMENT,
  `suite_id` int DEFAULT NULL COMMENT '测试套件编号',
  `processor_type` tinyint DEFAULT NULL COMMENT '0前置处理器1后置处理器',
  `type` tinyint DEFAULT NULL COMMENT '0执行依赖1公共头2公共params3公共data',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `value` varchar(200) DEFAULT NULL COMMENT '值',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_processor
-- ----------------------------
INSERT INTO `t_interface_suite_processor` VALUES ('50', '35', '0', '0', null, '${asd}', '2021-04-09 19:13:35', '2021-04-09 19:13:35');
INSERT INTO `t_interface_suite_processor` VALUES ('51', '35', '1', '0', null, '', '2021-04-09 19:13:35', '2021-04-09 19:13:35');

-- ----------------------------
-- Table structure for t_module
-- ----------------------------
DROP TABLE IF EXISTS `t_module`;
CREATE TABLE `t_module` (
  `project_id` int NOT NULL COMMENT '项目id',
  `module_id` int NOT NULL AUTO_INCREMENT COMMENT '模块id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模块名称',
  `desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模块描述',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`module_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('24', '14', '用户管理', null, '2021-03-08 15:24:31', '2021-03-08 15:24:31');

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `permission_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限编号 对应代码中@RequiresPermissions 的value',
  `permission_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限名称',
  `parent_id` int DEFAULT NULL COMMENT '父节点id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', 'user', '用户管理', '0');
INSERT INTO `t_permission` VALUES ('2', 'user:add', '新增', '1');
INSERT INTO `t_permission` VALUES ('3', 'user:modify', '修改', '1');
INSERT INTO `t_permission` VALUES ('4', 'user:remove', '删除', '1');
INSERT INTO `t_permission` VALUES ('5', 'user:find', '查询', '1');
INSERT INTO `t_permission` VALUES ('6', 'user:reset', '重置密码', '1');
INSERT INTO `t_permission` VALUES ('89', 'role', '角色管理', '0');
INSERT INTO `t_permission` VALUES ('90', 'role:add', '新增', '89');
INSERT INTO `t_permission` VALUES ('91', 'role:modify', '修改', '89');
INSERT INTO `t_permission` VALUES ('92', 'role:remove', '删除', '89');
INSERT INTO `t_permission` VALUES ('93', 'role:find', '查询', '89');
INSERT INTO `t_permission` VALUES ('94', 'role:permission', '授权', '89');
INSERT INTO `t_permission` VALUES ('95', 'permission', '权限管理', '0');
INSERT INTO `t_permission` VALUES ('100', 'permission:add_top', '新增顶级节点', '95');
INSERT INTO `t_permission` VALUES ('102', 'project', '项目管理', '0');
INSERT INTO `t_permission` VALUES ('103', 'project:add', '新增', '102');
INSERT INTO `t_permission` VALUES ('104', 'project:modify', '修改', '102');
INSERT INTO `t_permission` VALUES ('105', 'project:remove', '删除', '102');
INSERT INTO `t_permission` VALUES ('106', 'project:find', '查询', '102');
INSERT INTO `t_permission` VALUES ('107', 'module', '模块管理', '0');
INSERT INTO `t_permission` VALUES ('108', 'module:add', '新增', '107');
INSERT INTO `t_permission` VALUES ('109', 'module:modify', '修改', '107');
INSERT INTO `t_permission` VALUES ('110', 'module:remove', '删除', '107');
INSERT INTO `t_permission` VALUES ('111', 'module:find', '查询', '107');
INSERT INTO `t_permission` VALUES ('112', 'db', '数据源中心', '0');
INSERT INTO `t_permission` VALUES ('113', 'db:add', '新增', '112');
INSERT INTO `t_permission` VALUES ('114', 'db:modify', '修改', '112');
INSERT INTO `t_permission` VALUES ('115', 'db:remove', '删除', '112');
INSERT INTO `t_permission` VALUES ('116', 'db:find', '查询', '112');
INSERT INTO `t_permission` VALUES ('117', 'db:check', '预检', '112');
INSERT INTO `t_permission` VALUES ('118', 'factory', '数据工厂', '0');
INSERT INTO `t_permission` VALUES ('119', 'factory:add', '新增', '118');
INSERT INTO `t_permission` VALUES ('120', 'factory:modify', '修改', '118');
INSERT INTO `t_permission` VALUES ('121', 'factory:remove', '删除', '118');
INSERT INTO `t_permission` VALUES ('122', 'factory:find', '查询', '118');
INSERT INTO `t_permission` VALUES ('123', 'factory:execute', '执行', '118');
INSERT INTO `t_permission` VALUES ('124', 'data_center', '数据中心', '0');
INSERT INTO `t_permission` VALUES ('125', 'data_center:temp_env', '临时变量', '124');
INSERT INTO `t_permission` VALUES ('126', 'data_center:temp_env:find', '查询', '125');
INSERT INTO `t_permission` VALUES ('127', 'data_center:temp_env:clear', '清空', '125');
INSERT INTO `t_permission` VALUES ('128', 'data_center:if_rely', '接口依赖', '124');
INSERT INTO `t_permission` VALUES ('129', 'data_center:if_rely:add', '新增', '128');
INSERT INTO `t_permission` VALUES ('130', 'data_center:if_rely:modify', '修改', '128');
INSERT INTO `t_permission` VALUES ('131', 'data_center:if_rely:remove', '删除', '128');
INSERT INTO `t_permission` VALUES ('132', 'data_center:if_rely:find', '查询', '128');
INSERT INTO `t_permission` VALUES ('133', 'data_center:if_rely:execute', '执行', '128');
INSERT INTO `t_permission` VALUES ('134', 'data_center:rely_data', '其它依赖', '124');
INSERT INTO `t_permission` VALUES ('135', 'data_center:rely_data:add', '新增', '134');
INSERT INTO `t_permission` VALUES ('136', 'data_center:rely_data:modify', '修改', '134');
INSERT INTO `t_permission` VALUES ('137', 'data_center:rely_data:remove', '删除', '134');
INSERT INTO `t_permission` VALUES ('138', 'data_center:rely_data:find', '查询', '134');
INSERT INTO `t_permission` VALUES ('139', 'data_center:rely_sql_data', 'SQL语句', '124');
INSERT INTO `t_permission` VALUES ('140', 'data_center:rely_sql_data:add', '新增', '139');
INSERT INTO `t_permission` VALUES ('141', 'data_center:rely_sql_data:modify', '修改', '139');
INSERT INTO `t_permission` VALUES ('142', 'data_center:rely_sql_data:remove', '删除', '139');
INSERT INTO `t_permission` VALUES ('143', 'data_center:rely_sql_data:find', '查询', '139');
INSERT INTO `t_permission` VALUES ('144', 'data_center:rely_init_method', '预置方法', '124');
INSERT INTO `t_permission` VALUES ('146', 'data_center:rely_init_method:modify', '修改', '144');
INSERT INTO `t_permission` VALUES ('147', 'data_center:rely_init_method:remove', '删除', '144');
INSERT INTO `t_permission` VALUES ('148', 'data_center:rely_init_method:find', '查询', '144');
INSERT INTO `t_permission` VALUES ('149', 'data_center:rely_fixed_data', '固定字符', '124');
INSERT INTO `t_permission` VALUES ('150', 'data_center:rely_fixed_data:add', '新增', '149');
INSERT INTO `t_permission` VALUES ('151', 'data_center:rely_fixed_data:modify', '修改', '149');
INSERT INTO `t_permission` VALUES ('152', 'data_center:rely_fixed_data:remove', '删除', '149');
INSERT INTO `t_permission` VALUES ('153', 'data_center:rely_fixed_data:find', '查询', '149');
INSERT INTO `t_permission` VALUES ('154', 'interface', '接口测试', '0');
INSERT INTO `t_permission` VALUES ('155', 'interface:case', '测试用例', '154');
INSERT INTO `t_permission` VALUES ('156', 'interface:case:add', '新增', '155');
INSERT INTO `t_permission` VALUES ('157', 'interface:case:modify', '修改', '155');
INSERT INTO `t_permission` VALUES ('158', 'interface:case:remove', '删除', '155');
INSERT INTO `t_permission` VALUES ('159', 'interface:case:find', '查询', '155');
INSERT INTO `t_permission` VALUES ('160', 'interface:case:copy', '复制', '155');
INSERT INTO `t_permission` VALUES ('161', 'interface:case:generate', '生成', '155');
INSERT INTO `t_permission` VALUES ('162', 'interface:case:import', '导入', '155');
INSERT INTO `t_permission` VALUES ('163', 'interface:case:execute', '执行', '155');
INSERT INTO `t_permission` VALUES ('164', 'interface:suite', '测试套件', '154');
INSERT INTO `t_permission` VALUES ('165', 'interface:suite:add', '新增', '164');
INSERT INTO `t_permission` VALUES ('166', 'interface:suite:modify', '修改', '164');
INSERT INTO `t_permission` VALUES ('167', 'interface:suite:remove', '删除', '164');
INSERT INTO `t_permission` VALUES ('168', 'interface:suite:find', '查询', '164');
INSERT INTO `t_permission` VALUES ('169', 'interface:suite:copy', '复制', '164');
INSERT INTO `t_permission` VALUES ('170', 'interface:suite:manager', '用例维护', '164');
INSERT INTO `t_permission` VALUES ('171', 'interface:suite:execute', '执行', '164');
INSERT INTO `t_permission` VALUES ('172', 'interface:case_log', '执行日志', '154');
INSERT INTO `t_permission` VALUES ('173', 'interface:case_log:find', '查询', '172');
INSERT INTO `t_permission` VALUES ('174', 'interface:case_log:detail', '详情', '172');
INSERT INTO `t_permission` VALUES ('175', 'interface:case_log:chain', '链路跟踪', '172');
INSERT INTO `t_permission` VALUES ('176', 'interface:assert_log', '断言日志', '154');
INSERT INTO `t_permission` VALUES ('177', 'interface:assert_log:find', '查询', '176');
INSERT INTO `t_permission` VALUES ('178', 'setting', '配置中心', '0');
INSERT INTO `t_permission` VALUES ('179', 'setting:email', '邮箱管理', '178');
INSERT INTO `t_permission` VALUES ('180', 'setting:email:add', '新增', '179');
INSERT INTO `t_permission` VALUES ('181', 'setting:email:modify', '修改', '179');
INSERT INTO `t_permission` VALUES ('182', 'setting:email:remove', '删除', '179');
INSERT INTO `t_permission` VALUES ('183', 'setting:email:find', '查询', '179');
INSERT INTO `t_permission` VALUES ('184', 'setting:proxy', '代理管理', '178');
INSERT INTO `t_permission` VALUES ('185', 'setting:proxy:add', '新增', '184');
INSERT INTO `t_permission` VALUES ('186', 'setting:proxy:modify', '修改', '184');
INSERT INTO `t_permission` VALUES ('187', 'setting:proxy:remove', '删除', '184');
INSERT INTO `t_permission` VALUES ('188', 'setting:proxy:find', '查询', '184');
INSERT INTO `t_permission` VALUES ('189', 'setting:task', '定时任务', '178');
INSERT INTO `t_permission` VALUES ('190', 'setting:task:add', '新增', '189');
INSERT INTO `t_permission` VALUES ('191', 'setting:task:modify', '修改', '189');
INSERT INTO `t_permission` VALUES ('192', 'setting:task:remove', '删除', '189');
INSERT INTO `t_permission` VALUES ('193', 'setting:task:find', '查询', '189');
INSERT INTO `t_permission` VALUES ('194', 'setting:task:execute', '执行', '189');

-- ----------------------------
-- Table structure for t_permission_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_permission_role_ref`;
CREATE TABLE `t_permission_role_ref` (
  `id` int NOT NULL AUTO_INCREMENT,
  `permission_id` int DEFAULT NULL COMMENT '权限ID',
  `role_id` int DEFAULT NULL COMMENT '权限编号',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission_role_ref
-- ----------------------------
INSERT INTO `t_permission_role_ref` VALUES ('31', '89', '1', '2021-03-12 19:58:20');
INSERT INTO `t_permission_role_ref` VALUES ('32', '90', '1', '2021-03-12 19:58:21');
INSERT INTO `t_permission_role_ref` VALUES ('33', '91', '1', '2021-03-12 19:58:21');
INSERT INTO `t_permission_role_ref` VALUES ('34', '92', '1', '2021-03-12 19:58:22');
INSERT INTO `t_permission_role_ref` VALUES ('35', '93', '1', '2021-03-12 19:58:22');
INSERT INTO `t_permission_role_ref` VALUES ('36', '94', '1', '2021-03-12 19:58:23');
INSERT INTO `t_permission_role_ref` VALUES ('37', '95', '1', '2021-03-12 19:58:24');
INSERT INTO `t_permission_role_ref` VALUES ('38', '97', '1', '2021-03-12 19:58:25');
INSERT INTO `t_permission_role_ref` VALUES ('39', '96', '1', '2021-03-12 19:58:27');
INSERT INTO `t_permission_role_ref` VALUES ('40', '98', '1', '2021-03-12 19:58:28');
INSERT INTO `t_permission_role_ref` VALUES ('41', '99', '1', '2021-03-12 19:58:28');
INSERT INTO `t_permission_role_ref` VALUES ('42', '100', '1', '2021-03-12 19:58:30');
INSERT INTO `t_permission_role_ref` VALUES ('43', '102', '1', '2021-03-12 19:58:32');
INSERT INTO `t_permission_role_ref` VALUES ('44', '103', '1', '2021-03-12 19:58:33');
INSERT INTO `t_permission_role_ref` VALUES ('45', '104', '1', '2021-03-12 19:58:34');
INSERT INTO `t_permission_role_ref` VALUES ('46', '105', '1', '2021-03-12 19:58:34');
INSERT INTO `t_permission_role_ref` VALUES ('47', '106', '1', '2021-03-12 19:58:34');
INSERT INTO `t_permission_role_ref` VALUES ('48', '107', '1', '2021-03-12 19:58:38');
INSERT INTO `t_permission_role_ref` VALUES ('49', '108', '1', '2021-03-12 19:58:38');
INSERT INTO `t_permission_role_ref` VALUES ('50', '109', '1', '2021-03-12 19:58:39');
INSERT INTO `t_permission_role_ref` VALUES ('51', '110', '1', '2021-03-12 19:58:39');
INSERT INTO `t_permission_role_ref` VALUES ('52', '111', '1', '2021-03-12 19:58:40');
INSERT INTO `t_permission_role_ref` VALUES ('53', '112', '1', '2021-03-12 19:58:41');
INSERT INTO `t_permission_role_ref` VALUES ('54', '113', '1', '2021-03-12 19:58:42');
INSERT INTO `t_permission_role_ref` VALUES ('55', '114', '1', '2021-03-12 19:58:42');
INSERT INTO `t_permission_role_ref` VALUES ('56', '115', '1', '2021-03-12 19:58:42');
INSERT INTO `t_permission_role_ref` VALUES ('57', '116', '1', '2021-03-12 19:58:43');
INSERT INTO `t_permission_role_ref` VALUES ('58', '117', '1', '2021-03-12 19:58:43');
INSERT INTO `t_permission_role_ref` VALUES ('59', '118', '1', '2021-03-12 19:58:46');
INSERT INTO `t_permission_role_ref` VALUES ('60', '119', '1', '2021-03-12 19:58:47');
INSERT INTO `t_permission_role_ref` VALUES ('61', '120', '1', '2021-03-12 19:58:47');
INSERT INTO `t_permission_role_ref` VALUES ('62', '121', '1', '2021-03-12 19:58:48');
INSERT INTO `t_permission_role_ref` VALUES ('63', '122', '1', '2021-03-12 19:58:48');
INSERT INTO `t_permission_role_ref` VALUES ('64', '123', '1', '2021-03-12 19:58:49');
INSERT INTO `t_permission_role_ref` VALUES ('65', '124', '1', '2021-03-12 19:58:50');
INSERT INTO `t_permission_role_ref` VALUES ('66', '125', '1', '2021-03-12 19:58:52');
INSERT INTO `t_permission_role_ref` VALUES ('67', '126', '1', '2021-03-12 19:58:53');
INSERT INTO `t_permission_role_ref` VALUES ('68', '127', '1', '2021-03-12 19:58:55');
INSERT INTO `t_permission_role_ref` VALUES ('69', '128', '1', '2021-03-12 19:58:56');
INSERT INTO `t_permission_role_ref` VALUES ('70', '129', '1', '2021-03-12 19:58:56');
INSERT INTO `t_permission_role_ref` VALUES ('71', '130', '1', '2021-03-12 19:58:57');
INSERT INTO `t_permission_role_ref` VALUES ('72', '131', '1', '2021-03-12 19:58:58');
INSERT INTO `t_permission_role_ref` VALUES ('73', '132', '1', '2021-03-12 19:58:59');
INSERT INTO `t_permission_role_ref` VALUES ('74', '133', '1', '2021-03-12 19:58:59');
INSERT INTO `t_permission_role_ref` VALUES ('76', '135', '1', '2021-03-12 19:59:01');
INSERT INTO `t_permission_role_ref` VALUES ('77', '136', '1', '2021-03-12 19:59:01');
INSERT INTO `t_permission_role_ref` VALUES ('78', '137', '1', '2021-03-12 19:59:02');
INSERT INTO `t_permission_role_ref` VALUES ('79', '138', '1', '2021-03-12 19:59:02');
INSERT INTO `t_permission_role_ref` VALUES ('80', '139', '1', '2021-03-12 19:59:04');
INSERT INTO `t_permission_role_ref` VALUES ('81', '140', '1', '2021-03-12 19:59:04');
INSERT INTO `t_permission_role_ref` VALUES ('82', '141', '1', '2021-03-12 19:59:05');
INSERT INTO `t_permission_role_ref` VALUES ('83', '142', '1', '2021-03-12 19:59:05');
INSERT INTO `t_permission_role_ref` VALUES ('84', '143', '1', '2021-03-12 19:59:05');
INSERT INTO `t_permission_role_ref` VALUES ('85', '144', '1', '2021-03-12 19:59:07');
INSERT INTO `t_permission_role_ref` VALUES ('86', '145', '1', '2021-03-12 19:59:08');
INSERT INTO `t_permission_role_ref` VALUES ('87', '146', '1', '2021-03-12 19:59:10');
INSERT INTO `t_permission_role_ref` VALUES ('88', '147', '1', '2021-03-12 19:59:10');
INSERT INTO `t_permission_role_ref` VALUES ('89', '148', '1', '2021-03-12 19:59:11');
INSERT INTO `t_permission_role_ref` VALUES ('90', '149', '1', '2021-03-12 19:59:12');
INSERT INTO `t_permission_role_ref` VALUES ('91', '150', '1', '2021-03-12 19:59:13');
INSERT INTO `t_permission_role_ref` VALUES ('92', '152', '1', '2021-03-12 19:59:14');
INSERT INTO `t_permission_role_ref` VALUES ('93', '151', '1', '2021-03-12 19:59:14');
INSERT INTO `t_permission_role_ref` VALUES ('94', '153', '1', '2021-03-12 19:59:15');
INSERT INTO `t_permission_role_ref` VALUES ('95', '154', '1', '2021-03-12 19:59:16');
INSERT INTO `t_permission_role_ref` VALUES ('96', '155', '1', '2021-03-12 19:59:17');
INSERT INTO `t_permission_role_ref` VALUES ('97', '156', '1', '2021-03-12 19:59:18');
INSERT INTO `t_permission_role_ref` VALUES ('98', '157', '1', '2021-03-12 19:59:19');
INSERT INTO `t_permission_role_ref` VALUES ('99', '158', '1', '2021-03-12 19:59:20');
INSERT INTO `t_permission_role_ref` VALUES ('100', '159', '1', '2021-03-12 19:59:20');
INSERT INTO `t_permission_role_ref` VALUES ('101', '160', '1', '2021-03-12 19:59:21');
INSERT INTO `t_permission_role_ref` VALUES ('102', '161', '1', '2021-03-12 19:59:21');
INSERT INTO `t_permission_role_ref` VALUES ('103', '163', '1', '2021-03-12 19:59:23');
INSERT INTO `t_permission_role_ref` VALUES ('104', '162', '1', '2021-03-12 19:59:25');
INSERT INTO `t_permission_role_ref` VALUES ('105', '164', '1', '2021-03-12 19:59:27');
INSERT INTO `t_permission_role_ref` VALUES ('106', '165', '1', '2021-03-12 19:59:29');
INSERT INTO `t_permission_role_ref` VALUES ('107', '166', '1', '2021-03-12 19:59:29');
INSERT INTO `t_permission_role_ref` VALUES ('108', '167', '1', '2021-03-12 19:59:30');
INSERT INTO `t_permission_role_ref` VALUES ('109', '168', '1', '2021-03-12 19:59:30');
INSERT INTO `t_permission_role_ref` VALUES ('110', '169', '1', '2021-03-12 19:59:30');
INSERT INTO `t_permission_role_ref` VALUES ('111', '170', '1', '2021-03-12 19:59:31');
INSERT INTO `t_permission_role_ref` VALUES ('112', '171', '1', '2021-03-12 19:59:33');
INSERT INTO `t_permission_role_ref` VALUES ('113', '172', '1', '2021-03-12 19:59:34');
INSERT INTO `t_permission_role_ref` VALUES ('114', '173', '1', '2021-03-12 19:59:36');
INSERT INTO `t_permission_role_ref` VALUES ('115', '174', '1', '2021-03-12 19:59:37');
INSERT INTO `t_permission_role_ref` VALUES ('116', '175', '1', '2021-03-12 19:59:37');
INSERT INTO `t_permission_role_ref` VALUES ('117', '176', '1', '2021-03-12 19:59:38');
INSERT INTO `t_permission_role_ref` VALUES ('118', '177', '1', '2021-03-12 19:59:38');
INSERT INTO `t_permission_role_ref` VALUES ('119', '179', '1', '2021-03-12 19:59:40');
INSERT INTO `t_permission_role_ref` VALUES ('120', '178', '1', '2021-03-12 19:59:40');
INSERT INTO `t_permission_role_ref` VALUES ('121', '181', '1', '2021-03-12 19:59:42');
INSERT INTO `t_permission_role_ref` VALUES ('122', '180', '1', '2021-03-12 19:59:42');
INSERT INTO `t_permission_role_ref` VALUES ('123', '182', '1', '2021-03-12 19:59:43');
INSERT INTO `t_permission_role_ref` VALUES ('124', '183', '1', '2021-03-12 19:59:44');
INSERT INTO `t_permission_role_ref` VALUES ('125', '184', '1', '2021-03-12 19:59:45');
INSERT INTO `t_permission_role_ref` VALUES ('126', '185', '1', '2021-03-12 19:59:46');
INSERT INTO `t_permission_role_ref` VALUES ('127', '186', '1', '2021-03-12 19:59:47');
INSERT INTO `t_permission_role_ref` VALUES ('128', '187', '1', '2021-03-12 19:59:47');
INSERT INTO `t_permission_role_ref` VALUES ('129', '188', '1', '2021-03-12 19:59:48');
INSERT INTO `t_permission_role_ref` VALUES ('130', '189', '1', '2021-03-12 19:59:48');
INSERT INTO `t_permission_role_ref` VALUES ('131', '190', '1', '2021-03-12 19:59:49');
INSERT INTO `t_permission_role_ref` VALUES ('132', '191', '1', '2021-03-12 19:59:49');
INSERT INTO `t_permission_role_ref` VALUES ('133', '192', '1', '2021-03-12 19:59:51');
INSERT INTO `t_permission_role_ref` VALUES ('134', '193', '1', '2021-03-12 19:59:51');
INSERT INTO `t_permission_role_ref` VALUES ('137', '2', '1', '2021-03-12 20:36:59');
INSERT INTO `t_permission_role_ref` VALUES ('138', '4', '1', '2021-03-12 20:37:00');
INSERT INTO `t_permission_role_ref` VALUES ('139', '3', '1', '2021-03-12 20:37:00');
INSERT INTO `t_permission_role_ref` VALUES ('140', '5', '1', '2021-03-12 20:37:01');
INSERT INTO `t_permission_role_ref` VALUES ('141', '6', '1', '2021-03-12 20:37:01');
INSERT INTO `t_permission_role_ref` VALUES ('142', '1', '1', '2021-03-12 20:38:31');
INSERT INTO `t_permission_role_ref` VALUES ('153', '127', '4', '2021-03-12 21:02:08');
INSERT INTO `t_permission_role_ref` VALUES ('154', '126', '4', '2021-03-12 21:02:08');
INSERT INTO `t_permission_role_ref` VALUES ('155', '1', '4', '2021-03-12 21:25:53');
INSERT INTO `t_permission_role_ref` VALUES ('156', '89', '4', '2021-03-12 21:25:55');
INSERT INTO `t_permission_role_ref` VALUES ('157', '95', '4', '2021-03-12 21:25:56');
INSERT INTO `t_permission_role_ref` VALUES ('158', '102', '4', '2021-03-12 21:25:56');
INSERT INTO `t_permission_role_ref` VALUES ('159', '107', '4', '2021-03-12 21:25:57');
INSERT INTO `t_permission_role_ref` VALUES ('160', '112', '4', '2021-03-12 21:25:57');
INSERT INTO `t_permission_role_ref` VALUES ('161', '118', '4', '2021-03-12 21:25:58');
INSERT INTO `t_permission_role_ref` VALUES ('162', '124', '4', '2021-03-12 21:25:59');
INSERT INTO `t_permission_role_ref` VALUES ('163', '154', '4', '2021-03-12 21:25:59');
INSERT INTO `t_permission_role_ref` VALUES ('164', '178', '4', '2021-03-12 21:26:00');
INSERT INTO `t_permission_role_ref` VALUES ('165', '194', '1', '2021-04-02 09:20:55');

-- ----------------------------
-- Table structure for t_post_processor
-- ----------------------------
DROP TABLE IF EXISTS `t_post_processor`;
CREATE TABLE `t_post_processor` (
  `post_processor_id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `case_id` int DEFAULT NULL COMMENT '接口测试用例编号',
  `name` varchar(50) DEFAULT NULL COMMENT '后置处理器取值名称',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   提取数据类型   0response-json/1response-html/2response-header/3request-header/4request-params/5request-data/6request-json',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `default_value` varchar(100) DEFAULT NULL COMMENT '缺省值',
  `have_default_value` tinyint DEFAULT NULL COMMENT '是否存在缺省值0是1否',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`post_processor_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_post_processor
-- ----------------------------
INSERT INTO `t_post_processor` VALUES ('36', '75', 'token', '0', '$..token', null, '1', '2020-11-04 11:34:27', '2020-11-04 11:34:27');
INSERT INTO `t_post_processor` VALUES ('37', '82', 'userId', '4', '$..userId', null, '1', '2020-11-04 15:22:00', '2020-11-04 15:42:26');

-- ----------------------------
-- Table structure for t_post_processor_log
-- ----------------------------
DROP TABLE IF EXISTS `t_post_processor_log`;
CREATE TABLE `t_post_processor_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '后置处理器使用日志编号',
  `post_processor_id` int DEFAULT NULL COMMENT '自增主键',
  `case_id` int DEFAULT NULL COMMENT '接口测试用例编号',
  `case_execute_log_id` int DEFAULT NULL COMMENT '用例执行编号',
  `name` varchar(50) DEFAULT NULL COMMENT '后置处理器取值名称',
  `is_default_value` tinyint DEFAULT NULL COMMENT '是否为缺省值0是1否',
  `value` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '后值处理器获取的值',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   提取数据类型   0response-json/1response-html/2response-header/3request-header/4request-params/5request-data/6request-json',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `status` tinyint DEFAULT NULL COMMENT '状态0通过1失败',
  `error_msg` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '失败时错误日志',
  `wr` tinyint DEFAULT NULL COMMENT '读/写 0读1写',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=553 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_post_processor_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `project_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目名称',
  `desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目描述',
  `domain` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目地址',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `dev_domain` varchar(30) DEFAULT NULL COMMENT '开发环境域名',
  `test_domain` varchar(30) DEFAULT NULL COMMENT '测试环境域名',
  `stg_domain` varchar(30) DEFAULT NULL COMMENT '预上线环境域名',
  `prod_domain` varchar(30) DEFAULT NULL COMMENT '正式环境域名',
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('24', 'Alex', null, 'http://localhost:7777', null, null, 'http://localhost:7777', 'http://localhost:7777', 'http://localhost:7777', 'http://localhost:7777');

-- ----------------------------
-- Table structure for t_rely_data
-- ----------------------------
DROP TABLE IF EXISTS `t_rely_data`;
CREATE TABLE `t_rely_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '依赖名称',
  `value` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '依赖值',
  `desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '依赖描述',
  `type` tinyint DEFAULT NULL COMMENT '依赖类型 0固定值 1反射方法 2sql-select 3sql-insert 4sql-update 5sql-delete 6sql-script',
  `datasource_id` int DEFAULT NULL COMMENT '数据源id',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `enable_return` tinyint DEFAULT NULL COMMENT '0是1否当type=3（新增）时，是否返回自增主键',
  `analysis_rely` tinyint DEFAULT NULL COMMENT '0是1否当type<2时，是否解析sql中的依赖',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_rely_data
-- ----------------------------
INSERT INTO `t_rely_data` VALUES ('1', 'md5', 'md5(String s)', 'md5加密，引用方式示例：${md5(\"123456\")}', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58', null, null);
INSERT INTO `t_rely_data` VALUES ('2', 'uuid', 'uuid()', '获取uuid，引用方式示例：${uuid()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:29:54', null, null);
INSERT INTO `t_rely_data` VALUES ('3', 'base64', 'base64(String s)', 'base64加密，引用方式示例：${base64(\"123456\")}', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58', null, null);
INSERT INTO `t_rely_data` VALUES ('4', 'timestamp', 'timestamp()', '获取当前时间戳ms，引用方式示例：${timestamp()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null);
INSERT INTO `t_rely_data` VALUES ('5', 'now', 'now(String format)', '获取当前时间，引用方式示例：${now(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:07', null, null);
INSERT INTO `t_rely_data` VALUES ('10', 'yesterday', 'yesterday(String format)', '获取当前时间对应的昨天，引用方式示例：${yesterday(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:11', null, null);
INSERT INTO `t_rely_data` VALUES ('11', 'lastMonth', 'lastMonth(String format)', '获取当前时间对应的上个月，引用方式示例：${lastMonth(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:15', null, null);
INSERT INTO `t_rely_data` VALUES ('12', 'nextMonth', 'nextMonth(String format)', '获取当前时间对应的下个月，引用方式示例：${nextMonth(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:18', null, null);
INSERT INTO `t_rely_data` VALUES ('13', 'lastYear', 'lastYear(String format)', '获取当前时间对应的去年，引用方式示例：${lastYear(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:23', null, null);
INSERT INTO `t_rely_data` VALUES ('14', 'nextYear', 'nextYear(String format)', '获取当前时间对应的明年，引用方式示例：${nextYear(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:28', null, null);
INSERT INTO `t_rely_data` VALUES ('15', 'time', 'time(String operator, String amount, String format)', '获取相较于当前时间的指定时间，operator：y、M、d、h、m、s；分别对应：年、月、日、时、分、秒，amount：数额，format：格式，如yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58', null, null);
INSERT INTO `t_rely_data` VALUES ('16', 'randomInt', 'randomInt(String length)', '获取指定长度的随机整数，引用方式示例：${randomInt(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:17', null, null);
INSERT INTO `t_rely_data` VALUES ('17', 'randomUpper', 'randomUpper(String length)', '获取指定长度的随机大写英文，引用方式示例：${randomUpper(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:27', null, null);
INSERT INTO `t_rely_data` VALUES ('18', 'randomLower', 'randomLower(String length)', '获取指定长度的随机小写英文，引用方式示例：${randomLower(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:32', null, null);
INSERT INTO `t_rely_data` VALUES ('19', 'randomEn', 'randomEn(String length)', '获取指定长度的随机英文，引用方式示例：${randomEn(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:44', null, null);
INSERT INTO `t_rely_data` VALUES ('20', 'randomIllegal', 'randomIllegal(String length)', '获取指定长度的非法字符，引用方式示例：${randomIllegal(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:56', null, null);
INSERT INTO `t_rely_data` VALUES ('21', 'cs', '长沙', '长沙市，引用方式示例：${cs}', '0', null, '2020-09-02 11:37:06', '2020-10-02 09:35:17', null, null);
INSERT INTO `t_rely_data` VALUES ('29', 'city', 'city()', '随机城市，引用方式示例：${city()}', '1', null, '2020-10-21 15:26:23', '2020-10-21 15:26:32', null, null);
INSERT INTO `t_rely_data` VALUES ('30', 'province', 'province()', '随机省份，引用方式示例：${province()}', '1', null, '2020-10-21 16:16:16', null, null, null);
INSERT INTO `t_rely_data` VALUES ('31', 'country', 'country()', '随机国家，引用方式示例：${country()}', '1', null, '2020-10-21 16:16:20', null, null, null);
INSERT INTO `t_rely_data` VALUES ('32', 'phone', 'phone()', '随机手机号码，引用方式示例：${phone()}', '1', null, '2020-10-21 16:16:26', null, null, null);
INSERT INTO `t_rely_data` VALUES ('33', 'email', 'email()', '随机邮箱，引用方式示例：${email()}', '1', null, '2020-10-21 16:16:28', null, null, null);
INSERT INTO `t_rely_data` VALUES ('34', 'mac', 'mac()', '随机mac地址，引用方式示例：${mac()}', '1', null, '2020-10-21 16:16:30', null, null, null);
INSERT INTO `t_rely_data` VALUES ('35', 'book', 'book()', '随机书名，引用方式示例：${book()}', '1', null, '2020-10-21 16:16:35', null, null, null);
INSERT INTO `t_rely_data` VALUES ('36', 'name', 'name()', '随机中文名称，引用方式示例：${name()}', '1', null, '2020-10-21 16:16:37', null, null, null);
INSERT INTO `t_rely_data` VALUES ('37', 'ipv4', 'ipv4()', '随机ipv4地址，引用方式示例：${ipv4()}', '1', null, '2020-10-21 16:16:40', null, null, null);
INSERT INTO `t_rely_data` VALUES ('38', 'privateIpv4', 'privateIpv4()', '随机私有ipv4地址，引用方式示例：${privateIpv4()}', '1', null, '2020-10-21 16:16:42', null, null, null);
INSERT INTO `t_rely_data` VALUES ('39', 'publicIpv4', 'publicIpv4()', '随机公有ipv4地址，引用方式示例：${publicIpv4()}', '1', null, '2020-10-21 16:16:44', null, null, null);
INSERT INTO `t_rely_data` VALUES ('40', 'ipv6', 'ipv6()', '随机ipv6地址，引用方式示例：${ipv6()}', '1', null, '2020-10-21 16:16:47', null, null, null);
INSERT INTO `t_rely_data` VALUES ('41', 'InterfaceCaseTable', 'select * from t_interface_case where case_id = ?', '查询根据用例编号查询t_interface_case', '2', '1', '2021-03-08 15:32:57', '2021-03-31 11:43:19', null, '1');
INSERT INTO `t_rely_data` VALUES ('44', 'InsertUser', 'INSERT INTO `platform`.`t_user` (`username`, `password`, `job_number`, `sex`, `is_enable`, `created_time`, `update_time`, `real_name`, `role_id`) VALUES (\'${timestamp()}\', \'18e29620f058e8bf085bfed74fdf5e82\', \'\', \'1\', \'1\', NULL, \'2021-03-10 16:08:51\', \'123\', \'1\');', '新增用户', '3', '1', '2021-03-30 13:44:05', '2021-04-02 10:23:07', '0', '0');
INSERT INTO `t_rely_data` VALUES ('45', 'timestamps', 'timestamps()', '获取当前时间戳s，引用方式示例：${timestamps()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null);
INSERT INTO `t_rely_data` VALUES ('46', 'pick', 'pick(String... args)', '参数列表任选值，引用方式示例：${pick(\"1\", \"2\", \"3\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null);
INSERT INTO `t_rely_data` VALUES ('47', 'inversePick', 'inversePick(String... args)', '参数列表返选值，引用方式示例：${inversePick(\"1\", \"2\", \"3\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null);
INSERT INTO `t_rely_data` VALUES ('48', 'select', 'select(String dbId, String sql, String returnType)', '随机任选查询结果(仅支持单列)，returnType可选值有[integer, float, double, number, string],引用方式示例：${pick(\"1\", \"select username from t_user where id > 3\", \"integer\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null);
INSERT INTO `t_rely_data` VALUES ('49', 'inverseSelect', 'inverseSelect(String dbId, String sql, String returnType)', '随机反选查询结果(仅支持单列)，returnType可选值有[integer, float, double, number, string],引用方式示例：${pick(\"1\", \"select username from t_user where id > 3\", \"integer\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null);

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) DEFAULT NULL COMMENT '角色名称',
  `status` tinyint DEFAULT NULL COMMENT '状态0启用1禁用',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '超级管理员', '0');
INSERT INTO `t_role` VALUES ('4', '游客', '0');

-- ----------------------------
-- Table structure for t_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_setting`;
CREATE TABLE `t_setting` (
  `setting_id` int NOT NULL AUTO_INCREMENT COMMENT '配置编号',
  `key` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '配置项名称',
  `value` varchar(50) DEFAULT NULL,
  `desc` varchar(50) DEFAULT NULL COMMENT '配置项描述',
  PRIMARY KEY (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_setting
-- ----------------------------

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `cron` varchar(30) DEFAULT NULL COMMENT '定时任务表达式',
  `next_time` datetime DEFAULT NULL COMMENT '下次执行时间',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '创建时间',
  `suite_type` tinyint DEFAULT NULL COMMENT '0接口1UI',
  `suite_id` int DEFAULT NULL COMMENT '测试套件编号',
  `status` tinyint DEFAULT NULL COMMENT '状态0启用1禁用',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task
-- ----------------------------
INSERT INTO `t_task` VALUES ('119', '定时任务', '*/5 * * * * ?', '2021-04-02 19:21:16', '2021-03-09 10:34:23', '2021-04-02 10:13:02', '0', '35', '1');

-- ----------------------------
-- Table structure for t_task_email_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_task_email_ref`;
CREATE TABLE `t_task_email_ref` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '定时任务id',
  `task_id` int DEFAULT NULL,
  `email_address` varchar(30) DEFAULT NULL COMMENT '邮箱地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task_email_ref
-- ----------------------------

-- ----------------------------
-- Table structure for t_ui_case
-- ----------------------------
DROP TABLE IF EXISTS `t_ui_case`;
CREATE TABLE `t_ui_case` (
  `case_id` int NOT NULL AUTO_INCREMENT COMMENT '用例编号',
  `project_id` int DEFAULT NULL COMMENT '项目编号',
  `module_id` int DEFAULT NULL COMMENT '模块编号',
  `desc` varchar(200) DEFAULT NULL COMMENT '用例描述',
  `level` tinyint DEFAULT NULL COMMENT '用例级别0高，1中，2低',
  `setting_id` int DEFAULT NULL COMMENT 'ui设置编号',
  `creator` varchar(20) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_ui_case
-- ----------------------------

-- ----------------------------
-- Table structure for t_ui_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_ui_setting`;
CREATE TABLE `t_ui_setting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `desc` varchar(20) DEFAULT NULL COMMENT '描述',
  `chrome_ops_headless` tinyint DEFAULT NULL COMMENT '无界面模式0是1否',
  `driver_type` tinyint DEFAULT NULL COMMENT '0chrome1firfox2ie',
  `driver_maximize` tinyint DEFAULT NULL COMMENT '浏览器最大化0是1否',
  `driver_implicitly_wait` int DEFAULT NULL COMMENT '隐式等待时间s',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `creator` varchar(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_ui_setting
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `job_number` varchar(10) DEFAULT NULL COMMENT '工号',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别,0女 1男',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1启用 0禁用',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `role_id` int DEFAULT NULL COMMENT '角色类型',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=575 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '123', '18e29620f058e8bf085bfed74fdf5e82', '', '1', '1', null, '2021-03-10 16:08:51', '123', '1');
INSERT INTO `t_user` VALUES ('555', 'youke', '123', '1', '1', '1', '2021-03-12 20:42:04', '2021-03-12 20:42:04', 'youke', '4');
INSERT INTO `t_user` VALUES ('573', '1617600148708', '18e29620f058e8bf085bfed74fdf5e82', '', '1', '1', null, '2021-03-10 16:08:51', '123', '1');
INSERT INTO `t_user` VALUES ('574', 'h3pdzkeimwflm', '73710df4d9dc96eeb3dc1dcd98e0e5d3', null, '1', '1', '2021-04-09 14:48:28', '2021-04-09 14:48:28', 'yirgphsyap', null);
