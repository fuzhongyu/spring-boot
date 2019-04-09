# ************************************************************
# Sequel Pro SQL dump
# Version 5102
#
# https://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.6.41)
# Database: boot
# Generation Time: 2019-04-09 02:45:39 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table sys_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT NULL COMMENT '日志类型：1-接入日志，2-错误日志',
  `title` varchar(255) DEFAULT NULL COMMENT '日志标题',
  `remote_addr` char(15) NOT NULL DEFAULT '' COMMENT '用户请求ip',
  `request_uri` varchar(255) NOT NULL DEFAULT '' COMMENT '请求uri',
  `user_agent` varchar(64) DEFAULT NULL COMMENT '操作用户代理信息',
  `method` varchar(16) DEFAULT NULL COMMENT '请求方法',
  `params` mediumtext COMMENT '请求参数',
  `exception` mediumtext COMMENT '异常信息',
  `create_time` date NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNI_USER_ACCESS` (`remote_addr`,`request_uri`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户日志访问记录表';



# Dump of table sys_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_permission`;

CREATE TABLE `sys_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型:0-菜单，1-url ,2-按钮',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `permission` varchar(63) DEFAULT '' COMMENT '权限',
  `name` varchar(63) DEFAULT NULL COMMENT '菜单名',
  `icon` char(100) DEFAULT NULL COMMENT '图标',
  `sort` smallint(6) unsigned NOT NULL DEFAULT '0' COMMENT '排序id',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统权限资源表（顶级目录的id 需要为1）';

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;

INSERT INTO `sys_permission` (`id`, `parent_id`, `type`, `url`, `permission`, `name`, `icon`, `sort`, `remarks`)
VALUES
	(1,0,0,NULL,'*','功能菜单',NULL,0,NULL),
	(123186055305232384,1,0,NULL,'','系统管理',NULL,10,''),
	(123192289488011264,123186055305232384,1,NULL,'system:user:view','用户管理',NULL,10,''),
	(123192534527639552,123186055305232384,1,NULL,'system:permission:view','权限管理',NULL,10,''),
	(123192640496730112,123186055305232384,1,NULL,'system:role:view','角色管理',NULL,10,''),
	(123193718546104320,123192289488011264,2,NULL,'system:user:edit','编辑',NULL,10,''),
	(123193829548359680,123192289488011264,2,NULL,'system:user:del','删除',NULL,10,''),
	(123193902579580928,123192534527639552,2,NULL,'system:permission:edit','编辑',NULL,10,''),
	(123193975212343296,123192534527639552,2,NULL,'system:permission:del','删除',NULL,10,''),
	(123194057278095360,123192640496730112,2,NULL,'system:role:edit','编辑',NULL,10,''),
	(123194132385497088,123192640496730112,2,NULL,'system:role:del','删除',NULL,10,''),
	(124137658329858048,123192289488011264,2,NULL,'system:user:view','查看',NULL,5,''),
	(124137756623372288,123192534527639552,2,NULL,'system:permission:view','查看',NULL,5,''),
	(124137838957559808,123192640496730112,2,NULL,'system:role:view','查看',NULL,5,'');

/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(63) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(1000) DEFAULT NULL COMMENT '角色类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统角色表';

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;

INSERT INTO `sys_role` (`id`, `name`, `description`)
VALUES
	(1,'超级管理员','拥有除系统管理模块外所有权限');

/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role_permission
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role_permission`;

CREATE TABLE `sys_role_permission` (
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `permission_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '权限ID',
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统角色权限表';

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;

INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
VALUES
	(1,1);

/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(512) NOT NULL COMMENT '密码',
  `nickname` varchar(8) NOT NULL COMMENT '昵称',
  `type` int(11) NOT NULL COMMENT '0、普通管理员，1、超级管理员',
  `status` smallint(1) NOT NULL DEFAULT '0' COMMENT '0:正常 1:冻结',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统管理员表';

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;

INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `type`, `status`, `remarks`)
VALUES
	(1,'admin','3f6d4666cc735cacdfc1725e297eb4f1','系统管理员',1,0,'admin用户');

/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `role_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '角色ID',
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `INDEX_ADMIN_ID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统管理员角色表';

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;

INSERT INTO `sys_user_role` (`role_id`, `user_id`)
VALUES
	(1,1);

/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
