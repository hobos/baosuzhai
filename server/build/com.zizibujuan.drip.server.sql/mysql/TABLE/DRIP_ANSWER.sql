-- 习题的答案
-- -----------------------------------------------------
-- Table `drip`.`DRIP_ANSWER` 习题的答案
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_ANSWER`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_ANSWER` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `EXER_ID` BIGINT NOT NULL COMMENT '习题标识' ,
  `CONTENT` TEXT NULL COMMENT '答案' ,
  `CRT_TM` DATETIME NULL COMMENT '答题时间' ,
  `CRT_USER_ID` BIGINT NOT NULL COMMENT '答题人' ,
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '习题的答案';