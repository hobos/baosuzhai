-- -----------------------------------------------------
-- Table `drip`.`DRIP_EXER_OPTION` 存储一道题具有多个答案的选项表，如完型填空题和选择题。
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_EXER_OPTION`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_EXER_OPTION` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `EXER_ID` BIGINT NOT NULL COMMENT '习题标识' ,
  `CONTENT` TEXT NULL COMMENT '可选项内容' ,
  `OPT_SEQ` INT NULL COMMENT '选项次序',
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '存储习题的选项或者填空占位符表';

-- TODO：还需要一个概念就是：选择题的显示模板。用它统一选择题的显示方式， 如列表使用A，B，C，D还是1，2，3，4等