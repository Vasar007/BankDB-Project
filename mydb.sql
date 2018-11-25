-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 11, 2016 at 04:11 PM
-- Server version: 5.7.9
-- PHP Version: 5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mydb`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `accountID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(11) NOT NULL,
  `Password` varchar(11) NOT NULL,
  PRIMARY KEY (`Username`),
  UNIQUE KEY `Username` (`Username`),
  UNIQUE KEY `UserID` (`accountID`)
) ENGINE=MyISAM AUTO_INCREMENT=703826 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`accountID`, `Username`, `Password`) VALUES
(200331, 'berks', 'berk1234'),
(0, 'admin', 'admin'),
(456389, 'johns', 'john1234'),
(420064, 'Oliver', '9999999'),
(453850, 'deneme', '1234567'),
(619230, 'NEW', '1111111'),
(305774, 'Brian', '2222222'),
(630631, 'Jennifer', '8080808'),
(5, 'Barrack', '4124125'),
(703825, 'Obarack', '2523626');

-- --------------------------------------------------------

--
-- Table structure for table `bankdb`
--

DROP TABLE IF EXISTS `bankdb`;
CREATE TABLE IF NOT EXISTS `bankdb` (
  `accountID` int(11) NOT NULL,
  `name` varchar(15) NOT NULL,
  `lastname` varchar(10) NOT NULL,
  `SIN` int(7) NOT NULL,
  `birthdate` varchar(10) NOT NULL,
  `balleft` int(10) NOT NULL,
  `balright` int(5) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `lastactivity` varchar(100) NOT NULL,
  PRIMARY KEY (`accountID`),
  UNIQUE KEY `accountID` (`accountID`)
) ENGINE=MyISAM AUTO_INCREMENT=456390 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bankdb`
--

INSERT INTO `bankdb` (`accountID`, `name`, `lastname`, `SIN`, `birthdate`, `balleft`, `balright`, `currency`, `lastactivity`) VALUES
(200331, 'Berk', 'Soysal', 1234567, '22-02-1995', 4392, 77, 'CAD', 'Mon May 09 21:05:11 EDT 2016'),
(456389, 'John', 'Snow', 1891751, '11-04-1977', 513262, 11, 'USD', 'Mon May 09 22:06:05 EDT 2016'),
(0, 'admin', 'admin', 0, '00-00-0000', 0, 0, 'NA', 'NA'),
(420064, 'Oliver', 'Young', 9999999, '22-11-1992', 139899, 33, 'USD', 'Wed May 11 00:18:46 EDT 2016'),
(453850, 'Ricardinho', 'Oliveria', 1234567, '11-12-1991', 12415, 141, 'RUPI', 'none'),
(619230, 'NEW', 'NEW', 1111111, '22-01-1992', 15151, 141, 'CAD', 'none'),
(305774, 'Brian', 'Foster', 2222222, '11-02-1992', 87687, 1213, 'USD', 'none'),
(630631, 'Jennifer', 'Lopez', 8080808, '13-13-1333', 21111031, 11123, 'EUR', 'Wed May 11 01:25:01 EDT 2016'),
(5, 'Barrack', 'Obama', 4124125, '11-04-1965', 1782641421, 1414, 'USD', 'none'),
(703825, 'Obarack', 'Burama', 2523626, '11-04-1952', 3525235, 1414, 'USD', 'none');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
