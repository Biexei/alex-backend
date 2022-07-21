/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2022-07-20 19:46:14
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_data_factory
-- ----------------------------

-- ----------------------------
-- Table structure for t_db
-- ----------------------------
DROP TABLE IF EXISTS `t_db`;
CREATE TABLE `t_db` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `type` tinyint DEFAULT NULL COMMENT '0mysql 1oracle 2SqlServer',
  `desc` varchar(100) DEFAULT NULL COMMENT '描述',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '连接url',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint DEFAULT NULL COMMENT '0启动 1禁用',
  `test_url` varchar(200) DEFAULT NULL COMMENT '测试环境url',
  `test_username` varchar(100) DEFAULT NULL COMMENT '测试环境username',
  `test_password` varchar(100) DEFAULT NULL COMMENT '测试环境password',
  `dev_url` varchar(200) DEFAULT NULL COMMENT '开发环境url',
  `dev_username` varchar(100) DEFAULT NULL COMMENT '开发环境username',
  `dev_password` varchar(100) DEFAULT NULL COMMENT '开发环境password',
  `stg_url` varchar(200) DEFAULT NULL COMMENT '预上线环境url',
  `stg_username` varchar(100) DEFAULT NULL COMMENT '预上线环境username',
  `stg_password` varchar(100) DEFAULT NULL COMMENT '预上线环境password',
  `prod_url` varchar(200) DEFAULT NULL COMMENT '生产环境url',
  `prod_username` varchar(100) DEFAULT NULL COMMENT '生产环境username',
  `prod_password` varchar(100) DEFAULT NULL COMMENT '生产环境password',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_db
-- ----------------------------
INSERT INTO `t_db` VALUES ('1', 'platform', '0', '自动化平台库', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false', 'root', 'root', '2020-09-02 15:40:50', '2020-10-21 11:31:11', '0', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false', 'root', 'root', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false', 'root', 'root', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false', 'root', 'root', 'jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false', 'root', 'root');

-- ----------------------------
-- Table structure for t_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rate` tinyint DEFAULT NULL COMMENT '评分',
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '反馈内容',
  `created_time` datetime DEFAULT NULL,
  `status` tinyint DEFAULT NULL COMMENT '0未读 1已读未回 2已回待阅  3已回已阅',
  `creator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '反馈人昵称（real_name）',
  `creator_id` int DEFAULT NULL COMMENT '反馈人id（user_id）',
  `reply` varchar(200) DEFAULT NULL COMMENT '回复',
  `reply_time` datetime DEFAULT NULL,
  `solution` tinyint DEFAULT NULL COMMENT '解决方案 0暂不调整 1延期解决 2已解决 3未答复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_feedback
-- ----------------------------

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
  `type` tinyint DEFAULT NULL COMMENT '类型 0代理1公共头2邮箱3ConnectTimeout4ReadTimeout',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`setting_id`),
  KEY `status` (`status`),
  KEY `type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_http_setting
-- ----------------------------

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
  `operator` tinyint DEFAULT NULL COMMENT '操作符0/=、1/< 、2/>、3/<=、4/>=、5/in、6/!=、7/re、8/isNull、9/notNull、10/contains、11/isEmpty、12/isNotEmpty',
  `excepted_result` varchar(1000) DEFAULT NULL COMMENT '预期结果',
  `order` int DEFAULT NULL COMMENT '排序 执行断言时按照该字段排序',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`assert_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_assert
