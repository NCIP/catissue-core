DELIMITER $$

DROP PROCEDURE IF EXISTS `mysql`.`sf_compTablesDataRows`$$

CREATE DEFINER=`root`@`%` PROCEDURE `sf_compTablesDataRows`(
 pvchTablename        VARCHAR(100),pvchSchema1 varchar(100)
,pvchSchema2 varchar(100)
)
BEGIN
declare cols varchar(2000);
declare lsql varchar(2000);
declare asd int default 0;
declare inssql varchar(2000)default '';
	set cols= '';
	set lsql= '';
	set cols = sf_getcolumns(pvchTablename, pvchSchema1);
/*	set @inssql = concat('insert into mysql.compTablesDataRows select ',char(39), pvchSchema1,char(39),',',char(39),pvchTablename, char(39),',',char(39),pvchSchema2,char(39),',',char(39),pvchTablename,char(39), ',') ;
set @lsql=concat('', ' select  Count(*) into @asd from ', pvchSchema1 ,'.',pvchTablename, ' a inner join ' ,pvchSchema2 , '.' , pvchTablename, ' b using ( ');
*/
/*set @lsql= 'Select Count(*) from catissue1_5.catissue_participant a inner join catissue1_3.catissue_participant b using ( '; */

set @lsql= concat('insert into common_row_count (count_val, tab_name) select Count(*), "',pvchTablename,'" From (select  * from (select * from ', pvchSchema1 ,'.',pvchTablename,' union all select * from ', pvchSchema2 ,'.',pvchTablename, ' )A ' ) ;
set @lsql=concat(@lsql,' group by ', cols , '  having count(*) = 2) A ' ) ;




/*set @lsql=concat(@lsql,mysql.sf_getcolumns(pvchTablename, pvchSchema1), '  )  ' ) ; */
/*insert into test.asd values (@lsql);*/
PREPARE s1 FROM @lsql;
execute s1 ;
deallocate prepare s1 ;
END$$

DELIMITER ;