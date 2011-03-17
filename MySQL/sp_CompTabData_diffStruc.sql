DELIMITER $$;

DROP PROCEDURE IF EXISTS `mysql`.`sp_CompTabData_diffStruc`$$

CREATE DEFINER=`root`@`%` PROCEDURE `sp_CompTabData_diffStruc`(
 pvchTablename        VARCHAR(100),pvchSchema1 VARCHAR(100)
,pvchSchema2 VARCHAR(100)
)
BEGIN
DECLARE cols VARCHAR(2000);
DECLARE lsql VARCHAR(2000);
DECLARE asd1 INT DEFAULT 0;
DECLARE inssql VARCHAR(2000)DEFAULT '';
	SET cols= '';
	SET lsql= '';
	SET cols = mysql.sf_getColsDiffStruct(pvchTablename, pvchSchema1, pvchSchema2);
	SET @inssql = CONCAT('insert into mysql.compTablesDataRows select ',CHAR(39), pvchSchema1,CHAR(39),',',CHAR(39),pvchTablename, CHAR(39),',',CHAR(39),pvchSchema2,CHAR(39),',',CHAR(39),pvchTablename,CHAR(39), ',') ;
/*set @lsql=concat('', ' select  Count(*) into @asd1 from catissue1_5.catissue_participant a inner join catissue1_3.catissue_participant b using ( ');
*/
/*  SET @lsql=CONCAT('', ' select  Count(*) into @asd1 from ', pvchSchema1 ,'.',pvchTablename, ' a inner join ' ,pvchSchema2 , '.' , pvchTablename, ' b using ( ');*/
/*set @lsql= 'Select Count(*) from catissue1_5.catissue_participant a inner join catissue1_3.catissue_participant b using ( '; */
/*SET @lsql=CONCAT(@lsql,mysql.sf_getColsDiffStruct(pvchTablename, pvchSchema1, pvchSchema2), '  )  ' ) ;*/
SET @lsql= CONCAT('insert into common_row_count (count_val, tab_name) select Count(*),"',pvchTablename,'" From (select  * from (select ',cols, '  from ', pvchSchema1 ,'.',pvchTablename,' union all select ',cols, '  from ', pvchSchema2 ,'.',pvchTablename, ' )A ' ) ;
SET @lsql=CONCAT(@lsql,' group by ', cols , '  having count(*) = 2) A ' ) ;
/*INSERT INTO test.asd VALUES (@lsql);*/
PREPARE s1 FROM @lsql;
EXECUTE s1 ;
DEALLOCATE PREPARE s1 ;
    END$$

DELIMITER ;$$