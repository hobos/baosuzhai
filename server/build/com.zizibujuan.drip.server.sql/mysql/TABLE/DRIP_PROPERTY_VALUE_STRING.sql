-- -----------------------------------------------------
-- Table `drip`.`DRIP_PROPERTY_VALUE_STRING` 系统属性值表,存放字符串值
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_VALUE_STRING`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_PROPERTY_VALUE_STRING` (
  `KEY_ID` BIGINT NOT NULL COMMENT '键标识' ,
  `PROPERTY_VALUE` VARCHAR(1000) NOT NULL COMMENT '属性值' ,
  `I18N_ID` CHAR(4) NOT NULL COMMENT '语言_国家，应用于属性值' ,
  PRIMARY KEY (`KEY_ID`,`I18N_ID`))
ENGINE = InnoDB
COMMENT = '系统属性值表,存放字符串值';