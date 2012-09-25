-- 先删除约束，再删除数据库表
ALTER TABLE `drip`.`DRIP_EXERCISE` DROP FOREIGN KEY FK_EXERCISE_USER;
ALTER TABLE `drip`.`DRIP_ANSWER` DROP FOREIGN KEY FK_ANSWER_USER;
ALTER TABLE `drip`.`DRIP_ANSWER` DROP FOREIGN KEY FK_ANSWER_EXERCISE;
ALTER TABLE `drip`.`DRIP_CHOICE_OPTION` DROP FOREIGN KEY FK_CHOICE_OPTION_EXERCISE;

DROP TABLE IF EXISTS `drip`.`DRIP_USER`;
DROP TABLE IF EXISTS `drip`.`DRIP_EXERCISE`;

DROP TABLE IF EXISTS `drip`.`DRIP_I18N`;
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_GROUP`;
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_KEY`;
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_VALUE_STRING`;
DROP TABLE IF EXISTS `drip`.`DRIP_PROPERTY_VALUE_NUMBER`;

DROP TABLE IF EXISTS `drip`.`DRIP_CHOICE_OPTION`;
DROP TABLE IF EXISTS `drip`.`DRIP_ANSWER`;