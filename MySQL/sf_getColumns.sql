DELIMITER $$;

DROP FUNCTION IF EXISTS `mysql`.`sf_getColumns`$$

CREATE DEFINER=`root`@`%` FUNCTION `sf_getColumns`(  pvchTablename        VARCHAR(100),pvchSchema1 varchar(100)) RETURNS varchar(4000) CHARSET latin1
    DETERMINISTIC
BEGIN
	declare cols,col1 varchar(4000);
DECLARE done INT DEFAULT 0;
	declare maxop int ;
	declare c1 cursor for select concat(column_name , ',') as col from information_schema.columns where table_schema = pvchSchema1
			and TABLE_NAME = pvchTablename;
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

DELIMITER ;$$