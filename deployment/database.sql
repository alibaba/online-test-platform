/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `result`
--

DROP TABLE IF EXISTS `result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskid` int(11) DEFAULT NULL,
  `caseid` int(11) DEFAULT NULL,
  `request` longtext,
  `response` longtext,
  `rule_result` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rule`
--

DROP TABLE IF EXISTS `rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `tags` varchar(256) DEFAULT NULL,
  `category` varchar(256) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `r_when` text,
  `r_verify` text,
  `r_then` text,
  `enable` tinyint(4) DEFAULT NULL,
  `retry` tinyint(4) DEFAULT NULL,
  `owner` varchar(45) DEFAULT NULL,
  `modify` timestamp NULL DEFAULT NULL,
  `application` varchar(45) DEFAULT NULL,
  `debug` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rule`
--

LOCK TABLES `rule` WRITE;
/*!40000 ALTER TABLE `rule` DISABLE KEYS */;
INSERT INTO `rule` VALUES (1,'请求中的id与返回中cityid值一致',NULL,'字段取值验证',1,'返回结果jsonArr.size() > 0;','temp = 原始查询串.split(\"/\");\n\ntemp1 = temp[temp.length-1];\n\nreq_cityid = temp1.split(\".html\")[0];\n\n\nres_cityid = 返回结果jsonArr.getJSONObject(0).getJSONObject(\"weatherinfo\").get(\"cityid\");\nif (req_cityid != res_cityid) {\n    记录错误(\"返回中的cityid与请求中不一致\");\n    return false;\n}\n\nreturn true;',NULL,1,NULL,NULL,NULL,'steed_data',NULL),(2,'返回结果中的时间满足正则',NULL,'字段取值验证',1,'返回结果jsonArr.size() > 0;','res_time = 返回结果jsonArr.getJSONObject(0).getJSONObject(\"weatherinfo\").get(\"time\");\n\npattern = \"^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$\";\n\nflag = matchPattern(res_time,pattern);\n\nif( flag == false){\n    记录错误(\"返回时间格式异常\");\n    return false;\n}\n\nreturn true;',NULL,1,NULL,NULL,NULL,'steed_data',NULL),(3,'中国气象局查询天气服务结果统计空结果率正常',NULL,'其他验证',1,'(统计结果.get(\"total_query\") != null) and (统计结果.get(\"total_query\") >= 1)','total = 统计结果.get(\"total_query\");\n    emptys = 统计结果.get(\"empty_return\");\n    if(emptys == null){\n        return true;\n    }\n    if( total== null || total == 0){\n        记录错误(format(\"统计数据异常，total_query为0或者不存在\"));\n        return false;\n    }\n    emptys = emptys * 100;\n    ratio = emptys/total;\n    if(ratio > 80){\n        记录错误(format(\"空结果率为 %s，不在[0,80]范围内\",ratio));\n        return false;\n    }\n    return true;',NULL,1,NULL,NULL,NULL,'steed_data',NULL);
/*!40000 ALTER TABLE `rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` varchar(45) DEFAULT NULL,
  `host` varchar(45) DEFAULT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `end_time` timestamp NULL DEFAULT NULL,
  `task_result` int(11) DEFAULT NULL COMMENT '0：运行中、1：成功、2：失败',
  `task_info` varchar(200) DEFAULT NULL,
  `rule_result` longtext,
  `test_data` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;