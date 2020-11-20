/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2020-11-20 16:15:58
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_http_setting
-- ----------------------------
INSERT INTO `t_http_setting` VALUES ('7', '本地代理', 'localhost:8888', '', '1', '0', '2020-10-02 16:14:57', '2020-11-20 09:06:50');
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
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`assert_id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert
-- ----------------------------
INSERT INTO `t_interface_assert` VALUES ('54', '状态码=200', '69', '3', null, '0', '200', '0', null, '2020-11-09 12:18:40');
INSERT INTO `t_interface_assert` VALUES ('55', 'username=123', '69', '0', '$..data.list[0].username', '0', '123', '1', null, '2020-11-09 12:18:40');
INSERT INTO `t_interface_assert` VALUES ('56', '状态码=200', '70', '3', null, '0', '200', '0', null, '2020-11-20 15:46:21');
INSERT INTO `t_interface_assert` VALUES ('57', 'code=200', '70', '0', '$..code', '0', '200', '1', null, '2020-11-20 15:46:21');
INSERT INTO `t_interface_assert` VALUES ('58', '状态码=200', '71', '3', null, '0', '200', '0', null, '2020-11-19 11:30:27');
INSERT INTO `t_interface_assert` VALUES ('59', 'code=200', '71', '0', '$..code', '0', '200', '1', null, '2020-11-19 11:30:27');
INSERT INTO `t_interface_assert` VALUES ('65', '断言id=4', '73', '0', '$..data.projectId', '0', '${selectProjectIdByName(\"自动化平台接口\", \"描述\")}', '0', null, '2020-11-09 12:18:29');
INSERT INTO `t_interface_assert` VALUES ('67', 'http code == 200', '75', '3', null, '0', '200', '0', null, '2020-11-04 11:34:27');
INSERT INTO `t_interface_assert` VALUES ('68', 'sub code == 200', '75', '0', '$..code', '0', '200', '1', null, '2020-11-04 11:34:27');
INSERT INTO `t_interface_assert` VALUES ('84', '检查用户名一致性', '82', '0', '$..data.username', '0', '${selectUserNameById(\"#{userId}\")}', '0', '2020-10-19 11:52:21', '2020-11-04 15:42:26');
INSERT INTO `t_interface_assert` VALUES ('85', '检查Content-Type', '82', '2', 'Content-Type', '0', 'application/json', '1', '2020-10-19 14:01:59', '2020-11-04 15:42:26');
INSERT INTO `t_interface_assert` VALUES ('86', 'sub code == 200', '83', '0', '$..code', '0', '200', '0', '2020-10-19 14:56:00', '2020-11-04 11:27:34');
INSERT INTO `t_interface_assert` VALUES ('87', '模块名称列表', '83', '0', '$..data.list[*].moduleName', '0', '[\"用户管理\",\"项目管理\",\"首页\",\"首页\"]', '1', '2020-10-19 14:56:00', '2020-11-04 11:27:34');
INSERT INTO `t_interface_assert` VALUES ('102', '3', '89', '0', '3', '0', '3', '2', '2020-10-29 12:42:53', '2020-10-29 12:42:53');
INSERT INTO `t_interface_assert` VALUES ('103', '4', '89', '0', '4', '0', '4', '3', '2020-10-29 12:42:53', '2020-10-29 12:42:53');
INSERT INTO `t_interface_assert` VALUES ('117', '状态码=200', '124', '3', null, '0', '200', '0', '2020-11-17 11:17:57', '2020-11-19 11:31:55');
INSERT INTO `t_interface_assert` VALUES ('118', 'code=200', '124', '0', '$..code', '0', '200', '1', '2020-11-17 11:17:57', '2020-11-19 11:31:55');
INSERT INTO `t_interface_assert` VALUES ('122', 'http code == 200', '126', '3', null, '0', '200', '0', '2020-11-20 15:31:17', '2020-11-20 15:31:17');
INSERT INTO `t_interface_assert` VALUES ('123', 'sub code == 200', '126', '0', '$..code', '0', '200', '1', '2020-11-20 15:31:17', '2020-11-20 15:31:17');
INSERT INTO `t_interface_assert` VALUES ('124', 'sub code == 200', '128', '0', '$..code', '0', '200', '0', '2020-11-20 15:32:49', '2020-11-20 15:34:53');
INSERT INTO `t_interface_assert` VALUES ('125', '模块名称列表', '128', '0', '$..data.list[*].moduleName', '0', '[\"用户管理\",\"项目管理\",\"首页\",\"首页\"]', '1', '2020-11-20 15:32:49', '2020-11-20 15:34:53');

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
  `pre_case_list` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '前置用例编号列表',
  PRIMARY KEY (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case
-- ----------------------------
INSERT INTO `t_interface_case` VALUES ('4', '6', '69', '/user/list', '0', '查看所有的用户列表', '1', null, '{\"Token\":\"${Token}\"}', '', null, null, '1', '2020-09-26 21:14:31', '2020-11-09 12:18:40', null);
INSERT INTO `t_interface_case` VALUES ('4', '6', '70', '/user/save', '1', '添加用户', '0', null, '{\"Token\":\"${Token}\"}', '', '{\"username\":\"${timestamp()}\",\"password\":\"123\",\"sex\":0,\"realName\":\"${timestamp()}\"}', null, '1', '2020-09-26 21:19:49', '2020-11-20 15:46:21', null);
INSERT INTO `t_interface_case` VALUES ('4', '7', '71', '/project/list', '0', '查看项目列表', '0', null, '{\"Token\":\"${Token}\"}', '', null, null, '1', '2020-09-26 21:29:42', '2020-11-19 11:30:27', null);
INSERT INTO `t_interface_case` VALUES ('4', '7', '73', '/project/info', '0', '查看项目详情', '1', null, '{\"Token\":\"${Token}\"}', '{\"projectId\":\"4\"}', null, null, '1', '2020-10-01 21:56:05', '2020-11-09 12:18:29', null);
INSERT INTO `t_interface_case` VALUES ('4', '6', '75', '/user/login', '1', '用户登录', '0', null, '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '1', '2020-10-06 20:11:18', '2020-11-04 11:34:27', null);
INSERT INTO `t_interface_case` VALUES ('4', '6', '82', 'user/info/{userId}', '0', '获取用户详情', '0', null, '{\"Token\":\"${Token}\"}', '{\"userId\":\"1\"}', '', null, '超级管理员', '2020-10-19 11:27:37', '2020-11-04 15:42:26', null);
INSERT INTO `t_interface_case` VALUES ('4', '7', '83', '/module/list', '0', '获取模块列表', '0', null, '{\"Token\":\"${Token}\"}', '', null, null, '超级管理员', '2020-10-19 14:54:24', '2020-11-04 11:27:34', null);
INSERT INTO `t_interface_case` VALUES ('19', '8', '88', '/', '0', '查看首页', '1', null, '', '', null, null, '超级管理员', '2020-10-22 19:08:48', '2020-11-03 22:44:29', null);
INSERT INTO `t_interface_case` VALUES ('20', '9', '89', '/', '0', '查看首页', '1', null, '', '', null, null, '超级管理员', '2020-10-29 09:05:24', '2020-10-29 12:42:53', null);
INSERT INTO `t_interface_case` VALUES ('4', '10', '121', '/env', '0', '查看所有的临时变量', '1', null, '{\"Token\":\"#{token}\"}', '', null, null, '超级管理员', '2020-11-09 10:01:51', '2020-11-18 11:50:22', '');
INSERT INTO `t_interface_case` VALUES ('4', '6', '124', '/user/save', '1', '添加用户-copy', '0', null, '', '', null, '{\"1\":\"${projectNames[0]}\"}', '超级管理员', '2020-11-17 11:17:57', '2020-11-19 11:31:55', null);
INSERT INTO `t_interface_case` VALUES ('4', '6', '126', '/user/login', '1', '组合用例-1.用户登录', '0', null, '', '{\"username\":\"123\",\"password\":\"123\"}', '', null, '系统管理员', '2020-11-20 15:31:17', '2020-11-20 15:31:17', null);
INSERT INTO `t_interface_case` VALUES ('4', '10', '127', '/env', '0', '组合用例-2.查看所有的临时变量', '1', null, '{\"Token\":\"#{ztoken}\"}', '', null, null, '系统管理员', '2020-11-20 15:31:54', '2020-11-20 15:35:01', null);
INSERT INTO `t_interface_case` VALUES ('4', '7', '128', '/module/list', '0', '组合用例-3.获取模块列表', '0', null, '{\"Token\":\"#{ztoken}\"}', '{\"moduleName\":\"#{zcode}\"}', null, null, '系统管理员', '2020-11-20 15:32:49', '2020-11-20 15:34:53', '[126,127]');

-- ----------------------------
-- Table structure for t_interface_case_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_execute_log`;
CREATE TABLE `t_interface_case_execute_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用例执行日志id',
  `case_id` int DEFAULT NULL COMMENT '用例id',
  `case_desc` varchar(100) DEFAULT NULL COMMENT '用例描述',
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
  `chain` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志执行调用链list',
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_rely_data
-- ----------------------------
INSERT INTO `t_interface_case_rely_data` VALUES ('6', '71', 'projectSize', '获取总项目数', '0', '$..data.total', '2020-09-26 21:33:28', '2020-09-26 21:33:28');
INSERT INTO `t_interface_case_rely_data` VALUES ('8', '69', 'contentType', '获取响应数据的Content-Type', '2', 'Content-Type', '2020-09-27 19:02:22', '2020-10-10 17:32:52');
INSERT INTO `t_interface_case_rely_data` VALUES ('9', '75', 'Token', '获取登录token', '0', '$..data.token', '2020-10-06 20:12:08', '2020-10-26 17:32:10');
INSERT INTO `t_interface_case_rely_data` VALUES ('11', '88', 'meideTitle', '', '1', '//title/text()', '2020-10-26 17:33:45', '2020-10-26 17:36:54');
INSERT INTO `t_interface_case_rely_data` VALUES ('12', '71', 'projectNames', '', '0', '$..data.list[*]', '2020-11-19 11:13:45', '2020-11-19 11:30:59');

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
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_suite
-- ----------------------------
INSERT INTO `t_interface_case_suite` VALUES ('6', '平台测试套件', '描述', null, '2020-11-19 11:10:15', '', '0', '1', '1');
INSERT INTO `t_interface_case_suite` VALUES ('34', '测试呢', null, '2020-11-20 15:40:04', '2020-11-20 15:40:04', '系统管理员', '1', '4', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_processor
-- ----------------------------
INSERT INTO `t_interface_processor` VALUES ('36', '75', 'token', '0', '$..token', null, '1', '2020-11-04 11:34:27', '2020-11-04 11:34:27');
INSERT INTO `t_interface_processor` VALUES ('37', '82', 'userId', '4', '$..userId', null, '1', '2020-11-04 15:22:00', '2020-11-04 15:42:26');
INSERT INTO `t_interface_processor` VALUES ('54', '126', 'ztoken', '0', '$..token', null, '1', '2020-11-20 15:31:17', '2020-11-20 15:31:17');
INSERT INTO `t_interface_processor` VALUES ('55', '127', 'zcode', '0', '$..code', null, '1', '2020-11-20 15:34:15', '2020-11-20 15:35:01');

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
) ENGINE=InnoDB AUTO_INCREMENT=189 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_case_ref
-- ----------------------------
INSERT INTO `t_interface_suite_case_ref` VALUES ('105', '69', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('106', '73', '6', '0', '2');
INSERT INTO `t_interface_suite_case_ref` VALUES ('107', '71', '6', '0', '3');
INSERT INTO `t_interface_suite_case_ref` VALUES ('108', '70', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('143', '82', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('144', '75', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('145', '83', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('157', '88', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('159', '89', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('170', '121', '6', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('175', '128', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('176', '127', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('177', '126', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('178', '124', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('179', '121', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('180', '89', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('181', '88', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('182', '83', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('183', '82', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('184', '75', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('185', '73', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('186', '71', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('187', '70', '34', '0', '1');
INSERT INTO `t_interface_suite_case_ref` VALUES ('188', '69', '34', '0', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_processor
-- ----------------------------
INSERT INTO `t_interface_suite_processor` VALUES ('39', '6', '0', '0', null, '', '2020-11-19 11:10:15', '2020-11-19 11:10:15');

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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('4', '6', '用户管理', null, '2020-09-26 21:13:27', '2020-09-26 21:13:27');
INSERT INTO `t_module` VALUES ('4', '7', '项目管理', null, '2020-09-26 21:23:36', '2020-09-26 21:23:36');
INSERT INTO `t_module` VALUES ('19', '8', '首页', null, '2020-10-22 19:08:11', '2020-10-22 19:08:11');
INSERT INTO `t_module` VALUES ('20', '9', '首页', null, '2020-10-29 09:05:07', '2020-10-29 09:05:07');
INSERT INTO `t_module` VALUES ('4', '10', '后置处理器', null, '2020-11-09 10:00:56', '2020-11-09 10:00:56');

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('4', '自动化平台接口', '描述', 'http://localhost:7777', '2020-07-29 16:26:54', '2020-07-29 16:26:58', 'http://localhost:7777', 'http://localhost:7777', 'http://localhost:7777', 'http://localhost:7777');
INSERT INTO `t_project` VALUES ('19', '美德网', 'http://www.meidekan.com', 'http://www.meidekan.com', null, null, 'http://www.meidekan.com', 'http://www.meidekan.com', 'http://www.meidekan.com', 'http://www.meidekan.com');
INSERT INTO `t_project` VALUES ('20', '百度', '', 'https://www.baidu.com', null, null, 'https://www.baidu.com', 'https://www.baidu.com', 'https://www.baidu.com', 'https://www.baidu.com');

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
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_rely_data
-- ----------------------------
INSERT INTO `t_rely_data` VALUES ('1', 'md5', 'md5(String s)', 'md5加密，引用方式示例：${md5(\"123456\")}', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58');
INSERT INTO `t_rely_data` VALUES ('2', 'uuid', 'uuid()', '获取uuid，引用方式示例：${uuid()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:29:54');
INSERT INTO `t_rely_data` VALUES ('3', 'base64', 'base64(String s)', 'base64加密，引用方式示例：${base64(\"123456\")}', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58');
INSERT INTO `t_rely_data` VALUES ('4', 'timestamp', 'timestamp()', '获取当前时间戳，引用方式示例：${timestamp()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56');
INSERT INTO `t_rely_data` VALUES ('5', 'now', 'now(String format)', '获取当前时间，引用方式示例：${now(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:07');
INSERT INTO `t_rely_data` VALUES ('10', 'yesterday', 'yesterday(String format)', '获取当前时间对应的昨天，引用方式示例：${yesterday(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:11');
INSERT INTO `t_rely_data` VALUES ('11', 'lastMonth', 'lastMonth(String format)', '获取当前时间对应的上个月，引用方式示例：${lastMonth(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:15');
INSERT INTO `t_rely_data` VALUES ('12', 'nextMonth', 'nextMonth(String format)', '获取当前时间对应的下个月，引用方式示例：${nextMonth(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:18');
INSERT INTO `t_rely_data` VALUES ('13', 'lastYear', 'lastYear(String format)', '获取当前时间对应的去年，引用方式示例：${lastYear(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:23');
INSERT INTO `t_rely_data` VALUES ('14', 'nextYear', 'nextYear(String format)', '获取当前时间对应的明年，引用方式示例：${nextYear(\"yyyy-MM-dd HH:mm:ss\")}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-10-21 16:18:28');
INSERT INTO `t_rely_data` VALUES ('15', 'time', 'time(String operator, String amount, String format)', '获取相较于当前时间的指定时间，operator：y、M、d、h、m、s；分别对应：年、月、日、时、分、秒，amount：数额，format：格式，如yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58');
INSERT INTO `t_rely_data` VALUES ('16', 'randomInt', 'randomInt(String length)', '获取指定长度的随机整数，引用方式示例：${randomInt(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:17');
INSERT INTO `t_rely_data` VALUES ('17', 'randomUpper', 'randomUpper(String length)', '获取指定长度的随机大写英文，引用方式示例：${randomUpper(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:27');
INSERT INTO `t_rely_data` VALUES ('18', 'randomLower', 'randomLower(String length)', '获取指定长度的随机小写英文，引用方式示例：${randomLower(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:32');
INSERT INTO `t_rely_data` VALUES ('19', 'randomEn', 'randomEn(String length)', '获取指定长度的随机英文，引用方式示例：${randomEn(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:44');
INSERT INTO `t_rely_data` VALUES ('20', 'randomIllegal', 'randomIllegal(String length)', '获取指定长度的非法字符，引用方式示例：${randomIllegal(\"10\")}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:34:56');
INSERT INTO `t_rely_data` VALUES ('21', 'cs', '长沙', '长沙市，引用方式示例：${cs}', '0', null, '2020-09-02 11:37:06', '2020-10-02 09:35:17');
INSERT INTO `t_rely_data` VALUES ('22', 'selectUserNameById', 'select username from t_user where user_id=?', '获取用户名，引用方式示例：${selectUserNameById(\"1\")}', '2', '1', '2020-09-02 11:37:06', '2020-10-19 11:26:10');
INSERT INTO `t_rely_data` VALUES ('27', 'projectCount', 'select count(1) from `t_project`', '查询项目列表，引用方式示例：${projectCount}', '2', '1', '2020-09-26 21:27:10', '2020-10-02 09:36:02');
INSERT INTO `t_rely_data` VALUES ('28', 'selectProjectIdByName', 'select project_id from `t_project` where `name`=? and `desc`=?', '根据项目名称查询项目id，引用方式示例：${selectProjectIdByName(\"名称\", \"描述\")}', '2', '1', '2020-10-01 20:42:36', '2020-10-02 09:36:32');
INSERT INTO `t_rely_data` VALUES ('29', 'city', 'city()', '随机城市，引用方式示例：${city()}', '1', null, '2020-10-21 15:26:23', '2020-10-21 15:26:32');
INSERT INTO `t_rely_data` VALUES ('30', 'province', 'province()', '随机省份，引用方式示例：${province()}', '1', null, '2020-10-21 16:16:16', null);
INSERT INTO `t_rely_data` VALUES ('31', 'country', 'country()', '随机国家，引用方式示例：${country()}', '1', null, '2020-10-21 16:16:20', null);
INSERT INTO `t_rely_data` VALUES ('32', 'phone', 'phone()', '随机手机号码，引用方式示例：${phone()}', '1', null, '2020-10-21 16:16:26', null);
INSERT INTO `t_rely_data` VALUES ('33', 'email', 'email()', '随机邮箱，引用方式示例：${email()}', '1', null, '2020-10-21 16:16:28', null);
INSERT INTO `t_rely_data` VALUES ('34', 'mac', 'mac()', '随机mac地址，引用方式示例：${mac()}', '1', null, '2020-10-21 16:16:30', null);
INSERT INTO `t_rely_data` VALUES ('35', 'book', 'book()', '随机书名，引用方式示例：${book()}', '1', null, '2020-10-21 16:16:35', null);
INSERT INTO `t_rely_data` VALUES ('36', 'name', 'name()', '随机中文名称，引用方式示例：${name()}', '1', null, '2020-10-21 16:16:37', null);
INSERT INTO `t_rely_data` VALUES ('37', 'ipv4', 'ipv4()', '随机ipv4地址，引用方式示例：${ipv4()}', '1', null, '2020-10-21 16:16:40', null);
INSERT INTO `t_rely_data` VALUES ('38', 'privateIpv4', 'privateIpv4()', '随机私有ipv4地址，引用方式示例：${privateIpv4()}', '1', null, '2020-10-21 16:16:42', null);
INSERT INTO `t_rely_data` VALUES ('39', 'publicIpv4', 'publicIpv4()', '随机公有ipv4地址，引用方式示例：${publicIpv4()}', '1', null, '2020-10-21 16:16:44', null);
INSERT INTO `t_rely_data` VALUES ('40', 'ipv6', 'ipv6()', '随机ipv6地址，引用方式示例：${ipv6()}', '1', null, '2020-10-21 16:16:47', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task
-- ----------------------------
INSERT INTO `t_task` VALUES ('19', '每日凌晨执行', '*/5 * * * * ?', '2020-11-05 17:02:05', null, '2020-11-05 17:02:04', '0', '6', '1');

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
INSERT INTO `t_task_email_ref` VALUES ('60', '19', '1014759718@qq.com');

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '123', '123', null, '1', '1', '2020-11-18 14:45:32', '2020-11-18 14:45:32', '系统管理员', '2');
INSERT INTO `t_user` VALUES ('3', '1605834453955', '123', null, '0', '1', '2020-11-20 09:07:34', '2020-11-20 09:07:34', 'The Last Enemy', '2');
INSERT INTO `t_user` VALUES ('4', '1605834456053', '123', null, '0', '1', '2020-11-20 09:07:36', '2020-11-20 09:07:36', 'The Painted Veil', '2');
INSERT INTO `t_user` VALUES ('5', '1605834457082', '123', null, '0', '1', '2020-11-20 09:07:37', '2020-11-20 09:07:37', 'In a Glass Darkly', '2');
INSERT INTO `t_user` VALUES ('6', '1605858385100', '123', null, '0', '1', '2020-11-20 15:46:25', '2020-11-20 15:46:25', '1605858385100', '2');
INSERT INTO `t_user` VALUES ('7', '1605858386496', '123', null, '0', '1', '2020-11-20 15:46:27', '2020-11-20 15:46:27', '1605858386496', '2');
INSERT INTO `t_user` VALUES ('8', '1605858387405', '123', null, '0', '1', '2020-11-20 15:46:27', '2020-11-20 15:46:27', '1605858387405', '2');
INSERT INTO `t_user` VALUES ('9', '1605858426634', '123', null, '0', '1', '2020-11-20 15:47:07', '2020-11-20 15:47:07', '1605858426634', '2');
INSERT INTO `t_user` VALUES ('10', '1605858432857', '123', null, '0', '1', '2020-11-20 15:47:13', '2020-11-20 15:47:13', '1605858432857', '2');
INSERT INTO `t_user` VALUES ('11', '1605858434856', '123', null, '0', '1', '2020-11-20 15:47:15', '2020-11-20 15:47:15', '1605858434856', '2');