-- ----------------------------

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
  PRIMARY KEY (`assert_log_id`),
  KEY `case_id` (`case_id`),
  KEY `execute_log_id` (`execute_log_id`)
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
  `method` tinyint(1) DEFAULT NULL COMMENT '请求方式  0get,1post,2patch,3put,4delete,5head,6options,7trace',
  `desc` varchar(1000) DEFAULT NULL COMMENT '用例描述',
  `level` tinyint DEFAULT NULL COMMENT '用例级别0高，1中，2低',
  `doc` varchar(200) DEFAULT NULL COMMENT '接口文档地址',
  `headers` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求头',
  `params` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求参数',
  `form_data_encoded` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'formdataencoded',
  `form_data` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求formdata',
  `raw` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求体',
  `raw_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'raw类型 "Text" "JSON" "HTML" "XML"',
  `body_type` tinyint DEFAULT NULL COMMENT '请求体类型0form-data 1x-www-form-Encoded 2raw 9none',
  `creater` varchar(20) DEFAULT NULL COMMENT '用例创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '修改日期',
  `source` tinyint DEFAULT NULL COMMENT '来源0新增1excel导入2csv导入3json导入4yaml导入5har导入',
  `import_no` varchar(100) DEFAULT NULL COMMENT '导入编号',
  PRIMARY KEY (`case_id`),
  KEY `project_id` (`project_id`),
  KEY `module_id` (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case
-- ----------------------------

-- ----------------------------
-- Table structure for t_interface_case_execute_log
-- ----------------------------
DROP TABLE IF EXISTS `t_interface_case_execute_log`;
CREATE TABLE `t_interface_case_execute_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用例执行日志id',
  `case_id` int DEFAULT NULL COMMENT '用例id',
  `case_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用例描述',
  `case_url` varchar(300) DEFAULT NULL COMMENT '请求地址',
  `case_method` tinyint DEFAULT NULL COMMENT '0get,1post,2patch,3put,4delete,5head,6options,7trace',
  `request_headers` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求头',
  `request_params` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '请求参数',
  `request_body` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci,
  `raw_request_headers` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '未清洗依赖数据前的原始header',
  `raw_request_params` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '未清洗依赖数据前的原始params',
  `raw_request_body` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '未清洗依赖数据前的原始json',
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
  `raw_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'raw类型 "Text" "JSON" "HTML" "XML"',
  `body_type` tinyint DEFAULT NULL COMMENT '请求体类型0form-data 1x-www-form-Encoded 2raw 9none',
  PRIMARY KEY (`id`),
  KEY `case_id` (`case_id`),
  KEY `status` (`status`)
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
  `creator_id` int DEFAULT NULL COMMENT '创建人用户编号',
  `creator_name` varchar(30) DEFAULT NULL COMMENT '创建人昵称',
  `others_deletable` tinyint DEFAULT NULL COMMENT '非创建人能否删除0是1否',
  `others_modifiable` tinyint DEFAULT NULL COMMENT '非创建人能否修改0是1否',
  PRIMARY KEY (`rely_id`),
  UNIQUE KEY `rely_name` (`rely_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_rely_data
-- ----------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_case_suite
-- ----------------------------

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
  PRIMARY KEY (`id`),
  KEY `parent_case_id` (`parent_case_id`),
  KEY `pre_case_id` (`pre_case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_processor
-- ----------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_case_ref
-- ----------------------------

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_interface_suite_processor
-- ----------------------------

-- ----------------------------
-- Table structure for t_mock_api
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_api`;
CREATE TABLE `t_mock_api` (
  `api_id` int NOT NULL AUTO_INCREMENT,
  `server_id` int DEFAULT NULL,
  `desc` varchar(30) DEFAULT NULL COMMENT '描述',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求地址',
  `method` varchar(20) DEFAULT NULL COMMENT '请求方式',
  `response_code` int DEFAULT NULL COMMENT '状态码',
  `response_headers` varchar(1000) DEFAULT NULL COMMENT '响应头',
  `response_body` mediumtext COMMENT '响应body',
  `response_delay` int DEFAULT NULL COMMENT '响应时延ms',
  `status` tinyint DEFAULT NULL COMMENT '0启用1禁用',
  `created_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `response_headers_enable_rely` tinyint DEFAULT NULL COMMENT '0是1否 是否解析headers依赖',
  `response_body_enable_rely` tinyint DEFAULT NULL COMMENT '0是1否 是否解析body依赖',
  `creator_id` int DEFAULT NULL COMMENT '创建人id',
  `creator_name` varchar(30) DEFAULT NULL COMMENT '创建人名称',
  `response_body_type` tinyint DEFAULT NULL COMMENT '0文本1json2xml3html',
  PRIMARY KEY (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_mock_api
-- ----------------------------

-- ----------------------------
-- Table structure for t_mock_hit_policy
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_hit_policy`;
CREATE TABLE `t_mock_hit_policy` (
  `id` int NOT NULL AUTO_INCREMENT,
  `api_id` int DEFAULT NULL COMMENT 'api_id',
  `match_scope` tinyint DEFAULT NULL COMMENT '0请求头1请求body2pathparams3请求queryparams',
  `match_type` tinyint DEFAULT NULL COMMENT '0固定值1包含2正则3jsonschema4xpath5jsonpath',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `value` varchar(200) DEFAULT NULL COMMENT '值',
  `status` tinyint DEFAULT NULL COMMENT '0启用1禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_mock_hit_policy
-- ----------------------------

-- ----------------------------
-- Table structure for t_mock_sever
-- ----------------------------
DROP TABLE IF EXISTS `t_mock_sever`;
CREATE TABLE `t_mock_sever` (
  `server_id` int NOT NULL AUTO_INCREMENT,
  `port` int DEFAULT NULL COMMENT '端口号',
  `remote_host` varchar(30) DEFAULT NULL COMMENT '当没有命中时转发的主机地址',
  `remote_port` int DEFAULT NULL COMMENT '当没有命中时转发的端口',
  `desc` varchar(200) DEFAULT NULL COMMENT '描述',
  `creator_id` int DEFAULT NULL COMMENT '创建人user_id',
  `creator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人realname',
  `created_time` datetime DEFAULT NULL COMMENT 'update_time',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`server_id`),
  UNIQUE KEY `port` (`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_mock_sever
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_module
-- ----------------------------
INSERT INTO `t_module` VALUES ('1', '1', '用户管理', '', '2021-06-09 12:41:16', '2022-07-20 19:41:00');

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
) ENGINE=InnoDB AUTO_INCREMENT=243 DEFAULT CHARSET=utf8;

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
INSERT INTO `t_permission` VALUES ('149', 'data_center:rely_fixed_data', '环境变量', '124');
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
INSERT INTO `t_permission` VALUES ('178', 'setting', '系统设置', '0');
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
INSERT INTO `t_permission` VALUES ('195', 'setting:timeout', '超时配置', '178');
INSERT INTO `t_permission` VALUES ('196', 'setting:timeout:add', '新增', '195');
INSERT INTO `t_permission` VALUES ('197', 'setting:timeout:modify', '修改', '195');
INSERT INTO `t_permission` VALUES ('198', 'setting:timeout:remove', '删除', '195');
INSERT INTO `t_permission` VALUES ('199', 'setting:timeout:find', '查询', '195');
INSERT INTO `t_permission` VALUES ('200', 'interface:case:logInfo', '链路日志', '155');
INSERT INTO `t_permission` VALUES ('201', 'feedback', '反馈中心', '0');
INSERT INTO `t_permission` VALUES ('202', 'feedback:list', '反馈列表', '201');
INSERT INTO `t_permission` VALUES ('203', 'feedback:list:find', '查询', '202');
INSERT INTO `t_permission` VALUES ('204', 'feedback:list:remove', '删除', '202');
INSERT INTO `t_permission` VALUES ('205', 'feedback:list:reply', '回复', '202');
INSERT INTO `t_permission` VALUES ('206', 'feedback:my', '我的反馈', '201');
INSERT INTO `t_permission` VALUES ('207', 'feedback:my:add', '新增', '206');
INSERT INTO `t_permission` VALUES ('208', 'feedback:my:modify', '修改', '206');
INSERT INTO `t_permission` VALUES ('209', 'feedback:my:remove', '删除', '206');
INSERT INTO `t_permission` VALUES ('210', 'feedback:my:find', '查询', '206');
INSERT INTO `t_permission` VALUES ('211', 'interface:ifreport', '测试报告', '154');
INSERT INTO `t_permission` VALUES ('212', 'interface:ifreport:find', '查询', '211');
INSERT INTO `t_permission` VALUES ('213', 'mock', 'Mock', '0');
INSERT INTO `t_permission` VALUES ('214', 'mock:node', '节点管理', '213');
INSERT INTO `t_permission` VALUES ('215', 'mock:node:add', '添加', '214');
INSERT INTO `t_permission` VALUES ('216', 'mock:node:modify', '修改', '214');
INSERT INTO `t_permission` VALUES ('217', 'mock:node:remove', '删除', '214');
INSERT INTO `t_permission` VALUES ('218', 'mock:node:find', '查询', '214');
INSERT INTO `t_permission` VALUES ('219', 'mock:node:start', '启用', '214');
INSERT INTO `t_permission` VALUES ('220', 'mock:node:stop', '停用', '214');
INSERT INTO `t_permission` VALUES ('221', 'mock:api', '接口管理', '213');
INSERT INTO `t_permission` VALUES ('222', 'mock:api:add', '添加', '221');
INSERT INTO `t_permission` VALUES ('223', 'mock:api:modify', '修改', '221');
INSERT INTO `t_permission` VALUES ('224', 'mock:api:remove', '删除', '221');
INSERT INTO `t_permission` VALUES ('225', 'mock:api:query', '查询', '221');
INSERT INTO `t_permission` VALUES ('226', 'mock:api:stop', '停止', '221');
INSERT INTO `t_permission` VALUES ('227', 'mock:api:restart', '重启', '221');
INSERT INTO `t_permission` VALUES ('228', 'setting:loginLog', '登录日志', '178');
INSERT INTO `t_permission` VALUES ('229', 'setting:loginLog:find', '查询', '228');
INSERT INTO `t_permission` VALUES ('230', 'stability', '稳定性测试', '0');
INSERT INTO `t_permission` VALUES ('231', 'stability:case', '测试用例', '230');
INSERT INTO `t_permission` VALUES ('232', 'stability:log', '执行日志', '230');
INSERT INTO `t_permission` VALUES ('233', 'stability:case:add', '新增', '231');
INSERT INTO `t_permission` VALUES ('234', 'stability:case:modify', '修改', '231');
INSERT INTO `t_permission` VALUES ('235', 'stability:case:remove', '删除', '231');
INSERT INTO `t_permission` VALUES ('236', 'stability:case:execute', '执行', '231');
INSERT INTO `t_permission` VALUES ('237', 'stability:case:find', '查询', '231');
INSERT INTO `t_permission` VALUES ('238', 'stability:case:log:find', '查询', '232');
INSERT INTO `t_permission` VALUES ('239', 'stability:case:log:download', '下载', '232');
INSERT INTO `t_permission` VALUES ('240', 'stability:case:log:stop', '停止', '232');
INSERT INTO `t_permission` VALUES ('241', 'stability:case:log:last', '近况', '232');
INSERT INTO `t_permission` VALUES ('242', 'stability:case:log:delete', '删除', '232');

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
) ENGINE=InnoDB AUTO_INCREMENT=227 DEFAULT CHARSET=utf8;

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
INSERT INTO `t_permission_role_ref` VALUES ('166', '195', '1', '2021-04-11 17:13:45');
INSERT INTO `t_permission_role_ref` VALUES ('167', '196', '1', '2021-04-11 17:13:46');
INSERT INTO `t_permission_role_ref` VALUES ('168', '197', '1', '2021-04-11 17:13:47');
INSERT INTO `t_permission_role_ref` VALUES ('169', '198', '1', '2021-04-11 17:13:47');
INSERT INTO `t_permission_role_ref` VALUES ('170', '199', '1', '2021-04-11 17:13:48');
INSERT INTO `t_permission_role_ref` VALUES ('171', '200', '1', '2021-04-11 18:08:03');
INSERT INTO `t_permission_role_ref` VALUES ('172', '201', '1', '2021-04-11 21:05:27');
INSERT INTO `t_permission_role_ref` VALUES ('173', '202', '1', '2021-04-11 21:05:27');
INSERT INTO `t_permission_role_ref` VALUES ('174', '203', '1', '2021-04-11 21:05:28');
INSERT INTO `t_permission_role_ref` VALUES ('175', '204', '1', '2021-04-11 21:05:28');
INSERT INTO `t_permission_role_ref` VALUES ('176', '205', '1', '2021-04-11 21:05:29');
INSERT INTO `t_permission_role_ref` VALUES ('177', '206', '1', '2021-04-11 21:05:30');
INSERT INTO `t_permission_role_ref` VALUES ('178', '208', '1', '2021-04-11 21:05:31');
INSERT INTO `t_permission_role_ref` VALUES ('179', '209', '1', '2021-04-11 21:05:31');
INSERT INTO `t_permission_role_ref` VALUES ('180', '210', '1', '2021-04-11 21:05:32');
INSERT INTO `t_permission_role_ref` VALUES ('181', '207', '1', '2021-04-11 21:05:33');
INSERT INTO `t_permission_role_ref` VALUES ('182', '211', '1', '2021-04-13 18:46:52');
INSERT INTO `t_permission_role_ref` VALUES ('183', '212', '1', '2021-04-13 18:46:52');
INSERT INTO `t_permission_role_ref` VALUES ('184', '213', '1', '2021-04-14 19:45:39');
INSERT INTO `t_permission_role_ref` VALUES ('185', '214', '1', '2021-04-14 19:45:39');
INSERT INTO `t_permission_role_ref` VALUES ('186', '215', '1', '2021-04-14 19:45:40');
INSERT INTO `t_permission_role_ref` VALUES ('187', '216', '1', '2021-04-14 19:45:40');
INSERT INTO `t_permission_role_ref` VALUES ('188', '217', '1', '2021-04-14 19:45:41');
INSERT INTO `t_permission_role_ref` VALUES ('189', '218', '1', '2021-04-14 19:45:41');
INSERT INTO `t_permission_role_ref` VALUES ('190', '219', '1', '2021-04-14 19:45:42');
INSERT INTO `t_permission_role_ref` VALUES ('191', '220', '1', '2021-04-14 19:45:42');
INSERT INTO `t_permission_role_ref` VALUES ('192', '221', '1', '2021-04-16 21:22:41');
INSERT INTO `t_permission_role_ref` VALUES ('193', '222', '1', '2021-04-16 21:22:41');
INSERT INTO `t_permission_role_ref` VALUES ('194', '223', '1', '2021-04-16 21:22:42');
INSERT INTO `t_permission_role_ref` VALUES ('195', '225', '1', '2021-04-16 21:22:42');
INSERT INTO `t_permission_role_ref` VALUES ('196', '224', '1', '2021-04-16 21:22:43');
INSERT INTO `t_permission_role_ref` VALUES ('197', '226', '1', '2021-04-17 15:03:12');
INSERT INTO `t_permission_role_ref` VALUES ('198', '227', '1', '2021-04-17 16:04:29');
INSERT INTO `t_permission_role_ref` VALUES ('199', '228', '1', '2021-06-27 18:15:31');
INSERT INTO `t_permission_role_ref` VALUES ('200', '229', '1', '2021-06-27 18:15:32');
INSERT INTO `t_permission_role_ref` VALUES ('201', '230', '1', '2021-07-12 18:31:14');
INSERT INTO `t_permission_role_ref` VALUES ('202', '231', '1', '2021-07-12 18:31:15');
INSERT INTO `t_permission_role_ref` VALUES ('203', '233', '1', '2021-07-12 18:31:16');
INSERT INTO `t_permission_role_ref` VALUES ('204', '234', '1', '2021-07-12 18:31:16');
INSERT INTO `t_permission_role_ref` VALUES ('205', '235', '1', '2021-07-12 18:31:18');
INSERT INTO `t_permission_role_ref` VALUES ('206', '236', '1', '2021-07-12 18:31:18');
INSERT INTO `t_permission_role_ref` VALUES ('207', '237', '1', '2021-07-12 18:31:19');
INSERT INTO `t_permission_role_ref` VALUES ('208', '232', '1', '2021-07-12 18:31:20');
INSERT INTO `t_permission_role_ref` VALUES ('209', '238', '1', '2021-07-12 18:31:20');
INSERT INTO `t_permission_role_ref` VALUES ('210', '239', '1', '2021-07-12 18:31:21');
INSERT INTO `t_permission_role_ref` VALUES ('211', '230', '4', '2021-07-12 18:31:26');
INSERT INTO `t_permission_role_ref` VALUES ('212', '231', '4', '2021-07-12 18:31:26');
INSERT INTO `t_permission_role_ref` VALUES ('213', '233', '4', '2021-07-12 18:31:27');
INSERT INTO `t_permission_role_ref` VALUES ('214', '234', '4', '2021-07-12 18:31:27');
INSERT INTO `t_permission_role_ref` VALUES ('215', '235', '4', '2021-07-12 18:31:28');
INSERT INTO `t_permission_role_ref` VALUES ('216', '236', '4', '2021-07-12 18:31:28');
INSERT INTO `t_permission_role_ref` VALUES ('217', '237', '4', '2021-07-12 18:31:31');
INSERT INTO `t_permission_role_ref` VALUES ('218', '232', '4', '2021-07-12 18:31:31');
INSERT INTO `t_permission_role_ref` VALUES ('219', '238', '4', '2021-07-12 18:31:32');
INSERT INTO `t_permission_role_ref` VALUES ('220', '239', '4', '2021-07-12 18:31:32');
INSERT INTO `t_permission_role_ref` VALUES ('221', '240', '1', '2021-07-12 22:10:10');
INSERT INTO `t_permission_role_ref` VALUES ('222', '241', '1', '2021-07-12 22:10:11');
INSERT INTO `t_permission_role_ref` VALUES ('223', '240', '4', '2021-07-12 22:10:16');
INSERT INTO `t_permission_role_ref` VALUES ('224', '241', '4', '2021-07-12 22:10:17');
INSERT INTO `t_permission_role_ref` VALUES ('225', '242', '1', '2021-07-14 16:53:05');
INSERT INTO `t_permission_role_ref` VALUES ('226', '242', '4', '2021-07-14 16:53:09');

-- ----------------------------
-- Table structure for t_project
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `project_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目名称',
  `desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目描述',
  `protocol` varchar(10) DEFAULT NULL,
  `port` int DEFAULT NULL,
  `domain` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '项目地址',
  `dev_protocol` varchar(10) DEFAULT NULL,
  `dev_port` int DEFAULT NULL,
  `dev_domain` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '开发环境域名',
  `test_protocol` varchar(10) DEFAULT NULL,
  `test_port` int DEFAULT NULL,
  `test_domain` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '测试环境域名',
  `stg_protocol` varchar(10) DEFAULT NULL,
  `stg_port` int DEFAULT NULL,
  `stg_domain` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '预上线环境域名',
  `prod_protocol` varchar(10) DEFAULT NULL,
  `prod_port` int DEFAULT NULL,
  `prod_domain` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '正式环境域名',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('1', 'Alex', '', 'http', '7777', '127.0.0.1', 'http', '7777', '127.0.0.1', 'http', '7777', '127.0.0.1', 'http', '7777', '127.0.0.1', 'http', '7777', '127.0.0.1', null, null);

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
  `creator_id` int DEFAULT NULL COMMENT '创建人用户编号',
  `creator_name` varchar(30) DEFAULT NULL COMMENT '创建人用户昵称',
  `others_deletable` tinyint DEFAULT NULL COMMENT '非创建人能否删除0是1否',
  `others_modifiable` tinyint DEFAULT NULL COMMENT '非创建人能否修改0是1否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_rely_data
-- ----------------------------
INSERT INTO `t_rely_data` VALUES ('1', 'md5', 'md5(String s)', 'md5加密，引用方式示例：${md5(\'123456\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:41:24', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('2', 'uuid', 'uuid()', '获取uuid，引用方式示例：${uuid()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:29:54', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('3', 'base64', 'base64(String s)', 'base64加密，引用方式示例：${base64(\'123456\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:41:45', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('4', 'timestamp', 'timestamp()', '获取当前时间戳ms，引用方式示例：${timestamp()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('5', 'timestamps', 'timestamps()', '获取当前时间戳s，引用方式示例：${timestamps()}', '1', null, '2020-09-02 11:37:06', '2020-10-02 09:30:56', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('6', 'now', 'now(String format)', '获取当前时间，引用方式示例：${now(\'yyyy-MM-dd HH:mm:ss\')}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:41:59', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('7', 'tomorrow', 'tomorrow(String format)', '获取当前时间的明天，引用方式示例：${tomorrow(\'yyyy-MM-dd HH:mm:ss\')}，重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:42:04', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('9', 'time', 'time(String operator, String amount, String format)', '获取相较于当前时间的指定时间，operator：y、M、d、h、m、s；分别对应：年、月、日、时、分、秒，amount：数额，format：格式，如yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2020-09-02 11:36:58', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('10', 'yesterday', 'yesterday(String format)', '获取当前时间对应的昨天，引用方式示例：${yesterday(\'yyyy-MM-dd HH:mm:ss\')}，\n重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:42:19', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('11', 'lastMonth', 'lastMonth(String format)', '获取当前时间对应的上个月，引用方式示例：${lastMonth(\'yyyy-MM-dd HH:mm:ss\')}，\n重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:42:34', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('12', 'nextMonth', 'nextMonth(String format)', '获取当前时间对应的下个月，引用方式示例：${nextMonth(\'yyyy-MM-dd HH:mm:ss\')}，\n重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:42:44', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('13', 'lastYear', 'lastYear(String format)', '获取当前时间对应的去年，引用方式示例：${lastYear(\'yyyy-MM-dd HH:mm:ss\')}，\n重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:42:52', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('14', 'nextYear', 'nextYear(String format)', '获取当前时间对应的明年，引用方式示例：${nextYear(\'yyyy-MM-dd HH:mm:ss\')}，\n重载方法参数可为空，默认yyyy-MM-dd HH:mm:ss', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:42:59', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('16', 'randomInt', 'randomInt(String length)', '获取指定长度的随机整数，引用方式示例：${randomInt(\'10\')}；\nrandomInt(String minLen, String maxLen)重载方法为随机长度的随机数值', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:05', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('17', 'randomUpper', 'randomUpper(String length)', '获取指定长度的随机大写英文，引用方式示例：${randomUpper(\'10\')}；\nrandomUpper(String minLen, String maxLen)重载方法为随机长度的随机数值', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:15', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('18', 'randomLower', 'randomLower(String length)', '获取指定长度的随机小写英文，引用方式示例：${randomLower(\'10\')}；\nrandomLower(String minLen, String maxLen)重载方法为随机长度的随机数值', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:21', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('19', 'randomEn', 'randomEn(String length)', '获取指定长度的随机英文，引用方式示例：${randomEn(\'10\')}；\nrandomEn(String minLen, String maxLen)重载方法为随机长度的随机数值', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:28', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('20', 'randomIllegal', 'randomIllegal(String length)', '获取指定长度的非法字符，引用方式示例：${randomIllegal(\'10\')}；\nrandomIllegal(String minLen, String maxLen)重载方法为随机长度的随机数值', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:33', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('21', 'randomLegal', 'randomLegal(String length)', '获取指定长度字母数字，引用方式示例：${randomLegal(\'10\')}；\nandomLegal(String minLen, String maxLen)重载方法为随机长度的随机数值', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:39', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('22', 'nextNum', 'nextNum(String minSize, String maxSize)', '获取指定大小的数字，引用方式示例：nextNum(\'1.0\', \'2.0\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:43:55', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('23', 'nextInt', 'nextInt(String minSize, String maxSize)', '获取指定大小的整型数字，引用方式示例：${nextInt(\'1\', \'2\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:46:05', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('24', 'cs', '长沙', '长沙市，引用方式示例：${cs}', '0', null, '2020-09-02 11:37:06', '2021-06-04 10:32:24', null, '1', '1', '123', '0', '0');
INSERT INTO `t_rely_data` VALUES ('29', 'city', 'city()', '随机城市，引用方式示例：${city()}', '1', null, '2020-10-21 15:26:23', '2020-10-21 15:26:32', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('30', 'province', 'province()', '随机省份，引用方式示例：${province()}', '1', null, '2020-10-21 16:16:16', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('31', 'country', 'country()', '随机国家，引用方式示例：${country()}', '1', null, '2020-10-21 16:16:20', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('32', 'phone', 'phone()', '随机手机号码，引用方式示例：${phone()}', '1', null, '2020-10-21 16:16:26', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('33', 'email', 'email()', '随机邮箱，引用方式示例：${email()}', '1', null, '2020-10-21 16:16:28', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('34', 'mac', 'mac()', '随机mac地址，引用方式示例：${mac()}', '1', null, '2020-10-21 16:16:30', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('35', 'book', 'book()', '随机书名，引用方式示例：${book()}', '1', null, '2020-10-21 16:16:35', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('36', 'name', 'name()', '随机中文名称，引用方式示例：${name()}', '1', null, '2020-10-21 16:16:37', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('37', 'ipv4', 'ipv4()', '随机ipv4地址，引用方式示例：${ipv4()}', '1', null, '2020-10-21 16:16:40', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('38', 'privateIpv4', 'privateIpv4()', '随机私有ipv4地址，引用方式示例：${privateIpv4()}', '1', null, '2020-10-21 16:16:42', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('39', 'publicIpv4', 'publicIpv4()', '随机公有ipv4地址，引用方式示例：${publicIpv4()}', '1', null, '2020-10-21 16:16:44', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('40', 'ipv6', 'ipv6()', '随机ipv6地址，引用方式示例：${ipv6()}', '1', null, '2020-10-21 16:16:47', null, null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('41', 'InterfaceCaseTable', 'select * from t_interface_case where case_id = ?', '查询根据用例编号查询t_interface_case', '2', '1', '2021-03-08 15:32:57', '2021-06-04 14:34:52', null, '1', '1', '123', '1', '1');
INSERT INTO `t_rely_data` VALUES ('44', 'InsertUser', 'INSERT INTO `platform`.`t_user` (`username`, `password`, `job_number`, `sex`, `is_enable`, `created_time`, `update_time`, `real_name`, `role_id`) VALUES (\'${timestamp()}\', \'18e29620f058e8bf085bfed74fdf5e82\', \'\', \'1\', \'1\', NULL, \'2021-03-10 16:08:51\', \'123\', \'1\');', '新增用户', '3', '1', '2021-03-30 13:44:05', '2021-06-04 14:44:31', '0', '0', '1', '123', '0', '0');
INSERT INTO `t_rely_data` VALUES ('46', 'pick', 'pick(String... args)', '参数列表任选值，引用方式示例：${pick(\'1\', \'2\', \'3\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:44:12', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('47', 'inversePick', 'inversePick(String... args)', '参数列表返选值，引用方式示例：${inversePick(\'1\', \'2\', \'3\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:44:24', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('48', 'select', 'select(String dbId, String sql, String returnType)', '随机任选查询结果(仅支持单列)，returnType可选值有[integer, float, double, number, string],引用方式示例：${pick(\'1\', \'select username from t_user where id > 3\', \'integer\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:44:42', null, null, null, null, null, null);
INSERT INTO `t_rely_data` VALUES ('49', 'inverseSelect', 'inverseSelect(String dbId, String sql, String returnType)', '随机反选查询结果(仅支持单列)，returnType可选值有[integer, float, double, number, string],引用方式示例：${pick(\'1\', \'select username from t_user where id > 3\', \'integer\')}', '1', null, '2020-09-02 11:37:06', '2022-07-20 19:44:55', null, null, null, null, null, null);

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
-- Table structure for t_stability_case
-- ----------------------------
DROP TABLE IF EXISTS `t_stability_case`;
CREATE TABLE `t_stability_case` (
  `stability_test_id` int NOT NULL AUTO_INCREMENT,
  `desc` varchar(30) DEFAULT NULL COMMENT '稳定性用例描述',
  `protocol` tinyint DEFAULT NULL COMMENT '0http(s)1ws(s)2dubbo',
  `case_id` int DEFAULT NULL COMMENT '用例编号，根据协议确定',
  `execute_type` tinyint DEFAULT NULL COMMENT '调度方式0执行总次数   1截止至指定时间',
  `step` int DEFAULT NULL COMMENT '多久执行一次，单位ms',
  `execute_times` int DEFAULT NULL COMMENT '执行次数，当执行方式为0时，根据此项决定运行',
  `execute_end_time` datetime DEFAULT NULL COMMENT '执行结束时间，当执行方式为1时，根据此项决定运行',
  `on_error_stop` tinyint DEFAULT NULL COMMENT '请求报错是否停止0是1否',
  `on_failed_stop` tinyint DEFAULT NULL COMMENT '请求失败（断言）是否停止0是1否',
  `email_address` varchar(255) DEFAULT NULL COMMENT '邮箱地址',
  `log_record_content` tinyint DEFAULT NULL COMMENT '日志中记录的字段，0响应头 1响应体 2响应头+响应体',
  `creator_id` int DEFAULT NULL COMMENT '创建人用户编号',
  `creator_name` varchar(30) DEFAULT NULL COMMENT '创建人帐号',
  `created_time` datetime DEFAULT NULL COMMENT '任务创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '任务修改时间',
  `run_env` tinyint DEFAULT NULL COMMENT '0dev1test2stg3prod4debug',
  `last_execute_time` datetime DEFAULT NULL COMMENT '最近一次的执行时间',
  PRIMARY KEY (`stability_test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_stability_case
-- ----------------------------

-- ----------------------------
-- Table structure for t_stability_case_log
-- ----------------------------
DROP TABLE IF EXISTS `t_stability_case_log`;
CREATE TABLE `t_stability_case_log` (
  `stability_test_log_id` int NOT NULL AUTO_INCREMENT,
  `stability_test_log_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志编号',
  `stability_test_id` int DEFAULT NULL COMMENT '稳定性测试用例编号',
  `stability_test_desc` varchar(30) DEFAULT NULL COMMENT '稳定性用例描述',
  `log_path` varchar(100) DEFAULT NULL COMMENT '日志的目录',
  `status` tinyint DEFAULT NULL COMMENT '0进行中1停止2完成',
  `execute_id` int DEFAULT NULL COMMENT '执行人用户编号',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `run_env` tinyint DEFAULT NULL COMMENT '0dev1test2stg3prod4debug',
  `start_time` datetime DEFAULT NULL COMMENT '稳定性测试开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '稳定性测试结束时间',
  `request_count` int DEFAULT NULL COMMENT '请求数',
  `success_count` int DEFAULT NULL COMMENT '成功数',
  `warning_count` int DEFAULT NULL COMMENT '警告数',
  `failed_count` int DEFAULT NULL COMMENT '失败数',
  `error_count` int DEFAULT NULL COMMENT '失败数',
  `response_time_queue` mediumtext COMMENT '响应时间队列jsonarraystring',
  PRIMARY KEY (`stability_test_log_id`),
  UNIQUE KEY `stability_test_log_no` (`stability_test_log_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_stability_case_log
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task
-- ----------------------------

-- ----------------------------
-- Table structure for t_task_email_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_task_email_ref`;
CREATE TABLE `t_task_email_ref` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '定时任务id',
  `task_id` int DEFAULT NULL,
  `email_address` varchar(30) DEFAULT NULL COMMENT '邮箱地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task_email_ref
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
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', '75fefc85ac39bfcf082cbb0e29274aef', '', '1', '1', null, '2021-03-10 16:08:51', '超级管理员', '1');

-- ----------------------------
-- Table structure for t_user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_login_log`;
CREATE TABLE `t_user_login_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL COMMENT 'user_id',
  `user_name` varchar(30) DEFAULT NULL COMMENT 'username',
  `ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'ip地址',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of t_user_login_log
-- ----------------------------
