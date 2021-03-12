/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80019
Source Host           : localhost:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 80019
File Encoding         : 65001

Date: 2021-03-12 23:31:09
*/

SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8;

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
INSERT INTO `t_permission_role_ref` VALUES ('75', '134', '1', '2021-03-12 19:59:00');
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
