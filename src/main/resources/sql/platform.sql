/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2020-07-30 17:05:41
*/

SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('2', '2', '模块', '123', '2020-07-30 11:06:02', '2020-07-30 11:06:05');
INSERT INTO `t_module` VALUES ('4', '3', '模块名称', '模块描述', '2020-07-30 15:28:24', '2020-07-30 15:28:24');
INSERT INTO `t_module` VALUES ('4', '4', '模块名称', '模块描述', '2020-07-30 15:33:29', '2020-07-30 15:33:29');

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
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('2', '项目名称79', null, 'http://www.baidu.com/', null, null);
INSERT INTO `t_project` VALUES ('4', '项目名称1', '123', 'http://www.baidu.com/', '2020-07-29 16:26:54', '2020-07-29 16:26:58');
INSERT INTO `t_project` VALUES ('6', '项目名称3', null, 'http://www.baidu.com/', null, null);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `job_number` int DEFAULT NULL COMMENT '工号',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别,0女 1男',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1启用 0禁用',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `real_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '12345', '123', '1', '0', '0', null, null, null);
INSERT INTO `t_user` VALUES ('3', 'xiebei1', '123456', '1234', '1', '1', null, null, null);
INSERT INTO `t_user` VALUES ('5', 'xiebei11', '123456', '12345', '1', '1', null, null, null);
INSERT INTO `t_user` VALUES ('6', '谢贝', '123', null, '1', '1', '2020-07-29 09:09:12', null, null);
INSERT INTO `t_user` VALUES ('7', '谢贝1', '123', null, '1', '1', '2020-07-29 09:11:10', null, null);
INSERT INTO `t_user` VALUES ('8', '谢贝123', '123', null, '1', '1', '2020-07-29 09:15:22', null, null);
INSERT INTO `t_user` VALUES ('9', '谢贝1234', '123', null, '1', '1', '2020-07-29 09:17:33', null, null);
INSERT INTO `t_user` VALUES ('10', '谢贝12345', '123', null, '1', '1', '2020-07-29 09:22:17', null, null);
INSERT INTO `t_user` VALUES ('11', '谢贝123456', '123', '3', '1', '1', '2020-07-29 09:24:53', null, null);
INSERT INTO `t_user` VALUES ('12', '123', '123', '1', '1', '1', '2020-07-29 10:09:10', null, null);
INSERT INTO `t_user` VALUES ('13', '1234', '123', '1', '1', '1', '2020-07-29 10:12:52', null, null);
