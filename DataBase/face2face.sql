-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema face2face
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `face2face` ;

-- -----------------------------------------------------
-- Schema face2face
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `face2face` DEFAULT CHARACTER SET utf8 ;
USE `face2face` ;

-- -----------------------------------------------------
-- Table `face2face`.`t_user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `face2face`.`t_user` ;

CREATE TABLE IF NOT EXISTS `face2face`.`t_user` (
  `id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `face2face`.`t_admin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `face2face`.`t_admin` ;

CREATE TABLE IF NOT EXISTS `face2face`.`t_admin` (
  `t_user_id` VARCHAR(45) NOT NULL,
  INDEX `fk_t_admin_t_user_idx` (`t_user_id` ASC) VISIBLE,
  PRIMARY KEY (`t_user_id`),
  CONSTRAINT `fk_t_admin_t_user`
    FOREIGN KEY (`t_user_id`)
    REFERENCES `face2face`.`t_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `face2face`.`t_interviewee`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `face2face`.`t_interviewee` ;

CREATE TABLE IF NOT EXISTS `face2face`.`t_interviewee` (
  `t_user_id` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `cv` VARCHAR(100) NULL DEFAULT NULL,
  `status` VARCHAR(45) NOT NULL,
  `index` INT NOT NULL DEFAULT 0,
  INDEX `fk_t_interviewee_t_user1_idx` (`t_user_id` ASC) VISIBLE,
  PRIMARY KEY (`t_user_id`),
  CONSTRAINT `fk_t_interviewee_t_user1`
    FOREIGN KEY (`t_user_id`)
    REFERENCES `face2face`.`t_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `face2face`.`t_interviewer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `face2face`.`t_interviewer` ;

CREATE TABLE IF NOT EXISTS `face2face`.`t_interviewer` (
  `t_user_id` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  INDEX `fk_t_interviewer_t_user1_idx` (`t_user_id` ASC) VISIBLE,
  PRIMARY KEY (`t_user_id`),
  CONSTRAINT `fk_t_interviewer_t_user1`
    FOREIGN KEY (`t_user_id`)
    REFERENCES `face2face`.`t_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `face2face`.`t_record`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `face2face`.`t_record` ;

CREATE TABLE IF NOT EXISTS `face2face`.`t_record` (
  `t_interviewee_t_user_id` VARCHAR(45) NOT NULL,
  `t_interviewer_t_user_id` VARCHAR(45) NOT NULL,
  `remark` VARCHAR(2000) NULL DEFAULT NULL,
  `result` VARCHAR(45) NULL DEFAULT NULL,
  `link` VARCHAR(200) NULL DEFAULT NULL,
  `start_time` DATETIME NULL DEFAULT NULL,
  `end_time` VARCHAR(45) NULL DEFAULT NULL,
  `index` INT NOT NULL,
  `score` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`t_interviewee_t_user_id`, `t_interviewer_t_user_id`, `index`),
  INDEX `fk_t_interviewee_has_t_interviewer_t_interviewer1_idx` (`t_interviewer_t_user_id` ASC) VISIBLE,
  INDEX `fk_t_interviewee_has_t_interviewer_t_interviewee1_idx` (`t_interviewee_t_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_t_interviewee_has_t_interviewer_t_interviewee1`
    FOREIGN KEY (`t_interviewee_t_user_id`)
    REFERENCES `face2face`.`t_interviewee` (`t_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_t_interviewee_has_t_interviewer_t_interviewer1`
    FOREIGN KEY (`t_interviewer_t_user_id`)
    REFERENCES `face2face`.`t_interviewer` (`t_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `face2face`.`t_time_interval`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `face2face`.`t_time_interval` ;

CREATE TABLE IF NOT EXISTS `face2face`.`t_time_interval` (
  `t_user_id` VARCHAR(45) NOT NULL,
  `start_time` VARCHAR(45) NOT NULL,
  `end_time` VARCHAR(45) NOT NULL,
  INDEX `fk_table1_t_interviewer1_idx` (`t_user_id` ASC) VISIBLE,
  PRIMARY KEY (`t_user_id`, `start_time`, `end_time`),
  CONSTRAINT `fk_table1_t_interviewer1`
    FOREIGN KEY (`t_user_id`)
    REFERENCES `face2face`.`t_interviewer` (`t_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
