-- -----------------------------------------------------
-- Table `drip`.`DRIP_I18N` 国际化编码表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_I18N`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_I18N` (
  `CODE_NAME` CHAR(4) NULL COMMENT '编码' ,
  `CODE_VALUE` VARCHAR(32) NULL COMMENT '值' ,
  PRIMARY KEY (`CODE_NAME`),
  UNIQUE KEY (`CODE_VALUE`))
ENGINE = InnoDB
COMMENT = '国际化编码表';