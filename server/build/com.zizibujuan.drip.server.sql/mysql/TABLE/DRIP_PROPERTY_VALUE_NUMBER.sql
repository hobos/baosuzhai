-- -----------------------------------------------------
-- Table `drip`.`DRIP_PROPERTY_VALUE_NUMBER` 系统属性值表，存放整数值
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_VALUE_NUMBER`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_PROPERTY_VALUE_NUMBER` (
  `KEY_ID` BIGINT NOT NULL COMMENT '键标识' ,
  `PROPERTY_VALUE` int NOT NULL COMMENT '属性值' ,
  PRIMARY KEY (`KEY_ID`))
ENGINE = InnoDB
COMMENT = '系统属性值表，存放整数值';