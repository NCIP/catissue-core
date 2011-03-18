DELIMITER $$

DROP FUNCTION IF EXISTS `mysql`.`sf_compDBTab_W_Diffcols`$$

CREATE DEFINER=`root`@`%` FUNCTION `sf_compDBTab_W_Diffcols`(  pvchTablename        VARCHAR(100),pvchSchema1 varchar(100), pvchSchema2 varchar(100)) RETURNS varchar(4000) CHARSET latin1
    DETERMINISTIC
BEGIN
declare cols,col1  varchar(4000);
declare done int default 0;
declare maxop int ;
declare c1 cursor for
select concat(COLUMN_NAME, ' ',COLUMN_TYPE, ',' ) AS COL1
from information_schema.columns
	where table_schema = pvchSchema1
	and TABLE_NAME =pvchTablename
	and  (TABLE_NAME, COLUMN_NAME, COLUMN_TYPE) not in
	(select TABLE_NAME, COLUMN_NAME, COLUMN_TYPE
		from information_schema.columns
		where table_schema = pvchSchema2
			and TABLE_NAME =pvchTablename);
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set cols='';
	open c1;
	repeat
		fetch c1 into col1 ;
	if not done then
		set cols= concat(cols , col1);

	end if ;
		UNTIL done END REPEAT;
	close c1;
set cols= left(cols, length(cols)-1);
/*insert  into test.asd values (cols);*/
return cols;
END$$

DELIMITER ;