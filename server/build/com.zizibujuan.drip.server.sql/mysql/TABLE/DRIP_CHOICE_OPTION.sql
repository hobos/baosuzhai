-- -----------------------------------------------------
-- Table `drip`.`DRIP_CHOICE_OPTION` 存储选择题的可选项的表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_CHOICE_OPTION`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_CHOICE_OPTION` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `EXER_ID` BIGINT NOT NULL COMMENT '习题标识' ,
  `CONTENT` TEXT NULL COMMENT '可选项内容' ,
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '存储选择题的可选项的表';

-- TODO：还需要一个概念就是：选择题的显示模板。用它统一选择题的显示方式， 如列表使用A，B，C，D还是1，2，3，4等