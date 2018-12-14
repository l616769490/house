/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50560
 Source Host           : localhost:3306
 Source Schema         : house

 Target Server Type    : MySQL
 Target Server Version : 50560
 File Encoding         : 65001

 Date: 02/12/2018 09:15:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data
-- ----------------------------
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `xId` int(11) NOT NULL,
  `legendId` int(11) NOT NULL,
  `x` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `legend` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xid`(`xId`) USING BTREE,
  INDEX `legid`(`legendId`) USING BTREE,
  CONSTRAINT `legid` FOREIGN KEY (`legendId`) REFERENCES `legend` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `xid` FOREIGN KEY (`xId`) REFERENCES `xaxis` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for diagram
-- ----------------------------
DROP TABLE IF EXISTS `diagram`;
CREATE TABLE `diagram`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int(1) NOT NULL,
  `reportId` int(11) NOT NULL,
  `subtext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `reportId`(`reportId`) USING BTREE,
  CONSTRAINT `reportId` FOREIGN KEY (`reportId`) REFERENCES `report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dimension
-- ----------------------------
DROP TABLE IF EXISTS `dimension`;
CREATE TABLE `dimension`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dimName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dimNameEN` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dimension
-- ----------------------------
INSERT INTO `dimension` VALUES (1, '区域', '1', 'REGION');
INSERT INTO `dimension` VALUES (2, '区域', '2', 'REGION');
INSERT INTO `dimension` VALUES (3, '区域', '3', 'REGION');
INSERT INTO `dimension` VALUES (4, '区域', '4', 'REGION');
INSERT INTO `dimension` VALUES (5, '城市规模', '1', 'METRO3');
INSERT INTO `dimension` VALUES (6, '城市规模', '2', 'METRO3');
INSERT INTO `dimension` VALUES (7, '城市规模', '3', 'METRO3');
INSERT INTO `dimension` VALUES (8, '城市规模', '4', 'METRO3');
INSERT INTO `dimension` VALUES (9, '城市规模', '5', 'METRO3');
INSERT INTO `dimension` VALUES (10, '户主年龄', '0-18', 'AGE1');
INSERT INTO `dimension` VALUES (11, '户主年龄', '18-40', 'AGE1');
INSERT INTO `dimension` VALUES (12, '户主年龄', '40-65', 'AGE1');
INSERT INTO `dimension` VALUES (13, '户主年龄', '65+', 'AGE1');
INSERT INTO `dimension` VALUES (14, '建成年份', '1900-2000', 'BUILT');
INSERT INTO `dimension` VALUES (15, '建成年份', '2000-2010', 'BUILT');
INSERT INTO `dimension` VALUES (16, '建成年份', '2010-2018', 'BUILT');
INSERT INTO `dimension` VALUES (17, '租金', '0-1000', 'FMR');
INSERT INTO `dimension` VALUES (18, '租金', '1000-1500', 'FMR');
INSERT INTO `dimension` VALUES (19, '租金', '1500-2000', 'FMR');
INSERT INTO `dimension` VALUES (20, '租金', '2000-2500', 'FMR');
INSERT INTO `dimension` VALUES (21, '租金', '2500-3000', 'FMR');
INSERT INTO `dimension` VALUES (22, '租金', '3000+', 'FMR');
INSERT INTO `dimension` VALUES (23, '家庭人数', '1', 'PER');
INSERT INTO `dimension` VALUES (24, '家庭人数', '2', 'PER');
INSERT INTO `dimension` VALUES (25, '家庭人数', '3', 'PER');
INSERT INTO `dimension` VALUES (26, '家庭人数', '4', 'PER');
INSERT INTO `dimension` VALUES (27, '家庭人数', '5', 'PER');
INSERT INTO `dimension` VALUES (28, '家庭人数', '6', 'PER');
INSERT INTO `dimension` VALUES (29, '家庭人数', '6+', 'PER');
INSERT INTO `dimension` VALUES (30, '房产税', '0-500', 'ZSMHC');
INSERT INTO `dimension` VALUES (31, '房产税', '500-1000', 'ZSMHC');
INSERT INTO `dimension` VALUES (32, '房产税', '1000-1500', 'ZSMHC');
INSERT INTO `dimension` VALUES (33, '房产税', '1500-2000', 'ZSMHC');
INSERT INTO `dimension` VALUES (34, '房产税', '2000-2500', 'ZSMHC');
INSERT INTO `dimension` VALUES (35, '房产税', '2500-3000', 'ZSMHC');
INSERT INTO `dimension` VALUES (36, '房产税', '3000-3500', 'ZSMHC');
INSERT INTO `dimension` VALUES (37, '房产税', '3500+', 'ZSMHC');
INSERT INTO `dimension` VALUES (38, '水电费', '0-100', 'UTILITY');
INSERT INTO `dimension` VALUES (39, '水电费', '100-200', 'UTILITY');
INSERT INTO `dimension` VALUES (40, '水电费', '200-300', 'UTILITY');
INSERT INTO `dimension` VALUES (41, '水电费', '300-400', 'UTILITY');
INSERT INTO `dimension` VALUES (42, '水电费', '400-500', 'UTILITY');
INSERT INTO `dimension` VALUES (43, '水电费', '500+', 'UTILITY');
INSERT INTO `dimension` VALUES (44, '其他费用', '0-500', 'OTHERCOST');
INSERT INTO `dimension` VALUES (45, '其他费用', '500-1000', 'OTHERCOST');
INSERT INTO `dimension` VALUES (46, '其他费用', '1000-1500', 'OTHERCOST');
INSERT INTO `dimension` VALUES (47, '其他费用', '1500-2000', 'OTHERCOST');
INSERT INTO `dimension` VALUES (48, '其他费用', '2000-2500', 'OTHERCOST');
INSERT INTO `dimension` VALUES (49, '其他费用', '2500-3000', 'OTHERCOST');
INSERT INTO `dimension` VALUES (50, '其他费用', '3000-3500', 'OTHERCOST');
INSERT INTO `dimension` VALUES (51, '其他费用', '3500+', 'OTHERCOST');
INSERT INTO `dimension` VALUES (52, '房间数', '1', 'ROOMS');
INSERT INTO `dimension` VALUES (53, '房间数', '2', 'ROOMS');
INSERT INTO `dimension` VALUES (54, '房间数', '3', 'ROOMS');
INSERT INTO `dimension` VALUES (55, '房间数', '4', 'ROOMS');
INSERT INTO `dimension` VALUES (56, '房间数', '5', 'ROOMS');
INSERT INTO `dimension` VALUES (57, '房间数', '6', 'ROOMS');
INSERT INTO `dimension` VALUES (58, '房间数', '7', 'ROOMS');
INSERT INTO `dimension` VALUES (59, '房间数', '8', 'ROOMS');
INSERT INTO `dimension` VALUES (60, '房间数', '9', 'ROOMS');
INSERT INTO `dimension` VALUES (61, '房间数', '10', 'ROOMS');
INSERT INTO `dimension` VALUES (62, '房间数', '10+', 'ROOMS');
INSERT INTO `dimension` VALUES (63, '卧室数', '1', 'BEDRMS');
INSERT INTO `dimension` VALUES (64, '卧室数', '2', 'BEDRMS');
INSERT INTO `dimension` VALUES (65, '卧室数', '3', 'BEDRMS');
INSERT INTO `dimension` VALUES (66, '卧室数', '4', 'BEDRMS');
INSERT INTO `dimension` VALUES (67, '卧室数', '5', 'BEDRMS');
INSERT INTO `dimension` VALUES (68, '卧室数', '6', 'BEDRMS');
INSERT INTO `dimension` VALUES (69, '卧室数', '7', 'BEDRMS');
INSERT INTO `dimension` VALUES (70, '卧室数', '8', 'BEDRMS');
INSERT INTO `dimension` VALUES (71, '卧室数', '9', 'BEDRMS');
INSERT INTO `dimension` VALUES (72, '卧室数', '10', 'BEDRMS');
INSERT INTO `dimension` VALUES (73, '卧室数', '10+', 'BEDRMS');
INSERT INTO `dimension` VALUES (74, '建筑结构类型', '1', 'STRUCTURETYPE');
INSERT INTO `dimension` VALUES (75, '建筑结构类型', '2', 'STRUCTURETYPE');
INSERT INTO `dimension` VALUES (76, '建筑结构类型', '3', 'STRUCTURETYPE');
INSERT INTO `dimension` VALUES (77, '建筑结构类型', '4', 'STRUCTURETYPE');
INSERT INTO `dimension` VALUES (78, '建筑结构类型', '5', 'STRUCTURETYPE');
INSERT INTO `dimension` VALUES (79, '建筑结构类型', '6', 'STRUCTURETYPE');
INSERT INTO `dimension` VALUES (80, '单元数', '0-100', 'NUNITS');
INSERT INTO `dimension` VALUES (81, '单元数', '100-200', 'NUNITS');
INSERT INTO `dimension` VALUES (82, '单元数', '200-300', 'NUNITS');
INSERT INTO `dimension` VALUES (83, '单元数', '300-400', 'NUNITS');
INSERT INTO `dimension` VALUES (84, '单元数', '400-500', 'NUNITS');
INSERT INTO `dimension` VALUES (85, '单元数', '500-600', 'NUNITS');
INSERT INTO `dimension` VALUES (86, '单元数', '600-700', 'NUNITS');
INSERT INTO `dimension` VALUES (87, '单元数', '700-800', 'NUNITS');
INSERT INTO `dimension` VALUES (88, '单元数', '800-900', 'NUNITS');
INSERT INTO `dimension` VALUES (89, '单元数', '900-1000', 'NUNITS');
INSERT INTO `dimension` VALUES (90, '价格', '0-50', 'VALUE');
INSERT INTO `dimension` VALUES (91, '价格', '50-100', 'VALUE');
INSERT INTO `dimension` VALUES (92, '价格', '100-150', 'VALUE');
INSERT INTO `dimension` VALUES (93, '价格', '150-200', 'VALUE');
INSERT INTO `dimension` VALUES (94, '价格', '200-250', 'VALUE');
INSERT INTO `dimension` VALUES (95, '价格', '260-300', 'VALUE');
INSERT INTO `dimension` VALUES (96, '空置', 'true', 'VACANCY');
INSERT INTO `dimension` VALUES (97, '空置', 'false', 'VACANCY');
INSERT INTO `dimension` VALUES (98, '自住', 'true', 'TENURE');
INSERT INTO `dimension` VALUES (99, '自住', 'false', 'TENURE');
INSERT INTO `dimension` VALUES (100, '家庭收入', '0-5', 'ZINC2');
INSERT INTO `dimension` VALUES (101, '家庭收入', '5-10', 'ZINC2');
INSERT INTO `dimension` VALUES (102, '家庭收入', '10-15', 'ZINC2');
INSERT INTO `dimension` VALUES (103, '家庭收入', '15-20', 'ZINC2');
INSERT INTO `dimension` VALUES (104, '家庭收入', '20+', 'ZINC2');
INSERT INTO `dimension` VALUES (105, '最大值', 'max', 'MATH');
INSERT INTO `dimension` VALUES (106, '最小值', 'min', 'MATH');
INSERT INTO `dimension` VALUES (107, '平均值', 'avg', 'MATH');
INSERT INTO `dimension` VALUES (108, '房间数统计', '总房间数', 'null');
INSERT INTO `dimension` VALUES (109, '房间数统计', '总卧室数', 'null');
INSERT INTO `dimension` VALUES (110, '价格统计', '平均租金', 'null');
INSERT INTO `dimension` VALUES (111, '收入统计', '中位数', 'LMED');
INSERT INTO `dimension` VALUES (112, '收入统计', 'L30', 'L30');
INSERT INTO `dimension` VALUES (113, '收入统计', 'L50', 'L50');
INSERT INTO `dimension` VALUES (114, '收入统计', 'L80', 'L80');
INSERT INTO `dimension` VALUES (115, '收入统计', '贫困线', 'IPOV');
INSERT INTO `dimension` VALUES (116, '收入统计', '家庭收入', 'ZINC2');
INSERT INTO `dimension` VALUES (117, '价格统计', '平均价格', 'null');
INSERT INTO `dimension` VALUES (118, '数学统计', 'MAX', 'null');
INSERT INTO `dimension` VALUES (119, '数学统计', 'MIN', 'null');
INSERT INTO `dimension` VALUES (120, '数学统计', 'AVG', 'null');
INSERT INTO `dimension` VALUES (121, '房间数卧室数统计', '房间数', 'ROOMS');
INSERT INTO `dimension` VALUES (122, '房间数卧室数统计', '卧室数', 'BEDRMS');
INSERT INTO `dimension` VALUES (123, '空维度', '空维度', 'null');
INSERT INTO `dimension` VALUES (124, '建筑年份', '1900-1940', 'BUILD');
INSERT INTO `dimension` VALUES (125, '建筑年份', '1940-1960', 'BUILD');
INSERT INTO `dimension` VALUES (126, '建筑年份', '1960-1980', 'BUILD');
INSERT INTO `dimension` VALUES (127, '建筑年份', '1980-2000', 'BUILD');
INSERT INTO `dimension` VALUES (128, '建筑年份', '2000+', 'BUILD');


-- ----------------------------
-- Table structure for legend
-- ----------------------------
DROP TABLE IF EXISTS `legend`;
CREATE TABLE `legend`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dimGroupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `diagramId` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `diaID`(`diagramId`) USING BTREE,
  CONSTRAINT `diaID` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create` bigint(20) NOT NULL,
  `year` int(4) NOT NULL,
  `group` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `status` int(1) NOT NULL,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for search
-- ----------------------------
DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `dimGroupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `reportId` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `reid`(`reportId`) USING BTREE,
  CONSTRAINT `reid` FOREIGN KEY (`reportId`) REFERENCES `report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for xaxis
-- ----------------------------
DROP TABLE IF EXISTS `xaxis`;
CREATE TABLE `xaxis`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `diagramId` int(11) NOT NULL,
  `dimGroupName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `diagramId`(`diagramId`) USING BTREE,
  CONSTRAINT `diagramId` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for yaxis
-- ----------------------------
DROP TABLE IF EXISTS `yaxis`;
CREATE TABLE `yaxis`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `diagramId` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ydim`(`diagramId`) USING BTREE,
  CONSTRAINT `ydim` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
