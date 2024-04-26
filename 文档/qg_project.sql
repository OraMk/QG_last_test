-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: qg_project
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `application` (
  `aid` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `eid` int NOT NULL,
  `is_accept` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (1,'QGStudio',1,'yes','申请加入企业'),(2,'QGStudio',2,'pending','申请加入企业'),(19,'QGStudio',3,'pending','申请加入企业'),(20,'QGStudio',1,'yes','申请成为负责人'),(21,'QGStudio1',1,'yes','申请加入企业'),(22,'QGStudio1',2,'pending','申请加入企业'),(23,'QGStudio1',3,'pending','申请加入企业'),(25,'QGStudio1',1,'yes','申请成为负责人'),(26,'QGStudio3',1,'yes','申请加入企业');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blocking_application`
--

DROP TABLE IF EXISTS `blocking_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blocking_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `enterprise` varchar(255) DEFAULT NULL,
  `is_accept` varchar(10) NOT NULL DEFAULT 'pending',
  `processor` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `CheckNotEmptyUnique_one` CHECK (((`username` is not null) or (`enterprise` is not null)))
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blocking_application`
--

LOCK TABLES `blocking_application` WRITE;
/*!40000 ALTER TABLE `blocking_application` DISABLE KEYS */;
INSERT INTO `blocking_application` VALUES (5,'QGStudio5',NULL,'no','QGStudio2'),(6,'QGStudio5',NULL,'yes','QGStudio2'),(9,'QGStudio','','yes','QGStudio2'),(10,'QGStudio','','no','QGStudio2'),(11,'QGStudio','','no','QGStudio2'),(12,'QGStudio','','yes','QGStudio2'),(13,'QGStudio',NULL,'yes','QGStudio2');
/*!40000 ALTER TABLE `blocking_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enterprise`
--

DROP TABLE IF EXISTS `enterprise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise` (
  `eid` int NOT NULL AUTO_INCREMENT,
  `ename` varchar(255) NOT NULL,
  `number` bigint NOT NULL,
  `size` varchar(255) NOT NULL,
  `direction` varchar(255) DEFAULT NULL,
  `Public_mode` varchar(255) NOT NULL DEFAULT 'public',
  `Total_fund` double NOT NULL DEFAULT '0',
  `e_banned` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'no',
  `introduction` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`eid`,`ename`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enterprise`
--

LOCK TABLES `enterprise` WRITE;
/*!40000 ALTER TABLE `enterprise` DISABLE KEYS */;
INSERT INTO `enterprise` VALUES (1,'华为',10000,'small','5G通信','public',946530,'no','华为技术有限公司，成立于1987年，总部位于广东省深圳市龙岗区。华为是全球领先的信息与通信技术（ICT）解决方案供应商，专注于ICT领域，坚持稳健经营、持续创新、开放合作，在电信运营商、企业、终端和云计算等领域构筑了端到端的解决方案优势，为运营商客户、企业客户和消费者提供有竞争力的ICT解决方案、产品和服务，并致力于实现未来信息社会、构建更美好的全连接世界。'),(2,'腾讯',50000,'big','游戏开发','public',500000,'yes','腾讯，全称深圳市腾讯计算机系统有限公司，1998年11月由马化腾、张志东、许晨晔、陈一丹、曾李青五位创始人共同创立，2022年营收5545.52亿元。腾讯多元化的服务包括：社交和通信服务QQ及微信/WeChat、社交网络平台QQ空间、腾讯游戏旗下QQ游戏平台、门户网站腾讯网、腾讯新闻客户端和网络视频服务腾讯视频等。'),(3,'网易',1000,'small','互联网技术','public',10000,'no','网易公司（NASDAQ: NTES），1997年由创始人兼CEO丁磊先生在广州创办，2000年在美国纳斯达克股票交易所挂牌上市，是中国领先的互联网技术公司。在开发互联网应用、服务及其它技术方面，始终保持中国业界领先地位。本着对中国互联网发展强烈的使命感，缔造美好生活的愿景，网易利用最先进的互联网技术，加强人与人之间信息的交流和共享。'),(12,'QG后台',40,'small','开发','public',0,'yes','QG666'),(16,'cat',10,'small','11','private',0,'no','1234');
/*!40000 ALTER TABLE `enterprise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enterprise_application`
--

DROP TABLE IF EXISTS `enterprise_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `applicant` varchar(255) NOT NULL,
  `ename` varchar(255) NOT NULL,
  `number` bigint NOT NULL,
  `direction` varchar(255) NOT NULL,
  `size` varchar(10) NOT NULL,
  `public_mode` varchar(10) NOT NULL,
  `introduce` varchar(1024) DEFAULT NULL,
  `is_accept` varchar(10) NOT NULL DEFAULT 'pending',
  `processor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enterprise_application`
--

LOCK TABLES `enterprise_application` WRITE;
/*!40000 ALTER TABLE `enterprise_application` DISABLE KEYS */;
INSERT INTO `enterprise_application` VALUES (1,'QGStudio','cat',10,'11','small','public','1234','yes','QGStudio2'),(2,'QGStudio','dog',10,'12','small','public','23135','no','QGStudio2');
/*!40000 ALTER TABLE `enterprise_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_upload`
--

DROP TABLE IF EXISTS `file_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_upload` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `eid` int NOT NULL,
  `fund` double NOT NULL,
  `file` varchar(510) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_upload`
--

LOCK TABLES `file_upload` WRITE;
/*!40000 ALTER TABLE `file_upload` DISABLE KEYS */;
INSERT INTO `file_upload` VALUES (4,'QGStudio',1,10,'image-20240415101935290.png','2024-04-26 19:46:19'),(5,'QGStudio',1,10,'image-20240425213515086.png','2024-04-26 20:23:08'),(6,'QGStudio',1,10,'1.txt','2024-04-26 20:26:24');
/*!40000 ALTER TABLE `file_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_information`
--

DROP TABLE IF EXISTS `payment_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_information` (
  `username` varchar(255) NOT NULL,
  `payment_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_information`
--

LOCK TABLES `payment_information` WRITE;
/*!40000 ALTER TABLE `payment_information` DISABLE KEYS */;
INSERT INTO `payment_information` VALUES ('Admin0','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('Admin1','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('Cat1234','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('QGStudio','481f6cc0511143ccdd7e2d1b1b94faf0a700a8b49cd13922a70b5ae28acaa8c5'),('QGStudio1','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('QGStudio2','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('QGStudio3','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('QGStudio4','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('QGStudio5','8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');
/*!40000 ALTER TABLE `payment_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation`
--

DROP TABLE IF EXISTS `relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `relation` (
  `rid` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `eid` int NOT NULL,
  `isleader` varchar(10) NOT NULL DEFAULT 'no',
  `Allocation_funds` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation`
--

LOCK TABLES `relation` WRITE;
/*!40000 ALTER TABLE `relation` DISABLE KEYS */;
INSERT INTO `relation` VALUES (1,'QGStudio',1,'yes',99500),(5,'QGStudio1',1,'yes',80000),(6,'QGStudio3',1,'no',100000),(16,'QGStudio5',1,'no',10000),(17,'QGStudio',6,'yes',0),(18,'QGStudio',7,'yes',0),(19,'QGStudio',8,'yes',0),(20,'QGStudio',9,'yes',0),(23,'QGStudio',12,'yes',0),(29,'QGStudio',16,'yes',0),(30,'QGStudio1',16,'no',0);
/*!40000 ALTER TABLE `relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transfer`
--

DROP TABLE IF EXISTS `transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transfer` (
  `Tid` bigint NOT NULL AUTO_INCREMENT,
  `user_payer` varchar(255) DEFAULT NULL,
  `enterprise_payer` varchar(255) DEFAULT NULL,
  `user_payee` varchar(255) DEFAULT NULL,
  `enterprise_payee` varchar(255) DEFAULT NULL,
  `Date` datetime NOT NULL,
  `amount` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_tip` varchar(10) NOT NULL DEFAULT 'no',
  `is_accept` varchar(10) NOT NULL DEFAULT 'pending',
  PRIMARY KEY (`Tid`),
  CONSTRAINT `checknotemptyunique` CHECK (((`user_payee` is not null) or (`enterprise_payee` is not null))),
  CONSTRAINT `new_checknotemptyunique` CHECK (((`user_payee` is not null) or (`enterprise_payee` is not null))),
  CONSTRAINT `newchecknotemptyunique` CHECK (((`user_payee` is not null) or (`enterprise_payee` is not null)))
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transfer`
--

LOCK TABLES `transfer` WRITE;
/*!40000 ALTER TABLE `transfer` DISABLE KEYS */;
INSERT INTO `transfer` VALUES (5,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 15:49:24',1000,'测试代码','yes','yes'),(6,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 15:53:23',1000,'测试代码','yes','yes'),(7,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 15:55:02',1000,'测试代码','yes','yes'),(8,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 15:57:21',1000,'测试代码','yes','yes'),(9,'QGStudio',NULL,NULL,'','2024-04-20 15:58:35',1000,'测试代码','yes','yes'),(10,'QGStudio',NULL,NULL,'','2024-04-20 15:59:17',1000,'测试代码','yes','yes'),(11,'QGStudio',NULL,NULL,'','2024-04-20 16:00:44',1000,'测试代码','yes','yes'),(12,'QGStudio',NULL,NULL,'腾讯','2024-04-20 16:01:43',1000,'测试代码','no','pending'),(13,'QGStudio',NULL,NULL,'腾讯','2024-04-20 16:02:25',1000,'测试代码','no','pending'),(14,'QGStudio','','QGStudio1',NULL,'2024-04-20 16:04:07',120,'测试代码','yes','yes'),(15,'QGStudio','',NULL,'腾讯','2024-04-20 16:07:03',120,'测试代码','no','pending'),(16,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 16:53:49',10,'测试代码','yes','yes'),(17,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 16:55:11',10,'测试代码','yes','yes'),(18,'QGStudio','',NULL,'腾讯','2024-04-20 16:56:56',10,'测试代码','no','pending'),(19,'QGStudio','',NULL,'腾讯','2024-04-20 17:22:04',20,'测试代码','no','pending'),(20,'QGStudio','','QGStudio1',NULL,'2024-04-20 17:24:55',20,'测试代码','yes','yes'),(22,'QGStudio','','QGStudio1',NULL,'2024-04-20 17:35:20',10,'测试代码','yes','yes'),(23,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:36:50',20,'测试代码','yes','yes'),(24,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:37:42',30,'测试代码','yes','yes'),(25,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:38:56',10,'测试代码','yes','yes'),(26,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:41:09',10,'测试代码','yes','yes'),(27,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:42:18',15,'测试代码','yes','yes'),(28,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:42:50',5,'测试代码','yes','yes'),(29,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 17:46:10',10,'测试代码','yes','yes'),(30,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 20:11:09',10,'交易转账','yes','yes'),(31,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-20 20:11:39',10,NULL,'yes','yes'),(32,'QGStudio1',NULL,'QGStudio',NULL,'2024-04-22 17:56:11',100,'测试','yes','yes'),(33,'QGStudio1',NULL,'QGStudio',NULL,'2024-04-22 17:57:48',100,'测试','yes','yes'),(34,'QGStudio1',NULL,'QGStudio',NULL,'2024-04-22 19:02:18',1000,'测试','no','pending'),(35,'QGStudio',NULL,'QGStudio1',NULL,'2024-04-22 20:19:56',10000,'检测','yes','yes'),(36,'QGStudio','','QGStudio1',NULL,'2024-04-24 20:14:43',100,'测试','yes','yes'),(37,'QGStudio','华为','QGStudio1','','2024-04-25 15:48:02',10,'检测','yes','yes'),(38,'QGStudio','华为','QGStudio1','','2024-04-26 11:33:43',10,'检测','yes','yes'),(39,'QGStudio','华为','QGStudio1','','2024-04-26 11:33:59',10,'检测','yes','yes'),(40,'QGStudio','华为','QGStudio1','','2024-04-26 11:35:09',10,'检测','yes','yes'),(41,'QGStudio','华为','QGStudio1','','2024-04-26 11:35:31',10,'检测','yes','yes'),(42,'QGStudio','华为','QGStudio1','','2024-04-26 11:36:12',10,'检测','yes','yes'),(43,'QGStudio','华为','QGStudio1','','2024-04-26 11:37:04',10,'检测','yes','yes'),(44,'QGStudio','华为','QGStudio1','','2024-04-26 11:42:03',10,'检测','yes','yes'),(45,'QGStudio','华为','QGStudio1','','2024-04-26 11:42:48',10,'检测','yes','yes'),(46,'QGStudio','华为','QGStudio1','','2024-04-26 11:47:04',10,'检测','yes','yes'),(47,'QGStudio','华为','QGStudio1','','2024-04-26 11:49:45',10,'检测','yes','yes'),(48,'QGStudio','华为','QGStudio1','','2024-04-26 11:50:04',10,'检测','yes','yes'),(49,'QGStudio','华为','QGStudio1','','2024-04-26 11:53:11',10,'检测','yes','yes'),(50,'QGStudio','华为','QGStudio1','','2024-04-26 12:18:28',10,'检测','yes','yes'),(51,'QGStudio','华为','QGStudio1','','2024-04-26 12:19:36',10,'检测','yes','yes'),(52,'QGStudio','华为','QGStudio1','','2024-04-26 12:21:11',10,'检测','yes','yes'),(53,'QGStudio','华为','QGStudio1','','2024-04-26 12:25:02',10,'检测','yes','yes'),(54,'QGStudio','华为','QGStudio1','','2024-04-26 12:26:51',10,'检测','yes','yes'),(55,'QGStudio','华为','QGStudio1','','2024-04-26 12:32:02',10,'检测','yes','yes'),(56,'QGStudio','华为','QGStudio1','','2024-04-26 12:36:39',10,'检测','yes','yes'),(57,'QGStudio','华为','QGStudio1','','2024-04-26 12:38:54',20,'检测','yes','yes'),(58,'QGStudio','华为','QGStudio1','','2024-04-26 12:40:50',10,'检测上传文件','yes','yes'),(59,'QGStudio','华为','QGStudio1','','2024-04-26 12:41:22',10,'检测上传文件','yes','yes'),(60,'QGStudio','华为','QGStudio1','','2024-04-26 12:43:32',10,'检测上传文件','yes','yes'),(61,'QGStudio','华为','QGStudio1','','2024-04-26 12:43:56',10,'检测上传文件','yes','yes'),(62,'QGStudio','华为','QGStudio1','','2024-04-26 12:44:24',10,'检测','yes','yes'),(63,'QGStudio','华为','QGStudio1','','2024-04-26 12:46:40',10,'检测','yes','yes'),(64,'QGStudio','华为','QGStudio1','','2024-04-26 12:50:20',10,'检测','yes','yes'),(65,'QGStudio','华为','QGStudio1','','2024-04-26 12:53:32',10,'检测','yes','yes'),(66,'QGStudio','华为','QGStudio1','','2024-04-26 12:55:16',10,'检测','yes','yes'),(67,'QGStudio','华为','QGStudio1','','2024-04-26 13:03:28',10,'检测','yes','yes'),(68,'QGStudio','华为','QGStudio1','','2024-04-26 13:08:22',10,'检测','yes','yes'),(69,'QGStudio','华为','QGStudio1','','2024-04-26 13:08:32',10,'检测','yes','yes'),(70,'QGStudio','华为','QGStudio1','','2024-04-26 13:10:32',10,'检测','yes','yes'),(71,'QGStudio','华为','QGStudio1','','2024-04-26 13:11:16',10,'检测','yes','yes'),(72,'QGStudio','华为','QGStudio1','','2024-04-26 14:31:18',10,'检测','yes','yes'),(73,'QGStudio','华为','QGStudio1','','2024-04-26 14:31:43',10,'检测','yes','yes'),(74,'QGStudio','华为','QGStudio1','','2024-04-26 14:31:52',10,'检测','yes','yes'),(75,'QGStudio','华为','QGStudio1','','2024-04-26 14:34:53',10,'检测','yes','yes'),(76,'QGStudio','华为','QGStudio1','','2024-04-26 14:37:13',10,'检测','yes','yes'),(77,'QGStudio','华为','QGStudio1','','2024-04-26 14:40:29',10,'检测','yes','yes'),(78,'QGStudio','华为','QGStudio1','','2024-04-26 14:50:43',10,'检测','yes','yes'),(79,'QGStudio','华为','QGStudio1','','2024-04-26 14:53:17',10,'检测','yes','yes'),(80,'QGStudio','华为','QGStudio1','','2024-04-26 14:55:09',10,'检测','yes','yes'),(81,'QGStudio','华为','QGStudio1','','2024-04-26 14:55:57',10,'检测','yes','yes'),(82,'QGStudio','华为','QGStudio1','','2024-04-26 14:57:09',10,'检测','yes','yes'),(83,'QGStudio','华为','QGStudio1','','2024-04-26 14:59:17',10,'检测','yes','yes'),(84,'QGStudio','华为','QGStudio1','','2024-04-26 15:00:34',10,'检测','yes','yes'),(85,'QGStudio','华为','QGStudio1','','2024-04-26 15:04:57',10,'检测','yes','yes'),(86,'QGStudio','华为',NULL,'腾讯','2024-04-26 15:14:21',10,'检测','no','pending'),(87,'QGStudio','华为',NULL,'QG后台','2024-04-26 15:15:49',10,'检测','no','pending'),(88,'QGStudio','华为',NULL,'腾讯','2024-04-26 15:17:40',10,'检测','no','pending'),(89,'QGStudio','华为','QGStudio1','','2024-04-26 15:18:07',10,'检测','yes','yes'),(90,'QGStudio','华为','QGStudio1','','2024-04-26 15:36:07',10,'检测','yes','yes'),(91,'QGStudio','华为','QGStudio1','','2024-04-26 15:36:55',10,'检测','yes','yes'),(92,'QGStudio','华为',NULL,'腾讯','2024-04-26 15:38:25',10,'检测','no','pending'),(93,'QGStudio','华为',NULL,'腾讯','2024-04-26 15:40:03',10,'检测','no','pending'),(94,'QGStudio','华为',NULL,'腾讯','2024-04-26 18:11:27',10,'检测','no','pending'),(95,'QGStudio','华为','QGStudio1','','2024-04-26 19:44:56',10,'检测','no','pending'),(96,'QGStudio','华为',NULL,'腾讯','2024-04-26 19:46:18',10,'检测','no','pending'),(97,'QGStudio','华为',NULL,'腾讯','2024-04-26 20:23:08',10,'检测','no','pending'),(98,'QGStudio','华为','QGStudio1','','2024-04-26 20:26:22',10,'检测','no','pending');
/*!40000 ALTER TABLE `transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `avatar` varchar(510) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `pnumber` varchar(255) DEFAULT NULL unique,
  `fund` double NOT NULL DEFAULT '0',
  `is_administrator` varchar(255) NOT NULL DEFAULT 'no',
  `u_banned` varchar(10) NOT NULL DEFAULT 'no',
  PRIMARY KEY (`uid`,`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'QGStudio','fdf9ac58cefc4e14fa4ae306649ca55bbeffbb3ffec96d9a9c618853f7d93f76efa952241cdcc152a241fadc4d58c6f2dc5a6572c8eb01d6bc93f5f677822054','QG后台','https://th.bing.com/th/id/R.3049959b8c06c802d16d535975ce730d?rik=FBkdTolVCAOemQ&riu=http%3a%2f%2fimg.touxiangwu.com%2fuploads%2fallimg%2f2022053119%2fjal1spg1ses.jpg&ehk=N8IhTcYE9yv5m4YWdmWaapxNQ3WOo723iAOlTPGir5I%3d&risl=&pid=ImgRaw&r=0','12345678951',0,'no','no'),(2,'QGStudio1','fdf9ac58cefc4e14fa4ae306649ca55bbeffbb3ffec96d9a9c618853f7d93f76efa952241cdcc152a241fadc4d58c6f2dc5a6572c8eb01d6bc93f5f677822054','QG后台新人','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','12345678901',18940,'no','no'),(5,'QGStudio2','fdf9ac58cefc4e14fa4ae306649ca55bbeffbb3ffec96d9a9c618853f7d93f76efa952241cdcc152a241fadc4d58c6f2dc5a6572c8eb01d6bc93f5f677822054','QG后台萌新','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','13524679858',10000,'yes','no'),(6,'QGStudio3','fdf9ac58cefc4e14fa4ae306649ca55bbeffbb3ffec96d9a9c618853f7d93f76efa952241cdcc152a241fadc4d58c6f2dc5a6572c8eb01d6bc93f5f677822054','QGStudio小弟','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','15975364820',0,'no','no'),(7,'QGStudio5','fdf9ac58cefc4e14fa4ae306649ca55bbeffbb3ffec96d9a9c618853f7d93f76efa952241cdcc152a241fadc4d58c6f2dc5a6572c8eb01d6bc93f5f677822054','QG后台','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','13254698795',0,'no','yes'),(8,'Cat1234','5c05d25b14799ac1cfbc8a5f45109855e9fd5dd50ff910144f480371978413cb9da91446e524be1aab3a7bcdcc5a76552945596f7a065fdfb9be4610a062a9e0','小猫','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','15935745682',0,'no','no'),(9,'QGStudio4','fdf9ac58cefc4e14fa4ae306649ca55bbeffbb3ffec96d9a9c618853f7d93f76efa952241cdcc152a241fadc4d58c6f2dc5a6572c8eb01d6bc93f5f677822054','QG小萌新','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','16495784260',0,'no','no'),(10,'Admin0','3cedbe825e44d66e896dc40b185d94ba146f4a82c6c464663bde4ba420bbb0bdb2aed7b91835363d7cbc1ca6e5b56fe7aaca86692a245b5b23baa02a2b43fc5a','网站管理员','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','17825654657',0,'yes','no'),(14,'Admin1','3cedbe825e44d66e896dc40b185d94ba146f4a82c6c464663bde4ba420bbb0bdb2aed7b91835363d7cbc1ca6e5b56fe7aaca86692a245b5b23baa02a2b43fc5a','网站管理员1','https://img.zcool.cn/community/01a3865ab91314a8012062e3c38ff6.png@1280w_1l_2o_100sh.png','17825654952',0,'yes','no');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-26 21:21:40
