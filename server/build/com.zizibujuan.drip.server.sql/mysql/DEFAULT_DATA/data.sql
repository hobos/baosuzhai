insert into DRIP_I18N (CODE_NAME, CODE_VALUE) VALUES ('0101','zh_cn');

--1表示字符串,2表示整数，3表示日期，4表示小数

-- 系统分组
insert into DRIP_PROPERTY_GROUP (DBID, GROUP_NAME, DISPLAY_NAME,I18N_ID) VALUES (1,'drip.properties','drip.properties','0101');

-- 为匿名用户分配的标识，分配一个之后，该值加1
insert into DRIP_PROPERTY_KEY (DBID,PROPERTY_KEY,GROUP_ID,PROPERTY_TYPE) VALUES (1, 'drip.cookie.max.userId', 1, 2);
insert into DRIP_PROPERTY_VALUE_NUMBER (KEY_ID, PROPERTY_VALUE) VALUES (1, 0);