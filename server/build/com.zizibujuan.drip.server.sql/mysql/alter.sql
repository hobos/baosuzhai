-- 存储在开发过程中修改数据库的sql语句，保障已存的用户数据不丢失。

alter table DRIP_USER rename to DRIP_USER_INFO;

-- 往DRIP_USER_INFO表中添加派生字段
alter table DRIP_USER_INFO add column `FAN_COUNT` INT NULL DEFAULT 0 COMMENT '粉丝数量';
alter table DRIP_USER_INFO add column `FOLLOW_COUNT` INT NULL DEFAULT 0 COMMENT '关注人数量';
alter table DRIP_USER_INFO add column `EXER_DRAFT_COUNT` INT NULL DEFAULT 0 COMMENT '录入的习题草稿数量';
alter table DRIP_USER_INFO add column `EXER_PUBLISH_COUNT` INT NULL DEFAULT 0 COMMENT '录入的已发布习题数量';
alter table DRIP_USER_INFO add column `ANSWER_COUNT` INT NULL DEFAULT 0 COMMENT '回答的习题数量';