ALTER TABLE `drip`.`DRIP_EXERCISE` 
	ADD constraint FK_EXERCISE_USER 
	FOREIGN KEY (CRT_USER_ID) 
	REFERENCES `drip`.`DRIP_USER` (`DBID` )