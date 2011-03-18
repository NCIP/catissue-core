DELIMITER $$

DROP FUNCTION IF EXISTS `mysql`.`sf_getcolsdiffstruct`$$

CREATE DEFINER=`root`@`%` FUNCTION `sf_getColsDiffStruct`(
	pvchtablename        varchar(100),
	pvchschema1 varchar(100),
	pvchschema2 varchar(100)
) RETURNS varchar(4000) CHARSET latin1
    DETERMINISTIC
BEGIN
declare cols,col1 varchar(4000);
declare done int default 0;
declare maxop int ;
declare c1 cursor for
select CONCAT(a.column_name , ',') AS col from
		(select table_name, column_name, column_type  from information_schema.columns where table_schema in (pvchschema1)
			and table_name = pvchtablename) a,
		(select table_name, column_name, column_type  from information_schema.columns where table_schema in (pvchschema2)
			and table_name = pvchtablename ) b
		where a.table_name = b.table_name
		and a.column_name =  b.column_name
		and a.column_type  =  b.column_type  ;
	declare continue handler for not found set done = 1;
set cols='';
	open c1;
	repeat
		fetch c1 into col1 ;
	if not done then
		set cols= concat(cols , col1);

	end if ;
		until done end repeat;
	close c1;
set cols= left(cols, length(cols)-1);
/*insert  into test.asd values (cols);*/
return cols;
END$$

DELIMITER ;