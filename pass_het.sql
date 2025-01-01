-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: hotel_manager
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `passwords` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `fullname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `gender` bit(1) DEFAULT b'1',
  `role_id` int DEFAULT NULL,
  `is_delete` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_accounts_role_id` (`role_id`),
  CONSTRAINT `FK_accounts_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (6,'hotelowner','$2a$10$Y6wXGCWNahVGc1L19D8j7OfaaJ/STtE3WmI4u8rr.bbWgoJdUNuS6','Trần thừa Hoàng','0123456789','manager1@example.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',1,_binary ''),(7,'nhanvien1','$2a$10$iBNIHLelvSo4yEqR705zg.QtAOXcpYRSnD26fAjMH7Dz1LwI0zERa','Nguyễn ','012345s790','staff23@example.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '\0',1,_binary ''),(8,'nhanvien2','$2a$10$Bzb7ZIHFqWcaa00PX9zyJOvK9iSnl/d/9iuLvoW3FgdB9bOSq0VRC','Lê Anh Long','0123456791','employee03@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '\0',2,_binary ''),(9,'nhanvien3','$2a$10$B2PrtF2c2Tb/o0MhFrlCUeFttTPspZ8JDebl4egkHZuv1WbHovH4m','Hồ Chiêm Cơ','0123456792','staff1@example.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(10,'nhanvien4','$2a$10$uUERNneLbE0L0AgKCedvReyaPan1h4iYAIHCLPjSnlX8etXouHk5m','Triệu Trung','0123456793','admin1@example.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '\0',2,_binary ''),(11,'nhanvien5','$2a$10$3aKeGG4KEoZvBykvpoQcWex49I1z8cvSdom.3hX/aWwHKvLaMGw/y','Doanh Nguyệt','0982884660','admin@example.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(12,'nhanvien6','$2a$10$MPImidJgcFyHjOntnFIv.e8sylp7qLU5IZB8tUek2q6UG/4dV8DX2','Lý Hồ Hiếu','0123488789','admin888@example.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(34,'nhanvien7','$2a$10$4KQg92QNP9jIHu4Q3xPn3ukTmbduuRqVnw6gKuiA4BloU3sz9Qxb2','Lý Thiện Trường','0971285444','superman1245@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(35,'nhanvien8','$2a$10$A5P2co7B3D9936Fgw.V6kOkF4RdaoaDR.STQ7Bp4WoGhsOGqpcc..','Huỳnh Nhật Hạ','0971255544','nghialtpc06472@fpt.edu.vn','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(37,'luuthanhnghia2004@gmail.com',NULL,'nghia luu thanh','0123456739','luuthanhnghia2004@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(56,'nhanvien9','$2a$10$anbVvsRu5HtS9w7MkezFBOkeL7dKNufvT81xqNvU6DU8BGuKFkA0y','Nguyễn Ánh Loan','0978642897','nhanvien9@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(57,'nhanvien10','$2a$10$z.i4Ni/6jDEfN5zaOtnVNerw.MIlhPJoorSEX0J4M8f/WrA/HmuP6','Trần Bình Trọng','0970963155','nhanvien10@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(58,'nhanvien11','$2a$10$1HOWGr93V/nx4WAVMGYJNew/b5noM7F0uXCPKVsleSLfLEXQ7woZK','Phạm Khá ','0978433997','nhanvien11@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(59,'nhanvien12','$2a$10$cBU2sZUTJFeqav4s0iaztO4P3FbIyLs5QPC7rpelcV/6C77iBTUiy','Phan Văn Thái','0978657827','nhanvien12@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',2,_binary ''),(60,'khachhang01','$2a$10$zpMXrTk.XN1i6uQwNdZIVeX3vM4L0rcxjw1lUndQ1tAOtf2nOaHkO','Nguyễn Văn An','0912345678','khachhang01@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(61,'khachhang02','$2a$10$Ncpcs/L5.zBhh3aiFnMJFec/7Dxo6lvuxGPDrnZUAwDh6VeEUVCoC','Trần Thị Bích','0987654321','khachhang02@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(62,'khachhang03','$2a$10$uzGQEHgWxiw.c0LdXqzhLOAXagYCn0LI3etmhCUENTY7SB2jMkflu','Lê Minh Tuấn','0901234567','khachhang03@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(63,'khachhang04','$2a$10$BotWvy6TAMX6OGuI4gkkDuy3mNeNjP7P.P2gv4d8ZYOVxoafDmcUi','Phạm Thị Hương','0923456789','khachhang04@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(64,'khachhang05','$2a$10$cHy46.rVJMJ4CqZRoPQxruljQTiH2FnwdobCECXLZcWvGSyQ4upG.','Đỗ Văn Hải','0934567890','khachhang052@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(65,'khachhang06','$2a$10$QkWPyLZYXsgOOuo3k3gone0cAWYP45vh9EWphMthdnRxf2VzzJJFm','Hoàng Thị Lan','0945678901','khachhang06@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(66,'khachhang07','$2a$10$ORJgApPvlf0tZXtnEDqAiO6pTyY83Cs9G.lYGRYVktpnjOFNfXoyq','Nguyễn Quốc Duy','0956789012','khachhang07@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(67,'khachhang08','$2a$10$fbTis0xMeMRXrE2dhzGeF.Q2807.jF62RIMvfz3jteZvqXE7ZQLJK','Vũ Thị Mai','0967890123','khachhang08@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(68,'khachhang09','$2a$10$Dnc0hbcGpPMoMwhw3cHM7.fHc0UVPWM4GNlv3VVgsvpMEYcWMzHse','Trương Văn Hòa','0978901234','khachhang09@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(69,'khachhang010','$2a$10$xRnYEH.8xFA8t6eX9amxvONsn6ZvZV3ajwIZzOPk83WkryKANHMCK','Phan Quốc Bình','0981234567','khachhang010@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(70,'khachhang011','$2a$10$MGfNkl9/4Rd1VuPNzeRMPOUI4fWq0imRWZ039BeBNgsixe9PnQxMG','Phan Quốc Bình','0911112222','khachhang011@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(71,'khachhang012','$2a$10$1jnAPqydvUobIVOTXXTb4.rm5.4EyAbEFM4m.s3WACBXKTxVQkUc6','Bùi Thị Nhung','0922223333','khachhang012@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(72,'khachhang013','$2a$10$sx4Np5j5YxrxSyKvFqcjuO5q5SMutzs3uCjskkRzjXh1hhuhTROyO','Nguyễn Hoàng Nam','0933334444','khachhang013@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(73,'khachhang014','$2a$10$71FqKA5ZKqyGTNgTJZRkXOSbmxtu4AFxGCZPBIgQWLKgJt9jOVmTu','Trần Văn Phúc','0944445555','khachhang014@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(74,'mjkkhoi11@gmail.com',NULL,'Lê Minh Khôi',NULL,'mjkkhoi11@gmail.com','https://lh3.googleusercontent.com/a/ACg8ocIOLacaEqPlCA1BfhyaWpFefeDup3LvlrEuNAmVz7EKk4ozyFs=s96-c',_binary '',1,_binary ''),(75,'vunhpc06500@fpt.edu.vn','$2a$10$l2BG0ap5qnBAeUF5hiDdc.V2dSWBH8FLAlZ7K4fF5cYAUG0RQ5Jd.','Nguyen Ho Vu (FPL CT)','0934900152','vunhpc061500@fpt.edu.vn','https://lh3.googleusercontent.com/a/ACg8ocII6zXekMmL9uOeZV9H6ZA2Wb4Wzqr0gBGUbBXg3-zoPQk2Clo=s96-c',_binary '',1,_binary ''),(76,'nguyenhovu2004@gmail.com',NULL,'Vu Nguyen','0764900152','nguyenhovu20104@gmail.com','https://lh3.googleusercontent.com/a/ACg8ocKsRmAHk9DxNopq7aJPyXKMVJYY4atjluxvTzlTsdvXTifpNw=s96-c',_binary '',1,_binary ''),(77,'vn12345.lol.com@gmail.com',NULL,'Hồ Vũ','0987654322','vn12345.lol.com@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fbd874489-c76c-4b67-b544-f39ce2b5d108?alt=media&token=9b9d890b-b02b-44dc-af44-0d51d16e300f',_binary '',2,_binary ''),(79,'baotri','$2a$10$35Dq92X1/8ZaP28PWSFLnegVwZQwsIer.p12QZScKGx57PBqsUhHa','nguyen van a','0987654221','abc@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',1,_binary ''),(80,'mjkkhoi','$2a$10$d63Y83PjZ12GCThPaIvze.Sb/P3M0WYJ/tjdKx3K6h0c3kT.H3ex.','baotri','0989986543','mjkhii@gamil.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1734435910129_Screenshot%20(44).png?alt=media&token=d2a855f7-f505-42da-a237-ca8f30eafbdc',_binary '',2,_binary ''),(82,'nguyenvana','$2a$10$sOmR7/iGj4JHQiQRHZBmguBN3asy3RnHd2FwJRtq61JlKLjQj7mx6','baotri','0764900159','xyz@gmail.com','',_binary '\0',1,_binary ''),(83,'vuNHfff','$2a$10$CaQlI3qZ1R31NiIIcuH4ueOfxjouePJs7Q.H8npTYmYUPIRd5FgJO','hvffff','0964900153','hoangtam110322022@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1734522800438_UseCaseHotel-C%C6%A0%20S%E1%BB%9E%20D%E1%BB%AE%20LI%E1%BB%86U.jpg?alt=media&token=e9622bca-6226-44fd-884f-d6257ea9b167',_binary '',2,_binary ''),(84,'sdfdfdf','$2a$10$T4v5nWW4XLezOuArfPCrSuMRtvzZ3h6t1Ahc4LJGeWfdl2AWKlrZS','hv12345','0764900158','vn12345.lpl.com@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1734523109637_e.png?alt=media&token=6299bbe9-5fb2-4be0-803d-e04332e7657d',NULL,2,_binary ''),(86,'abc123456789','$2a$10$pSvaqzXT7fi9AfnZ7TBNbutJC3bPXfpVX/1qiuTpUisQz/J8DP/Y6','Nguyen Ho Vu nè','0164567892','tuongdz550@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(87,'tuong','$2a$10$Cd61n0OQxCUCzZqHHw0VFujL4xSQay0GmIXYt1noUfqCo53SoLjTi','Tường nè','0123456799','tuongdzz550@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(90,'kh001','$2a$10$SxE3UkEWSl2E9ay040vi4eNibpf1CzaOJc0IW3Wh2/ZLS2Rqb/z8i','nguyen van a','0987654371','vunhpc06500@fpt.edu.vn','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary ''),(91,'123','$2a$10$pYAnxc9lwSXr0X3qBPcou.Wn3SfgY4Lj7FDRb4UlpPJHwNoomC7vq','hv12345','0987654331','abc12@gmail.com','',NULL,1,_binary '\0'),(94,'nhanvien','$2a$10$NP5kpH5O763uxnoQCXEyG.MT3Bdp.o1Pex76ZEt79iUtMc4JOmdYa','nguyen thi bb','0964900143','nguyenhovu22004@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',1,_binary ''),(95,'nguyenvana21','$2a$10$ZfZx1rdXbvJ76E8Fy6pW3.ItYz05lH/x3ccOFEa8F.NnETiHh6gKm','Vu Nguyen 2','0123452790','nguyenhovu2004@gmail.com','https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F140aa596-c596-49d4-b679-f2df70845b8d?alt=media&token=54bedd2b-f56f-4b23-8820-cb7e04d43c92',_binary '',3,_binary '');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `amenities_hotel`
--

DROP TABLE IF EXISTS `amenities_hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amenities_hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amenities_hotel_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `hotel_id` int DEFAULT NULL,
  `descriptions` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_amenities_hotel_hotel_id` (`hotel_id`),
  CONSTRAINT `fk_amenities_hotel_hotel_id` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amenities_hotel`
