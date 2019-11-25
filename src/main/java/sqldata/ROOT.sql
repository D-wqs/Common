/*
 Navicat Premium Data Transfer

 Source Server         : oracle_root
 Source Server Type    : Oracle
 Source Server Version : 110200
 Source Host           : 192.168.85.143:1521
 Source Schema         : ROOT

 Target Server Type    : Oracle
 Target Server Version : 110200
 File Encoding         : 65001

 Date: 25/11/2019 13:13:11
*/


-- ----------------------------
-- Table structure for FILE_INFO
-- ----------------------------
DROP TABLE "ROOT"."FILE_INFO";
CREATE TABLE "ROOT"."FILE_INFO" (
  "ID" NUMBER(11) NOT NULL ,
  "SEAL_RECORD_INFO_ID" NUMBER(11) ,
  "ORIGIN_NAME" VARCHAR2(100 BYTE) ,
  "FILE_NAME" VARCHAR2(100 BYTE) ,
  "PATH" VARCHAR2(100 BYTE) ,
  "RELATIVE_PATH" VARCHAR2(255 BYTE) ,
  "ABSOLUTE_PATH" VARCHAR2(255 BYTE) ,
  "CREATE_DATE" TIMESTAMP(6) ,
  "UPDATE_DATE" TIMESTAMP(6) ,
  "DELETE_DATE" TIMESTAMP(6) 
)
TABLESPACE "SYSTEM"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  FREELISTS 1
  FREELIST GROUPS 1
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "ROOT"."FILE_INFO"."SEAL_RECORD_INFO_ID" IS '记录id';
COMMENT ON COLUMN "ROOT"."FILE_INFO"."ORIGIN_NAME" IS '原文件名';
COMMENT ON COLUMN "ROOT"."FILE_INFO"."FILE_NAME" IS '新文件名';
COMMENT ON COLUMN "ROOT"."FILE_INFO"."PATH" IS '文件配置地址';
COMMENT ON COLUMN "ROOT"."FILE_INFO"."RELATIVE_PATH" IS '本次存储相对路径';
COMMENT ON COLUMN "ROOT"."FILE_INFO"."ABSOLUTE_PATH" IS '本次存储绝对路径';

-- ----------------------------
-- Table structure for FINGER
-- ----------------------------
DROP TABLE "ROOT"."FINGER";
CREATE TABLE "ROOT"."FINGER" (
  "ID" NUMBER(11) NOT NULL ,
  "USER_ID" NUMBER(11) ,
  "USER_NAME" VARCHAR2(20 BYTE) ,
  "DEVICE_ID" NUMBER(11) ,
  "FINGER_ADDR" NUMBER(11) ,
  "CREATE_DATE" TIMESTAMP(6) ,
  "UPDATE_DATE" TIMESTAMP(6) ,
  "DELETE_DATE" TIMESTAMP(6) 
)
TABLESPACE "SYSTEM"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  FREELISTS 1
  FREELIST GROUPS 1
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "ROOT"."FINGER"."USER_ID" IS '用户ID';
COMMENT ON COLUMN "ROOT"."FINGER"."USER_NAME" IS '用户名';
COMMENT ON COLUMN "ROOT"."FINGER"."DEVICE_ID" IS '设备ID';
COMMENT ON COLUMN "ROOT"."FINGER"."FINGER_ADDR" IS '指纹地址';

-- ----------------------------
-- Table structure for SEAL_RECORD_INFO
-- ----------------------------
DROP TABLE "ROOT"."SEAL_RECORD_INFO";
CREATE TABLE "ROOT"."SEAL_RECORD_INFO" (
  "ID" NUMBER(11) NOT NULL ,
  "CREATE_DATE" TIMESTAMP(6) ,
  "UPDATE_DATE" TIMESTAMP(6) ,
  "DELETE_DATE" TIMESTAMP(6) ,
  "DEVICE_ID" NUMBER(11) ,
  "UUID" VARCHAR2(30 BYTE) ,
  "IDENTITY" VARCHAR2(20 BYTE) ,
  "PICUSERID" NUMBER(11) ,
  "LOCATION" VARCHAR2(150 BYTE) ,
  "COUNT" NUMBER(11) ,
  "APPLICATION_ID" NUMBER(11) ,
  "IS_AUDIT" NUMBER(11) ,
  "TIME" NVARCHAR2(20) ,
  "SEAL_COUNT" NUMBER(11) ,
  "SEAL_ID" NUMBER(11) ,
  "ALARM" NUMBER(11) ,
  "FILE_ID" NUMBER(11) 
)
TABLESPACE "SYSTEM"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  FREELISTS 1
  FREELIST GROUPS 1
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."DEVICE_ID" IS '设备ID';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."UUID" IS '设备唯一码';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."IDENTITY" IS '用印人名称';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."PICUSERID" IS '用印人ID';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."LOCATION" IS '用印地址';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."COUNT" IS '当前试用记录对应的次数';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."APPLICATION_ID" IS '申请单ID';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."IS_AUDIT" IS '0:该条记录是盖章上传创建的  1:该条记录是审计上传创建的''';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."TIME" IS '真实盖章时间';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."SEAL_COUNT" IS '高拍仪关联的盖章次数';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."SEAL_ID" IS '高拍仪关联的印章ID';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."ALARM" IS '0:正常 1:长按警报 2:防拆卸报警';
COMMENT ON COLUMN "ROOT"."SEAL_RECORD_INFO"."FILE_ID" IS '关联上传的记录文件';

