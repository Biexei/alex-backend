/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2020-10-06 23:03:35
*/

SET FOREIGN_KEY_CHECKS=0;

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_db
-- ----------------------------
INSERT INTO `t_db` VALUES ('1', 'platform', '0', '自动化平台库', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8', 'root', 'root', '2020-09-02 15:40:50', '2020-10-05 20:41:48', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_http_setting
-- ----------------------------
INSERT INTO `t_http_setting` VALUES ('7', '本地代理', 'localhost:8888', '', '1', '0', '2020-10-02 16:14:57', '2020-10-06 20:14:42');
INSERT INTO `t_http_setting` VALUES ('8', 'Cookie', 'this is my cookie', 'this is my cookie', '0', '1', '2020-10-02 16:18:23', '2020-10-02 17:25:17');
INSERT INTO `t_http_setting` VALUES ('11', 'qq', '1014759718@qq.com', '', '0', '2', '2020-10-02 18:57:14', '2020-10-05 14:56:46');
INSERT INTO `t_http_setting` VALUES ('12', '163', 'biexei@163.com', '', '0', '2', '2020-10-04 19:21:30', '2020-10-05 11:23:12');
INSERT INTO `t_http_setting` VALUES ('13', '126', 'biexei@126.com', null, '0', '2', '2020-10-04 21:10:46', '2020-10-04 21:10:46');

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
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert
-- ----------------------------
INSERT INTO `t_interface_assert` VALUES ('54', '状态码=200', '69', '3', '', '0', '200', '0');
INSERT INTO `t_interface_assert` VALUES ('55', 'username=123', '69', '0', '$..data.list[0].username', '0', '[\"123\"]', '1');
INSERT INTO `t_interface_assert` VALUES ('56', '状态码=200', '70', '3', '', '0', '200', '0');
INSERT INTO `t_interface_assert` VALUES ('57', 'code=200', '70', '0', '$..code', '0', '[200]', '1');
INSERT INTO `t_interface_assert` VALUES ('58', '状态码=200', '71', '3', '', '0', '200', '0');
INSERT INTO `t_interface_assert` VALUES ('59', 'code=200', '71', '0', '$..code', '0', '[200]', '1');
INSERT INTO `t_interface_assert` VALUES ('60', '项目总数和数据库总数一致', '71', '0', '$..data.total', '0', '[${projectCount}]', '2');
INSERT INTO `t_interface_assert` VALUES ('65', '断言id=4', '73', '0', '$..data.projectId', '0', '[${selectProjectIdByName(\"#{$..data.name}\", \"#{$..data.desc}\")}]', '0');
INSERT INTO `t_interface_assert` VALUES ('67', 'http code == 200', '75', '3', '', '0', '200', '0');
INSERT INTO `t_interface_assert` VALUES ('68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1');

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
  `excepted_result` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '预期结果',
  `order` int DEFAULT NULL COMMENT '排序 执行断言时按照该字段排序',
  `actual_result` mediumtext COMMENT '实际运行结果',
  `status` tinyint DEFAULT NULL COMMENT '是否通过 0通过 1失败 2错误',
  `error_message` mediumtext COMMENT '断言出错异常信息',
  `created_time` datetime DEFAULT NULL,
  PRIMARY KEY (`assert_log_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2320 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert_log
-- ----------------------------
INSERT INTO `t_interface_assert_log` VALUES ('2293', '1344', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 21:21:21');
INSERT INTO `t_interface_assert_log` VALUES ('2294', '1344', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 21:21:21');
INSERT INTO `t_interface_assert_log` VALUES ('2295', '1345', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 21:21:22');
INSERT INTO `t_interface_assert_log` VALUES ('2296', '1345', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 21:21:22');
INSERT INTO `t_interface_assert_log` VALUES ('2297', '1346', '65', '断言id=4', '73', '0', '$..data.projectId', '0', '[4]', '0', '[4]', '0', null, '2020-10-06 21:21:22');
INSERT INTO `t_interface_assert_log` VALUES ('2298', '1347', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2299', '1347', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2300', '1348', '56', '状态码=200', '70', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2301', '1348', '57', 'code=200', '70', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2302', '1349', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2303', '1349', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2304', '1350', '54', '状态码=200', '69', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2305', '1350', '55', 'username=123', '69', '0', '$..data.list[0].username', '0', '[\"123\"]', '1', '[\"123\"]', '0', null, '2020-10-06 21:21:23');
INSERT INTO `t_interface_assert_log` VALUES ('2306', '1351', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 22:01:23');
INSERT INTO `t_interface_assert_log` VALUES ('2307', '1351', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 22:01:23');
INSERT INTO `t_interface_assert_log` VALUES ('2308', '1352', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 22:01:25');
INSERT INTO `t_interface_assert_log` VALUES ('2309', '1352', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 22:01:25');
INSERT INTO `t_interface_assert_log` VALUES ('2310', '1353', '65', '断言id=4', '73', '0', '$..data.projectId', '0', '[4]', '0', '[4]', '0', null, '2020-10-06 22:01:25');
INSERT INTO `t_interface_assert_log` VALUES ('2311', '1354', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 22:01:27');
INSERT INTO `t_interface_assert_log` VALUES ('2312', '1354', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 22:01:27');
INSERT INTO `t_interface_assert_log` VALUES ('2313', '1355', '58', '状态码=200', '71', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 22:01:27');
INSERT INTO `t_interface_assert_log` VALUES ('2314', '1355', '59', 'code=200', '71', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 22:01:27');
INSERT INTO `t_interface_assert_log` VALUES ('2315', '1355', '60', '项目总数和数据库总数一致', '71', '0', '$..data.total', '0', '[1]', '2', '[1]', '0', null, '2020-10-06 22:01:27');
INSERT INTO `t_interface_assert_log` VALUES ('2316', '1356', '67', 'http code == 200', '75', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 22:01:29');
INSERT INTO `t_interface_assert_log` VALUES ('2317', '1356', '68', 'sub code == 200', '75', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 22:01:29');
INSERT INTO `t_interface_assert_log` VALUES ('2318', '1357', '56', '状态码=200', '70', '3', '', '0', '200', '0', '200', '0', null, '2020-10-06 22:01:29');
INSERT INTO `t_interface_assert_log` VALUES ('2319', '1357', '57', 'code=200', '70', '0', '$..code', '0', '[200]', '1', '[200]', '0', null, '2020-10-06 22:01:29');

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
  `creater` varchar(20) DEFAULT NULL COMMENT '用例创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case
-- ----------------------------
INSERT INTO `t_interface_case` VALUES ('4', '6', '69', 'user/list', '0', '查看所有的用户列表', '1', null, '{\"Token\":\"${Token}\"}', '', null, null, '1', '2020-09-26 21:14:31', '2020-10-06 20:16:19');
INSERT INTO `t_interface_case` VALUES ('4', '6', '70', 'user/register', '1', '用户注册', '0', null, '{\"Token\":\"${Token}\"}', '', '{\"username\":\"${timestamp()}\",\"password\":\"123\",\"sex\":\"0\"}', null, '1', '2020-09-26 21:19:49', '2020-10-06 20:15:50');
INSERT INTO `t_interface_case` VALUES ('4', '7', '71', 'project/list', '0', '查看项目列表', '0', null, '{\"Token\":\"${Token}\"}', '', null, null, '1', '2020-09-26 21:29:42', '2020-10-06 20:15:14');
INSERT INTO `t_interface_case` VALUES ('4', '7', '73', 'project/info', '0', '查看项目详情', '1', null, '{\"Token\":\"${Token}\"}', '{\"projectId\":\"4\"}', null, null, '1', '2020-10-01 21:56:05', '2020-10-06 20:13:06');
INSERT INTO `t_interface_case` VALUES ('4', '6', '75', '/user/login', '1', '用户登录', '0', null, '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '1', '2020-10-06 20:11:18', '2020-10-06 20:26:40');

-- ----------------------------
-- Table structure for t_interface_case_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_execute_log`;
CREATE TABLE `t_interface_case_execute_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用例执行日志id',
  `case_id` int DEFAULT NULL COMMENT '用例id',
  `case_desc` varchar(100) DEFAULT NULL COMMENT '用例描述',
  `case_url` varchar(300) DEFAULT NULL COMMENT '请求地址',
  `request_headers` varchar(1000) DEFAULT NULL COMMENT '请求头',
  `request_params` varchar(1000) DEFAULT NULL COMMENT '请求参数',
  `request_data` varchar(1000) DEFAULT NULL COMMENT '请求formdata',
  `request_json` varchar(1000) DEFAULT NULL,
  `response_code` int DEFAULT NULL COMMENT '响应状态码',
  `response_headers` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '响应头',
  `response_body` mediumtext COMMENT '响应正文',
  `run_time` int DEFAULT NULL COMMENT '运行时长',
  `executer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行人',
  `status` tinyint DEFAULT NULL COMMENT '运行结果 0成功 1失败 2错误',
  `created_time` datetime DEFAULT NULL COMMENT '执行时间',
  `error_message` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '执行失败异常错误信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1358 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_execute_log
-- ----------------------------
INSERT INTO `t_interface_case_execute_log` VALUES ('1344', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:20 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"fefc2ec9-712b-401e-bc7a-3852d57c93df\",\"username\":\"123\",\"isEnable\":1}}', '20', '系统管理员', '0', '2020-10-06 21:21:21', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1345', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:20 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"5b5a0594-126d-4612-a34d-13d3b79076ae\",\"username\":\"123\",\"isEnable\":1}}', '23', '系统调度', '0', '2020-10-06 21:21:22', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1346', '73', '查看项目详情', 'http://localhost:7777/project/info', '{\"Token\":\"5b5a0594-126d-4612-a34d-13d3b79076ae\"}', '{\"projectId\":\"4\"}', null, null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:20 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"projectId\":4,\"name\":\"自动化平台接口\",\"desc\":\"描述\",\"domain\":\"http://localhost:7777/\",\"createdTime\":\"2020-07-29 16:26:54\",\"updateTime\":\"2020-07-29 16:26:58\"}}', '23', '系统管理员', '0', '2020-10-06 21:21:22', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1347', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:22 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"3ea52e5c-ad48-47d6-9018-d548363b451d\",\"username\":\"123\",\"isEnable\":1}}', '23', '系统调度', '0', '2020-10-06 21:21:23', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1348', '70', '用户注册', 'http://localhost:7777/user/register', '{\"Token\":\"3ea52e5c-ad48-47d6-9018-d548363b451d\"}', '', '{\"username\":\"1601990482694\",\"password\":\"123\",\"sex\":\"0\"}', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:22 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"注册成功\",\"data\":{}}', '104', '系统管理员', '0', '2020-10-06 21:21:23', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1349', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:22 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"c9a009ac-9a78-41b3-a63e-fa97ef535d57\",\"username\":\"123\",\"isEnable\":1}}', '22', '系统调度', '0', '2020-10-06 21:21:23', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1350', '69', '查看所有的用户列表', 'http://localhost:7777/user/list', '{\"Token\":\"c9a009ac-9a78-41b3-a63e-fa97ef535d57\"}', '', null, null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 13:21:22 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"pageNum\":1,\"pageSize\":10,\"size\":10,\"startRow\":1,\"endRow\":10,\"total\":50,\"pages\":5,\"list\":[{\"userId\":10,\"username\":\"123\",\"password\":null,\"jobNumber\":\"123\",\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-16 14:12:55\",\"updateTime\":null,\"realName\":\"系统管理员\"},{\"userId\":30,\"username\":\"1601203864275\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-27 18:51:05\",\"updateTime\":null,\"realName\":null},{\"userId\":31,\"username\":\"1601203873947\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-27 18:51:14\",\"updateTime\":null,\"realName\":null},{\"userId\":32,\"username\":\"1601203888837\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-27 18:51:29\",\"updateTime\":null,\"realName\":null},{\"userId\":33,\"username\":\"1601203902344\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-27 18:51:42\",\"updateTime\":null,\"realName\":null},{\"userId\":34,\"username\":\"1601204272723\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-27 18:57:53\",\"updateTime\":null,\"realName\":null},{\"userId\":35,\"username\":\"1601204357587\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-09-27 18:59:18\",\"updateTime\":null,\"realName\":null},{\"userId\":36,\"username\":\"1601609319871\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-10-02 11:28:40\",\"updateTime\":null,\"realName\":null},{\"userId\":37,\"username\":\"1601609340496\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-10-02 11:29:00\",\"updateTime\":null,\"realName\":null},{\"userId\":38,\"username\":\"1601609639187\",\"password\":null,\"jobNumber\":null,\"sex\":0,\"isEnable\":1,\"createdTime\":\"2020-10-02 11:33:59\",\"updateTime\":null,\"realName\":null}],\"prePage\":0,\"nextPage\":2,\"isFirstPage\":true,\"isLastPage\":false,\"hasPreviousPage\":false,\"hasNextPage\":true,\"navigatePages\":8,\"navigatepageNums\":[1,2,3,4,5],\"navigateFirstPage\":1,\"navigateLastPage\":5,\"lastPage\":5,\"firstPage\":1}}', '27', '系统管理员', '0', '2020-10-06 21:21:23', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1351', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:22 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"d09d103d-9651-4928-bcf5-cb714a4ba63c\",\"username\":\"123\",\"isEnable\":1}}', '247', '系统管理员', '0', '2020-10-06 22:01:23', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1352', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:24 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"a0a77808-5f30-467e-9b1e-9f5f7869bd20\",\"username\":\"123\",\"isEnable\":1}}', '14', '系统调度', '0', '2020-10-06 22:01:25', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1353', '73', '查看项目详情', 'http://localhost:7777/project/info', '{\"Token\":\"a0a77808-5f30-467e-9b1e-9f5f7869bd20\"}', '{\"projectId\":\"4\"}', null, null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:24 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"projectId\":4,\"name\":\"自动化平台接口\",\"desc\":\"描述\",\"domain\":\"http://localhost:7777/\",\"createdTime\":\"2020-07-29 16:26:54\",\"updateTime\":\"2020-07-29 16:26:58\"}}', '18', '系统管理员', '0', '2020-10-06 22:01:25', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1354', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:27 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"1044b340-b4c5-439f-a16d-73574fa921c9\",\"username\":\"123\",\"isEnable\":1}}', '19', '系统调度', '0', '2020-10-06 22:01:27', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1355', '71', '查看项目列表', 'http://localhost:7777/project/list', '{\"Token\":\"1044b340-b4c5-439f-a16d-73574fa921c9\"}', '', null, null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:27 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"操作成功\",\"data\":{\"pageNum\":1,\"pageSize\":10,\"size\":1,\"startRow\":1,\"endRow\":1,\"total\":1,\"pages\":1,\"list\":[{\"created_time\":\"2020-07-29 16:26:54\",\"update_time\":\"2020-07-29 16:26:58\",\"project_id\":4,\"domain\":\"http://localhost:7777/\",\"name\":\"自动化平台接口\",\"desc\":\"描述\"}],\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1}}', '25', '系统管理员', '0', '2020-10-06 22:01:27', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1356', '75', '用户登录', 'http://localhost:7777//user/login', '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:28 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"登录成功\",\"data\":{\"realName\":\"系统管理员\",\"userId\":10,\"token\":\"723c00a5-c534-44fe-bdef-623a7054f571\",\"username\":\"123\",\"isEnable\":1}}', '22', '系统调度', '0', '2020-10-06 22:01:29', null);
INSERT INTO `t_interface_case_execute_log` VALUES ('1357', '70', '用户注册', 'http://localhost:7777/user/register', '{\"Token\":\"723c00a5-c534-44fe-bdef-623a7054f571\"}', '', '{\"username\":\"1601992888712\",\"password\":\"123\",\"sex\":\"0\"}', null, '200', '{\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\"Content-Type\":[\"application/json\"],\"Transfer-Encoding\":[\"chunked\"],\"Date\":[\"Tue, 06 Oct 2020 14:01:28 GMT\"],\"Keep-Alive\":[\"timeout=60\"],\"Connection\":[\"keep-alive\"]}', '{\"code\":200,\"msg\":\"注册成功\",\"data\":{}}', '90', '系统管理员', '0', '2020-10-06 22:01:29', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_rely_data
-- ----------------------------
INSERT INTO `t_interface_case_rely_data` VALUES ('6', '71', 'projectSize', '获取总项目数', '0', '$..data.total', '2020-09-26 21:33:28', '2020-09-26 21:33:28');
INSERT INTO `t_interface_case_rely_data` VALUES ('8', '69', 'contentType', '获取响应数据的Content-Type', '2', 'Content-Type1', '2020-09-27 19:02:22', '2020-10-06 12:17:35');
INSERT INTO `t_interface_case_rely_data` VALUES ('9', '75', 'Token', '获取登录token', '0', '$..data.token', '2020-10-06 20:12:08', '2020-10-06 20:12:21');

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
  PRIMARY KEY (`suite_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_suite
-- ----------------------------
INSERT INTO `t_interface_case_suite` VALUES ('6', '平台测试套件', '平台测试套件', '2020-10-05 20:39:29', '2020-10-05 20:39:29', null, '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_case_ref
-- ----------------------------
INSERT INTO `t_interface_suite_case_ref` VALUES ('105', '69', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('106', '73', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('107', '71', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('108', '70', '6', '0', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('4', '6', '用户管理', null, '2020-09-26 21:13:27', '2020-09-26 21:13:27');
INSERT INTO `t_module` VALUES ('4', '7', '项目管理', null, '2020-09-26 21:23:36', '2020-09-26 21:23:36');

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('4', '自动化平台接口', '描述', 'http://localhost:7777/', '2020-07-29 16:26:54', '2020-07-29 16:26:58');

-- ----------------------------
-- Table structure for t_rely_data
-- ----------------------------
DROP TABLE IF EXISTS `t_rely_data`;
CREATE TABLE `t_rely_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '依赖名称',
  `value` varchar(100) DEFAULT NULL COMMENT '依赖值',
  `desc` varchar(100) DEFAULT NULL COMMENT '依赖描述',
  `type` tinyint DEFAULT NULL COMMENT '依赖类型 0固定值 1反射方法 2sql',
  `datasource_id` int DEFAULT NULL COMMENT '数据源id',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_rely_data
-- ----------------------------
INSERT INTO `t_rely_data` VALUES ('1', 'md5', 'md5(String s)', 'md5加密，引用方式示例：${md5(\"123456\")}', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58');
INSERT INTO `t_rely_data` VALUES ('2', 'uuid', 'uuid()', '获取uuid，引用方式示例：${uuid()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:29:54');
INSERT INTO `t_rely_data` VALUES ('3', 'base64', 'base64(String s)', 'base64加密，引用方式示例：${base64(\"123456\")}', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58');
INSERT INTO `t_rely_data` VALUES ('4', 'timestamp', 'timestamp()', '获取当前时间戳，引用方式示例：${timestamp()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56');
INSERT INTO `t_rely_data` VALUES ('5', 'now', 'now(String format)', '获取当前时间，引用方式示例：${now(\"yyyy-MM-dd HH:mm:ss\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:31:39');
INSERT INTO `t_rely_data` VALUES ('10', 'yesterday', 'yesterday(String format)', '获取当前时间对应的昨天，引用方式示例：${yesterday(\"yyyy-MM-dd HH:mm:ss\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:31:55');
INSERT INTO `t_rely_data` VALUES ('11', 'lastMonth', 'lastMonth(String format)', '获取当前时间对应的上个月，引用方式示例：${lastMonth(\"yyyy-MM-dd HH:mm:ss\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:32:09');
INSERT INTO `t_rely_data` VALUES ('12', 'nextMonth', 'nextMonth(String format)', '获取当前时间对应的下个月，引用方式示例：${nextMonth(\"yyyy-MM-dd HH:mm:ss\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:32:25');
INSERT INTO `t_rely_data` VALUES ('13', 'lastYear', 'lastYear(String format)', '获取当前时间对应的去年，引用方式示例：${lastYear(\"yyyy-MM-dd HH:mm:ss\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:32:39');
INSERT INTO `t_rely_data` VALUES ('14', 'nextYear', 'nextYear(String format)', '获取当前时间对应的明年，引用方式示例：${nextYear(\"yyyy-MM-dd HH:mm:ss\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:32:49');
INSERT INTO `t_rely_data` VALUES ('15', 'time', 'time(String operator, String amount, String format)', '获取相较于当前时间的指定时间，operator：y、M、d、h、m、s；分别对应：年、月、日、时、分、秒，amount：数额，format：格式，如yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58');
INSERT INTO `t_rely_data` VALUES ('16', 'randomInt', 'randomInt(String length)', '获取指定长度的随机整数，引用方式示例：${randomInt(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:17');
INSERT INTO `t_rely_data` VALUES ('17', 'randomUpper', 'randomUpper(String length)', '获取指定长度的随机大写英文，引用方式示例：${randomUpper(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:27');
INSERT INTO `t_rely_data` VALUES ('18', 'randomLower', 'randomLower(String length)', '获取指定长度的随机小写英文，引用方式示例：${randomLower(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:32');
INSERT INTO `t_rely_data` VALUES ('19', 'randomEn', 'randomEn(String length)', '获取指定长度的随机英文，引用方式示例：${randomEn(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:44');
INSERT INTO `t_rely_data` VALUES ('20', 'randomIllegal', 'randomIllegal(String length)', '获取指定长度的非法字符，引用方式示例：${randomIllegal(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:56');
INSERT INTO `t_rely_data` VALUES ('21', 'city', '长沙', '当前市，引用方式示例：${city}', '0', null, '2020-09-02 11:37:06', '2020-10-02 09:35:17');
INSERT INTO `t_rely_data` VALUES ('22', 'username', 'select username, password from t_user', '获取用户名，引用方式示例：${username}', '2', '1', '2020-09-02 11:37:06', '2020-10-02 09:35:45');
INSERT INTO `t_rely_data` VALUES ('27', 'projectCount', 'select count(1) from `t_project`', '查询项目列表，引用方式示例：${projectCount}', '2', '1', '2020-09-26 21:27:10', '2020-10-02 09:36:02');
INSERT INTO `t_rely_data` VALUES ('28', 'selectProjectIdByName', 'select project_id from `t_project` where `name`=? and `desc`=?', '根据项目名称查询项目id，引用方式示例：${selectProjectIdByName(\"名称\", \"描述\")}', '2', '1', '2020-10-01 20:42:36', '2020-10-02 09:36:32');

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task
-- ----------------------------
INSERT INTO `t_task` VALUES ('19', '每日凌晨执行', '0 0 1 * * *', '2020-10-05 16:40:40', null, '2020-10-05 20:44:07', '0', '6', '1');

-- ----------------------------
-- Table structure for t_task_email_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_task_email_ref`;
CREATE TABLE `t_task_email_ref` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '定时任务id',
  `task_id` int DEFAULT NULL,
  `email_address` varchar(30) DEFAULT NULL COMMENT '邮箱地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task_email_ref
-- ----------------------------
INSERT INTO `t_task_email_ref` VALUES ('58', '19', '1014759718@qq.com');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `job_number` varchar(10) DEFAULT NULL COMMENT '工号',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别,0女 1男',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1启用 0禁用',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `role_id` int DEFAULT NULL COMMENT '角色类型 0超级管理员 1系统管理员 2普通用户 ',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '123', '123', '1', '1', '1', '2020-10-06 22:43:09', '2020-10-06 22:43:09', '超级管理员', '0');
