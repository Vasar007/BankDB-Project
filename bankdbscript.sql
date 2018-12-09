-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8;
USE `mydb`;

-- -----------------------------------------------------
-- Table `mydb`.`bankdb`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bankdb` (
  `accountID` VARCHAR(11) NOT NULL,
  `SIN` VARCHAR(7) NOT NULL,
  `lastactivity` VARCHAR(100) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`accountID`),
  UNIQUE KEY `accountID` (`accountID`),
  UNIQUE KEY `SIN_UNIQUE` (`SIN`))
  ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `mydb`.`bankdb` (`accountID`, `SIN`, `lastactivity`) VALUES
('0',           '0',       'NA'),
('20033112345', '1234567', 'Mon May 09 21:05:11 EDT 2016'),
('45638912345', '1891751', 'Mon May 09 22:06:05 EDT 2016'),
('42006412345', '9999999', 'Wed May 11 00:18:46 EDT 2016'),
('45385012345', '1234568', 'None'),
('61923012345', '1111111', 'None'),
('30577412345', '2222222', 'None'),
('63063112345', '8080808', 'Wed May 11 01:25:01 EDT 2016'),
('50000012345', '4124125', 'None'),
('70382512345', '2523626', 'None');

-- -----------------------------------------------------
-- Table `mydb`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`account` (
  `accountID` VARCHAR(11) NOT NULL,
  `username` VARCHAR(16) CHARACTER SET 'utf8' NOT NULL,
  `password` VARCHAR(128) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `accountID_UNIQUE` (`accountID`),
  UNIQUE KEY `username` (`username`),
  CONSTRAINT `fk_accountID_A`
    FOREIGN KEY (`accountID`)
    REFERENCES `mydb`.`bankdb` (`accountID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `mydb`.`account` (`accountID`, `username`, `password`) VALUES
('0',           'admin',    '$2a$10$dfpH/XruGxYxHgvyjb/OAOA4PRRcxnW6iz0RLleyt3w8J/OOrnsmi'), -- admin
('20033112345', 'berks',    '$2a$10$BzxqhvHh8zSPCdAY65fT2uV/VeyyNTgOjRxm13xLycTMhonI052p2'), -- berk123
('45638912345', 'johns',    '$2a$10$gjyP1IwB9shgOlmBI5/nMu0PK7JAe6exwdeSU1cXom/2iWABr2L7K'), -- john123
('42006412345', 'Oliver',   '$2a$10$sheazP1KkpszPXhgT928S.NfJNMLa83hJZdZAVYZCHjtwi1K155X2'), -- Oliver123
('45385012345', 'deneme',   '$2a$10$bN7z2koeTrMciZEXwnKi0OnJRY4aaZ5XvFEP/H4w/3zgaW3c6rzEG'), -- deneme123
('61923012345', 'NEW',      '$2a$10$DE4SqPby2qAgzuuxlKgHPuRR6pkhr5Lak5gh4g7BeOAdtaNEqNEly'), -- NEW123
('30577412345', 'Brian',    '$2a$10$WnVdndyCdVWIT6F5PY/W8uwoXHJeh4MkLygAENmvE1xl7C4MFecFK'), -- Brian123
('63063112345', 'Jennifer', '$2a$10$SqmkJxAa9GjSBTSzUO9kY.rJ7g1876nKHf1BnUuYQmj6nH.Q43kOq'), -- Jennifer123
('50000012345', 'Barrack',  '$2a$10$I87aXvCSYM4xQVytUbkzzOINez8Gyq12LbZNJY1ibtrSERCkJEnXK'), -- Barrack123
('70382512345', 'Obarack',  '$2a$10$diT.kw1XMhaqZ.2TyLZutuBcoQlC2/qDgqoFLSzQRWuiveOBU78sS'); -- Obarack123

