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
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`bankdb`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bankdb` (
  `accountID` INT(11) NOT NULL AUTO_INCREMENT,
  `SIN` INT(7) NOT NULL,
  `lastactivity` VARCHAR(100) CHARACTER SET 'latin1' NOT NULL,
  UNIQUE INDEX `accountID_UNIQUE` (`accountID` ASC),
  PRIMARY KEY (`accountID`),
  UNIQUE INDEX `SIN_UNIQUE` (`SIN` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`account` (
  `accountID` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(16) CHARACTER SET 'latin1' NOT NULL,
  `password` VARCHAR(128) CHARACTER SET 'latin1' NOT NULL,
  UNIQUE INDEX `accountID_UNIQUE` (`accountID` ASC),
  UNIQUE INDEX `Username_UNIQUE` (`username` ASC),
  PRIMARY KEY (`username`),
  CONSTRAINT `fk_accountID_A`
    FOREIGN KEY (`accountID`)
    REFERENCES `mydb`.`bankdb` (`accountID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `mydb`.`clientinfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`clientinfo` (
  `name` VARCHAR(25) NOT NULL,
  `lastname` VARCHAR(25) NOT NULL,
  `SIN` INT(7) NOT NULL,
  `birthdate` DATE NOT NULL,
  PRIMARY KEY (`SIN`),
  UNIQUE INDEX `SIN_UNIQUE` (`SIN` ASC),
  CONSTRAINT `fk_SIN`
    FOREIGN KEY (`SIN`)
    REFERENCES `mydb`.`bankdb` (`SIN`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`bankaccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bankaccount` (
  `accountID` INT(11) NOT NULL AUTO_INCREMENT,
  `currencyaccountID` INT(11) NOT NULL,
  `balleft` INT(10) NOT NULL,
  `balright` INT(5) NOT NULL,
  `currency` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`currencyaccountID`),
  UNIQUE INDEX `currencyaccountID_UNIQUE` (`currencyaccountID` ASC),
  UNIQUE INDEX `accountID_UNIQUE` (`accountID` ASC),
  CONSTRAINT `fk_accountID_BA`
    FOREIGN KEY (`accountID`)
    REFERENCES `mydb`.`bankdb` (`accountID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
