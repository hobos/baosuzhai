-- -----------------------------------------------------
-- Table `drip`.`DRIP_PROPERTY_GROUP` 系统参数分类表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_GROUP`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_PROPERTY_GROUP` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `GROUP_NAME` VARCHAR(32) NULL COMMENT '分类名称,英文描述' ,
  `DISPLAY_NAME` VARCHAR(32) NULL COMMENT '显示名称' ,
  `I18N_ID` CHAR(4) NOT NULL COMMENT '语言_国家标识，应用于显示名称' ,
  PRIMARY KEY (`DBID`),
  UNIQUE KEY (`DBID`,`GROUP_NAME`))
ENGINE = InnoDB
COMMENT = '系统参数分类表';