-- -----------------------------------------------------
-- Table `mydb`.`clientinfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`clientinfo` (
  `name` VARCHAR(25) NOT NULL,
  `lastname` VARCHAR(25) NOT NULL,
  `SIN` VARCHAR(7) NOT NULL,
  `birthdate` DATE NOT NULL,
  PRIMARY KEY (`SIN`),
  UNIQUE KEY `SIN` (`SIN`),
  CONSTRAINT `fk_SIN`
    FOREIGN KEY (`SIN`)
    REFERENCES `mydb`.`bankdb` (`SIN`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `mydb`.`clientinfo` (`name`, `lastname`, `SIN`, `birthdate`) VALUES
('admin',      'admin',    '0',       '1000-01-01'),
('Berk',       'Soysal',   '1234567', '1995-02-22'),
('John',       'Snow',     '1891751', '1977-04-11'),
('Oliver',     'Young',    '9999999', '1992-11-22'),
('Ricardinho', 'Oliveria', '1234568', '1991-12-11'),
('NEW',        'NEW',      '1111111', '1992-01-22'),
('Brian',      'Foster',   '2222222', '1992-02-11'),
('Jennifer',   'Lopez',    '8080808', '1933-12-13'),
('Barrack',    'Obama',    '4124125', '1965-04-11'),
('Obarack',    'Burama',   '2523626', '1952-04-11');

-- -----------------------------------------------------
-- Table `mydb`.`bankaccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bankaccount` (
  `accountID` VARCHAR(11) NOT NULL,
  `currencyaccountID` VARCHAR(13) NOT NULL,
  `balance` DECIMAL(65, 10) NOT NULL,
  `currency` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`currencyaccountID`),
  UNIQUE KEY `currencyaccountID` (`currencyaccountID`),
  CONSTRAINT `fk_accountID_BA`
    FOREIGN KEY (`accountID`)
    REFERENCES `mydb`.`bankdb` (`accountID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `mydb`.`bankaccount` (`accountID`, `currencyaccountID`, `balance`, `currency`) VALUES
('0',           '0',             0,               'NA'),
('20033112345', '1234567000001', 4392.77,         'CAD'),
('20033112345', '1234567000002', 300.78,          'USD'),
('45638912345', '1891751000001', 513262.11,       'USD'),
('42006412345', '9999999000001', 139899.33,       'USD'),
('45385012345', '1234568000001', 12415.141,       'RUPI'),
('61923012345', '1111111000001', 15151.145,       'CAD'),
('30577412345', '2222222000001', 87687.1213,      'USD'),
('63063112345', '8080808000001', 211.11123,       'EUR'),
('63063112345', '8080808000002', 21111031.11,     'RUB'),
('63063112345', '8080808000003', 9991.113,        'USD'),
('50000012345', '4124125000001', 1782641421.1414, 'USD'),
('70382512345', '2523626000001', 3525235.1417,    'USD');

-- -----------------------------------------------------
-- Table `mydb`.`bankaccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`activity` (
  `accountID` VARCHAR(11) NOT NULL,
  `actionID` VARCHAR(15) NOT NULL,
  `action` VARCHAR(50) NOT NULL,
  `actiondatetime` DATETIME NOT NULL,
  PRIMARY KEY (`actionID`),
  UNIQUE KEY `actionID` (`actionID`),
  CONSTRAINT `fk_accountID_ACC`
    FOREIGN KEY (`accountID`)
      REFERENCES `mydb`.`bankdb` (`accountID`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION)
  ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `mydb`.`activity` (`accountID`, `actionID`, `action`, `actiondatetime`) VALUES
('0',           '0',               'NA',                                   '1000-01-01 00:00:00'),
('20033112345', '200331123450001', 'deposit 20 c.u. from 1234567000001',   '2018-12-01 00:01:29'),
('20033112345', '200331123450002', 'withdraw 42 c.u. from 1234567000002',  '2018-12-11 00:02:34'),
('45638912345', '456389123450001', 'withdraw 42 c.u. from 1891751000001',  '2018-11-05 07:05:38'),
('42006412345', '420064123450001', 'withdraw 42 c.u. from 9999999000001',  '2018-11-25 08:05:36'),
('45385012345', '453850123450001', 'withdraw 42 c.u. from 1234568000001',  '2018-11-26 23:35:39'),
('61923012345', '619230123450001', 'withdraw 42 c.u. from 1111111000001',  '2018-11-21 09:35:35'),
('30577412345', '305774123450001', 'withdraw 42 c.u. from 2222222000001',  '2018-12-01 12:35:59'),
('63063112345', '630631123450001', 'deposit 20 c.u. from 8080808000001',   '2018-10-04 13:39:00'),
('63063112345', '630631123450002', 'deposit 55 c.u. from 8080808000002',   '2018-10-13 14:32:59'),
('63063112345', '630631123450003', 'deposit 19 c.u. from 8080808000003',   '2018-10-12 15:59:50'),
('50000012345', '500000123450001', 'deposit 1000 c.u. from 4124125000001', '2018-10-14 15:02:54'),
('70382512345', '703825123450001', 'deposit 102 c.u. from 2523626000001',  '2018-12-11 00:02:54');

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
