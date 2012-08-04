-- 先删除约束，再删除数据库表
alter table `drip`.`DRIP_EXERCISE` drop foreign key FK_EXERCISE_USER;


DROP TABLE IF EXISTS `drip`.`DRIP_USER`;
DROP TABLE IF EXISTS `drip`.`DRIP_EXERCISE`;