-- ----------------------------
-- Table structure for SIGNET
-- ----------------------------
DROP TABLE "ROOT"."SIGNET";
CREATE TABLE "ROOT"."SIGNET" (
  "ID" NUMBER(11) NOT NULL ,
  "CREATE_DATE" TIMESTAMP(6) ,
  "UPDATE_DATE" TIMESTAMP(6) ,
  "DELETE_DATE" TIMESTAMP(6) ,
  "CORP_ID" VARCHAR2(64 BYTE) ,
  "NAME" VARCHAR2(50 BYTE) ,
  "NET_TYPE" VARCHAR2(50 BYTE) ,
  "ADDR" VARCHAR2(255 BYTE) ,
  "COUNT" NUMBER(11) ,
  "UUID" VARCHAR2(50 BYTE) ,
  "SIM_NUM" VARCHAR2(20 BYTE) ,
  "ICCID" VARCHAR2(20 BYTE) ,
  "IMSI" VARCHAR2(20 BYTE) ,
  "IMEI" VARCHAR2(20 BYTE) ,
  "TYPE" NUMBER(2) ,
  "STATUS" NUMBER(2) ,
  "SLEEP_TIME" NUMBER(11) ,
  "FINGER_PATTERN" NUMBER(1) 
)
TABLESPACE "SYSTEM"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  FREELISTS 1
  FREELIST GROUPS 1
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "ROOT"."SIGNET"."CORP_ID" IS '第三方企业ID';
COMMENT ON COLUMN "ROOT"."SIGNET"."NAME" IS '印章名';
COMMENT ON COLUMN "ROOT"."SIGNET"."NET_TYPE" IS '网络状态';
COMMENT ON COLUMN "ROOT"."SIGNET"."ADDR" IS '印章地址';
COMMENT ON COLUMN "ROOT"."SIGNET"."COUNT" IS '出厂后已使用次数';
COMMENT ON COLUMN "ROOT"."SIGNET"."UUID" IS '设备唯一码';
COMMENT ON COLUMN "ROOT"."SIGNET"."SIM_NUM" IS '物联网卡号';
COMMENT ON COLUMN "ROOT"."SIGNET"."ICCID" IS '物联网卡';
COMMENT ON COLUMN "ROOT"."SIGNET"."TYPE" IS '类型';
COMMENT ON COLUMN "ROOT"."SIGNET"."STATUS" IS '印章状态: 0:正常 1:异常 2:销毁 3:停用 4:锁定';
COMMENT ON COLUMN "ROOT"."SIGNET"."SLEEP_TIME" IS '休眠时间';
COMMENT ON COLUMN "ROOT"."SIGNET"."FINGER_PATTERN" IS '指纹模式';

-- ----------------------------
-- Table structure for TJ_APPLICATIONS
-- ----------------------------
DROP TABLE "ROOT"."TJ_APPLICATIONS";
CREATE TABLE "ROOT"."TJ_APPLICATIONS" (
  "ID" NUMBER(11) NOT NULL ,
  "CREATE_DATE" TIMESTAMP(6) ,
  "UPDATE_DATE" TIMESTAMP(6) ,
  "DELETE_DATE" TIMESTAMP(6) ,
  "APPLICATION_ID" NUMBER(11) ,
  "TITLE" VARCHAR2(255 BYTE) ,
  "TOTAL_COUNT" NUMBER(11) ,
  "NEED_COUNT" NUMBER(11) ,
  "DEVICE_ID" NUMBER(11) ,
  "USER_NAME" VARCHAR2(20 BYTE) ,
  "USER_ID" NUMBER(11) 
)
TABLESPACE "SYSTEM"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  FREELISTS 1
  FREELIST GROUPS 1
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."APPLICATION_ID" IS '申请单id，业务id';
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."TITLE" IS '申请标题';
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."TOTAL_COUNT" IS '申请总次数';
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."NEED_COUNT" IS '申请已用次数';
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."DEVICE_ID" IS '设备id';
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."USER_NAME" IS '用户名';
COMMENT ON COLUMN "ROOT"."TJ_APPLICATIONS"."USER_ID" IS '用户id';

