/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50556
 Source Host           : localhost:3306
 Source Schema         : common

 Target Server Type    : MySQL
 Target Server Version : 50556
 File Encoding         : 65001

 Date: 27/11/2019 17:22:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ding_applications
-- ----------------------------
DROP TABLE IF EXISTS `ding_applications`;
CREATE TABLE `ding_applications`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` timestamp NULL DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `delete_date` timestamp NULL DEFAULT NULL,
  `application_id` int(11) NULL DEFAULT NULL COMMENT '申请单id，业务id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请标题',
  `total_count` int(11) NULL DEFAULT NULL COMMENT '申请总次数',
  `need_count` int(11) NULL DEFAULT NULL COMMENT '申请已用次数',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备id',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seal_record_info_id` int(11) NULL DEFAULT NULL COMMENT '记录id',
  `original_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原文件名',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新文件名',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件配置地址',
  `relative_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本次存储相对路径',
  `absolute_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本地存储绝对路径',
  `create_date` timestamp NULL DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `delete_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for finger
-- ----------------------------
DROP TABLE IF EXISTS `finger`;
CREATE TABLE `finger`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备id',
  `finger_addr` int(11) NULL DEFAULT NULL COMMENT '指纹地址',
  `create_date` timestamp NULL DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `delete_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for seal_record_info
-- ----------------------------
DROP TABLE IF EXISTS `seal_record_info`;
CREATE TABLE `seal_record_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` timestamp NULL DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `delete_date` timestamp NULL DEFAULT NULL,
  `device_id` int(11) NULL DEFAULT NULL COMMENT '设备id',
  `uuid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备唯一码',
  `identity` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用印人名称',
  `picUserId` int(11) NULL DEFAULT NULL COMMENT '用印人id',
  `location` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用印地址',
  `count` int(11) NULL DEFAULT NULL COMMENT '当前使用记录对应的次数',
  `application_id` int(11) NULL DEFAULT NULL COMMENT '申请单id',
  `is_aduit` int(1) NULL DEFAULT NULL COMMENT '0:该条记录是盖章上传创建的  1:该条记录是审计上传创建的\'',
  `time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实盖章时间',
  `seal_count` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '高拍仪关联的盖章次数',
  `seal_Id` int(11) NULL DEFAULT NULL COMMENT '高拍仪关联的印章id',
  `alarm` int(11) NULL DEFAULT NULL COMMENT '0:正常 1:长按警报 2:防拆卸报警',
  `file_id` int(11) NULL DEFAULT NULL COMMENT '关联上传的记录文件',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for signet
-- ----------------------------
DROP TABLE IF EXISTS `signet`;
CREATE TABLE `signet`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` timestamp NULL DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `delete_date` timestamp NULL DEFAULT NULL,
  `corp_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方企业ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '印章名',
  `net_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网络状态',
  `addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '印章地址',
  `count` int(6) NULL DEFAULT NULL COMMENT '出厂后已使用次数',
  `uuid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备唯一码',
  `sim_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物联网卡号',
  `iccid` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物联网卡',
  `imsi` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `imei` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(2) NULL DEFAULT NULL COMMENT '类型',
  `status` int(2) NULL DEFAULT NULL COMMENT '印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定',
  `sleep_time` int(11) NULL DEFAULT NULL COMMENT '休眠时间',
  `finger_pattern` bit(1) NULL DEFAULT NULL COMMENT '指纹模式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1002 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
