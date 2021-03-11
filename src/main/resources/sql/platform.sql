/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80020
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80020
File Encoding         : 65001

Date: 2021-03-11 16:46:06
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
INSERT INTO `t_http_setting` VALUES ('7', '本地代理', 'localhost:8888', '', '1', '0', '2020-10-02 16:14:57', '2020-12-14 16:23:01');
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
) ENGINE=InnoDB AUTO_INCREMENT=282 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert
-- ----------------------------
INSERT INTO `t_interface_assert` VALUES ('265', 'HTTP响应状态码', '277', '3', null, '0', '200', '0', '2021-03-08 15:25:56', '2021-03-08 15:25:56');
INSERT INTO `t_interface_assert` VALUES ('266', '接口状态码', '277', '0', '$..code', '0', '200', '1', '2021-03-08 15:25:56', '2021-03-08 15:25:56');
INSERT INTO `t_interface_assert` VALUES ('268', '接口状态码', '279', '0', '$..code', '0', '200', '0', '2021-03-08 15:33:36', '2021-03-08 15:33:36');
INSERT INTO `t_interface_assert` VALUES ('269', '接口状态码', '280', '0', '$..code', '0', '200', '0', '2021-03-08 15:34:20', '2021-03-09 09:27:29');
INSERT INTO `t_interface_assert` VALUES ('270', 'url', '280', '0', '$..data.url', '0', '${InterfaceCaseTable(\"$..url\",\"#{id}\")}', '1', '2021-03-08 15:36:25', '2021-03-09 09:27:29');
INSERT INTO `t_interface_assert` VALUES ('272', 'level', '280', '0', '$..data.level', '0', '${InterfaceCaseTable(\"$..level\",\"#{id}\")}', '2', '2021-03-08 16:12:12', '2021-03-09 09:27:29');
INSERT INTO `t_interface_assert` VALUES ('281', '执行耗时', '280', '4', null, '1', '5', '3', '2021-03-09 09:22:25', '2021-03-09 09:27:29');

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
) ENGINE=InnoDB AUTO_INCREMENT=3195 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case
-- ----------------------------
INSERT INTO `t_interface_case` VALUES ('24', '14', '277', '/user/login', '1', '用户登录', '0', null, '', '', '{\"username\":\"123\",\"password\":\"123\"}', null, '系统管理员', '2021-03-08 15:25:56', '2021-03-08 15:25:56', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '279', '/interface/case/list', '0', '查看用例列表', '0', null, '{\"Token\":\"${Token}\"}', '', null, null, '超级管理员', '2021-03-08 15:33:36', '2021-03-08 15:33:36', '0', null);
INSERT INTO `t_interface_case` VALUES ('24', '14', '280', '/interface/case/info/{id}', '0', '查看用例详情', '0', null, '{\"Token\":\"${Token}\"}', '{\"id\":\"277\"}', null, null, '超级管理员', '2021-03-08 15:34:20', '2021-03-09 09:27:29', '0', null);

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
  `chain` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志执行调用链list',
  `is_failed_retry` tinyint DEFAULT NULL COMMENT '是否为失败重跑用例0是1否',
  `source` tinyint DEFAULT NULL COMMENT '来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1785 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_execute_log
