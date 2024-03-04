-- MySQL dump 10.13  Distrib 8.3.0, for macos14.2 (arm64)
--
-- Host: localhost    Database: Moravianwomensteam24
-- ------------------------------------------------------
-- Server version	8.1.0

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
-- Table structure for table `FreeThrows`
--

DROP TABLE IF EXISTS `FreeThrows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FreeThrows` (
  `date` date DEFAULT NULL,
  `made` int DEFAULT NULL,
  `attempted` int DEFAULT NULL,
  `player_id` int DEFAULT NULL,
  KEY `player_id` (`player_id`),
  CONSTRAINT `freethrows_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `TeamRoster` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FreeThrows`
--

LOCK TABLES `FreeThrows` WRITE;
/*!40000 ALTER TABLE `FreeThrows` DISABLE KEYS */;
/*!40000 ALTER TABLE `FreeThrows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TeamRoster`
--

DROP TABLE IF EXISTS `TeamRoster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TeamRoster` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `player_name` varchar(100) DEFAULT NULL,
  `player_number` int DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `expected_graduation_date` int DEFAULT NULL,
  `height` decimal(5,2) DEFAULT NULL,
  `weight` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TeamRoster`
--

LOCK TABLES `TeamRoster` WRITE;
/*!40000 ALTER TABLE `TeamRoster` DISABLE KEYS */;
/*!40000 ALTER TABLE `TeamRoster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ThreePointShots`
--

DROP TABLE IF EXISTS `ThreePointShots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ThreePointShots` (
  `date` date DEFAULT NULL,
  `made` int DEFAULT NULL,
  `attempted` int DEFAULT NULL,
  `player_id` int DEFAULT NULL,
  KEY `player_id` (`player_id`),
  CONSTRAINT `threepointshots_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `TeamRoster` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ThreePointShots`
--

LOCK TABLES `ThreePointShots` WRITE;
/*!40000 ALTER TABLE `ThreePointShots` DISABLE KEYS */;
/*!40000 ALTER TABLE `ThreePointShots` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-22 10:58:23
