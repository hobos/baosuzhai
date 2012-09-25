-- -----------------------------------------------------
-- Table `drip`.`DRIP_EXERCISE` 维护所有的习题
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_EXERCISE`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_EXERCISE` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `CONTENT` TEXT NULL COMMENT '习题内容' ,
  `EXER_TYPE` BIGINT NULL COMMENT '习题类型' ,
  `CRT_TM` DATETIME NULL COMMENT '创建时间/贡献时间' ,
  `CRT_USER_ID` BIGINT NOT NULL COMMENT '创建人/贡献者标识' ,
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '习题';

-- 支持的习题类型 单项选择题，多项选择题，不定项选择题， 填空题，问答题等。