--

LOCK TABLES `amenities_hotel` WRITE;
/*!40000 ALTER TABLE `amenities_hotel` DISABLE KEYS */;
INSERT INTO `amenities_hotel` VALUES (1,'WiFi Miễn Phí',1,'Khách sạn cung cấp dịch vụ WiFi miễn phí tốc độ cao trong toàn bộ khuôn viên, giúp bạn dễ dàng kết nối và làm việc mọi lúc mọi nơi trong thời gian lưu trú.'),(2,'Phòng Tập Gym 24h',1,'Phòng tập gym hiện đại đầy đủ trang thiết bị từ máy chạy bộ, tạ, đến các dụng cụ thể dục khác, phù hợp cho mọi đối tượng, giúp bạn duy trì sức khỏe và thể hình ngay trong kỳ nghỉ.'),(3,'Phòng Xông Hơi',1,'Phòng xông hơi hiện đại mang đến trải nghiệm thư giãn tuyệt vời, giúp bạn giải tỏa căng thẳng và phục hồi sức khỏe sau những ngày dài khám phá.'),(4,'Bữa Sáng Tự Chọn',1,'Bữa sáng tự chọn phong phú với nhiều món ăn nóng hổi, từ bánh mì tươi, trái cây tươi ngon đến các món ăn truyền thống, đáp ứng sở thích đa dạng của khách hàng.'),(5,'Quầy Bar Cocktail',1,'Quầy bar cocktail phục vụ đa dạng các loại đồ uống độc đáo, từ cocktail truyền thống đến các món sáng tạo, nơi lý tưởng để thư giãn và giao lưu với bạn bè.'),(9,'Hồ Bơi Nước Nóng',1,'Hồ bơi nước nóng với không gian thư giãn, lý tưởng cho những buổi chiều mát mẻ, giúp bạn tận hưởng sự thoải mái và thư giãn tuyệt đối.'),(10,'Khu Vui Chơi Trẻ Em',1,'Khu vui chơi trẻ em an toàn và thú vị, nơi các bé có thể thỏa sức vui đùa và khám phá, mang đến trải nghiệm tuyệt vời cho gia đình có trẻ nhỏ.'),(11,'Spa Thư Giãn',1,'Dịch vụ spa thư giãn với nhiều liệu pháp chăm sóc sức khỏe và sắc đẹp, mang đến cho bạn những giây phút thư giãn tuyệt vời và tái tạo năng lượng.'),(12,'Khu Vườn Thiên Nhiên',1,'Khu vườn thiên nhiên xanh mát, nơi bạn có thể đi dạo, thư giãn và tận hưởng không gian yên bình, phù hợp để tổ chức các buổi tiệc nhỏ hoặc sự kiện ngoài trời.'),(13,'Dịch Vụ Thú Cưng',1,'Dịch vụ thân thiện với thú cưng, cho phép bạn mang theo thú cưng của mình trong suốt thời gian lưu trú, với nhiều tiện ích dành riêng cho chúng.'),(14,'Khu vực BBQ',1,'Khu vực BBQ ngoài trời với trang bị đầy đủ, nơi bạn có thể tổ chức tiệc nướng cùng gia đình và bạn bè trong không gian thoáng đãng.'),(15,'Dịch Vụ 24/7',1,'Dịch vụ tiếp tân 24/7 sẵn sàng hỗ trợ bạn mọi lúc, giúp bạn giải quyết nhanh chóng các yêu cầu và nhu cầu trong suốt thời gian lưu trú.'),(16,'Bữa Tối Đặc Biệt',1,'Bữa tối đặc biệt được tổ chức vào cuối tuần với thực đơn đa dạng, mang đến cho bạn trải nghiệm ẩm thực độc đáo và khó quên.'),(17,'Dịch Vụ Chăm Sóc Sức Khỏe',1,'Các liệu pháp chăm sóc sức khỏe chuyên nghiệp, bao gồm massage, xông hơi và chăm sóc da, giúp bạn tái tạo sức lực và thư giãn tối đa.'),(18,'Nơi Tổ Chức Tiệc Cưới',1,'Không gian tổ chức tiệc cưới sang trọng với dịch vụ toàn diện, từ trang trí đến ẩm thực, giúp biến ngày trọng đại của bạn thành kỷ niệm đáng nhớ.'),(19,'Khu Vực Nghỉ Ngơi Ngoài Trời',1,'Khu vực nghỉ ngơi ngoài trời với ghế tắm nắng và ô che, lý tưởng để thư giãn và tận hưởng không khí trong lành của thiên nhiên.'),(20,'Khu Tắm Nắng',1,'Khu tắm nắng với ghế nằm thoải mái, nơi bạn có thể thư giãn và tận hưởng ánh nắng mặt trời trong không gian yên tĩnh và dễ chịu.');
/*!40000 ALTER TABLE `amenities_hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `amenities_type_room`
--

DROP TABLE IF EXISTS `amenities_type_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amenities_type_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amenities_type_room_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amenities_type_room`
--

LOCK TABLES `amenities_type_room` WRITE;
/*!40000 ALTER TABLE `amenities_type_room` DISABLE KEYS */;
INSERT INTO `amenities_type_room` VALUES (1,'WiFi'),(2,'Điều Hoà'),(3,'TV'),(4,'Mini Bar'),(5,'Dịch Vụ Phòng'),(6,'Bữa sáng miễn phí'),(7,'Giặt ủi'),(8,'Đưa Đón'),(9,'Bồn Tắm'),(10,'Máy  Nước Nóng'),(11,'Đồ Dùng Vệ Sinh Cao Cấp'),(12,'Tầm Nhìn Đẹp'),(13,'Gương Lớn'),(14,'Điều Hòa Hai Chiều'),(15,'Tủ Lạnh Mini'),(16,'Cửa Sổ Cách Âm'),(17,'Hệ Thống Đóng Mở Cửa Tự Động'),(18,'Khu Vực Dành Cho Người Hút Thuốc'),(19,'Máy Lọc Không Khí'),(20,'Áo Khoác Tắm');
/*!40000 ALTER TABLE `amenities_type_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_at` datetime DEFAULT NULL,
  `start_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL,
  `status_payment` bit(1) DEFAULT NULL,
  `method_payment_id` int DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  `discount_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `discount_percent` float DEFAULT NULL,
  `descriptions` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_booking_method_payment_id` (`method_payment_id`),
  KEY `FK_booking_account_id` (`account_id`),
  KEY `FK_booking_status_id` (`status_id`),
  CONSTRAINT `FK_booking_account_id` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `FK_booking_method_payment_id` FOREIGN KEY (`method_payment_id`) REFERENCES `method_payment` (`id`),
  CONSTRAINT `FK_booking_status_id` FOREIGN KEY (`status_id`) REFERENCES `status_booking` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (11,'2024-01-02 18:02:02','2024-01-25 14:00:00','2024-01-27 12:00:00',_binary '',1,9,6,NULL,NULL,'Đặt trước'),(12,'2024-01-03 19:00:02','2024-01-10 14:00:00','2024-12-11 10:12:31',_binary '',2,10,6,NULL,NULL,'Đã hủy vì: Khác'),(13,'2024-01-06 09:02:02','2024-01-08 14:00:00','2024-01-10 12:00:00',_binary '',1,9,8,NULL,NULL,'Đặt trước'),(14,'2024-01-12 09:02:02','2024-01-14 14:00:00','2024-12-11 10:12:22',_binary '',2,9,6,NULL,NULL,'Đã hủy vì: Khác'),(15,'2024-01-12 09:02:02','2024-01-15 14:00:00','2024-01-17 12:00:00',_binary '',2,10,8,NULL,NULL,'Đặt trước'),(45,'2024-01-12 09:02:02','2024-01-18 14:00:00','2024-12-11 10:12:26',_binary '',2,8,6,NULL,NULL,'Đã hủy vì: Khác'),(50,'2024-01-12 09:02:02','2024-01-20 14:00:00','2024-12-11 10:12:28',_binary '',1,7,6,NULL,NULL,'Đã hủy vì: Khác'),(51,'2024-01-14 09:02:02','2024-01-20 14:00:00','2024-12-11 10:12:44',_binary '',2,34,6,NULL,NULL,'Đã hủy vì: Khác'),(53,'2024-01-15 09:02:02','2024-01-22 14:00:00','2024-12-11 10:12:58',_binary '',1,35,6,NULL,NULL,'Đã hủy vì: Khác'),(227,'2024-01-20 13:30:00','2024-01-20 14:00:00','2024-01-22 12:00:00',_binary '',2,60,8,NULL,NULL,'Đặt trực tiếp'),(228,'2024-01-26 09:02:02','2024-02-05 14:00:00','2024-02-07 12:00:00',_binary '',2,63,8,NULL,NULL,'Đặt trước'),(229,'2024-12-11 17:16:11','2024-12-11 07:00:00','2024-12-12 05:00:00',_binary '',1,62,8,NULL,NULL,'Đặt trực tiếp'),(230,'2024-12-11 17:23:18','2024-12-11 07:00:00','2024-12-12 05:00:00',_binary '',1,62,8,NULL,NULL,'Đặt trực tiếp'),(231,'2024-12-11 17:23:25','2024-12-11 07:00:00','2024-12-12 05:00:00',_binary '',1,62,8,NULL,NULL,'Đặt trực tiếp'),(232,'2024-12-11 17:23:30','2024-12-11 07:00:00','2024-12-12 05:00:00',_binary '',1,62,8,NULL,NULL,'Đặt trực tiếp'),(233,'2024-12-12 20:00:52','2024-12-13 14:00:00','2024-12-15 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(234,'2024-12-12 20:08:43','2024-12-13 14:00:00','2024-12-20 12:00:00',_binary '',2,75,4,NULL,NULL,'Đặt trước'),(235,'2024-12-12 20:14:29','2024-12-13 14:00:00','2024-12-17 12:41:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(236,'2024-12-12 20:19:54','2024-12-13 14:00:00','2024-12-19 12:00:00',_binary '',2,75,7,NULL,NULL,'Đặt trước'),(237,'2024-12-12 20:23:11','2024-12-12 14:00:00','2024-12-13 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(238,'2024-12-12 20:30:19','2024-12-13 14:00:00','2024-12-19 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(239,'2024-12-12 20:33:44','2024-12-20 14:00:00','2024-12-25 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(240,'2024-12-12 20:38:14','2024-12-13 14:00:00','2024-12-20 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(241,'2024-12-12 21:00:45','2024-12-13 14:00:00','2024-12-15 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(242,'2024-12-12 22:01:54','2024-12-13 14:00:00','2024-12-21 12:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trước'),(243,'2024-12-13 11:20:28','2024-12-14 14:00:00','2024-12-17 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(244,'2024-12-13 11:27:55','2024-12-14 14:00:00','2024-12-20 12:00:00',_binary '\0',2,75,6,'khmd0122',15,'Đặt trước'),(245,'2024-12-13 11:28:09','2024-12-14 14:00:00','2024-12-20 12:00:00',_binary '',2,75,8,'khmd0122',15,'Đặt trước'),(246,'2024-12-13 11:29:55','2024-12-14 14:00:00','2024-12-17 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(247,'2024-12-13 11:31:31','2024-12-13 14:00:00','2024-12-14 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(248,'2024-12-13 11:35:38','2024-12-13 14:00:00','2024-12-18 12:00:00',_binary '',2,75,8,NULL,NULL,'Đặt trước'),(249,'2024-12-13 11:43:55','2024-12-27 14:00:00','2025-01-04 12:00:00',_binary '',1,75,8,NULL,NULL,'Đặt trước'),(250,'2024-12-13 11:47:57','2024-12-20 14:00:00','2024-12-27 12:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trước'),(251,'2024-12-13 11:48:13','2024-12-20 14:00:00','2024-12-27 12:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trước'),(252,'2024-12-13 11:50:40','2024-12-13 14:00:00','2024-12-19 12:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trước'),(253,'2024-12-13 12:51:04','2024-12-13 14:00:00','2024-12-15 12:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trước'),(254,'2024-12-13 13:01:01','2024-12-26 14:00:00','2024-12-27 12:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trước'),(255,'2024-12-13 13:06:59','2024-12-14 14:00:00','2024-12-19 12:00:00',_binary '\0',1,77,6,NULL,NULL,'Đặt trước'),(256,'2024-12-13 13:08:05','2024-12-13 14:00:00','2024-12-19 12:00:00',_binary '',2,77,6,NULL,NULL,'Đặt trước'),(257,'2024-12-13 15:32:25','2024-12-13 14:00:00','2024-12-14 12:00:00',_binary '\0',2,75,6,NULL,NULL,'Đặt trước'),(258,'2024-12-13 15:36:50','2024-12-13 14:00:00','2024-12-15 12:00:00',_binary '\0',2,75,6,NULL,NULL,'Đặt trước'),(259,'2024-12-13 15:44:25','2024-12-14 14:00:00','2024-12-17 12:00:00',_binary '',2,75,6,NULL,NULL,'Đặt trước'),(260,'2024-12-13 15:51:21','2024-12-13 07:00:00','2024-12-14 05:00:00',_binary '',1,62,8,NULL,NULL,'Đặt trực tiếp'),(261,'2024-12-13 16:10:13','2024-12-13 14:00:00','2024-12-19 12:00:00',_binary '\0',2,76,6,'Giảm giá tình nhân',20,'Đặt trước'),(262,'2024-12-13 16:10:44','2024-12-13 14:00:00','2024-12-14 12:00:00',_binary '\0',1,76,6,'Giảm giá tình nhân',20,'Đặt trước'),(263,'2024-12-13 17:27:32','2024-12-13 14:00:00','2024-12-21 12:00:00',_binary '',2,76,7,NULL,NULL,'Đặt trước'),(264,'2024-12-13 17:34:43','2024-12-14 14:00:00','2024-12-19 12:00:00',_binary '\0',2,75,6,NULL,NULL,'Đặt trực tuyến'),(265,'2024-12-13 17:34:58','2024-12-14 14:00:00','2024-12-19 12:00:00',_binary '',2,75,8,'Giảm giá tình nhân',20,'Đặt trực tuyến'),(266,'2024-12-13 17:40:44','2024-12-21 00:00:00','2024-12-28 00:00:00',_binary '',NULL,62,8,NULL,NULL,NULL),(267,'2024-12-13 17:40:58','2024-12-30 00:00:00','2024-12-31 00:00:00',_binary '',NULL,62,8,NULL,NULL,NULL),(268,'2024-12-13 19:20:23','2024-12-14 14:00:00','2024-12-19 12:00:00',_binary '',2,76,6,NULL,NULL,'Đặt trực tuyến'),(269,'2024-12-13 19:24:56','2024-12-13 00:00:00','2024-12-14 00:00:00',_binary '',NULL,62,8,NULL,NULL,NULL),(270,'2024-12-17 11:22:07','2024-12-14 07:00:00','2024-12-15 05:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trực tuyến'),(271,'2024-12-17 11:42:37','2024-12-20 07:00:00','2024-12-27 05:00:00',_binary '',1,75,8,NULL,NULL,'Đặt trực tuyến'),(272,'2024-12-17 12:08:36','2024-12-17 07:00:00','2024-12-20 05:00:00',_binary '',1,75,8,NULL,NULL,'Đặt trực tuyến'),(273,'2024-12-17 12:11:29','2024-12-17 07:00:00','2024-12-20 05:00:00',_binary '',2,75,6,NULL,NULL,'Đặt trực tuyến'),(274,'2024-12-17 14:44:37','2024-12-14 00:00:00','2024-12-15 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(275,'2024-12-17 15:12:30','2024-12-16 07:00:00','2024-12-17 05:00:00',_binary '\0',1,76,10,NULL,NULL,'Bảo trì'),(276,'2024-12-17 15:15:11','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,76,10,NULL,NULL,'Bảo trì'),(277,'2024-12-17 15:19:59','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,76,10,NULL,NULL,'Bảo trì'),(278,'2024-12-17 15:20:14','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,76,10,NULL,NULL,'Bảo trì'),(279,'2024-12-17 16:00:41','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,76,10,NULL,NULL,'Bảo trì'),(280,'2024-12-17 16:10:55','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,79,10,NULL,NULL,'Bảo trì'),(281,'2024-12-17 16:14:10','2024-12-18 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(282,'2024-12-17 16:22:43','2024-12-18 00:00:00','2024-12-20 00:00:00',_binary '\0',2,75,3,NULL,NULL,NULL),(283,'2024-12-17 16:23:54','2024-12-18 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(284,'2024-12-17 16:25:44','2024-12-18 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(285,'2024-12-17 16:27:18','2024-12-17 00:00:00','2024-12-20 00:00:00',_binary '\0',2,75,3,NULL,NULL,NULL),(286,'2024-12-17 16:27:21','2024-12-17 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(287,'2024-12-17 16:27:50','2024-12-17 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(288,'2024-12-17 16:35:07','2024-12-17 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,6,NULL,NULL,NULL),(289,'2024-12-17 16:39:26','2024-12-17 00:00:00','2024-12-20 00:00:00',_binary '\0',1,75,7,'Giảm giá mùa xuân',12,NULL),(290,'2024-12-17 16:46:42','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',1,75,4,'Giảm giá mùa xuân',12,NULL),(291,'2024-12-17 17:01:16','2024-12-17 07:00:00','2024-12-19 05:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trực tuyến'),(292,'2024-12-17 17:01:38','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '',2,75,6,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(293,'2024-12-17 17:14:46','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '',2,75,6,NULL,NULL,'Đặt trực tuyến'),(294,'2024-12-17 17:17:48','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',1,75,6,NULL,NULL,'Đặt trực tuyến'),(295,'2024-12-17 17:38:01','2024-12-17 07:00:00','2024-12-19 05:00:00',_binary '',2,75,6,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(296,'2024-12-17 17:45:06','2024-12-17 07:00:00','2024-12-17 11:16:00',_binary '',2,75,8,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(297,'2024-12-17 17:48:48','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,75,7,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(298,'2024-12-17 17:54:15','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',1,75,6,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(299,'2024-12-17 18:18:34','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '',2,75,7,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(300,'2024-12-17 18:29:16','2024-12-17 07:00:00','2024-12-18 05:00:00',_binary '\0',2,75,6,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(301,'2024-12-17 18:38:18','2024-12-17 07:00:00','2024-12-19 05:00:00',_binary '\0',1,79,7,NULL,NULL,'Đặt trực tiếp'),(302,'2024-12-17 19:06:58','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '',2,75,6,'Giảm giá mùa xuân',12,'Đặt trực tuyến'),(303,'2024-12-17 19:20:06','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',1,75,7,NULL,NULL,'Đặt trực tuyến'),(304,'2024-12-17 19:53:48','2024-12-17 07:00:00','2024-12-19 05:00:00',_binary '\0',1,79,4,NULL,NULL,'Đặt trực tiếp'),(305,'2024-12-17 19:57:12','2024-12-17 07:00:00','2024-12-19 05:00:00',_binary '\0',1,79,4,NULL,NULL,'Đặt trực tiếp'),(306,'2024-12-17 20:00:54','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '\0',1,79,10,NULL,NULL,'Bảo trì'),(307,'2024-12-17 23:36:21','2024-12-17 07:00:00','2024-12-19 05:00:00',_binary '',2,75,4,NULL,NULL,'Đặt trực tuyến'),(308,'2024-12-17 23:39:33','2024-12-17 07:00:00','2024-12-20 05:00:00',_binary '\0',1,75,4,NULL,NULL,'Đặt trực tuyến'),(309,'2024-12-18 04:05:47','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '',2,77,6,NULL,NULL,'Đặt trực tuyến'),(310,'2024-12-18 04:36:13','2024-12-19 07:00:00','2024-12-20 05:00:00',_binary '\0',1,77,7,NULL,NULL,'Đặt trực tuyến'),(311,'2024-12-18 04:45:17','2024-12-19 07:00:00','2024-12-20 05:00:00',_binary '\0',1,77,7,NULL,NULL,'Đặt trực tuyến'),(312,'2024-12-18 04:54:45','2024-12-19 07:00:00','2024-12-21 05:00:00',_binary '\0',1,77,7,NULL,NULL,'Đặt trực tuyến'),(313,'2024-12-18 05:07:48','2024-12-18 07:00:00','2024-12-21 05:00:00',_binary '',2,77,7,NULL,NULL,'Đặt trực tuyến'),(314,'2024-12-18 06:13:39','2024-12-18 07:00:00','2024-12-21 05:00:00',_binary '\0',1,77,7,NULL,NULL,'Đặt trực tuyến'),(315,'2024-12-18 07:05:47','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '\0',1,77,6,NULL,NULL,'Đặt trực tuyến'),(316,'2024-12-18 11:25:59','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '\0',1,77,6,NULL,NULL,'Đặt trực tuyến'),(317,'2024-12-18 14:33:55','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',2,79,6,NULL,NULL,'Đặt trực tuyến'),(318,'2024-12-18 15:21:06','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',2,75,3,'Giảm giá mùa đông',15,'Đặt trực tuyến'),(319,'2024-12-18 15:23:47','2024-12-18 07:00:00','2024-12-21 05:00:00',_binary '',2,75,7,'Giảm giá mùa đông',15,'Đặt trực tuyến'),(320,'2024-12-18 16:06:59','2024-12-18 07:00:00','2024-12-18 09:16:00',_binary '',1,75,8,NULL,NULL,'Đặt trực tuyến'),(321,'2024-12-18 16:18:43','2024-12-18 07:00:00','2024-12-18 09:19:00',_binary '',1,79,8,NULL,NULL,'Đặt trực tiếp'),(322,'2024-12-18 16:21:27','2024-12-23 07:00:00','2024-12-27 05:00:00',_binary '\0',1,79,10,NULL,NULL,'Bảo trì'),(323,'2024-12-18 16:23:24','2024-12-18 07:00:00','2024-12-18 09:25:08',_binary '\0',1,75,6,NULL,NULL,'Hủy do chưa xác nhận!'),(324,'2024-12-18 20:28:24','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '\0',1,90,6,NULL,NULL,'d'),(325,'2024-12-18 20:38:10','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '',2,90,6,NULL,NULL,'abc'),(327,'2024-12-18 22:56:18','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '',2,94,4,NULL,NULL,'Đặt trực tuyến'),(328,'2024-12-18 23:11:36','2024-12-18 07:00:00','2024-12-19 05:00:00',_binary '\0',1,94,6,NULL,NULL,'Hủy do chưa xác nhận!'),(329,'2024-12-18 23:18:02','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',1,76,6,NULL,NULL,'Hủy do chưa xác nhận!'),(330,'2024-12-18 23:20:21','2024-12-18 07:00:00','2024-12-20 05:00:00',_binary '\0',1,95,4,NULL,NULL,'Đặt trực tuyến'),(331,'2024-12-19 07:46:39','2024-12-19 07:00:00','2024-12-19 07:02:00',_binary '',2,95,8,NULL,NULL,'Đặt trực tuyến'),(332,'2024-12-19 14:02:42','2024-12-19 07:00:00','2024-12-20 05:00:00',_binary '',2,95,4,NULL,NULL,'Đặt trực tuyến'),(333,'2024-12-19 14:04:47','2024-12-19 07:00:00','2024-12-19 07:04:55',_binary '\0',1,94,6,NULL,NULL,'Đã hủy vì: Khác');
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_room`
--

DROP TABLE IF EXISTS `booking_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `check_in` datetime DEFAULT NULL,
  `check_out` datetime DEFAULT NULL,
  `price` double DEFAULT NULL,
  `booking_id` int DEFAULT NULL,
  `room_id` int DEFAULT NULL,
  `id_staff` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_booking_room_booking_id` (`booking_id`),
  KEY `FK_booking_room_room_id` (`room_id`),
  KEY `FK_booking_room_staff_id` (`id_staff`),
  CONSTRAINT `FK_booking_room_booking_id` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`),
  CONSTRAINT `FK_booking_room_room_id` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_booking_room_staff_id` FOREIGN KEY (`id_staff`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=335 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_room`
--

LOCK TABLES `booking_room` WRITE;
/*!40000 ALTER TABLE `booking_room` DISABLE KEYS */;
INSERT INTO `booking_room` VALUES (1,'2024-10-24 09:04:17','2024-10-25 09:04:17',50,11,1,NULL),(2,'2024-10-24 09:04:17','2024-10-26 09:04:17',75,12,3,NULL),(3,'2024-10-24 09:04:17','2024-12-11 03:13:00',100,13,3,NULL),(4,'2024-10-24 09:04:17','2024-10-28 09:04:17',150,14,19,NULL),(5,'2024-10-24 09:04:17','2024-12-11 03:13:00',120,15,5,NULL),(20,'2024-11-06 09:04:17','2024-11-09 09:04:17',100,45,2,NULL),(21,'2024-11-06 09:04:17','2024-11-09 09:04:17',100,45,4,NULL),(22,'2024-11-10 09:04:17','2024-11-12 09:04:17',50,50,8,NULL),(23,'2024-11-10 09:04:17','2024-11-12 09:04:17',50,50,7,NULL),(24,'2024-11-01 09:02:02','2024-11-03 09:02:02',75,51,10,NULL),(25,'2024-11-10 09:04:17','2024-11-12 09:04:17',50,50,24,NULL),(26,'2024-11-04 09:04:17','2024-11-06 09:04:17',50,53,23,NULL),(219,'2024-12-10 17:00:00','2024-12-12 21:42:00',150000,229,1,NULL),(220,'2024-12-10 17:00:00','2024-12-12 21:42:00',175000,229,5,NULL),(221,NULL,'2024-12-16 22:05:00',900000,230,6,NULL),(222,'2024-12-17 05:01:34','2024-12-16 22:04:00',1500000,231,11,NULL),(223,'2024-12-17 05:01:30','2024-12-16 22:04:00',1200000,232,12,NULL),(224,NULL,'2024-12-16 22:04:00',600000,233,1,NULL),(225,NULL,NULL,1050000,234,8,NULL),(226,NULL,NULL,1050000,234,16,NULL),(227,'2024-12-17 12:34:02','2024-12-17 12:41:00',770000,235,6,NULL),(228,'2024-12-17 10:36:23','2024-12-19 12:00:00',1500000,236,11,NULL),(229,'2024-12-17 10:36:23','2024-12-19 12:00:00',1500000,236,4,NULL),(230,NULL,'2024-12-16 22:04:00',1250000,237,18,NULL),(231,'2024-12-17 05:01:18','2024-12-16 22:04:00',1000000,238,2,NULL),(232,'2024-12-17 05:01:18','2024-12-16 22:04:00',1000000,238,13,NULL),(233,'2024-12-17 05:01:11','2024-12-16 22:04:00',-450000,239,6,NULL),(234,'2024-12-12 17:00:00','2024-12-13 03:39:00',1750000,240,15,NULL),(235,'2024-12-17 05:01:06','2024-12-16 22:03:00',480000,241,22,NULL),(236,NULL,NULL,960000,242,23,NULL),(237,NULL,NULL,960000,242,27,NULL),(238,'2024-12-17 05:01:01','2024-12-16 22:03:00',750000,243,18,NULL),(239,NULL,NULL,1050000,244,3,NULL),(240,NULL,NULL,1050000,244,5,NULL),(241,'2024-12-17 05:00:56','2024-12-16 22:03:00',525000,245,3,NULL),(242,'2024-12-17 05:00:56','2024-12-16 22:03:00',575000,245,5,NULL),(243,'2024-12-12 17:00:00','2024-12-16 22:03:00',360000,246,26,NULL),(244,'2024-12-12 17:00:00','2024-12-16 22:03:00',480000,247,27,NULL),(245,'2024-12-12 17:00:00','2024-12-13 01:57:00',600000,248,23,NULL),(246,'2024-12-12 17:00:00','2024-12-16 22:03:00',-3000000,249,33,NULL),(247,NULL,NULL,1050000,250,1,NULL),(248,NULL,NULL,1050000,251,1,NULL),(249,NULL,NULL,1800000,252,40,NULL),(250,NULL,NULL,350000,253,7,NULL),(251,NULL,NULL,350000,253,9,NULL),(252,NULL,NULL,150000,254,1,NULL),(253,NULL,NULL,150000,254,6,NULL),(254,NULL,NULL,1500000,255,29,NULL),(255,NULL,NULL,1500000,255,33,NULL),(256,NULL,NULL,1800000,256,40,NULL),(257,NULL,NULL,175000,257,7,NULL),(258,NULL,NULL,175000,257,9,NULL),(259,NULL,NULL,350000,258,7,NULL),(260,NULL,NULL,350000,258,9,NULL),(261,NULL,NULL,360000,259,32,NULL),(262,'2024-12-12 17:00:00','2024-12-13 01:51:00',175000,260,3,NULL),(263,'2024-12-12 17:00:00','2024-12-13 01:51:00',175000,260,5,NULL),(264,NULL,NULL,720000,261,36,NULL),(265,NULL,NULL,120000,262,36,NULL),(266,'2024-12-12 17:00:00','2024-12-16 22:02:00',480000,263,36,NULL),(267,NULL,NULL,875000,264,7,NULL),(268,NULL,NULL,875000,264,9,NULL),(269,'2024-12-12 17:00:00','2024-12-16 22:02:00',525000,265,7,NULL),(270,'2024-12-12 17:00:00','2024-12-16 22:02:00',525000,265,9,NULL),(271,'2024-12-12 17:00:00','2024-12-16 22:02:00',-1000000,266,15,NULL),(272,'2024-12-12 17:00:00','2024-12-13 03:41:00',250000,267,15,NULL),(273,NULL,NULL,875000,268,19,NULL),(274,'2024-12-17 05:00:52','2024-12-16 22:01:00',800000,269,10,NULL),(275,NULL,NULL,500000,270,34,NULL),(276,'2024-12-17 05:00:43','2024-12-16 22:01:00',-600000,271,10,NULL),(277,'2024-12-17 05:09:11','2024-12-16 22:09:00',300000,272,29,NULL),(278,NULL,NULL,900000,273,40,NULL),(279,NULL,NULL,200000,274,10,NULL),(280,'2024-12-16 07:00:00',NULL,NULL,275,30,76),(281,'2024-12-17 07:00:00',NULL,NULL,279,1,76),(282,'2024-12-17 07:00:00',NULL,NULL,280,40,79),(283,NULL,NULL,400000,281,12,NULL),(284,NULL,NULL,400000,284,20,NULL),(285,'2024-12-17 09:52:54','2024-12-20 00:00:00',316800,289,22,NULL),(286,NULL,NULL,220000,290,18,NULL),(287,NULL,NULL,240000,291,27,NULL),(288,NULL,NULL,400000,292,12,NULL),(289,NULL,NULL,400000,293,20,NULL),(290,NULL,NULL,400000,294,17,NULL),(291,NULL,NULL,240000,295,27,NULL),(292,'2024-12-17 10:51:21','2024-12-17 11:16:00',450000,296,33,NULL),(293,'2024-12-17 10:50:14','2024-12-18 05:00:00',175000,297,14,NULL),(294,'2024-12-17 10:55:11','2024-12-18 05:00:00',175000,298,19,NULL),(295,'2024-12-17 11:20:35','2024-12-18 05:00:00',120000,299,27,NULL),(296,NULL,NULL,120000,300,32,NULL),(297,'2024-12-17 12:31:42','2024-12-19 05:00:00',240000,301,32,79),(298,'2024-12-17 12:31:42','2024-12-19 05:00:00',1200000,301,35,79),(299,NULL,NULL,240000,302,26,NULL),(300,NULL,NULL,600000,303,33,NULL),(301,NULL,NULL,400000,304,10,79),(302,NULL,NULL,350000,305,19,79),(303,'2024-12-18 07:00:00',NULL,NULL,306,1,79),(304,NULL,NULL,1200000,307,37,NULL),(305,'2024-12-17 22:04:37','2024-12-20 05:00:00',1800000,308,38,NULL),(306,NULL,NULL,150000,309,6,NULL),(307,'2024-12-19 07:01:22','2024-12-20 05:00:00',120000,310,23,NULL),(308,'2024-12-19 07:00:32','2024-12-20 05:00:00',120000,311,26,NULL),(309,NULL,NULL,240000,312,27,NULL),(310,'2024-12-18 07:01:51','2024-12-21 05:00:00',900000,313,33,NULL),(311,'2024-12-18 07:07:54','2024-12-21 05:00:00',600000,314,12,NULL),(312,NULL,NULL,150000,315,6,NULL),(313,NULL,NULL,250000,316,21,NULL),(314,NULL,NULL,400000,317,20,NULL),(315,NULL,NULL,400000,317,17,NULL),(316,NULL,NULL,900000,318,24,NULL),(317,NULL,NULL,900000,318,25,NULL),(318,'2024-12-18 08:26:43','2024-12-21 05:00:00',600000,319,17,NULL),(319,'2024-12-18 08:26:43','2024-12-21 05:00:00',1500000,319,34,NULL),(320,'2024-12-18 09:15:43','2024-12-18 09:16:00',470000,320,28,NULL),(321,'2024-12-18 09:15:43','2024-12-18 09:16:00',550000,320,30,NULL),(322,'2024-12-18 09:19:19','2024-12-18 09:19:00',200000,321,20,79),(323,'2024-12-23 07:00:00',NULL,NULL,322,2,79),(324,NULL,NULL,500000,323,21,NULL),(325,NULL,NULL,150000,324,6,NULL),(326,NULL,NULL,500000,325,21,NULL),(328,NULL,NULL,150000,327,6,NULL),(329,NULL,NULL,250000,328,21,NULL),(330,NULL,NULL,1000000,329,31,NULL),(331,NULL,NULL,1000000,330,39,NULL),(332,'2024-12-19 07:01:24','2024-12-19 07:02:00',450000,331,40,NULL),(333,NULL,NULL,200000,332,20,NULL),(334,NULL,NULL,500000,333,31,94);
/*!40000 ALTER TABLE `booking_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_room_customer_information`
--

DROP TABLE IF EXISTS `booking_room_customer_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_room_customer_information` (
  `id` int NOT NULL AUTO_INCREMENT,
  `booking_room_id` int DEFAULT NULL,
  `customer_information_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_booking_room_customer_information_booking_room_id` (`booking_room_id`),
  KEY `FK_booking_room_customer_information_customer_information_id` (`customer_information_id`),
  CONSTRAINT `FK_booking_room_customer_information_booking_room_id` FOREIGN KEY (`booking_room_id`) REFERENCES `booking_room` (`id`),
  CONSTRAINT `FK_booking_room_customer_information_customer_information_id` FOREIGN KEY (`customer_information_id`) REFERENCES `customer_information` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_room_customer_information`
--

LOCK TABLES `booking_room_customer_information` WRITE;
/*!40000 ALTER TABLE `booking_room_customer_information` DISABLE KEYS */;
INSERT INTO `booking_room_customer_information` VALUES (1,1,1),(2,2,2),(3,3,3),(4,4,4),(5,5,5),(6,243,6),(7,270,7),(8,270,8),(9,322,9);
/*!40000 ALTER TABLE `booking_room_customer_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_room_service_hotel`
--

DROP TABLE IF EXISTS `booking_room_service_hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_room_service_hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_at` datetime DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `booking_room_id` int DEFAULT NULL,
  `service_hotel_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_booking_room_service_hotel_booking_room_id` (`booking_room_id`),
  KEY `FK_booking_room_service_hotel_service_hotel_id` (`service_hotel_id`),
  CONSTRAINT `FK_booking_room_service_hotel_booking_room_id` FOREIGN KEY (`booking_room_id`) REFERENCES `booking_room` (`id`),
  CONSTRAINT `FK_booking_room_service_hotel_service_hotel_id` FOREIGN KEY (`service_hotel_id`) REFERENCES `service_hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_room_service_hotel`
--

LOCK TABLES `booking_room_service_hotel` WRITE;
/*!40000 ALTER TABLE `booking_room_service_hotel` DISABLE KEYS */;
INSERT INTO `booking_room_service_hotel` VALUES (1,'2024-10-24 09:04:55',15,1,1,1),(2,'2024-10-24 09:04:55',5,1,2,2),(3,'2024-10-24 09:04:55',40,1,3,3),(4,'2024-10-24 09:04:55',10,1,4,4),(5,'2024-10-24 09:04:55',25,1,5,5);
/*!40000 ALTER TABLE `booking_room_service_hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_room_service_room`
--

DROP TABLE IF EXISTS `booking_room_service_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_room_service_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_at` datetime DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `booking_room_id` int DEFAULT NULL,
  `service_room_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_booking_room_service_room_booking_room_id` (`booking_room_id`),
  KEY `FK_booking_room_service_room_service_room_id` (`service_room_id`),
  CONSTRAINT `FK_booking_room_service_room_booking_room_id` FOREIGN KEY (`booking_room_id`) REFERENCES `booking_room` (`id`),
  CONSTRAINT `FK_booking_room_service_room_service_room_id` FOREIGN KEY (`service_room_id`) REFERENCES `service_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_room_service_room`
--

LOCK TABLES `booking_room_service_room` WRITE;
/*!40000 ALTER TABLE `booking_room_service_room` DISABLE KEYS */;
INSERT INTO `booking_room_service_room` VALUES (1,'2024-10-24 09:04:48',20,1,1,1),(2,'2024-10-24 09:04:48',15,1,2,2),(3,'2024-10-24 09:04:48',10,1,3,3),(4,'2024-10-24 09:04:48',25,1,4,4),(5,'2024-10-24 09:04:48',50,1,5,5),(6,'2024-12-11 10:22:59',20000,8,219,20),(7,'2024-12-13 08:52:25',18000,1,262,13),(8,'2024-12-13 08:52:25',30000,2,262,14),(9,'2024-12-13 08:52:25',150000,1,262,4),(10,'2024-12-13 08:52:25',20000,2,262,8),(11,'2024-12-13 08:52:53',20000,2,242,2),(12,'2024-12-13 08:52:53',10000,1,242,7),(13,'2024-12-13 08:52:53',20000,1,242,8),(14,'2024-12-17 11:10:31',150000,1,294,4),(15,'2024-12-17 11:11:13',150000,1,292,4),(16,'2024-12-17 11:20:57',30000,1,295,19),(17,'2024-12-17 12:36:09',30000,4,227,14),(18,'2024-12-17 12:36:09',25000,2,227,11),(19,'2024-12-18 09:16:26',20000,4,320,2),(20,'2024-12-18 09:16:47',20000,1,321,8),(21,'2024-12-18 09:16:47',30000,1,321,19),(22,'2024-12-19 07:02:10',150000,1,332,4);
/*!40000 ALTER TABLE `booking_room_service_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_information`
--

DROP TABLE IF EXISTS `customer_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_information` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cccd` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `fullname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `phone` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `gender` bit(1) DEFAULT b'1',
  `birthday` datetime DEFAULT NULL,
  `img_first_card` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `img_last_card` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_information`
--

LOCK TABLES `customer_information` WRITE;
/*!40000 ALTER TABLE `customer_information` DISABLE KEYS */;
INSERT INTO `customer_information` VALUES (1,'123456789','Nguyễn Văn A','0123456789',_binary '','1990-01-01 00:00:00','front.jpg','back.jpg'),(2,'987654321','Trần Thị B','0987654321',_binary '\0','1992-02-02 00:00:00','front2.jpg','back2.jpg'),(3,'555555555','Lê Văn C','1234567890',_binary '','1985-03-03 00:00:00','front3.jpg','back3.jpg'),(4,'666666666','Phạm Văn D','2345678901',_binary '\0','1980-04-04 00:00:00','front4.jpg','back4.jpg'),(5,'777777777','Ngô Văn E','3456789012',_binary '','1975-05-05 00:00:00','front5.jpg','back5.jpg'),(6,'09876567554','Lê Minh Khôi','0987654321',_binary '','2003-11-18 17:00:00',NULL,NULL),(7,'09876567534','Lê Minh Khôi','0987654323',_binary '','2024-12-08 17:00:00',NULL,NULL),(8,'09876567532','Lê Minh Khôi','0987654326',_binary '','2003-11-18 17:00:00',NULL,NULL),(9,'09876517534','Lê Minh Khôi Lỏ','0987652321',_binary '','2024-12-17 17:00:00',NULL,NULL);
/*!40000 ALTER TABLE `customer_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount` (
  `id` int NOT NULL AUTO_INCREMENT,
  `discount_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `percent` double DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `type_room_id` int DEFAULT NULL,
  `status` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `FK_discount_type_room_id` (`type_room_id`),
  CONSTRAINT `FK_discount_type_room_id` FOREIGN KEY (`type_room_id`) REFERENCES `type_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount`
--

LOCK TABLES `discount` WRITE;
/*!40000 ALTER TABLE `discount` DISABLE KEYS */;
INSERT INTO `discount` VALUES (14,'Giảm giá mùa xuân',12,'2024-12-17 00:00:00','2024-12-17 23:59:59',NULL,_binary ''),(15,'Giảm giá mùa đông',15,'2024-12-18 00:00:00','2024-12-19 23:59:59',NULL,_binary '');
/*!40000 ALTER TABLE `discount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discount_account`
--

DROP TABLE IF EXISTS `discount_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discount_account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `discount_id` int DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  `status_da` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_discount_account_da` (`discount_id`),
  KEY `fk_discount_account_ad` (`account_id`),
  CONSTRAINT `fk_discount_account_ad` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`),
  CONSTRAINT `fk_discount_account_da` FOREIGN KEY (`discount_id`) REFERENCES `discount` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discount_account`
--

LOCK TABLES `discount_account` WRITE;
/*!40000 ALTER TABLE `discount_account` DISABLE KEYS */;
INSERT INTO `discount_account` VALUES (16,14,75,_binary '\0'),(17,15,79,_binary '\0'),(18,15,75,_binary ''),(19,15,90,_binary '\0');
/*!40000 ALTER TABLE `discount_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `stars` int DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `rating_status` bit(1) DEFAULT b'1',
  `invoice_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_feedback_invoice_id` (`invoice_id`),
  CONSTRAINT `FK_feedback_invoice_id` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (6,'Kỳ nghỉ tuyệt vời!',5,'2024-10-24 09:03:34',_binary '',6),(7,'Trải nghiệm trung bình.',3,'2024-10-24 09:03:34',_binary '',7),(8,'Không xứng đáng với giá tiền!',2,'2024-10-24 09:03:34',_binary '',8),(9,'Sẽ quay lại!',4,'2024-10-24 09:03:34',_binary '',9),(10,'Dịch vụ xuất sắc!',5,'2024-10-24 09:03:34',_binary '',10),(12,'Quá tuyệt vời',5,'2024-10-24 09:03:34',_binary '',11),(13,'Nào rãnh lại',4,'2024-10-24 09:03:34',_binary '',12),(14,'Cảnh quan ở đây quá tuyệt vời',5,'2024-10-24 09:03:34',_binary '',13),(15,'Không khác những khách sạn khác',3,'2024-10-24 09:03:34',_binary '',14),(21,'abc',4,'2024-12-18 09:38:33',_binary '',49);
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `floors`
--

DROP TABLE IF EXISTS `floors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `floors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `floor_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `floors`
--

LOCK TABLES `floors` WRITE;
/*!40000 ALTER TABLE `floors` DISABLE KEYS */;
INSERT INTO `floors` VALUES (1,'Tầng 1'),(2,'Tầng 2'),(3,'Tầng 3'),(4,'Tầng 4'),(5,'Tầng 5');
/*!40000 ALTER TABLE `floors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hotel_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `descriptions` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `district` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `ward` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `hotel_phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,'Khách sạn Grand Plaza','Khách sạn sang trọng với đầy đủ tiện nghi.','Hà Nội','Cầu Giấy','Mỹ Đình','123 Mỹ Đình','1900 6522');
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel_image`
--

DROP TABLE IF EXISTS `hotel_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `hotel_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hotel_image_hotel_id` (`hotel_id`),
  CONSTRAINT `FK_hotel_image_hotel_id` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel_image`
--

LOCK TABLES `hotel_image` WRITE;
/*!40000 ALTER TABLE `hotel_image` DISABLE KEYS */;
INSERT INTO `hotel_image` VALUES (3,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fea8bcfc6-3b14-4fb3-986c-3139eaf203a5?alt=media&token=6de6714c-ba42-46f1-bd9a-db89015cc12f',1),(4,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',1),(5,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fc82b9920-8be0-411d-89eb-2bef6942e1af?alt=media&token=d62f6276-58b1-4a9e-a6aa-46e0282e0d71',1),(6,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fab81795f-9416-4a79-871a-70bb99a55421?alt=media&token=5e1aa923-8a49-42f9-a0c7-8bd1ad1870ef',1),(7,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F4edf821b-449a-4c45-b961-20e2ebba3e83?alt=media&token=66fa71e8-51ae-4c15-811e-de482334fe57',1),(8,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F2126ce2a-0252-48da-b2bc-1d4e67287295?alt=media&token=351e4b01-7313-45c6-9f0a-0a89eff2bb5b',1),(9,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F72e181f4-b390-43a0-96d0-8c8c4241ca95?alt=media&token=98eb7169-514a-40c5-babb-12095ba52fac',1),(10,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F4bee420e-fa05-4a51-b6a9-d110a3e58453?alt=media&token=3faf8667-5f48-49e9-af96-bcdf8ef92c6a',1);
/*!40000 ALTER TABLE `hotel_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `create_at` datetime DEFAULT NULL,
  `invoice_status` bit(1) DEFAULT b'0',
  `total_amount` double DEFAULT NULL,
  `booking_id` int DEFAULT NULL,
  `method_payments` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_invoice_booking_id` (`booking_id`),
  CONSTRAINT `FK_invoice_booking_id` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (6,'2024-10-24 09:02:52',_binary '\0',500000,11,NULL),(7,'2024-10-24 09:02:52',_binary '',750000,12,NULL),(8,'2024-10-24 09:02:52',_binary '\0',1000000,13,NULL),(9,'2024-10-24 09:02:52',_binary '',150000,14,NULL),(10,'2024-10-24 09:02:52',_binary '\0',120000,15,NULL),(11,'2024-11-09 09:02:02',_binary '',2000000,45,NULL),(12,'2024-11-10 09:02:02',_binary '',500000,50,NULL),(13,'2024-11-12 09:02:02',_binary '\0',220000,51,NULL),(14,'2024-11-05 09:02:02',_binary '\0',1000000,53,NULL),(21,'2024-12-11 03:13:00',_binary '',8575050,15,NULL),(22,'2024-12-11 03:13:00',_binary '',8575010,13,NULL),(23,'2024-12-12 21:42:00',_binary '',1135000,229,NULL),(24,'2024-12-13 01:51:00',_binary '',618000,260,NULL),(25,'2024-12-13 01:57:00',_binary '',120000,248,NULL),(26,'2024-12-13 03:39:00',_binary '',250000,240,NULL),(27,'2024-12-13 03:41:00',_binary '',250000,267,NULL),(28,'2024-12-16 22:01:00',_binary '',200000,271,NULL),(29,'2024-12-16 22:01:00',_binary '',200000,269,NULL),(30,'2024-12-16 22:02:00',_binary '',1250000,266,NULL),(31,'2024-12-16 22:02:00',_binary '',1750000,265,NULL),(32,'2024-12-16 22:02:00',_binary '',600000,263,NULL),(33,'2024-12-16 22:03:00',_binary '',1500000,249,NULL),(34,'2024-12-16 22:03:00',_binary '',600000,247,NULL),(35,'2024-12-16 22:03:00',_binary '',600000,246,NULL),(36,'2024-12-16 22:03:00',_binary '',250000,243,NULL),(37,'2024-12-16 22:03:00',_binary '',420000,245,NULL),(38,'2024-12-16 22:03:00',_binary '',120000,241,NULL),(39,'2024-12-16 22:04:00',_binary '',250000,231,NULL),(40,'2024-12-16 22:04:00',_binary '',150000,239,NULL),(41,'2024-12-16 22:04:00',_binary '',0,237,NULL),(42,'2024-12-16 22:04:00',_binary '',500000,238,NULL),(43,'2024-12-16 22:04:00',_binary '',200000,232,NULL),(44,'2024-12-16 22:04:00',_binary '',0,233,NULL),(45,'2024-12-16 22:05:00',_binary '',0,230,NULL),(46,'2024-12-16 22:09:00',_binary '',300000,272,NULL),(47,'2024-12-17 11:16:00',_binary '',414000,296,NULL),(48,'2024-12-17 12:41:00',_binary '',320000,235,NULL),(49,'2024-12-18 09:16:00',_binary '',1020000,320,NULL),(50,'2024-12-18 09:19:00',_binary '',200000,321,NULL),(51,'2024-12-18 09:19:00',_binary '',200000,321,NULL),(52,'2024-12-19 07:02:00',_binary '',450000,331,NULL);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `method_payment`
--

DROP TABLE IF EXISTS `method_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `method_payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `method_payment_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `method_payment`
--

LOCK TABLES `method_payment` WRITE;
/*!40000 ALTER TABLE `method_payment` DISABLE KEYS */;
INSERT INTO `method_payment` VALUES (1,'Tiền mặt'),(2,'VN pay');
/*!40000 ALTER TABLE `method_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'HotelOwner'),(2,'Staff'),(3,'Customer');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `floor_id` int DEFAULT NULL,
  `type_room_id` int DEFAULT NULL,
  `status_room_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_room_floor_id` (`floor_id`),
  KEY `FK_room_type_room_id` (`type_room_id`),
  KEY `FK_room_status_room_id` (`status_room_id`),
  CONSTRAINT `FK_room_floor_id` FOREIGN KEY (`floor_id`) REFERENCES `floors` (`id`),
  CONSTRAINT `FK_room_status_room_id` FOREIGN KEY (`status_room_id`) REFERENCES `status_room` (`id`),
  CONSTRAINT `FK_room_type_room_id` FOREIGN KEY (`type_room_id`) REFERENCES `type_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,'Phòng 101',1,1,1),(2,'Phòng 102',1,2,5),(3,'Phòng 103',1,3,5),(4,'Phòng 104',1,2,2),(5,'Phòng 105',1,3,5),(6,'Phòng 106',1,1,5),(7,'Phòng 107',1,3,5),(8,'Phòng 108',1,1,4),(9,'Phòng 201',2,3,5),(10,'Phòng 202',2,4,5),(11,'Phòng 203',2,2,2),(12,'Phòng 204',2,4,2),(13,'Phòng 205',2,2,5),(14,'Phòng 206',2,3,1),(15,'Phòng 207',2,2,5),(16,'Phòng 208',2,1,4),(17,'Phòng 301',3,4,2),(18,'Phòng 302',3,2,5),(19,'Phòng 303',3,3,2),(20,'Phòng 304',3,4,4),(21,'Phòng 305',3,5,1),(22,'Phòng 306',3,6,2),(23,'Phòng 307',3,6,2),(24,'Phòng 308',3,7,1),(25,'Phòng 401',4,7,1),(26,'Phòng 402',4,6,2),(27,'Phòng 403',4,6,2),(28,'Phòng 404',4,7,5),(29,'Phòng 405',4,9,5),(30,'Phòng 406',4,8,5),(31,'Phòng 407',4,8,1),(32,'Phòng 408',4,10,2),(33,'Phòng 501',5,9,2),(34,'Phòng 502',5,8,2),(35,'Phòng 503',5,11,1),(36,'Phòng 504',5,10,1),(37,'Phòng 505',5,11,1),(38,'Phòng 506',5,11,1),(39,'Phòng 507',5,8,1),(40,'Phòng 508',5,9,5);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_hotel`
--

DROP TABLE IF EXISTS `service_hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_hotel_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `hotel_id` int DEFAULT NULL,
  `descriptions` varchar(225) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_service_hotel_hotel_id` (`hotel_id`),
  CONSTRAINT `fk_service_hotel_hotel_id` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_hotel`
--

LOCK TABLES `service_hotel` WRITE;
/*!40000 ALTER TABLE `service_hotel` DISABLE KEYS */;
INSERT INTO `service_hotel` VALUES (1,'Giao Thức Ăn',300000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1734539526744_UseCaseHotel-C%C6%A0%20S%E1%BB%9E%20D%E1%BB%AE%20LI%E1%BB%86U.jpg?alt=media&token=b8b4fd0e-7978-43fd-9440-f96b2ccf0659',1,'Dịch vụ giao thức ăn tận phòng, mang đến cho bạn sự tiện lợi và thoải mái khi thưởng thức những món ăn ngon ngay trong không gian riêng tư của bạn. Chúng tôi cung cấp thực đơn đa dạng từ các món ăn địa phương đến quốc tế, với'),(2,'Giặt Ủi',150000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F56eae4d0-a288-44fc-abdc-8f2d1464ad8a?alt=media&token=6f4a08cd-b4eb-44c8-b8f1-010d8c9a5b33',1,'Dịch vụ giặt ủi nhanh chóng và chất lượng, giúp bạn luôn có trang phục sạch sẽ trong suốt kỳ nghỉ.'),(3,'Tham Quan Có Hướng Dẫn',450000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fac31a087-eef8-4f19-bcf1-d95a692b0169?alt=media&token=04090c60-2bb3-4c51-9385-2722c3c83534',1,'Dịch vụ tham quan có hướng dẫn viên chuyên nghiệp, giúp bạn khám phá các địa điểm nổi bật và hiểu rõ hơn về văn hóa, lịch sử của khu vực. Chúng tôi cung cấp các tour du lịch linh hoạt, từ tham quan di tích lịch sử đến các hoạ'),(4,'Đỗ Xe',40000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fea1b5e4f-b280-4d62-8249-4fa48c112b25?alt=media&token=fda67fc9-f9ef-4dda-a569-a6c0d8e032b9',1,'Dịch vụ đỗ xe an toàn và tiện lợi tại khách sạn, giúp bạn yên tâm khi di chuyển. Chúng tôi cung cấp chỗ đậu xe rộng rãi và bảo mật, với nhân viên hỗ trợ sẵn sàng phục vụ 24/7, đảm bảo xe của bạn luôn được chăm sóc và bảo vệ.'),(5,'Xe Đưa Đón',300000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fff73c37f-393d-4568-a3b3-367ce87ded61?alt=media&token=ba8da9f3-9756-4b29-8b50-212885f12d14',1,'Dịch vụ đưa đón tận nơi với xe hơi tiện nghi, giúp bạn dễ dàng di chuyển từ và đến sân bay hoặc các điểm tham quan nổi tiếng. Đội ngũ lái xe chuyên nghiệp, thân thiện và am hiểu địa phương sẽ đảm bảo chuyến đi của bạn thoải m'),(6,'Truyền Hình Cáp',50000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F0508b500-a5ce-40c2-8411-6eecf94f0874?alt=media&token=d11f3d04-f129-4067-a09b-f47e303c8d71',1,'Dịch vụ truyền hình cáp với nhiều kênh giải trí và thông tin, giúp bạn thư giãn và giải trí ngay trong phòng nghỉ của mình.'),(7,'Chăm Sóc Trẻ Em',250000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F23e290be-4011-4f23-b301-74a16be01d42?alt=media&token=e905ead7-4363-4716-b7cd-d109673393df',1,'Dịch vụ chăm sóc trẻ em chuyên nghiệp, giúp bạn yên tâm tận hưởng kỳ nghỉ trong khi trẻ nhỏ được chăm sóc và vui chơi an toàn.'),(8,'Cưỡi Ngựa',700000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F0a6abb4e-7f56-4b19-a69d-342390f4b3dd?alt=media&token=4f7d706d-b54b-4e89-b4d7-33bccdddcfbf',1,'Dịch vụ cưỡi ngựa thú vị, cho phép bạn khám phá cảnh quan xung quanh trong một trải nghiệm độc đáo và đầy thú vị.'),(9,'Chăm Sóc Thú Cưng',200000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fa4bbf87f-ae61-4be5-98bf-d371755056b8?alt=media&token=7a92cb43-f709-490b-8097-82e67b5a45b3',1,'Dịch vụ chăm sóc thú cưng tại khách sạn, đảm bảo thú cưng của bạn được chăm sóc tốt trong khi bạn đi du lịch.'),(10,'Tổ Chức Sự Kiện',1000000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Feae60034-2c30-45aa-bba8-b47c33b2b661?alt=media&token=8d99c574-60d1-432c-8dcc-770d37b3953f',1,'Dịch vụ tổ chức sự kiện với đầy đủ trang thiết bị và dịch vụ hỗ trợ, giúp bạn tổ chức các buổi tiệc, hội thảo hay sự kiện đặc biệt.'),(11,'Bơi Lội Dưới Nước',400000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb6551f57-20e4-44a5-a780-c7d8a805251b?alt=media&token=d848abb0-5c48-4481-85ce-d6c8b059bb18',1,'Dịch vụ bơi lội dưới nước với trang bị đầy đủ và hướng dẫn viên, mang đến cho bạn trải nghiệm thú vị khi khám phá thế giới dưới nước.'),(12,'Dịch Vụ Chơi Game',100000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fe35d75fc-5e2f-49b5-b9ea-3be7872761e7?alt=media&token=b95e8ee5-5fad-422a-b129-aad3045d12ac',1,'Dịch vụ cho thuê thiết bị chơi game, với nhiều trò chơi đa dạng, mang đến cho bạn những giờ phút giải trí thư giãn tại khách sạn.'),(13,'Đưa Đón Bằng Thuyền',800000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Ff67af6b1-937d-4330-8687-4d3c70a9195f?alt=media&token=c3ea5d48-3cc3-404d-bd8c-29148867598f',1,'Dịch vụ đưa đón bằng thuyền đến các đảo và bãi biển xung quanh, mang đến cho bạn trải nghiệm thú vị và phong cảnh tuyệt đẹp.'),(14,'Dịch Vụ Chụp Ảnh',800000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1bc68408-6bb9-4d0f-a766-8aeb5d775486?alt=media&token=5f67d505-1a62-484c-906d-3cde032cffeb',1,'Dịch vụ chụp ảnh chuyên nghiệp để ghi lại những khoảnh khắc đáng nhớ trong kỳ nghỉ của bạn, với các nhiếp ảnh gia tài năng và ý tưởng sáng tạo.'),(15,'Tổ Chức Tiệc Sinh Nhật',1000000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fc3861585-f2a9-4360-869a-8dfa12a6ae69?alt=media&token=19592724-02d0-4ba5-bd56-15b8b64d276b',1,'Dịch vụ tổ chức tiệc sinh nhật trọn gói, bao gồm trang trí, ẩm thực và giải trí, giúp bạn tạo ra những kỷ niệm đẹp bên gia đình và bạn bè.'),(16,'Du Thuyền Ngắm Hoàng Hôn',1500000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Ff67af6b1-937d-4330-8687-4d3c70a9195f?alt=media&token=c3ea5d48-3cc3-404d-bd8c-29148867598f',1,'Dịch vụ du thuyền ngắm hoàng hôn trên biển, mang đến cho bạn trải nghiệm lãng mạn và không gian yên bình tuyệt đẹp.'),(17,'Dịch Vụ Tắm Bùn',600000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fe781b0c7-cc86-4fb2-812b-3322f8f5e829?alt=media&token=9e10ea9b-1588-464b-8113-fb4cbf6863e3',1,'Dịch vụ tắm bùn thư giãn và làm đẹp, giúp bạn cải thiện sức khỏe và làn da, mang lại cảm giác thoải mái và trẻ trung.'),(18,'Dịch Vụ Nghe Nhạc Trực Tiếp',600000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F5ce2a752-a14a-4d70-90e2-14c9a36d89e2?alt=media&token=3e75cabd-70d8-4bcb-a449-d5d10fa3c6fb',1,'Dịch vụ thưởng thức âm nhạc trực tiếp với các nghệ sĩ địa phương, mang đến cho bạn trải nghiệm văn hóa âm nhạc phong phú và sống động.'),(19,'Giảm giá',100,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1734539601295_UseCaseHotel-T%E1%BB%95ng%20qu%C3%A1t.drawio%20(1).png?alt=media&token=08200fe4-5d87-4425-b9f1-753d74ffc967',1,NULL);
/*!40000 ALTER TABLE `service_hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_package`
--

DROP TABLE IF EXISTS `service_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_package` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_package_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_package`
--

LOCK TABLES `service_package` WRITE;
/*!40000 ALTER TABLE `service_package` DISABLE KEYS */;
INSERT INTO `service_package` VALUES (1,'Gói Ăn Sáng',100000),(2,'Bữa Tối Lãng Mạn',500000),(3,'Gói Spa',700000),(4,'Chuyến Tham Quan Thành Phố',300000),(5,'Đón Sân Bay',200000),(6,'Gói Dịch Vụ Thư Giãn',1500000),(7,'Gói Dịch Vụ Thể Thao Mạo Hiểm',1800000),(8,'Gói Dịch Vụ Cặp Đôi Lãng Mạn',1200000),(9,'Gói Dịch Vụ Tham Quan Địa Phương',2500000),(10,'Gói Dịch Vụ Nghỉ Dưỡng Cuối Tuần',3200000),(11,'Gói Dịch Vụ Thể Thao Nước',2400000),(12,'Gói Dịch Vụ Lễ Hội Âm Nhạc',2200000),(13,'Gói Dịch Vụ Khám Phá Ẩm Thực',1900000),(14,'Gói Dịch Vụ Lặn Biển',3000000),(15,'Gói Dịch Vụ Tham Quan Thiên Nhiên',2200000),(16,'Gói Dịch Vụ Tiệc BBQ Ngoài Trời',2900000),(17,'Gói Dịch Vụ Khám Phá Biển Đảo',5000000),(18,'Gói Dịch Vụ Lễ Hội Văn Hóa',2400000),(19,'Gói Dịch Vụ Tham Quan Các Khu Di Tích',2600000);
/*!40000 ALTER TABLE `service_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_room`
--

DROP TABLE IF EXISTS `service_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_room_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `type_service_room_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_type_service_room_` (`type_service_room_id`),
  CONSTRAINT `FK_type_service_room_` FOREIGN KEY (`type_service_room_id`) REFERENCES `type_service_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_room`
--

LOCK TABLES `service_room` WRITE;
/*!40000 ALTER TABLE `service_room` DISABLE KEYS */;
INSERT INTO `service_room` VALUES (1,'Nước ngọt',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fdd5c1bf7-400c-4d15-8236-0a931337d11a?alt=media&token=ea2e6cba-deb3-473e-9851-0d84891a62f9',2),(2,'mì ly',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcb539b61-6fbb-433a-b7fd-5facf77700c8?alt=media&token=f729c95d-0050-4e1b-9601-04b12aca41fe',1),(3,'thay ga nệm, gói',0,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F928b4cc9-1fd1-4b32-9b4d-10d91a5752f8?alt=media&token=eb6c5c47-74aa-4f85-9c3e-9712e3fd2a32',3),(4,'Bữa ăn tối',150000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F7c1122af-c70c-4394-9fb5-6f30020a777a?alt=media&token=212fc114-1fe2-4e3e-8210-a6b21975aa02',1),(5,'Trà sữa',45000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F51dc81c3-de4e-4fa4-87b1-be2a86c13800?alt=media&token=2b02fe30-778d-45d9-9599-84e2892b3078',2),(6,'Nước Ép',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb325b44f-9c4a-46a6-ae94-9bca5a3bc309?alt=media&token=9d5127be-8625-473a-a1bc-01382f2ca894',2),(7,'Kẹo Ngọt',10000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F572e949c-fa6f-4cbf-a10a-6345d8e48b3c?alt=media&token=29c51371-c8e8-40ca-a4b1-394e0693cb38',1),(8,'Hạt Dinh Dưỡng',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fa90b0861-7b43-464b-b6ca-1baae14e7490?alt=media&token=c07997d3-3d56-4b90-83ea-407063c8a80e',1),(9,'Bữa Ăn Trưa',150000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F7c1122af-c70c-4394-9fb5-6f30020a777a?alt=media&token=212fc114-1fe2-4e3e-8210-a6b21975aa02',1),(10,'Bỏng Ngô',12000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F97355eef-5863-4027-b423-d257b188101f?alt=media&token=00f0cd15-5a17-4e43-852e-9bcb48b3924b',1),(11,'Trà',25000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Febbea4af-04f4-4d01-a8b3-4a07a188489f?alt=media&token=c9c36e4b-ce85-498d-8276-018573ace24c',2),(12,'Cà Phê',30000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Ffcd8a3d8-21f4-441a-9e9f-4b239c271afa?alt=media&token=7c2e85d7-2c60-4c02-827f-f80237b1b8f7',2),(13,'Trái Cây Tươi',18000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F9461dcbd-9e47-4d33-b5a4-e4ff286847d1?alt=media&token=b95bcd51-ead6-433a-8d6d-984984b64e7d',1),(14,'Sôcôla',30000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F1a84b172-3cdb-4b85-a4b3-d76f4fc4771a?alt=media&token=af7d2ed7-68a9-4f0c-aa0e-6b4a7688c26a',1),(15,'Nước Uống Đóng Chai',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F2d290eed-e23b-4a42-b730-ad69aa23a65d?alt=media&token=88fb0664-50dc-42a7-b163-3bf2c9aab410',2),(16,'Sữa Tươi',25000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F39bcfbb5-a101-4338-a614-4fe3c565acf9?alt=media&token=4e41c84c-e720-483c-9edd-cb1080964da3',1),(17,'Salad Tươi',40000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fbe4bc2f8-9a13-467e-9afc-e2a4c6ea8bd5?alt=media&token=78fcec90-76fb-468b-99e9-54c39cf1f1af',1),(18,'Kem',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb3b3b79a-bacf-4df4-b411-9fb1f9843614?alt=media&token=38e0d91c-52ed-47e0-8186-8992050d023d',1),(19,'Trái Cây Sấy',30000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fa90b0861-7b43-464b-b6ca-1baae14e7490?alt=media&token=c07997d3-3d56-4b90-83ea-407063c8a80e',1),(20,'Nước Ngọt Trái Cây',20000,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fdd5c1bf7-400c-4d15-8236-0a931337d11a?alt=media&token=ea2e6cba-deb3-473e-9851-0d84891a62f9',2);
/*!40000 ALTER TABLE `service_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_booking`
--

DROP TABLE IF EXISTS `status_booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_booking` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status_booking_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_booking`
--

LOCK TABLES `status_booking` WRITE;
/*!40000 ALTER TABLE `status_booking` DISABLE KEYS */;
INSERT INTO `status_booking` VALUES (1,'đang xử lý'),(2,'khách hàng đã xác nhận'),(3,'chờ xác nhận'),(4,'đã đặt trước'),(5,'quá hạn trả'),(6,'Đã hủy'),(7,'trả phòng'),(8,' hoàn thành'),(9,'quá hạn nhận phòng'),(10,'bảo trì');
/*!40000 ALTER TABLE `status_booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_room`
--

DROP TABLE IF EXISTS `status_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status_room_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_room`
--

LOCK TABLES `status_room` WRITE;
/*!40000 ALTER TABLE `status_room` DISABLE KEYS */;
INSERT INTO `status_room` VALUES (1,'phòng trống'),(2,'phòng có khách'),(3,'Bảo Trì'),(4,'sạch'),(5,'Chưa dọn');
/*!40000 ALTER TABLE `status_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_bed`
--

DROP TABLE IF EXISTS `type_bed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_bed` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bed_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_bed`
--

LOCK TABLES `type_bed` WRITE;
/*!40000 ALTER TABLE `type_bed` DISABLE KEYS */;
INSERT INTO `type_bed` VALUES (1,'Giường Đơn'),(2,'Giường Đôi'),(3,'Giường Ngủ Thoải Mái'),(4,'Giường King'),(5,'Giường Queen'),(6,'Giường Lớn Cho Gia Đình'),(7,'Giường Đệm Lò Xo'),(8,'Giường Gỗ Tự Nhiên'),(9,'Giường Có Thiết Kế Hiện Đại'),(10,'Giường Kiểu Cổ Điển'),(11,'Giường Thư Giãn'),(12,'Giường Có Khả Năng Điều Chỉnh Cao Thấp'),(13,'Giường Có Đệm Dày'),(14,'Giường Thoáng Khí');
/*!40000 ALTER TABLE `type_bed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_room`
--

DROP TABLE IF EXISTS `type_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_room_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `price` double DEFAULT NULL,
  `bed_count` int DEFAULT NULL,
  `acreage` double DEFAULT NULL,
  `guest_limit` int DEFAULT NULL,
  `type_bed_id` int DEFAULT NULL,
  `describes` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_type_bed` (`type_bed_id`),
  CONSTRAINT `FK_type_bed` FOREIGN KEY (`type_bed_id`) REFERENCES `type_bed` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_room`
--

LOCK TABLES `type_room` WRITE;
/*!40000 ALTER TABLE `type_room` DISABLE KEYS */;
INSERT INTO `type_room` VALUES (1,'Phòng Đơn',150000,1,25,1,1,'Phòng phù hợp cho một người, tiết kiệm và tiện nghi cơ bản, thích hợp cho những chuyến công tác hoặc du lịch ngắn ngày.'),(2,'Phòng Đôi',250000,2,30,2,2,'Phòng dành cho hai người, rộng rãi và thoải mái, thích hợp cho các cặp đôi hoặc bạn bè cùng du lịch'),(3,'Phòng Suite',175000,1,50,3,3,'Đẳng cấp và tiện nghi vượt trội'),(4,'Phòng Gia Đình',200000,2,40,4,4,'Phù hợp cho gia đình, không gian rộng rãi'),(5,'Phòng Deluxe',250000,1,35,5,5,'Không gian đẳng cấp, dịch vụ cao cấp'),(6,'Phòng Thường',120000,2,30,2,3,'Tối ưu chi phí, thoải mái và thư giãn'),(7,'Phòng Cao Cấp',450000,1,50,4,5,'Rộng rãi, tiện nghi và sang trọng'),(8,'Phòng VIP',500000,2,55,5,5,'Trải nghiệm thượng lưu, dịch vụ hoàn hảo'),(9,'Phòng Hạng Thương Gia',300000,1,45,2,5,'Dịch vụ ưu tiên, không gian sang trọng'),(10,'Phòng Tiết Kiệm',120000,1,30,2,1,'Phù hợp ngân sách, tiện lợi'),(11,'Phòng Doanh Nhân',600000,1,60,3,5,'Không gian làm việc và thư giãn thoải mái');
/*!40000 ALTER TABLE `type_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_room_amenities_type_room`
--

DROP TABLE IF EXISTS `type_room_amenities_type_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_room_amenities_type_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_room_id` int DEFAULT NULL,
  `amenities_type_room_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_type_room_amenities_type_room_type_room_id` (`type_room_id`),
  KEY `FK_type_room_amenities_type_room_amenities_type_room_id` (`amenities_type_room_id`),
  CONSTRAINT `FK_type_room_amenities_type_room_amenities_type_room_id` FOREIGN KEY (`amenities_type_room_id`) REFERENCES `amenities_type_room` (`id`),
  CONSTRAINT `FK_type_room_amenities_type_room_type_room_id` FOREIGN KEY (`type_room_id`) REFERENCES `type_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_room_amenities_type_room`
--

LOCK TABLES `type_room_amenities_type_room` WRITE;
/*!40000 ALTER TABLE `type_room_amenities_type_room` DISABLE KEYS */;
INSERT INTO `type_room_amenities_type_room` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,2,1),(8,2,2),(9,2,3),(10,2,4),(11,3,1),(12,3,2),(13,3,3),(14,3,4),(15,3,5),(16,4,1),(17,4,2),(18,4,3),(19,4,5),(20,4,6),(21,5,1),(22,5,2),(23,5,3),(24,5,4),(25,5,6),(26,6,1),(27,6,2),(28,6,3),(29,6,5),(30,7,1),(31,7,2),(32,7,3),(33,7,4),(34,7,5),(35,7,6),(36,8,1),(37,8,2),(38,8,3),(39,8,4),(40,8,5),(41,9,1),(42,9,2),(43,9,3),(44,9,4),(45,9,5),(46,9,6),(47,10,1),(48,10,2),(49,10,3),(50,10,5),(51,11,1),(52,11,2),(53,11,3),(54,11,4),(55,11,5),(56,11,6);
/*!40000 ALTER TABLE `type_room_amenities_type_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_room_image`
--

DROP TABLE IF EXISTS `type_room_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_room_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `image_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `type_room_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_type_room_image_type_room_id` (`type_room_id`),
  CONSTRAINT `FK_type_room_image_type_room_id` FOREIGN KEY (`type_room_id`) REFERENCES `type_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_room_image`
--

LOCK TABLES `type_room_image` WRITE;
/*!40000 ALTER TABLE `type_room_image` DISABLE KEYS */;
INSERT INTO `type_room_image` VALUES (1,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F80237ffb-3cbf-4714-9f2e-275b4bb12db4?alt=media&token=ecfc9834-469b-42f5-b954-821caecc7e27',1),(2,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb5edfe10-f2b6-4a40-90fb-d587ebe00fde?alt=media&token=006c4a4b-6da4-462e-b05b-a55b7c3ac603',1),(3,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F534640ed-26d8-4d09-9746-191852bba358?alt=media&token=860e8f86-9eae-47b4-9d7c-af96f085e846',1),(4,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',1),(5,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fc82b9920-8be0-411d-89eb-2bef6942e1af?alt=media&token=d62f6276-58b1-4a9e-a6aa-46e0282e0d71',2),(6,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb5edfe10-f2b6-4a40-90fb-d587ebe00fde?alt=media&token=006c4a4b-6da4-462e-b05b-a55b7c3ac603',2),(7,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F534640ed-26d8-4d09-9746-191852bba358?alt=media&token=860e8f86-9eae-47b4-9d7c-af96f085e846',2),(8,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F80237ffb-3cbf-4714-9f2e-275b4bb12db4?alt=media&token=ecfc9834-469b-42f5-b954-821caecc7e27',2),(9,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F80237ffb-3cbf-4714-9f2e-275b4bb12db4?alt=media&token=ecfc9834-469b-42f5-b954-821caecc7e27',3),(10,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb5edfe10-f2b6-4a40-90fb-d587ebe00fde?alt=media&token=006c4a4b-6da4-462e-b05b-a55b7c3ac603',3),(11,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F534640ed-26d8-4d09-9746-191852bba358?alt=media&token=860e8f86-9eae-47b4-9d7c-af96f085e846',3),(12,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',3),(13,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F80237ffb-3cbf-4714-9f2e-275b4bb12db4?alt=media&token=ecfc9834-469b-42f5-b954-821caecc7e27',4),(14,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fb5edfe10-f2b6-4a40-90fb-d587ebe00fde?alt=media&token=006c4a4b-6da4-462e-b05b-a55b7c3ac603',4),(15,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2F534640ed-26d8-4d09-9746-191852bba358?alt=media&token=860e8f86-9eae-47b4-9d7c-af96f085e846',4),(16,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',4),(17,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',5),(18,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',5),(19,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',5),(20,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',5),(21,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',6),(22,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',6),(23,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',6),(24,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',6),(25,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',7),(26,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',7),(27,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',7),(28,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',7),(29,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',8),(30,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',8),(31,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',8),(32,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',8),(33,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',9),(34,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',9),(35,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',9),(36,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',9),(37,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',10),(38,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',10),(39,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',10),(40,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',10),(41,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',11),(42,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',11),(43,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',11),(44,'https://firebasestorage.googleapis.com/v0/b/myprojectimg-164dd.appspot.com/o/files%2Fcfb0e746-811f-4f3f-9ec3-e2d9d17067a3?alt=media&token=b6e4609c-32be-40d2-aefa-e19a5b841880',11);
/*!40000 ALTER TABLE `type_room_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_room_service_package`
--

DROP TABLE IF EXISTS `type_room_service_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_room_service_package` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_room_id` int DEFAULT NULL,
  `service_package_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_type_room_service_package_type_room_id` (`type_room_id`),
  KEY `FK_type_room_service_package_service_package_id` (`service_package_id`),
  CONSTRAINT `FK_type_room_service_package_service_package_id` FOREIGN KEY (`service_package_id`) REFERENCES `service_package` (`id`),
  CONSTRAINT `FK_type_room_service_package_type_room_id` FOREIGN KEY (`type_room_id`) REFERENCES `type_room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_room_service_package`
--

LOCK TABLES `type_room_service_package` WRITE;
/*!40000 ALTER TABLE `type_room_service_package` DISABLE KEYS */;
INSERT INTO `type_room_service_package` VALUES (1,1,1),(2,1,2),(3,2,4),(4,2,5),(5,3,1),(40,3,3),(41,4,5),(42,4,2),(43,5,4),(44,5,3),(45,6,5),(46,6,2),(47,7,1),(48,7,5),(49,8,3),(50,8,2),(51,9,2),(52,9,3),(53,10,5),(54,10,1),(55,11,4),(56,11,4);
/*!40000 ALTER TABLE `type_room_service_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_service_room`
--

DROP TABLE IF EXISTS `type_service_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_service_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `service_room_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `duration` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_service_room`
--

LOCK TABLES `type_service_room` WRITE;
/*!40000 ALTER TABLE `type_service_room` DISABLE KEYS */;
INSERT INTO `type_service_room` VALUES (1,'Đồ Ăn ','phần'),(2,'Đồ Uống','ly/chai'),(3,'Dịch Vụ Dọn Dẹp','giờ'),(4,'ádf',NULL);
/*!40000 ALTER TABLE `type_service_room` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-19 14:09:24
