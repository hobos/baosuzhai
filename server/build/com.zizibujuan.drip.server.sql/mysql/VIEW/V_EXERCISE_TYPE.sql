-- 习题类型
-- 为每一类写一个存储过程，不合理，应该在dao做一个统一的调用方法，传入组的英文标识。
-- 这里的视图，在与其他表进行联合查询的时候使用。
CREATE OR REPLACE VIEW V_DATASOURCE_TYPES AS 
		SELECT PK.DBID,PK.PROPERTY_KEY, PVS.PROPERTY_VALUE FROM BANG_PROPERTY_GROUP PG, BANG_PROPERTY_KEY PK,  BANG_PROPERTY_VALUE_STRING PVS 
		WHERE PG.GROUP_NAME='bang.datasource.type' and PG.DBID = PK.GROUP_ID and PK.DBID=PVS.KEY_ID;
		