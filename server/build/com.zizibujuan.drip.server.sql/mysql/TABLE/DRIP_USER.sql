-- -----------------------------------------------------
-- Table `drip`.`DRIP_USER` 用户表
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_USER`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_USER` (
  `DBID` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `LOGIN_NM` VARCHAR(45) NULL COMMENT '登录名' ,
  `LOGIN_EMAIL` VARCHAR(45) NULL COMMENT '登录邮箱' ,
  `LOGIN_PWD` VARCHAR(45) NULL COMMENT '登录密码' ,
  `MOBILE` VARCHAR(12) NULL COMMENT '绑定的手机号码，目前只支持一个号码',
  `CRT_TM` DATETIME NULL COMMENT '创建时间' ,
  `LAST_LOGIN_TM` DATETIME NULL COMMENT '最近登录时间' ,
  PRIMARY KEY (`DBID`) )
ENGINE = InnoDB
COMMENT = '用户表';