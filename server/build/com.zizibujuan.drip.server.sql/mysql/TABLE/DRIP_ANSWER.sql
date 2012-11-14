-- 习题答案的基本信息
-- -----------------------------------------------------
-- Table `drip`.`DRIP_ANSWER` 习题答案的基本信息
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_ANSWER`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_ANSWER` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `EXER_ID` BIGINT NOT NULL COMMENT '习题标识' ,
  `GUIDE` TEXT NULL COMMENT '答题解析',
  `CRT_TM` DATETIME NULL COMMENT '创建时间' ,
  `CRT_USER_ID` BIGINT NOT NULL COMMENT '创建人标识',
  `UPT_TM` DATETIME NULL COMMENT '最近一次修改时间',
  `UPT_USER_ID` BIGINT NULL COMMENT '最近一次修改人标识',
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '习题答案的基本信息';