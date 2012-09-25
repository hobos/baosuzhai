-- -----------------------------------------------------
-- Table `drip`.`DRIP_PROPERTY_KEY` 系统属性表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_KEY`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_PROPERTY_KEY` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `PROPERTY_KEY` VARCHAR(100) NOT NULL COMMENT '键' ,
  `GROUP_ID` BIGINT NOT NULL COMMENT '分类标识' ,
  `PROPERTY_TYPE` INT(2) NOT NULL COMMENT '值的类型，1表示字符串,2表示数字' ,
  PRIMARY KEY (`DBID`),
  UNIQUE KEY (`PROPERTY_KEY`,`GROUP_ID`))
ENGINE = InnoDB
COMMENT = '存放属性的KEY值,VALUE值单独存放';