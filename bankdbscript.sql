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
-- Table `mydb`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`account` (
  `accountID` INT(11) NOT NULL AUTO_INCREMENT,
  `Username` VARCHAR(11) CHARACTER SET 'latin1' NOT NULL,
  `Password` VARCHAR(11) CHARACTER SET 'latin1' NOT NULL,
  PRIMARY KEY (`Username`),
  UNIQUE INDEX `Password_UNIQUE` (`Password` ASC),
  UNIQUE INDEX `Username_UNIQUE` (`Username` ASC),
  UNIQUE INDEX `accountID_UNIQUE` (`accountID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`bankdb`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bankdb` (
  `accountID` INT(11) NOT NULL,
  `name` VARCHAR(15) CHARACTER SET 'latin1' NOT NULL,
  `lastname` VARCHAR(10) CHARACTER SET 'latin1' NOT NULL,
  `SIN` INT(7) NOT NULL,
  `birthdate` VARCHAR(10) CHARACTER SET 'latin1' NOT NULL,
  `balleft` INT(10) NOT NULL,
  `balright` INT(5) NOT NULL,
  `currency` VARCHAR(10) CHARACTER SET 'latin1' NOT NULL,
  `lastactivity` VARCHAR(100) CHARACTER SET 'latin1' NOT NULL,
  PRIMARY KEY (`accountID`),
  UNIQUE INDEX `accountID_UNIQUE` (`accountID` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
