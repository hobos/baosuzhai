-- -----------------------------------------------------
-- Table `drip`.`DRIP_USER_INFO` 用户表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_USER_INFO`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_USER_INFO` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `LOGIN_NM` VARCHAR(45) NULL COMMENT '登录名' , -- TODO: 准备删除
  `LOGIN_EMAIL` VARCHAR(100) NULL COMMENT '登录邮箱' ,
  `LOGIN_PWD` VARCHAR(45) NULL COMMENT '登录密码' ,
  `MOBILE` VARCHAR(12) NULL COMMENT '绑定的手机号码，目前只支持一个号码',
  `REAL_NM` VARCHAR(12) NULL COMMENT '真实姓名',
  `CRT_TM` DATETIME NULL COMMENT '创建时间' ,
  `LAST_LOGIN_TM` DATETIME NULL COMMENT '最近登录时间' ,
  `FAN_COUNT` INT NULL DEFAULT 0 COMMENT '粉丝数量' ,
  `FOLLOW_COUNT` INT NULL DEFAULT 0 COMMENT '关注人数量' ,
  `EXER_DRAFT_COUNT` INT NULL DEFAULT 0 COMMENT '录入的习题草稿数量' ,
  `EXER_PUBLISH_COUNT` INT NULL DEFAULT 0 COMMENT '录入的已发布习题数量' ,
  `ANSWER_COUNT` INT NULL DEFAULT 0 COMMENT '回答的习题数量' ,
  
  PRIMARY KEY (`DBID`),
  UNIQUE KEY (`LOGIN_EMAIL`))
ENGINE = InnoDB
COMMENT = '用户表';