DELIMITER $$

DROP FUNCTION IF EXISTS `mysql`.`sf_compTablesInschemas`$$

CREATE DEFINER=`root`@`%` FUNCTION `sf_compTablesInschemas`(  pvchTablename        VARCHAR(100),pvchSchema1 varchar(100)
,pvchSchema2 varchar(100)
) RETURNS varchar(20) CHARSET latin1
    DETERMINISTIC
BEGIN
declare asd int default 0;
	select COUNT(*)  From (
	select  *  from (
	select TABLE_NAME, COLUMN_NAME, COLUMN_TYPE,IS_NULLABLE,DATA_TYPE  from information_schema.columns where table_schema = pvchSchema1
				and TABLE_NAME =pvchTablename
	union all
	select TABLE_NAME, COLUMN_NAME, COLUMN_TYPE,IS_NULLABLE,DATA_TYPE  from information_schema.columns where table_schema = pvchSchema2
				and TABLE_NAME =pvchTablename) A
	group by TABLE_NAME, COLUMN_NAME, COLUMN_TYPE,IS_NULLABLE,DATA_TYPE   having COUNT(*) =1  ) A1
	
	into asd;
if asd > 0 then
return 'false';
else
return 'true';
end if ;
END$$

DELIMITER ;