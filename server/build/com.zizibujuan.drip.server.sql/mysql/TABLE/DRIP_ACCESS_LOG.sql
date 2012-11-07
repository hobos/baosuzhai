-- 用户访问日志
-- -----------------------------------------------------
-- Table `drip`.`DRIP_ACCESS_LOG` 用户访问日志
-- -----------------------------------------------------
DROP TABLE IF EXISTS `drip`.`DRIP_ACCESS_LOG`;

CREATE  TABLE IF NOT EXISTS `drip`.`DRIP_ACCESS_LOG` (
  `DBID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键' ,
  `USER_ID` BIGINT NOT NULL COMMENT '活动用户标识' ,
  `IP` VARCHAR(255) NULL COMMENT 'IP地址',
  `ANONYMOUS` TINYINT(1) NULL DEFAULT 1 COMMENT '是否匿名用户',
  `URL_FROM` VARCHAR(255) NULL COMMENT '来自哪个页面',
  `URL_ACCESS` VARCHAR(255) NULL COMMENT '当前访问本网站的页面',
  `ACCESS_TIME` DATETIME NULL COMMENT '访问时间',
  `LEAVE_TIME` DATETIME NULL COMMENT '离开时间',
  PRIMARY KEY (`DBID`))
ENGINE = InnoDB
COMMENT = '用户访问日志';