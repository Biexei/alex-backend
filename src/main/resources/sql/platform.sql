/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2020-08-13 23:23:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_interface_assert
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_assert`;
CREATE TABLE `t_interface_assert` (
  `assert_id` int NOT NULL AUTO_INCREMENT COMMENT '断言编号',
  `assert_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '断言名称',
  `case_id` int DEFAULT NULL COMMENT '用例编号',
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   0json/1html/2header/3responsecode',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `operator` tinyint DEFAULT NULL COMMENT '操作符0/=、1/< 、2/>、3/<=、4/>=、5/in、6/!=、7/re',
  `excepted_result` varchar(1000) DEFAULT NULL COMMENT '预期结果',
  `order` int DEFAULT NULL COMMENT '排序 执行断言时按照该字段排序',
  PRIMARY KEY (`assert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert
-- ----------------------------
INSERT INTO `t_interface_assert` VALUES ('1', null, '6', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('2', null, '6', '12', '12', '12', '12', '12');
INSERT INTO `t_interface_assert` VALUES ('5', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('6', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('7', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('8', '1', '19', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('9', '1', '20', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('10', '1', '21', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('11', '1', '22', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('12', '1', '23', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('13', '1', '23', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('14', '1', '28', '1', '1', '1', '1', '1');
INSERT INTO `t_interface_assert` VALUES ('15', '1', '28', '1', '1', '1', '1', '2');
INSERT INTO `t_interface_assert` VALUES ('16', '1', '29', '1', '1', '1', '1', '1');

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
  `type` tinyint DEFAULT NULL COMMENT '提取数据类型   0json/1html/2header/3responsecode',
  `expression` varchar(50) DEFAULT NULL COMMENT '提取表达式',
  `operator` tinyint DEFAULT NULL COMMENT '操作符0/=、1/< 、2/>、3/<=、4/>=、5/in、6/!=、7/re',
  `excepted_result` varchar(1000) DEFAULT NULL COMMENT '预期结果',
  `order` int DEFAULT NULL COMMENT '排序 执行断言时按照该字段排序',
  `actual_result` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '实际运行结果',
  `status` tinyint DEFAULT NULL COMMENT '是否通过 0通过 1失败 2错误',
  `error_message` varchar(1000) DEFAULT NULL COMMENT '断言出错异常信息',
  PRIMARY KEY (`assert_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert_log
-- ----------------------------
INSERT INTO `t_interface_assert_log` VALUES ('1', '1', null, '1132', '1', null, null, null, null, null, '123', null, null);

-- ----------------------------
-- Table structure for t_interface_case
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case`;
CREATE TABLE `t_interface_case` (
  `project_id` int DEFAULT NULL COMMENT '项目名称',
  `module_id` int DEFAULT NULL COMMENT '模块编号',
  `case_id` int NOT NULL AUTO_INCREMENT COMMENT '用例编号',
  `url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `method` tinyint(1) DEFAULT NULL COMMENT '请求方式  0get,1post,2update,3put,4delete',
  `desc` varchar(100) DEFAULT NULL COMMENT '用例描述',
  `level` tinyint DEFAULT NULL COMMENT '用例级别0高，1中，2低',
  `doc` varchar(200) DEFAULT NULL COMMENT '接口文档地址',
  `headers` varchar(1000) DEFAULT NULL COMMENT '请求头',
  `params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `data` varchar(1000) DEFAULT NULL COMMENT '请求formdata',
  `json` varchar(1000) DEFAULT NULL COMMENT '请求json',
  `creater` int DEFAULT NULL COMMENT '用例创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case
-- ----------------------------
INSERT INTO `t_interface_case` VALUES ('2', '2', '6', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 11:44:45', '2020-08-04 11:44:45');
INSERT INTO `t_interface_case` VALUES ('4', '3', '7', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 11:45:04', '2020-08-04 11:45:04');
INSERT INTO `t_interface_case` VALUES ('4', '4', '8', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 11:45:09', '2020-08-04 11:45:09');
INSERT INTO `t_interface_case` VALUES ('2', '2', '9', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 14:44:50', '2020-08-04 14:44:50');
INSERT INTO `t_interface_case` VALUES ('2', '2', '10', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 14:46:25', '2020-08-04 14:46:25');
INSERT INTO `t_interface_case` VALUES ('2', '2', '11', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 14:51:19', '2020-08-04 14:51:19');
INSERT INTO `t_interface_case` VALUES ('2', '2', '12', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 14:51:53', '2020-08-04 14:51:53');
INSERT INTO `t_interface_case` VALUES ('2', '2', '13', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 14:52:12', '2020-08-04 14:52:12');
INSERT INTO `t_interface_case` VALUES ('2', '2', '14', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 14:52:49', '2020-08-04 14:52:49');
INSERT INTO `t_interface_case` VALUES ('2', '2', '15', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 15:26:23', '2020-08-04 15:26:23');
INSERT INTO `t_interface_case` VALUES ('2', '2', '16', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:27:08', '2020-08-04 17:27:08');
INSERT INTO `t_interface_case` VALUES ('2', '2', '17', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:28:14', '2020-08-04 17:28:14');
INSERT INTO `t_interface_case` VALUES ('2', '2', '18', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:33:54', '2020-08-04 17:33:54');
INSERT INTO `t_interface_case` VALUES ('2', '2', '19', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:43:31', '2020-08-04 17:43:31');
INSERT INTO `t_interface_case` VALUES ('2', '2', '20', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:46:35', '2020-08-04 17:46:35');
INSERT INTO `t_interface_case` VALUES ('2', '2', '21', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:48:23', '2020-08-04 17:48:23');
INSERT INTO `t_interface_case` VALUES ('2', '2', '22', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:50:17', '2020-08-04 17:50:17');
INSERT INTO `t_interface_case` VALUES ('2', '2', '23', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 17:50:37', '2020-08-04 17:50:37');
INSERT INTO `t_interface_case` VALUES ('2', '2', '26', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 18:20:22', '2020-08-04 18:20:22');
INSERT INTO `t_interface_case` VALUES ('2', '2', '27', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 18:20:33', '2020-08-04 18:20:33');
INSERT INTO `t_interface_case` VALUES ('2', '2', '28', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 18:26:29', '2020-08-04 18:26:29');
INSERT INTO `t_interface_case` VALUES ('2', '2', '29', '/u/r/l', '0', '用例描述', '0', '接口文档', '请求头', '请求参数', '请求数据', '请求json', '1', '2020-08-04 18:26:38', '2020-08-04 18:26:38');

-- ----------------------------
-- Table structure for t_interface_case_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_execute_log`;
CREATE TABLE `t_interface_case_execute_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用例执行日志id',
  `case_id` int DEFAULT NULL COMMENT '用例id',
  `case_desc` varchar(100) DEFAULT NULL COMMENT '用例描述',
  `request_headers` varchar(1000) DEFAULT NULL COMMENT '请求头',
  `request_params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `request_data` varchar(1000) DEFAULT NULL COMMENT '请求formdata',
  `request_json` varchar(1000) DEFAULT NULL,
  `response_code` int DEFAULT NULL COMMENT '响应状态码',
  `response_headers` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '响应头',
  `response_body` text COMMENT '响应正文',
  `executer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行人',
  `status` tinyint DEFAULT NULL COMMENT '运行结果 0成功 1失败 2错误',
  `created_time` datetime DEFAULT NULL COMMENT '执行时间',
  `error_message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行失败异常错误信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_execute_log
-- ----------------------------
INSERT INTO `t_interface_case_execute_log` VALUES ('1', '6', 'desc', 'headers', 'params', 'data', 'json', '200', 'rheader', 'rbody', 'executer', '1', '2020-08-06 16:47:09', '1');
INSERT INTO `t_interface_case_execute_log` VALUES ('2', null, 'case desc', '{\"a\":\"1\",\"b\":\"2\"}', null, null, null, null, null, null, null, null, '2020-08-06 16:56:34', null);

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
