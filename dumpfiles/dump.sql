-- MySQL dump 10.13  Distrib 8.2.0, for Win64 (x86_64)
--
-- Host: localhost    Database: moravianwomensteam24
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Table structure for table `archiveplayers`
--


CREATE DATABASE IF NOT EXISTS moravianwomensteam24;

USE moravianwomensteam24;

DROP TABLE IF EXISTS `archiveplayers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archiveplayers` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `player_number` int DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `expected_graduation_date` int DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `archiveplayers`
--

LOCK TABLES `archiveplayers` WRITE;
/*!40000 ALTER TABLE `archiveplayers` DISABLE KEYS */;
/*!40000 ALTER TABLE `archiveplayers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `freethrows`
--

DROP TABLE IF EXISTS `freethrows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `freethrows` (
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `made` int DEFAULT NULL,
  `attempted` int DEFAULT NULL,
  `player_id` int DEFAULT NULL,
  KEY `player_id` (`player_id`),
  CONSTRAINT `freethrows_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `teamroster` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `freethrows`
--

LOCK TABLES `freethrows` WRITE;
/*!40000 ALTER TABLE `freethrows` DISABLE KEYS */;
/*!40000 ALTER TABLE `freethrows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teamroster`
--

DROP TABLE IF EXISTS `teamroster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teamroster` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `player_number` int DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `expected_graduation_date` int DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teamroster`
--

LOCK TABLES `teamroster` WRITE;
/*!40000 ALTER TABLE `teamroster` DISABLE KEYS */;
/*!40000 ALTER TABLE `teamroster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `threepointshots`
--

DROP TABLE IF EXISTS `threepointshots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `threepointshots` (
  `first_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `made` int DEFAULT NULL,
  `attempted` int DEFAULT NULL,
  `player_id` int DEFAULT NULL,
  KEY `player_id` (`player_id`),
  CONSTRAINT `threepointshots_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `teamroster` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `threepointshots`
--

LOCK TABLES `threepointshots` WRITE;
/*!40000 ALTER TABLE `threepointshots` DISABLE KEYS */;
/*!40000 ALTER TABLE `threepointshots` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-27  4:00:26
