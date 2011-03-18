DELIMITER $$

DROP PROCEDURE IF EXISTS `mysql`.`sp_compareDataWithDiffStruc`$$

CREATE DEFINER=`root`@`%` PROCEDURE `sp_compareDataWithDiffStruc`
(  pvchTablename        VARCHAR(100),
   pvchSchema1 varchar(100),
   colName1Schema1 varchar(100),
   colName2Schema1 varchar(100),
   pvchSchema2 varchar(100),
   colName1Schema2 varchar(100),
   colName2Schema2 varchar(100),
   tabName VARCHAR(100)
 )
BEGIN
declare query1 varchar(2000);
set query1= '';
SET @query1 = concat('insert into common_row_count (count_val, tab_name) SELECT COUNT(*),"',tabName,'" FROM ((SELECT ',colName1Schema1,',',colName2Schema1,' FROM ', pvchSchema1 ,'.',pvchTablename,' WHERE (',colName1Schema1,',',colName2Schema1,') NOT IN (SELECT ',colName1Schema2,',',colName2Schema2,' FROM ', pvchSchema2 ,'.',pvchTablename,')) UNION ALL (SELECT ',colName1Schema2,',',colName2Schema2,' FROM ', pvchSchema2 ,'.',pvchTablename,' WHERE (',colName1Schema2,',',colName2Schema2,') NOT IN (SELECT ',colName1Schema1,',',colName2Schema1,' FROM ', pvchSchema1 ,'.',pvchTablename,')))A1');
/*INSERT INTO test.asd VALUES (@query1);*/
PREPARE s2 FROM @query1;
execute s2;
deallocate prepare s2;
END$$

DELIMITER ;