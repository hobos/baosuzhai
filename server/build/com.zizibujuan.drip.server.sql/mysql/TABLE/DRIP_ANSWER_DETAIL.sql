-- 习题答案详情
-- -----------------------------------------------------
-- Table `drip`.`DRIP_ANSWER_DETAIL` 习题答案详情
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_ANSWER_DETAIL`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_ANSWER_DETAIL` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `ANSWER_ID` BIGINT NOT NULL COMMENT '答案标识' ,
  `OPT_ID` BIGINT NULL COMMENT '答案选项标识，用于完型填空。如果没有选项，则为null' ,
  `CONTENT` TEXT NULL COMMENT '文本答案 （注意，答案中也可能需要附图）,完型填空等，如果没有则为null',
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '习题答案详情';