-- ----------------------------
-- Sequence structure for FILE_INFO_SEQ
-- ----------------------------
DROP SEQUENCE "ROOT"."FILE_INFO_SEQ";
CREATE SEQUENCE "ROOT"."FILE_INFO_SEQ" MINVALUE 1 MAXVALUE 99999999999 INCREMENT BY 1 CACHE 20;

-- ----------------------------
-- Sequence structure for FINGER_SEQ
-- ----------------------------
DROP SEQUENCE "ROOT"."FINGER_SEQ";
CREATE SEQUENCE "ROOT"."FINGER_SEQ" MINVALUE 1 MAXVALUE 99999999999 INCREMENT BY 1 CACHE 20;

-- ----------------------------
-- Sequence structure for SEAL_RECORD_INFO_SEQ
-- ----------------------------
DROP SEQUENCE "ROOT"."SEAL_RECORD_INFO_SEQ";
CREATE SEQUENCE "ROOT"."SEAL_RECORD_INFO_SEQ" MINVALUE 1 MAXVALUE 99999999999 INCREMENT BY 1 CACHE 20;

-- ----------------------------
-- Sequence structure for SIGNET_SEQ
-- ----------------------------
DROP SEQUENCE "ROOT"."SIGNET_SEQ";
CREATE SEQUENCE "ROOT"."SIGNET_SEQ" MINVALUE 1000 MAXVALUE 99999999999 INCREMENT BY 1 CACHE 20;

-- ----------------------------
-- Sequence structure for TJ_APPLICATIONS_SEQ
-- ----------------------------
DROP SEQUENCE "ROOT"."TJ_APPLICATIONS_SEQ";
CREATE SEQUENCE "ROOT"."TJ_APPLICATIONS_SEQ" MINVALUE 1 MAXVALUE 99999999999 INCREMENT BY 1 CACHE 20;

-- ----------------------------
-- Primary Key structure for table FILE_INFO
-- ----------------------------
ALTER TABLE "ROOT"."FILE_INFO" ADD CONSTRAINT "SYS_C007002" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table FILE_INFO
-- ----------------------------
ALTER TABLE "ROOT"."FILE_INFO" ADD CONSTRAINT "SYS_C007022" CHECK ("ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Primary Key structure for table FINGER
-- ----------------------------
ALTER TABLE "ROOT"."FINGER" ADD CONSTRAINT "SYS_C006999" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table FINGER
-- ----------------------------
ALTER TABLE "ROOT"."FINGER" ADD CONSTRAINT "SYS_C007023" CHECK ("ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Primary Key structure for table SEAL_RECORD_INFO
-- ----------------------------
ALTER TABLE "ROOT"."SEAL_RECORD_INFO" ADD CONSTRAINT "SYS_C006997" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table SEAL_RECORD_INFO
-- ----------------------------
ALTER TABLE "ROOT"."SEAL_RECORD_INFO" ADD CONSTRAINT "SYS_C007024" CHECK ("ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Primary Key structure for table SIGNET
-- ----------------------------
ALTER TABLE "ROOT"."SIGNET" ADD CONSTRAINT "SYS_C007000" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table SIGNET
-- ----------------------------
ALTER TABLE "ROOT"."SIGNET" ADD CONSTRAINT "SYS_C007025" CHECK ("ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;

-- ----------------------------
-- Primary Key structure for table TJ_APPLICATIONS
-- ----------------------------
ALTER TABLE "ROOT"."TJ_APPLICATIONS" ADD CONSTRAINT "SYS_C007001" PRIMARY KEY ("ID");

-- ----------------------------
-- Checks structure for table TJ_APPLICATIONS
-- ----------------------------
ALTER TABLE "ROOT"."TJ_APPLICATIONS" ADD CONSTRAINT "SYS_C007026" CHECK ("ID" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;