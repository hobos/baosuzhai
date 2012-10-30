-- 习题解析
-- -----------------------------------------------------
-- Table `drip`.`DRIP_EXER_GUIDE` 习题解析
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_EXER_GUIDE`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_EXER_GUIDE` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `EXER_ID` BIGINT NOT NULL COMMENT '习题标识' ,
  `CONTENT` TEXT NOT NULL COMMENT '习题解析内容',
  `CRT_TM` DATETIME NULL COMMENT '创建时间' ,
  `CRT_USER_ID` BIGINT NOT NULL COMMENT '创建人标识',
  `UPT_TM` DATETIME NULL COMMENT '最近一次修改时间',
  `UPT_USER_ID` BIGINT NULL COMMENT '最近一次修改人标识',
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '习题解析';