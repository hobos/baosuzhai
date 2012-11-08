-- -----------------------------------------------------
-- Table `drip`.`DRIP_USER_RELATION` 用户关系表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_USER_RELATION`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_USER_RELATION` (
  `DBID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `USER_ID` BIGINT UNSIGNED NOT NULL COMMENT '用户标识' ,
  `WATCH_USER_ID` BIGINT UNSIGNED NOT NULL COMMENT '关注用户标识' ,
  `CRT_TM` DATETIME NOT NULL COMMENT '建立关系时间' ,
  PRIMARY KEY (`DBID`),
  UNIQUE KEY (`USER_ID`,`WATCH_USER_ID`))
ENGINE = InnoDB
COMMENT = '用户关系表';