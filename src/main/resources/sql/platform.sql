/*
Navicat MySQL Data Transfer

Source Server         : loalhost
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2020-04-23 18:15:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `job_number` int NOT NULL COMMENT '工号',
  `sex` tinyint(1) NOT NULL COMMENT '性别,0女 1男',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1启用 0禁用',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `job_number` (`job_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'xiebei', '123456', '4', '0', '0');
INSERT INTO `user` VALUES ('3', 'xiebei1', '123456', '1234', '1', '1');
INSERT INTO `user` VALUES ('5', 'xiebei11', '123456', '12345', '1', '1');
