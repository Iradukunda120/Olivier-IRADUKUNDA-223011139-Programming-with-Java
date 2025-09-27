-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 26, 2025 at 04:39 PM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `conferencecrm`
--

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
  `ClientID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `PasswordHash` varchar(255) NOT NULL,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`ClientID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`ClientID`, `Name`, `PasswordHash`, `Email`) VALUES
(1, 'Alice Johnson', 'password123', 'alice@example.com'),
(2, 'Bob Smith', 'mypassword', 'bob@example.com'),
(3, 'Carol White', 'securepass', 'carol@example.com'),
(4, 'eric', 'gff', 'eric@gmail.com'),
(5, 'eric', 'gff', 'eric@gmail');

-- --------------------------------------------------------

--
-- Table structure for table `participants`
--

DROP TABLE IF EXISTS `participants`;
CREATE TABLE IF NOT EXISTS `participants` (
  `ParticipantID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `SessionID` int DEFAULT NULL,
  PRIMARY KEY (`ParticipantID`),
  UNIQUE KEY `Email` (`Email`),
  KEY `SessionID` (`SessionID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `participants`
--

INSERT INTO `participants` (`ParticipantID`, `Name`, `Email`, `SessionID`) VALUES
(1, 'David King', 'davidking@example.com', 1),
(2, 'Sophia Lee', 'sophialee@example.com', 2),
(3, 'Henry White', 'henrywhite@example.com', 1),
(4, 'Olivia Adams', 'oliviaadams@example.com', 3);

-- --------------------------------------------------------

--
-- Table structure for table `reporters`
--

DROP TABLE IF EXISTS `reporters`;
CREATE TABLE IF NOT EXISTS `reporters` (
  `ReporterID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Organization` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ReporterID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `reporters`
--

INSERT INTO `reporters` (`ReporterID`, `Name`, `Email`, `Organization`) VALUES
(1, 'Lara Green', 'lara.green@news.com', 'TechNews'),
(2, 'Tom Harris', 'tom.harris@media.com', 'Global Media'),
(3, 'Emma Black', 'emma.black@reporters.org', 'Reporters United');

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
CREATE TABLE IF NOT EXISTS `sessions` (
  `SessionID` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(150) NOT NULL,
  `Date` date NOT NULL,
  `Status` varchar(50) NOT NULL,
  PRIMARY KEY (`SessionID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`SessionID`, `Title`, `Date`, `Status`) VALUES
(1, 'AI in 2025', '2025-11-15', 'Scheduled'),
(2, 'Data Science Workshop', '2025-11-16', 'Scheduled'),
(3, 'Cloud Computing Trends', '2025-11-17', 'Planned');

-- --------------------------------------------------------

--
-- Table structure for table `speakers`
--

DROP TABLE IF EXISTS `speakers`;
CREATE TABLE IF NOT EXISTS `speakers` (
  `SpeakerID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Bio` text,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`SpeakerID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `speakers`
--

INSERT INTO `speakers` (`SpeakerID`, `Name`, `Bio`, `Email`) VALUES
(1, 'Dr. John Doe', 'Expert in AI and Machine Learning', 'johndoe@example.com'),
(2, 'Prof. Jane Roe', 'Data Science Researcher', 'janeroe@example.com'),
(3, 'Mr. Mike Brown', 'Cloud Computing Specialist', 'mikebrown@example.com');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