-- ----------------------------
INSERT INTO `t_interface_case_execute_log` VALUES ('1771', '277', '用户登录', 'http://localhost:7777/user/login', '1', '', '', '{\n	\"password\":\"123\",\n	\"username\":\"123\"\n}', null, '', '', '{\"username\":\"123\",\"password\":\"123\"}', null, '200', '{\n	\"Vary\":[\"Origin\",\"Access-Control-Request-Method\",\"Access-Control-Request-Headers\"],\n	\"Content-Type\":[\"application/json\"],\n	\"Transfer-Encoding\":[\"chunked\"],\n	\"Date\":[\"Tue, 09 Mar 2021 02:34:04 GMT\"],\n	\"Keep-Alive\":[\"timeout=60\"],\n	\"Connection\":[\"keep-alive\"]\n}', '{\n	\"msg\":\"登录成功\",\n	\"code\":200,\n	\"data\":{\n		\"realName\":\"超级管理员\",\n		\"userId\":1,\n		\"token\":\"49825cba-ab3c-4459-a9d8-58bcab8c89e2\",\n		\"username\":\"123\",\n		\"isEnable\":1\n	}\n}', '9', '超级管理员', '0', '2021-03-09 10:34:05', null, 'SN20210309103404QOYCE87664', 'SND20210309103404QOYCE87664', '[]', '1', '2');

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
INSERT INTO `t_interface_case_suite` VALUES ('35', '测试', null, '2021-03-09 10:33:55', '2021-03-09 10:33:55', '超级管理员', '1', '4', '1');

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
INSERT INTO `t_interface_processor` VALUES ('56', '280', 'id', '4', '$..id', null, '1', '2021-03-08 16:11:19', '2021-03-09 09:27:29');

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
) ENGINE=InnoDB AUTO_INCREMENT=1149 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_case_ref
-- ----------------------------
INSERT INTO `t_interface_suite_case_ref` VALUES ('189', '277', '35', '0', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_log
-- ----------------------------
INSERT INTO `t_interface_suite_log` VALUES ('111', '35', 'SN20210309103404QOYCE87664', '72', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:34:05', '2021-03-09 10:34:05', '1', '4', '超级管理员', '1');
INSERT INTO `t_interface_suite_log` VALUES ('112', '35', 'SN20210309103515OHDWM38266', '275', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:35:15', '2021-03-09 10:35:15', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('113', '35', 'SN20210309103535IRMDA48650', '71', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:35:35', '2021-03-09 10:35:35', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('114', '35', 'SN20210309103555WQOWF33518', '56', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:35:55', '2021-03-09 10:35:55', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('115', '35', 'SN20210309103640JOILC23147', '745', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:36:40', '2021-03-09 10:36:41', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('116', '35', 'SN20210309103645SFKRD15346', '71', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:36:45', '2021-03-09 10:36:45', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('117', '35', 'SN20210309103650DCQHC01510', '54', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:36:50', '2021-03-09 10:36:50', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('118', '35', 'SN20210309103655AICCC01585', '53', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:36:55', '2021-03-09 10:36:55', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('119', '35', 'SN20210309103700MYVEE62410', '48', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:37:00', '2021-03-09 10:37:00', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('120', '35', 'SN20210309103705MNIHI58317', '48', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:37:05', '2021-03-09 10:37:05', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('121', '35', 'SN20210309103710LDADY12661', '63', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:37:10', '2021-03-09 10:37:10', '1', '4', '定时任务', '1');
INSERT INTO `t_interface_suite_log` VALUES ('122', '35', 'SN20210309103715BGKQE85866', '54', '1', '1', '0', '1', '0', '0', null, '2021-03-09 10:37:15', '2021-03-09 10:37:15', '1', '4', '定时任务', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_processor
-- ----------------------------

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
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', 'user', '用户管理', '0');
INSERT INTO `t_permission` VALUES ('2', 'user:add', '新增', '1');
INSERT INTO `t_permission` VALUES ('3', 'user:modify', '修改', '1');
INSERT INTO `t_permission` VALUES ('4', 'user:remove', '删除', '1');
INSERT INTO `t_permission` VALUES ('5', 'user:list', '列表', '1');
INSERT INTO `t_permission` VALUES ('6', 'user:reset', '重置', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission_role_ref
-- ----------------------------
INSERT INTO `t_permission_role_ref` VALUES ('17', '1', '1', null);
INSERT INTO `t_permission_role_ref` VALUES ('22', '4', '1', null);
INSERT INTO `t_permission_role_ref` VALUES ('26', '2', '1', '2021-03-11 16:45:38');
INSERT INTO `t_permission_role_ref` VALUES ('27', '5', '1', '2021-03-11 16:45:47');
INSERT INTO `t_permission_role_ref` VALUES ('28', '6', '1', '2021-03-11 16:45:47');

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
INSERT INTO `t_post_processor_log` VALUES ('87', '36', '75', '3926', 'token', '1', '5620cddb-5ccd-4ca7-872b-5a1014b479b9', '0', '$..token', '2020-11-04 17:04:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('88', '37', '82', '3927', 'userId', '1', '1', '4', '$..userId', '2020-11-04 17:04:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('89', null, null, null, 'userId', null, '1', null, null, '2020-11-04 17:04:06', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('90', '36', '75', '3928', 'token', '1', 'af2c4b18-535c-4094-af73-cb53ca5d2331', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('91', '36', '75', '3929', 'token', '1', 'f9badef6-0188-4e37-aa99-c74ae6b1735c', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('92', '36', '75', '3932', 'token', '1', '1e904ef4-c99a-4fb3-b1b9-827a9d322f9d', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('93', '36', '75', '3934', 'token', '1', 'f010a79a-1d33-4b63-9167-2c98bca31d19', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('94', '36', '75', '3936', 'token', '1', 'aeca24cb-a248-4e26-bf64-3fcb3fe4c66c', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('95', '36', '75', '3938', 'token', '1', 'e7547d0e-3bf1-4c19-9afe-855fed1d8767', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('96', '37', '82', '3939', 'userId', '1', '1', '4', '$..userId', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('97', null, null, null, 'userId', null, '1', null, null, '2020-11-05 09:50:51', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('98', '36', '75', '3940', 'token', '1', '8c7129bb-ebdd-449c-a88d-cd5c08c75e7a', '0', '$..token', '2020-11-05 09:50:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('99', '36', '75', '3941', 'token', '1', '5a5beb5f-944c-4f96-be55-dce7a9d5c06b', '0', '$..token', '2020-11-05 09:50:52', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('100', '36', '75', '3945', 'token', '1', '81ce6f74-8296-477f-b27d-e39b862760c3', '0', '$..token', '2020-11-05 09:50:59', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('101', '36', '75', '3946', 'token', '1', '78d0f8aa-207b-4e07-819b-88cffc16d05c', '0', '$..token', '2020-11-05 09:50:59', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('102', '36', '75', '3949', 'token', '1', '127099f3-4ccf-4bc5-910e-27436ae6c125', '0', '$..token', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('103', '36', '75', '3951', 'token', '1', '1c1ba6c3-08db-4096-a42a-0447c2ebc3c5', '0', '$..token', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('104', '36', '75', '3953', 'token', '1', '0ef0a5f1-f599-4e04-907b-c0d31c7a9606', '0', '$..token', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('105', '36', '75', '3955', 'token', '1', 'a982ee84-9438-4069-bda7-3c2f63d9d112', '0', '$..token', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('106', '37', '82', '3956', 'userId', '1', '1', '4', '$..userId', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('107', null, null, null, 'userId', null, '1', null, null, '2020-11-05 09:51:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('108', '36', '75', '3957', 'token', '1', '035dc7e3-e1dd-4ac5-8630-d9ebdd40c723', '0', '$..token', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('109', '36', '75', '3958', 'token', '1', '54545750-e020-4604-a7ae-db99bc30fddb', '0', '$..token', '2020-11-05 09:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('110', '36', '75', '3962', 'token', '1', 'ab2b93af-37f6-433a-a3f2-166d143f7563', '0', '$..token', '2020-11-05 09:51:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('111', '36', '75', '3963', 'token', '1', '9bb9a9aa-98b9-4094-833a-3b8dedd85112', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('112', '36', '75', '3966', 'token', '1', '50859088-7870-4390-b17f-3e5373e27580', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('113', '36', '75', '3968', 'token', '1', '3a1b5455-ce8c-4053-8abc-46173fb483d1', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('114', '36', '75', '3970', 'token', '1', '93ee1fce-0d7e-467f-af82-e166b45038b5', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('115', '36', '75', '3972', 'token', '1', 'cb0ce2a2-6c0d-4d28-a20a-5cd6db675277', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('116', '37', '82', '3973', 'userId', '1', '1', '4', '$..userId', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('117', null, null, null, 'userId', null, '1', null, null, '2020-11-05 09:51:02', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('118', '36', '75', '3974', 'token', '1', '94373016-6244-4aa1-8080-170e3d660e8c', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('119', '36', '75', '3975', 'token', '1', '4be3df60-a056-46c9-88c2-bff123358174', '0', '$..token', '2020-11-05 09:51:02', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('120', '36', '75', '3980', 'token', '1', 'f233f274-44c8-4380-aa3e-a0a753e54657', '0', '$..token', '2020-11-05 09:51:11', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('121', '36', '75', '3981', 'token', '1', 'e219cc77-6e5d-4e28-9edf-c159e89e882e', '0', '$..token', '2020-11-05 09:51:11', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('122', '36', '75', '3979', 'token', '1', 'abedcb5e-094d-44f7-8e7c-4dfcb7f55ae2', '0', '$..token', '2020-11-05 09:51:11', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('123', '36', '75', '3982', 'token', '1', '94181a85-5d7f-4433-90e2-3a298367d01e', '0', '$..token', '2020-11-05 09:51:11', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('124', '36', '75', '3983', 'token', '1', 'bae1ac19-dcc7-4087-a7df-c4161e5ed5ea', '0', '$..token', '2020-11-05 09:51:11', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('125', '37', '82', '3987', 'userId', '1', '1', '4', '$..userId', '2020-11-05 09:51:11', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('126', null, null, null, 'userId', null, '1', null, null, '2020-11-05 09:51:12', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('127', '36', '75', '3985', 'token', '1', '689bcaed-cab1-445a-bde7-7bc434c306b9', '0', '$..token', '2020-11-05 09:51:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('128', '36', '75', '3990', 'token', '1', '495eb979-8c9c-4e14-af5b-192af11ee149', '0', '$..token', '2020-11-05 09:51:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('129', '36', '75', '3992', 'token', '1', '30888fa4-f7e0-445c-a1d9-744e3fd96492', '0', '$..token', '2020-11-05 09:51:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('130', '36', '75', '3996', 'token', '1', '00e37938-901f-41db-8764-b7cc9b23a9a0', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('131', '36', '75', '3999', 'token', '1', 'aba02b99-fed8-44f5-b1fc-ff1f50b407aa', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('132', '36', '75', '3998', 'token', '1', 'ccc58974-79b5-4a5f-9e8e-553ccb4c16ce', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('133', '36', '75', '4000', 'token', '1', '06d65337-7c2d-46ff-9ed3-96bdf31bdf9b', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('134', '36', '75', '3997', 'token', '1', '085f35a7-ce04-40b0-b408-97f9cf0ed756', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('135', '36', '75', '4001', 'token', '1', 'ebe0eea3-116b-42aa-9b37-85234390657e', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('136', '36', '75', '4002', 'token', '1', '2c3b3ab9-e947-42a4-a83e-6b0fb9b59f86', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('137', '36', '75', '4006', 'token', '1', '451285ba-786a-4cf2-ac98-0f21af5cdc10', '0', '$..token', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('138', '37', '82', '4009', 'userId', '1', '1', '4', '$..userId', '2020-11-05 09:51:13', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('139', null, null, null, 'userId', null, '1', null, null, '2020-11-05 09:51:13', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('140', '36', '75', '4013', 'token', '1', '1e103e66-c5ce-4e21-a869-01aa9ae1e333', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('141', '36', '75', '4014', 'token', '1', '657035fb-ba44-4de8-a8be-376c3da8b1f8', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('142', '36', '75', '4016', 'token', '1', 'd8afb081-e20c-4d96-9cca-ef1099e53af3', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('143', '36', '75', '4015', 'token', '1', '9a6ec191-7b86-4223-9d89-b86362748ce3', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('144', '36', '75', '4017', 'token', '1', '305461ea-eb72-4914-a731-ab2e699dc137', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('145', '36', '75', '4018', 'token', '1', '6a3017b7-5811-428f-b731-1892d2f177be', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('146', '37', '82', '4022', 'userId', '1', '1', '4', '$..userId', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('147', '36', '75', '4019', 'token', '1', 'c1ba6cf3-bce0-41f8-889b-478cdabf3cb7', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('148', null, null, null, 'userId', null, '1', null, null, '2020-11-05 09:51:14', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('149', '36', '75', '4027', 'token', '1', 'b3c5f0d7-5cdb-4697-afd7-68bee75b85e4', '0', '$..token', '2020-11-05 09:51:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('150', '36', '75', '4030', 'token', '1', '7f08e6c9-f81b-4db1-9617-e92b62392811', '0', '$..token', '2020-11-05 14:37:18', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('151', '37', '82', '4031', 'userId', '1', '1', '4', '$..userId', '2020-11-05 14:37:18', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('152', null, null, null, 'userId', null, '1', null, null, '2020-11-05 14:37:18', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('153', '36', '75', '4032', 'token', '1', 'b2abe208-5bf8-4775-93f8-48b68bde406b', '0', '$..token', '2020-11-05 14:37:43', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('154', '36', '75', '4033', 'token', '1', '97023e28-6c03-4c0a-ac40-a6b8ae7ff4f7', '0', '$..token', '2020-11-05 15:43:48', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('155', '37', '82', '4034', 'userId', '1', '1', '4', '$..userId', '2020-11-05 15:43:48', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('156', null, null, null, 'userId', null, '1', null, null, '2020-11-05 15:43:48', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('157', '36', '75', '4038', 'token', '1', 'e3ad4efd-f74b-49e8-bd8b-f9d90b89f27a', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('158', '36', '75', '4035', 'token', '1', '337a35f6-d62b-4b88-be24-334ce72fa833', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('159', '36', '75', '4039', 'token', '1', '7051f8a5-0c75-4b5d-af6e-0e23ad90b1e3', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('160', '36', '75', '4037', 'token', '1', 'a379d83e-ba2b-405a-a25b-af9fabdab639', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('161', '36', '75', '4036', 'token', '1', '0e0dc1bf-88e8-449d-b7b7-af34f03cb4a0', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('162', '36', '75', '4040', 'token', '1', 'a510c5ea-1759-41cb-ad32-a2f7a1327566', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('163', '37', '82', '4042', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('164', '36', '75', '4041', 'token', '1', 'd8d0691e-95ed-4f91-8c79-2c400d7f9037', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('165', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:33:01', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('166', '36', '75', '4046', 'token', '1', '896b5d80-2948-4399-905b-550b3f1dd480', '0', '$..token', '2020-11-05 16:33:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('167', '36', '75', '4052', 'token', '1', 'bfb6fdbd-a1bc-4644-a814-5002228d9049', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('168', '36', '75', '4053', 'token', '1', '502a3989-7ae3-4047-8eb3-743d847d2b67', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('169', '36', '75', '4054', 'token', '1', 'a637d345-cdc4-4586-bcb5-b7721e920669', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('170', '36', '75', '4055', 'token', '1', '2ad19ab5-8128-407f-9690-80e43ce701f1', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('171', '36', '75', '4056', 'token', '1', '649f9dda-87f5-4203-87b8-1fb7d682eff4', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('172', '36', '75', '4059', 'token', '1', '05d3e77c-a9b7-4660-829b-a5a4cd6cc47f', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('173', '36', '75', '4060', 'token', '1', 'c46934b0-21ec-4a2f-9b03-65227e48694c', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('174', '37', '82', '4062', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('175', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:34:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('176', '36', '75', '4063', 'token', '1', 'e0dc64de-842b-4620-be77-300f853a0fd9', '0', '$..token', '2020-11-05 16:34:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('177', '36', '75', '4069', 'token', '1', 'd39cc25d-159b-4a83-9412-cace74a5c219', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('178', '36', '75', '4070', 'token', '1', 'f97fd36d-a03d-4458-a62d-0f60702a1f87', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('179', '36', '75', '4072', 'token', '1', '7adb7b44-bf30-49e1-abd3-ddb78d4eb2d8', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('180', '36', '75', '4071', 'token', '1', '6e72652a-6993-4a28-b904-f64c7b6d8094', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('181', '36', '75', '4073', 'token', '1', '9e9cf53d-5cd8-4e1f-8a8d-9f80f5131fb8', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('182', '37', '82', '4075', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('183', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:35:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('184', '36', '75', '4074', 'token', '1', 'ffab265c-b27b-42d7-bfb0-717659ec06bf', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('185', '36', '75', '4078', 'token', '1', '86205e29-5bbe-4427-9444-d776a6d29151', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('186', '36', '75', '4082', 'token', '1', 'a7a39c42-4c5a-4eaa-8db7-2c0cb0fde59d', '0', '$..token', '2020-11-05 16:35:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('187', '36', '75', '4088', 'token', '1', '89d190ce-53f9-40b9-8191-b99061945be8', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('188', '36', '75', '4089', 'token', '1', '3cfd38fa-486d-40f2-b413-75fe0267ef2f', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('189', '36', '75', '4086', 'token', '1', 'bdf992e1-9720-4f54-b396-7b4b2ff20dce', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('190', '36', '75', '4087', 'token', '1', 'ffb467ca-847e-4e16-ac1c-2eed64e471b5', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('191', '36', '75', '4091', 'token', '1', '74f114ad-0a69-459b-af3f-08e7e864d145', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('192', '37', '82', '4094', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('193', '36', '75', '4093', 'token', '1', '8d9d08a1-dbd8-465b-948a-3da643ea190d', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('194', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:42:14', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('195', '36', '75', '4095', 'token', '1', '0e9d8e38-147b-48dd-ba73-d425ac68b0f4', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('196', '36', '75', '4092', 'token', '1', 'd1e7a2d4-3f33-4e1a-b3f0-d7dd55bca639', '0', '$..token', '2020-11-05 16:42:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('197', '36', '75', '4107', 'token', '1', 'af7be3c2-fcc4-4a7c-b238-b56532c5d2ce', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('198', '36', '75', '4103', 'token', '1', '1a7bc1d3-d62d-4f13-bb78-5d870a1e6bb8', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('199', '36', '75', '4105', 'token', '1', '0ed86483-5e32-4415-8fed-bdce516044e9', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('200', '36', '75', '4106', 'token', '1', '668d455c-2206-465a-aa53-880cffc96b1d', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('201', '36', '75', '4104', 'token', '1', '231bfec7-5cf5-4bb6-aa83-09de5c1ac46f', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('202', '36', '75', '4109', 'token', '1', 'e45cdc93-6d05-4eda-bf1b-3236fc308eca', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('203', '36', '75', '4111', 'token', '1', 'c7444ca6-c462-4199-a4c7-bdcbaba7ba7b', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('204', '37', '82', '4112', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('205', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:44:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('206', '36', '75', '4113', 'token', '1', '652d7aaf-92a2-4dff-9974-fcd1d2a21be8', '0', '$..token', '2020-11-05 16:44:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('207', '36', '75', '4120', 'token', '1', 'f649d9b3-9709-4924-932a-001c398c9a8c', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('208', '36', '75', '4122', 'token', '1', '4c867578-0a53-4a29-92a4-a36003ea5533', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('209', '36', '75', '4121', 'token', '1', '510e9f8a-2700-4aa4-a7b4-6f4805d220f0', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('210', '36', '75', '4123', 'token', '1', '907cb4c8-e907-470e-a4ab-6ec58411f560', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('211', '36', '75', '4124', 'token', '1', 'e44de4ed-abd7-4750-b1fa-104cbe578db4', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('212', '37', '82', '4126', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('213', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:45:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('214', '36', '75', '4125', 'token', '1', 'f07f60b8-5750-4333-abe4-979621a95cd6', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('215', '36', '75', '4127', 'token', '1', '53897566-f6db-499c-b279-0ed2859e80fc', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('216', '36', '75', '4132', 'token', '1', 'fc256be8-551a-4f70-b5c3-74df6812f6c5', '0', '$..token', '2020-11-05 16:45:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('217', '36', '75', '4141', 'token', '1', '16fb2e79-70c3-4c2b-81bf-b9e8ce76471f', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('218', '36', '75', '4139', 'token', '1', '55149706-23b8-481d-ab22-e170730bb2b6', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('219', '36', '75', '4140', 'token', '1', '99d42fff-f05e-4e82-925f-7d4e4d59c93d', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('220', '36', '75', '4138', 'token', '1', 'b51bab8e-a784-45f8-bb3b-d9091529ce65', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('221', '36', '75', '4137', 'token', '1', 'f418dfc7-0a67-4669-9d82-496fa089427b', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('222', '36', '75', '4144', 'token', '1', 'b68e08a9-65a0-4128-9f73-da5ac8a5efdf', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('223', '36', '75', '4145', 'token', '1', '27ba3feb-91bb-4575-9503-cdd2b5df6349', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('224', '37', '82', '4146', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('225', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:51:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('226', '36', '75', '4147', 'token', '1', '50211c9e-9e1c-441e-838f-784a9a9d038d', '0', '$..token', '2020-11-05 16:51:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('227', '36', '75', '4156', 'token', '1', '17f2b4aa-6e09-4419-b18f-24723442cc82', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('228', '36', '75', '4155', 'token', '1', '392d31bb-4193-4391-a2a4-652934020163', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('229', '36', '75', '4157', 'token', '1', 'e28bcb25-91b6-4ab8-9f4a-718524d7bb59', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('230', '36', '75', '4160', 'token', '1', 'ba929a18-03ab-4d4d-a284-1bc966101671', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('231', '36', '75', '4159', 'token', '1', 'bdddee2a-6951-4be0-87d5-e5b0c49b7946', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('232', '36', '75', '4158', 'token', '1', 'b75ab38e-ef59-4847-a4f7-3032c2fd845f', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('233', '36', '75', '4161', 'token', '1', '9b2aa1ba-caf6-4dd7-b946-fe467f26c39f', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('234', '37', '82', '4164', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('235', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:56:01', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('236', '36', '75', '4163', 'token', '1', '03d1f629-fefc-4fca-a471-169b4e1a2176', '0', '$..token', '2020-11-05 16:56:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('237', '36', '75', '4171', 'token', '1', '0b53ea5e-ace8-4623-afeb-e32ca83c71d9', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('238', '36', '75', '4172', 'token', '1', '34281a24-5120-4123-bd42-360cfbc24aa8', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('239', '36', '75', '4173', 'token', '1', 'b95aa5f3-cef3-479f-b246-3b719e8b7395', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('240', '36', '75', '4174', 'token', '1', '6cd6445b-3957-4bec-a17f-af46f706e442', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('241', '36', '75', '4175', 'token', '1', 'c2b624c8-9682-4a17-8d03-ee97f8128821', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('242', '36', '75', '4176', 'token', '1', '86247146-21b4-4ec3-858e-b30592bd2519', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('243', '36', '75', '4178', 'token', '1', '57de319a-3bd5-46db-a601-be67c041ff1e', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('244', '37', '82', '4183', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('245', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:57:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('246', '36', '75', '4182', 'token', '1', 'baa2c3a1-8223-4503-ac62-15b14c688bce', '0', '$..token', '2020-11-05 16:57:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('247', '36', '75', '4190', 'token', '1', '6fceca88-e95e-475f-b451-3278aa6a1c6a', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('248', '36', '75', '4191', 'token', '1', '081ac81d-9fd3-4b07-99c6-eaed839c94ff', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('249', '36', '75', '4189', 'token', '1', '729d32b8-5505-473e-82ea-03bc7290cb86', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('250', '36', '75', '4192', 'token', '1', '28d384aa-5fdd-4e33-9dab-ea67a8e86433', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('251', '36', '75', '4188', 'token', '1', '152e7ec3-3ef7-4975-87fe-78ac91cb834d', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('252', '37', '82', '4197', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('253', '36', '75', '4193', 'token', '1', 'd06460c4-fae3-4bf1-8569-39e36bf6ad66', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('254', '36', '75', '4194', 'token', '1', 'd7bbba79-22d7-4934-a93c-dea6340960ac', '0', '$..token', '2020-11-05 16:57:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('255', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:57:35', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('256', '36', '75', '4199', 'token', '1', '53019e2c-cf62-4c43-98c7-db104d1ef4ce', '0', '$..token', '2020-11-05 16:57:36', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('257', '36', '75', '4205', 'token', '1', '8cb9ca56-428b-4cf7-94e2-6d39513dcfaf', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('258', '36', '75', '4206', 'token', '1', 'b3652940-6c1b-4941-bb07-00a5983cc46f', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('259', '36', '75', '4207', 'token', '1', '81511047-a11a-456b-9f88-69031019ddf0', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('260', '36', '75', '4209', 'token', '1', '97ffa8bb-cace-42c9-8045-456b8324f6d7', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('261', '36', '75', '4208', 'token', '1', '3bc57cad-4223-4868-9123-d8c50855b6fc', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('262', '37', '82', '4213', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('263', '36', '75', '4210', 'token', '1', 'd134bad5-4103-467c-9aa8-821ad67e508e', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('264', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:57:55', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('265', '36', '75', '4212', 'token', '1', '994f26ef-5d8f-41df-8569-dff1b768d007', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('266', '36', '75', '4217', 'token', '1', 'a1cb5172-db53-4145-8232-344a8b360d25', '0', '$..token', '2020-11-05 16:57:55', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('267', '36', '75', '4222', 'token', '1', '8c4f0b2f-a2b1-4c7f-8545-428945cca80d', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('268', '36', '75', '4225', 'token', '1', '89c447eb-c5de-4aaa-a8e9-bafaa67d1e17', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('269', '36', '75', '4223', 'token', '1', 'd6fb5f72-b3a0-417d-8f51-27c9af2b29c9', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('270', '36', '75', '4224', 'token', '1', '2fa2ae32-f58d-4374-8395-0402fdb84f99', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('271', '36', '75', '4226', 'token', '1', '874dddf8-65bf-425a-9c45-573b535d2f2f', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('272', '37', '82', '4229', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('273', '36', '75', '4227', 'token', '1', '6ee89504-2ff9-4213-a002-c83b19cc86ca', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('274', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:58:15', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('275', '36', '75', '4230', 'token', '1', '1b470b42-f7ed-4798-b519-224c2681fe38', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('276', '36', '75', '4233', 'token', '1', 'ca1b8cd0-7794-4ea5-b6b7-5e8a108fc745', '0', '$..token', '2020-11-05 16:58:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('277', '36', '75', '4239', 'token', '1', 'fd4ed492-f8a3-4c40-bd60-5eee366cd97d', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('278', '36', '75', '4243', 'token', '1', 'da857531-ccc8-4f59-92ad-4bc44068944a', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('279', '36', '75', '4241', 'token', '1', '258813c2-cbcf-4c42-b804-4cddfe26b6f3', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('280', '36', '75', '4242', 'token', '1', 'c8d8ff17-5966-4fac-b412-67413db558be', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('281', '36', '75', '4240', 'token', '1', '0928d25a-bd42-478a-95af-3a6932443e1d', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('282', '37', '82', '4245', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('283', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:58:35', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('284', '36', '75', '4244', 'token', '1', 'f0c9947a-193f-46c6-8673-4366b935757b', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('285', '36', '75', '4249', 'token', '1', '81b1b961-d932-43f2-998f-b95c0181bcc7', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('286', '36', '75', '4252', 'token', '1', '53a7902c-b943-4bdd-a070-f7dae5ec6667', '0', '$..token', '2020-11-05 16:58:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('287', '36', '75', '4259', 'token', '1', '7679d022-fcfa-4038-a849-728837f9a8cf', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('288', '36', '75', '4256', 'token', '1', 'fa9f3d30-e7fc-4d01-85c2-a33b40bb2d1a', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('289', '36', '75', '4260', 'token', '1', '36ef5d6f-4739-40e6-84d8-e719bb964bdf', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('290', '36', '75', '4257', 'token', '1', 'a4b31608-40f6-46e8-99a4-793e9af99712', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('291', '36', '75', '4258', 'token', '1', 'c0e566fa-b75b-4729-b98e-20a925c8df20', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('292', '37', '82', '4264', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('293', '36', '75', '4261', 'token', '1', 'f085e9d5-3760-4346-9271-d351a63a8eae', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('294', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:59:00', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('295', '36', '75', '4265', 'token', '1', 'ba772971-9f42-4f0f-9f8b-a91677bcc3c8', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('296', '36', '75', '4269', 'token', '1', '39765459-0ef4-4241-80de-05887f8c7185', '0', '$..token', '2020-11-05 16:59:00', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('297', '36', '75', '4273', 'token', '1', 'a71bcb24-98f0-4530-95a6-4754fff77e47', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('298', '36', '75', '4274', 'token', '1', 'c404386b-e2ce-4198-810e-338e0e942ab4', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('299', '36', '75', '4276', 'token', '1', 'ef0e1bd3-3185-43fd-9528-57e1cd65a36f', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('300', '36', '75', '4275', 'token', '1', 'a56b2ca4-c9fd-4aef-9e4a-931112b47d34', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('301', '36', '75', '4277', 'token', '1', 'bb782dee-8e39-4f55-94ca-db48191d26ee', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('302', '37', '82', '4281', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('303', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:59:25', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('304', '36', '75', '4278', 'token', '1', 'd0de39f3-fa92-4597-9bb6-c81f7aa1fbf5', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('305', '36', '75', '4283', 'token', '1', '1ffe39bd-356b-4dff-935d-899b7af33caa', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('306', '36', '75', '4284', 'token', '1', 'ffb21c7d-b123-439d-9765-0afbf45b436b', '0', '$..token', '2020-11-05 16:59:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('307', '36', '75', '4290', 'token', '1', '90450bf6-68db-435f-bcb4-587fd965c97f', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('308', '36', '75', '4291', 'token', '1', '20f5fbbb-c4cd-4a69-97bd-b54b26075a5b', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('309', '36', '75', '4292', 'token', '1', 'e8561f2a-e227-4804-990c-4872a29d860b', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('310', '36', '75', '4293', 'token', '1', '1f41fbaa-a8f6-4500-b0e4-9dcd9246fa65', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('311', '36', '75', '4294', 'token', '1', 'dcdda1e3-976a-4d91-b8fa-8c637ed90ff9', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('312', '37', '82', '4299', 'userId', '1', '1', '4', '$..userId', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('313', '36', '75', '4295', 'token', '1', '3fdf4883-8206-40f0-9d3f-7e094c5f207e', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('314', null, null, null, 'userId', null, '1', null, null, '2020-11-05 16:59:45', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('315', '36', '75', '4298', 'token', '1', '1820db8d-8207-4d10-bc74-c1978a67587c', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('316', '36', '75', '4300', 'token', '1', '6804b5ac-a953-41ed-8ed7-119c47623c14', '0', '$..token', '2020-11-05 16:59:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('317', '36', '75', '4307', 'token', '1', '385d3e2a-ebc8-4218-ae9a-d5ed9f43169d', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('318', '36', '75', '4309', 'token', '1', '5e916a1d-f349-4576-b4ce-7163384698e5', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('319', '36', '75', '4310', 'token', '1', '34bb031e-e0b7-49fa-94f5-8bc096dd520c', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('320', '36', '75', '4311', 'token', '1', 'bc2914d1-b9db-47f4-bbbc-774d9a756079', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('321', '36', '75', '4308', 'token', '1', 'c0e05be5-bffb-4185-a420-580c5692430f', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('322', '37', '82', '4314', 'userId', '1', '1', '4', '$..userId', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('323', null, null, null, 'userId', null, '1', null, null, '2020-11-05 17:00:05', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('324', '36', '75', '4312', 'token', '1', '1301e3ae-0b86-4811-add8-c22dd3c2551b', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('325', '36', '75', '4315', 'token', '1', 'da4278b8-2cf3-48e4-8ba4-5f468037b36b', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('326', '36', '75', '4319', 'token', '1', '2b7f44aa-6c7e-44e1-8cfc-4a16c8b38e16', '0', '$..token', '2020-11-05 17:00:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('327', '36', '75', '4328', 'token', '1', '568972d1-9405-4d4e-9881-2cf292334b19', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('328', '36', '75', '4325', 'token', '1', 'ecde432d-5985-4121-a765-9bb655fa0487', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('329', '36', '75', '4324', 'token', '1', '18f3e894-758b-43f6-b63d-420f1d8afade', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('330', '36', '75', '4326', 'token', '1', '0094da7e-b813-4a49-8577-f4e8dc4fc09a', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('331', '36', '75', '4327', 'token', '1', '65d2bd7a-488c-4d46-9e9f-41ffde467f0f', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('332', '37', '82', '4331', 'userId', '1', '1', '4', '$..userId', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('333', '36', '75', '4329', 'token', '1', 'b971a0a4-a7c2-4352-85b3-3915c69e01be', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('334', null, null, null, 'userId', null, '1', null, null, '2020-11-05 17:01:06', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('335', '36', '75', '4330', 'token', '1', 'da0b6141-15bc-4066-9add-222a19c819c3', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('336', '36', '75', '4335', 'token', '1', 'a15dd5e1-0904-45f2-a005-78aa3d39a72d', '0', '$..token', '2020-11-05 17:01:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('337', '36', '75', '4341', 'token', '1', 'e15a0a34-2643-4165-a408-39314146ce74', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('338', '36', '75', '4342', 'token', '1', 'd25bcfd7-de3b-4915-9e3f-2b160ef63196', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('339', '36', '75', '4343', 'token', '1', '1b4b4f29-9093-44ed-91e9-f0fd46d173cd', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('340', '36', '75', '4345', 'token', '1', '7d80ea08-1aaa-4138-b0d7-8bbe02c0de24', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('341', '36', '75', '4344', 'token', '1', '4d97d8ad-69ed-4c63-b3be-9704848013b5', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('342', '37', '82', '4348', 'userId', '1', '1', '4', '$..userId', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('343', '36', '75', '4346', 'token', '1', '55a835d3-afd2-4166-956a-f5555863a5d1', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('344', null, null, null, 'userId', null, '1', null, null, '2020-11-05 17:01:25', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('345', '36', '75', '4350', 'token', '1', 'd52760c6-3f11-4a5c-8d67-4c96fe090f39', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('346', '36', '75', '4353', 'token', '1', 'ec4d424b-7780-48de-8c99-6b3434b08bc6', '0', '$..token', '2020-11-05 17:01:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('347', '36', '75', '4358', 'token', '1', '2e0f6eef-65c3-41c1-a93a-173986253199', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('348', '36', '75', '4362', 'token', '1', 'fe607dad-883b-435e-96c7-b342d9b8dd49', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('349', '36', '75', '4360', 'token', '1', '3f539804-cf00-4e32-8a27-da8e7d553075', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('350', '36', '75', '4359', 'token', '1', '00b4c938-f738-405d-9b42-6dbc2f77c34e', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('351', '36', '75', '4361', 'token', '1', '4ac97991-8ddd-41c6-ac7b-7d99f82c1b8f', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('352', '37', '82', '4365', 'userId', '1', '1', '4', '$..userId', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('353', null, null, null, 'userId', null, '1', null, null, '2020-11-05 17:01:45', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('354', '36', '75', '4363', 'token', '1', 'afa72d7d-2a41-4e57-bb5b-fdc477370620', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('355', '36', '75', '4366', 'token', '1', 'e8090b83-5ac7-437b-ac49-b94a7e492f03', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('356', '36', '75', '4370', 'token', '1', 'b8678a44-c17f-43bb-b4d7-31d53c116d8b', '0', '$..token', '2020-11-05 17:01:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('357', '36', '75', '4377', 'token', '1', 'a31d013d-1cfe-40b8-9001-eaa8f8547314', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('358', '36', '75', '4376', 'token', '1', '7f74bd6c-704d-486d-a4a4-a8056dd75635', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('359', '36', '75', '4375', 'token', '1', '47e2f6c5-d0d4-4bfe-b947-7bd21eae6c88', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('360', '36', '75', '4378', 'token', '1', 'efabb4c0-ac56-4279-9122-a50ea2f20e99', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('361', '36', '75', '4379', 'token', '1', '57ee7b1b-5282-4010-be3f-2bbb4b5464ad', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('362', '37', '82', '4382', 'userId', '1', '1', '4', '$..userId', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('363', null, null, null, 'userId', null, '1', null, null, '2020-11-05 17:02:05', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('364', '36', '75', '4380', 'token', '1', '5addf1fb-03e5-4552-85d6-89ae41104d1f', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('365', '36', '75', '4385', 'token', '1', 'ac45ddd1-a94a-4243-a0af-b7f93d275a28', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('366', '36', '75', '4384', 'token', '1', '18bfd783-8db8-4282-88e9-e2824b896486', '0', '$..token', '2020-11-05 17:02:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('367', '36', '75', '4395', 'token', '1', '7a1284e8-bb4e-4884-a817-f144f06c13d0', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('368', '36', '75', '4394', 'token', '1', 'dcc27b49-1661-4faa-9fdc-da5ae11b7034', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('369', '36', '75', '4392', 'token', '1', '4bf24ac7-ce88-422f-a40d-28dd14fd2926', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('370', '36', '75', '4393', 'token', '1', 'edec1c3e-4d27-40ec-80aa-ce0cff8770a8', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('371', '36', '75', '4396', 'token', '1', 'ba998b33-6aaf-442c-ac0a-253523ffb415', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('372', '36', '75', '4401', 'token', '1', 'd3f569fe-8e82-4812-943f-d1c824cbdd0c', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('373', '36', '75', '4400', 'token', '1', 'c8466fa6-12e2-44dc-b33d-a70f6671effe', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('374', '36', '75', '4402', 'token', '1', '2a1d863e-644a-482d-8009-cce0726081eb', '0', '$..token', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('375', '37', '82', '4404', 'userId', '1', '1', '4', '$..userId', '2020-11-09 09:35:22', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('376', null, null, null, 'userId', null, '1', null, null, '2020-11-09 09:35:22', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('377', '36', '75', '4409', 'token', '1', '6b7d378d-1e4a-4dfe-bca5-854b67e90d10', '0', '$..token', '2020-11-09 09:36:39', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('378', '36', '75', '4411', 'token', '1', '03be4c9d-a9c3-41e6-b6fb-4c1f2bf83d49', '0', '$..token', '2020-11-09 09:36:40', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('379', '36', '75', '4413', 'token', '1', '0885e45c-8194-4194-83d4-2855c5ac59a6', '0', '$..token', '2020-11-09 09:36:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('380', '36', '75', '4414', 'token', '1', 'b4d38be9-6c22-4d30-9e00-48e0c610d88a', '0', '$..token', '2020-11-09 09:36:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('381', '36', '75', '4417', 'token', '1', '0310d421-da17-413a-9fa6-c40078503113', '0', '$..token', '2020-11-09 09:36:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('382', '36', '75', '4419', 'token', '1', 'df10aa5f-cc70-42f9-89c8-dc2f879c7bf9', '0', '$..token', '2020-11-09 09:36:42', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('383', '36', '75', '4420', 'token', '1', '4c898750-4bf0-4a6f-8f2a-c854672da1ac', '0', '$..token', '2020-11-09 09:36:43', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('384', '37', '82', '4421', 'userId', '1', '1', '4', '$..userId', '2020-11-09 09:36:43', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('385', null, null, null, 'userId', null, '1', null, null, '2020-11-09 09:36:43', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('386', null, null, null, 'token', null, '4c898750-4bf0-4a6f-8f2a-c854672da1ac', null, null, '2020-11-09 10:01:53', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('387', null, null, null, 'token', null, '4c898750-4bf0-4a6f-8f2a-c854672da1ac', null, null, '2020-11-09 10:04:57', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('388', null, null, null, 'token', null, '4c898750-4bf0-4a6f-8f2a-c854672da1ac', null, null, '2020-11-09 10:08:22', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('389', null, null, null, 'token', null, '4c898750-4bf0-4a6f-8f2a-c854672da1ac', null, null, '2020-11-09 10:09:16', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('390', null, null, null, 'token', null, '4c898750-4bf0-4a6f-8f2a-c854672da1ac', null, null, '2020-11-09 10:11:06', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('391', '36', '75', '4429', 'token', '1', 'a31fb521-1015-4692-a275-1b89ca307a8d', '0', '$..token', '2020-11-09 10:52:01', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('392', '36', '75', '4432', 'token', '1', 'b726d133-3a35-45a3-b8ba-86c73ae027fa', '0', '$..token', '2020-11-09 10:52:05', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('393', '36', '75', '4434', 'token', '1', '356e7e11-f299-492d-8d99-d841230a7d67', '0', '$..token', '2020-11-09 10:52:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('394', '36', '75', '4436', 'token', '1', 'a60b36b6-156a-44a7-84c7-630405c3ee07', '0', '$..token', '2020-11-09 10:52:07', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('395', '36', '75', '4437', 'token', '1', '794a08f0-ede3-4d10-a6d0-7d6a6493d3f5', '0', '$..token', '2020-11-09 10:52:07', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('396', '36', '75', '4440', 'token', '1', '9c3b5660-f019-4f7b-97f2-1b0c3ca78910', '0', '$..token', '2020-11-09 10:52:07', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('397', '36', '75', '4442', 'token', '1', 'cecc525a-6e3a-4e18-8127-7a64e9ec69a8', '0', '$..token', '2020-11-09 10:52:08', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('398', null, null, null, 'token', null, 'cecc525a-6e3a-4e18-8127-7a64e9ec69a8', null, null, '2020-11-09 10:52:17', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('399', '36', '75', '4447', 'token', '1', '3d10d6f0-136e-4bcd-8b03-62e3f7fa0b93', '0', '$..token', '2020-11-09 10:52:21', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('400', '36', '75', '4449', 'token', '1', '0e40d2b5-8edc-4b6b-b720-f634d58d0cf0', '0', '$..token', '2020-11-09 10:52:24', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('401', '37', '82', '4450', 'userId', '1', '1', '4', '$..userId', '2020-11-09 10:52:24', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('402', null, null, null, 'userId', null, '1', null, null, '2020-11-09 10:52:24', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('403', '36', '75', '4451', 'token', '1', '3d983256-dfea-4d3e-9a33-2c871314decf', '0', '$..token', '2020-11-09 10:52:24', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('404', '36', '75', '4452', 'token', '1', '69314896-eadb-4e2e-b427-4ad418724239', '0', '$..token', '2020-11-09 10:52:25', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('405', '36', '75', '4454', 'token', '1', '77a46f13-ee54-44b2-a983-0af09b37502d', '0', '$..token', '2020-11-09 10:52:26', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('406', '36', '75', '4455', 'token', '1', '03d0b6ef-c9db-464d-aaa8-dab4dc724fa9', '0', '$..token', '2020-11-09 10:52:26', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('407', '36', '75', '4458', 'token', '1', '2a745819-8620-4e8f-8a4b-6a338c13c9ca', '0', '$..token', '2020-11-09 10:52:26', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('408', '36', '75', '4460', 'token', '1', 'f69d846f-df32-485b-b3db-25ea525151c4', '0', '$..token', '2020-11-09 10:52:27', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('409', null, null, null, 'token', null, 'f69d846f-df32-485b-b3db-25ea525151c4', null, null, '2020-11-09 10:53:30', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('410', '36', '75', '4463', 'token', '1', '2f3d58e9-bb5d-4f69-ae84-7f874f376e3d', '0', '$..token', '2020-11-09 10:53:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('411', '36', '75', '4465', 'token', '1', '3bbd7b23-0492-4975-8d8b-772c1001302d', '0', '$..token', '2020-11-09 10:53:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('412', '37', '82', '4466', 'userId', '1', '1', '4', '$..userId', '2020-11-09 10:53:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('413', null, null, null, 'userId', null, '1', null, null, '2020-11-09 10:53:33', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('414', '36', '75', '4468', 'token', '1', 'd342655f-2437-483f-801e-08fd66ab72ce', '0', '$..token', '2020-11-09 10:53:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('415', '36', '75', '4469', 'token', '1', '893242b6-6f86-4b29-8a86-e91da3499e29', '0', '$..token', '2020-11-09 10:53:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('416', '36', '75', '4471', 'token', '1', 'eb9df252-5fa8-4602-aa58-446ad9e5738e', '0', '$..token', '2020-11-09 10:53:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('417', '36', '75', '4472', 'token', '1', '07e71499-3b1a-412e-b679-17163091fa30', '0', '$..token', '2020-11-09 10:53:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('418', '36', '75', '4475', 'token', '1', '209cb05e-888d-443a-9a4c-790b942317e5', '0', '$..token', '2020-11-09 10:53:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('419', '36', '75', '4477', 'token', '1', '9e505b3e-9462-4a48-a41c-81e90636a860', '0', '$..token', '2020-11-09 10:53:36', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('420', '36', '75', '4481', 'token', '1', '33e7de48-f202-42be-b562-30f0b2ee7840', '0', '$..token', '2020-11-09 11:09:39', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('421', null, null, null, 'token', null, '33e7de48-f202-42be-b562-30f0b2ee7840', null, null, '2020-11-09 11:09:39', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('422', '36', '75', '4484', 'token', '1', 'e3476cb1-1d79-4bcb-abfa-8215cf41d6e1', '0', '$..token', '2020-11-09 11:09:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('423', '36', '75', '4487', 'token', '1', '44502e96-f99c-451d-ad77-a996a2f2119e', '0', '$..token', '2020-11-09 11:09:42', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('424', '37', '82', '4488', 'userId', '1', '1', '4', '$..userId', '2020-11-09 11:09:42', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('425', null, null, null, 'userId', null, '1', null, null, '2020-11-09 11:09:42', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('426', '36', '75', '4489', 'token', '1', '31c07515-587e-4f4e-a480-199ad7d9876d', '0', '$..token', '2020-11-09 11:09:43', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('427', '36', '75', '4490', 'token', '1', '26e8e3fd-b343-4041-a11c-7b6d1a776014', '0', '$..token', '2020-11-09 11:09:43', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('428', '36', '75', '4492', 'token', '1', 'a37cb23e-8df4-4eb5-8b8c-4776462d6207', '0', '$..token', '2020-11-09 11:09:44', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('429', '36', '75', '4493', 'token', '1', 'd53bdfa2-58ac-4b8b-852b-04e39cdcb0bd', '0', '$..token', '2020-11-09 11:09:44', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('430', '36', '75', '4496', 'token', '1', '21290e51-3ca4-466a-b8df-2d70c2bcfb27', '0', '$..token', '2020-11-09 11:09:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('431', '36', '75', '4498', 'token', '1', 'a1edd00c-cd83-4982-b7c3-233344c7cc85', '0', '$..token', '2020-11-09 11:09:45', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('432', '36', '75', '4500', 'token', '1', '70e71333-70f2-4c42-8f63-fae7e6162299', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('433', '36', '75', '4503', 'token', '1', '157b0cbc-b055-4012-8380-cc7f60037866', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('434', '36', '75', '4501', 'token', '1', 'f9109dc6-ae42-4b11-97ee-9b243e564c30', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('435', '36', '75', '4504', 'token', '1', 'cdd0b806-5fc3-496e-842e-f19bf909e90e', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('436', '36', '75', '4502', 'token', '1', 'd2ebabb2-45be-41de-933d-6bb02f661bde', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('437', '36', '75', '4507', 'token', '1', 'aa08c045-ec33-4a38-903b-c0cc2d04a228', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('438', '36', '75', '4508', 'token', '1', 'd0a9f2c7-c258-4ec9-b9aa-142c6ef74d1d', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('439', '37', '82', '4511', 'userId', '1', '1', '4', '$..userId', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('440', null, null, null, 'userId', null, '1', null, null, '2020-11-09 11:35:33', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('441', '36', '75', '4513', 'token', '1', 'ce4e4aee-5cc5-4adb-b489-2721ee080feb', '0', '$..token', '2020-11-09 11:35:33', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('442', '36', '75', '4518', 'token', '1', 'd78b9039-c17d-40df-a02b-a336c09e2c47', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('443', '36', '75', '4519', 'token', '1', 'cbc46e6d-e3f7-41e9-8cc7-b9e3949b3deb', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('444', '36', '75', '4520', 'token', '1', 'd0054af8-30b3-45e5-b6f8-a6a02ec20b11', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('445', '36', '75', '4521', 'token', '1', '9d156c12-59f0-4f93-9250-c2cc8d4aa78a', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('446', '36', '75', '4522', 'token', '1', 'd2653d38-c5a2-4674-b395-3dca1b97ddb8', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('447', '36', '75', '4525', 'token', '1', '589f61f2-4671-4a8d-9fb8-6c374de7df7d', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('448', '37', '82', '4530', 'userId', '1', '1', '4', '$..userId', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('449', null, null, null, 'userId', null, '1', null, null, '2020-11-09 11:36:04', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('450', '36', '75', '4528', 'token', '1', 'e53a8e78-4463-4210-96b3-32a462269491', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('451', '36', '75', '4529', 'token', '1', 'e4d4a6e6-10a1-4edc-b2dd-e8c0fb51ef22', '0', '$..token', '2020-11-09 11:36:04', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('452', '36', '75', '4535', 'token', '1', '6e55088b-908b-45a7-bd7d-58bc164c80ea', '0', '$..token', '2020-11-09 11:50:49', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('453', '36', '75', '4536', 'token', '1', '7791150c-57f7-48a4-9f81-19e7915cd6f5', '0', '$..token', '2020-11-09 12:17:56', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('454', '36', '75', '4538', 'token', '1', '9b15b80e-06b4-45a1-b438-43c36ba2f3e2', '0', '$..token', '2020-11-09 12:19:14', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('455', '36', '75', '4539', 'token', '1', '1aea2e9c-9888-4cc8-9a42-48faece6bbce', '0', '$..token', '2020-11-10 15:17:35', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('456', null, null, null, 'token', null, '1aea2e9c-9888-4cc8-9a42-48faece6bbce', null, null, '2020-11-10 15:17:38', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('457', '36', '75', '4542', 'token', '1', '5e31f411-9b65-4e69-a029-4a111d08e7f8', '0', '$..token', '2020-11-10 15:17:40', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('458', '36', '75', '4545', 'token', '1', '2dcafe43-8234-49d3-9b5a-c49324aa5f55', '0', '$..token', '2020-11-10 15:17:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('459', '36', '75', '4547', 'token', '1', '5914a00d-902d-406e-b000-c67454075e4c', '0', '$..token', '2020-11-10 15:17:42', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('460', '36', '75', '4548', 'token', '1', 'dfc25c58-1915-4b9b-b4a0-b66814ffd106', '0', '$..token', '2020-11-10 15:17:42', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('461', '36', '75', '4551', 'token', '1', '38b45e05-542b-439f-b042-6a95768ac871', '0', '$..token', '2020-11-10 15:17:42', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('462', '36', '75', '4553', 'token', '1', '8b8fa593-f7c2-4860-ba4a-b18b718f5b09', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('463', '36', '75', '4555', 'token', '1', 'a167c949-e1b0-47c5-99d7-436912cbffe4', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('464', '36', '75', '4554', 'token', '1', 'e8b497d5-f092-41a3-b7cc-f9c8bf2376a5', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('465', '36', '75', '4556', 'token', '1', '9272e61e-f938-4bc3-8327-33cfe2f66d18', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('466', null, null, null, 'token', null, 'a167c949-e1b0-47c5-99d7-436912cbffe4', null, null, '2020-11-10 15:19:34', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('467', '37', '82', '4562', 'userId', '1', '1', '4', '$..userId', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('468', null, null, null, 'userId', null, '1', null, null, '2020-11-10 15:19:34', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('469', '36', '75', '4558', 'token', '1', '9cebfce0-81df-4e4c-993e-3591a031a9b4', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('470', '36', '75', '4564', 'token', '1', 'c64f5b9d-7f77-4aa5-8828-183976ba4b83', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('471', '36', '75', '4563', 'token', '1', 'b3306acd-b09d-43d0-876b-06184ea9d0c2', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('472', '36', '75', '4567', 'token', '1', '0b6583c6-dd1c-4f32-8a7b-35d95ea53328', '0', '$..token', '2020-11-10 15:19:34', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('473', '36', '75', '4571', 'token', '1', '45d060f7-54b1-4a29-abd1-20bc4b14bfeb', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('474', '36', '75', '4573', 'token', '1', '3e286764-889d-40d6-acfe-ad7a94009e57', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('475', '36', '75', '4572', 'token', '1', 'd690aa42-2d67-4f6a-9a9f-75b605c913c3', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('476', '36', '75', '4575', 'token', '1', '89c9a3e8-9683-4e18-89e1-e0b218a9ede8', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('477', '36', '75', '4574', 'token', '1', '0e4eb52e-1c5f-4c73-b5a5-70186bf51445', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('478', '37', '82', '4578', 'userId', '1', '1', '4', '$..userId', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('479', '36', '75', '4576', 'token', '1', '70bd72e3-c0f2-4e30-b349-c7a28a47e608', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('480', null, null, null, 'userId', null, '1', null, null, '2020-11-10 15:19:46', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('481', null, null, null, 'token', null, '70bd72e3-c0f2-4e30-b349-c7a28a47e608', null, null, '2020-11-10 15:19:46', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('482', '36', '75', '4581', 'token', '1', '2297928f-abc3-47a0-9691-c9942340655e', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('483', '36', '75', '4583', 'token', '1', 'fe1127b7-d5ce-4e60-90d2-8fa24e53d4f2', '0', '$..token', '2020-11-10 15:19:46', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('484', '36', '75', '4590', 'token', '1', 'b94baafd-7271-48a1-87c7-9dd5893392ad', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('485', '36', '75', '4591', 'token', '1', '5d7cee83-deef-40ff-956f-22057e5b92b0', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('486', '36', '75', '4592', 'token', '1', '7b8527e9-8c98-4ed8-a67b-58dcf1d443d0', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('487', '36', '75', '4593', 'token', '1', 'f75415c4-989e-41f7-8c17-f6c621a2052d', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('488', '36', '75', '4594', 'token', '1', 'd706fecf-7187-4541-921c-209625e5e265', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('489', '36', '75', '4595', 'token', '1', '05c64440-e34f-4ff2-b1a0-45b0a10691cb', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('490', '37', '82', '4601', 'userId', '1', '1', '4', '$..userId', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('491', null, null, null, 'userId', null, '1', null, null, '2020-11-10 15:20:06', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('492', '36', '75', '4599', 'token', '1', '2b519efc-fd21-4aed-88ac-b32e00b44571', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('493', '36', '75', '4602', 'token', '1', 'fd833d14-1074-4e56-895b-8c55128487e6', '0', '$..token', '2020-11-10 15:20:06', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('494', '36', '75', '4607', 'token', '1', '90fc6162-c719-4b98-a552-e84da8f316f0', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('495', '36', '75', '4609', 'token', '1', 'ed8f3b13-861b-4cbe-a446-97c9a25fc262', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('496', '36', '75', '4610', 'token', '1', 'e45ec1a7-a331-4675-b6f7-aaf7f7991fe2', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('497', '36', '75', '4608', 'token', '1', 'eb59738f-ae9f-42e9-a969-44a3c828be2a', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('498', null, null, null, 'token', null, 'eb59738f-ae9f-42e9-a969-44a3c828be2a', null, null, '2020-11-10 15:20:12', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('499', '36', '75', '4611', 'token', '1', 'f940ac27-d270-444b-afd4-acc186ce8007', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('500', '36', '75', '4612', 'token', '1', '086f61c5-4514-4921-b6c1-ef03eb139c1d', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('501', '37', '82', '4619', 'userId', '1', '1', '4', '$..userId', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('502', null, null, null, 'userId', null, '1', null, null, '2020-11-10 15:20:12', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('503', '36', '75', '4617', 'token', '1', '64b25d95-78c1-46ef-8a89-c03cf0e513f6', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('504', '36', '75', '4620', 'token', '1', '24e18427-370b-4e66-9f53-f0bd84946b7d', '0', '$..token', '2020-11-10 15:20:12', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('505', '36', '75', '4628', 'token', '1', '8a739eea-3c3b-4ef0-a2f5-76f401523fb1', '0', '$..token', '2020-11-10 16:54:50', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('506', '36', '75', '4629', 'token', '1', '8ba0e5fd-aa65-4c13-9caa-da337ab925f7', '0', '$..token', '2020-11-10 16:54:50', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('507', '36', '75', '4630', 'token', '1', '017973e4-016c-4970-a64c-635865a56f45', '0', '$..token', '2020-11-10 16:54:50', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('508', '36', '75', '4631', 'token', '1', '54c8fdd4-c174-4479-9260-dd99dd134a20', '0', '$..token', '2020-11-10 16:54:50', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('509', '36', '75', '4632', 'token', '1', '4bf54362-00bd-48e1-b8ea-9e3d452fa93c', '0', '$..token', '2020-11-10 16:54:50', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('510', '36', '75', '4634', 'token', '1', 'aecbfdf8-558d-4caa-bf7c-3229fe76ecaa', '0', '$..token', '2020-11-10 16:54:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('511', '37', '82', '4639', 'userId', '1', '1', '4', '$..userId', '2020-11-10 16:54:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('512', null, null, null, 'userId', null, '1', null, null, '2020-11-10 16:54:51', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('513', '36', '75', '4638', 'token', '1', '7a15b742-3b36-4fe4-a4ec-b4356ae07923', '0', '$..token', '2020-11-10 16:54:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('514', '36', '75', '4640', 'token', '1', 'e2346265-07f9-4a0e-a77c-a3f11b0975ad', '0', '$..token', '2020-11-10 16:54:51', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('515', '36', '75', '4645', 'token', '1', '9a081fea-bf0c-41b9-90ba-5fef6a031c0e', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('516', '36', '75', '4647', 'token', '1', '48fbb6f8-a312-43ce-97c7-cae0039784dd', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('517', '36', '75', '4646', 'token', '1', '2d0de39a-a0ce-4b33-8471-3c0d8c1a570c', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('518', '36', '75', '4649', 'token', '1', '10e2b98e-769d-4fff-b6b1-1440a27c3ae0', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('519', '36', '75', '4648', 'token', '1', 'd4f09c66-ab1e-4377-a71a-7de32b40f855', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('520', null, null, null, 'token', null, '48fbb6f8-a312-43ce-97c7-cae0039784dd', null, null, '2020-11-10 16:55:15', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('521', '37', '82', '4651', 'userId', '1', '1', '4', '$..userId', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('522', null, null, null, 'userId', null, '1', null, null, '2020-11-10 16:55:15', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('523', '36', '75', '4655', 'token', '1', '64b5ee4c-cbc4-4dde-be76-045e79784780', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('524', '36', '75', '4657', 'token', '1', '6a7b5ec6-6bfd-4df4-bb5a-ada41c8db08e', '0', '$..token', '2020-11-10 16:55:15', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('525', '36', '75', '4658', 'token', '1', 'f3196809-4ada-4f31-a2c1-aa22cec2f9bc', '0', '$..token', '2020-11-10 16:55:16', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('526', '36', '75', '4663', 'token', '1', '38625e8f-76e7-41d3-9f8e-1095fd5efb49', '0', '$..token', '2020-11-10 16:56:48', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('527', '36', '75', '4664', 'token', '1', 'a0ec14c8-499b-4499-967b-576e2bdb87ae', '0', '$..token', '2020-11-10 16:56:48', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('528', '36', '75', '4669', 'token', '1', '8bf37bf9-b138-4340-9127-59950c454101', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('529', '36', '75', '4667', 'token', '1', 'c5273f5e-45e2-48b2-9370-f338036f91c8', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('530', '36', '75', '4670', 'token', '1', 'b1d327c3-dec7-4c42-ba90-2b70db0cbb1f', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('531', '36', '75', '4668', 'token', '1', 'ae470768-ff1f-4b43-8a9f-0dea9bc82168', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('532', null, null, null, 'token', null, 'c5273f5e-45e2-48b2-9370-f338036f91c8', null, null, '2020-11-11 10:49:17', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('533', '36', '75', '4672', 'token', '1', '7bf7a9ad-d4ac-4e6f-9cab-2f0a4906a98d', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('534', '36', '75', '4675', 'token', '1', 'f535c24e-9337-4c26-98e0-71ae0397c172', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('535', '36', '75', '4677', 'token', '1', '8d145f7d-c376-4bf3-b0e1-e66a00177f17', '0', '$..token', '2020-11-11 10:49:17', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('536', '37', '82', '4680', 'userId', '1', '1', '4', '$..userId', '2020-11-11 10:49:18', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('537', null, null, null, 'userId', null, '1', null, null, '2020-11-11 10:49:18', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('538', '36', '75', '4679', 'token', '1', '86ebea26-fc55-4583-a268-b38d8ea81608', '0', '$..token', '2020-11-11 10:49:18', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('539', '36', '75', '4685', 'token', '1', '43a05d26-db6d-45cb-aa1b-993a85956b2b', '0', '$..token', '2020-11-11 11:51:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('540', '37', '82', '4686', 'userId', '1', '1', '4', '$..userId', '2020-11-11 11:51:41', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('541', null, null, null, 'userId', null, '1', null, null, '2020-11-11 11:51:41', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('542', '36', '75', '4688', 'token', '1', 'e76b0c73-42ae-4a1e-acfe-f9de8ca4d5b8', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('543', '36', '75', '4689', 'token', '1', '1dadd914-ce0f-4710-a9e5-c5ff648b04f4', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('544', '36', '75', '4687', 'token', '1', 'ace4c7c1-8e44-4c02-aa91-97a717e668e1', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('545', '36', '75', '4690', 'token', '1', 'ec502124-e9c9-4b38-88bb-b4a37fb9f910', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('546', '36', '75', '4691', 'token', '1', '97c3d8c7-835a-4085-937f-480691ca1c8a', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('547', '36', '75', '4692', 'token', '1', '96970f19-9047-4aa8-a2dd-356b17b9eeaf', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('548', null, null, null, 'token', null, '97c3d8c7-835a-4085-937f-480691ca1c8a', null, null, '2020-11-11 20:09:32', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('549', '37', '82', '4698', 'userId', '1', '1', '4', '$..userId', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('550', null, null, null, 'userId', null, '1', null, null, '2020-11-11 20:09:32', '0', null, '0');
INSERT INTO `t_post_processor_log` VALUES ('551', '36', '75', '4699', 'token', '1', 'd8f651e2-94a3-4b16-b02c-ee3f2f182eed', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');
INSERT INTO `t_post_processor_log` VALUES ('552', '36', '75', '4700', 'token', '1', 'e1615f8d-85b5-4c42-9708-1ab4ab44240d', '0', '$..token', '2020-11-11 20:09:32', '0', null, '1');

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
  `value` varchar(100) DEFAULT NULL COMMENT '依赖值',
  `desc` varchar(100) DEFAULT NULL COMMENT '依赖描述',
  `type` tinyint DEFAULT NULL COMMENT '依赖类型 0固定值 1反射方法 2sql',
  `datasource_id` int DEFAULT NULL COMMENT '数据源id',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

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
INSERT INTO `t_rely_data` VALUES ('41', 'InterfaceCaseTable', 'select * from t_interface_case where case_id = ?', '查询根据用例编号查询t_interface_case', '2', '1', '2021-03-08 15:32:57', '2021-03-08 15:47:17');

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
INSERT INTO `t_task` VALUES ('119', '定时任务', '*/5 * * * * ?', '2021-03-09 10:37:20', '2021-03-09 10:34:23', '2021-03-09 10:37:15', '0', '35', '1');

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
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `job_number` varchar(10) DEFAULT NULL COMMENT '工号',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别,0女 1男',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用 1启用 0禁用',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `real_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `role_id` int DEFAULT NULL COMMENT '角色类型',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=555 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', '123', '123', '', '1', '1', null, '2021-03-10 16:08:51', '123', '1');
